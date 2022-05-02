package cc.xfl12345.mybigdata.server.initializer;


import cc.xfl12345.mybigdata.server.appconst.MyConst;
import cc.xfl12345.mybigdata.server.model.SpringBeanAPI;
import cc.xfl12345.mybigdata.server.model.jdbc.MysqlJdbcUrlHelper;
import cc.xfl12345.mybigdata.server.utility.MyBatisSqlUtils;
import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.cj.conf.ConnectionUrl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@Component("myDatabaseInitializer")
@Slf4j
public class MyDatabaseInitializer implements SpringBeanAPI {
    protected ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Autowired
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    protected String username;
    protected String password;
    protected String url;
    protected String driverClassName;

    public String getUsername() {
        return username;
    }

    @Value("${spring.datasource.username}")
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    @Value("${spring.datasource.password}")
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    //@ConditionalOnProperty(prefix = "spring.datasource",name = "url",havingValue = "true")
    @Value("${spring.datasource.url}")
    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    //@ConditionalOnProperty(prefix = "spring.datasource",name = "driver-class-name",havingValue = "true")
    @Value("${spring.datasource.driver-class-name}")
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String mysqlJdbcProtocolHeader = "jdbc:mysql://";
        if (url != null && url.startsWith(mysqlJdbcProtocolHeader)) {
            initMySQL();
        }
    }

    public void initMySQL() throws SQLException, IOException {
        MysqlJdbcUrlHelper mysqlJdbcUrlHelper = new MysqlJdbcUrlHelper(ConnectionUrl.getConnectionUrlInstance(url, null));
        mysqlJdbcUrlHelper.setDatabaseName("information_schema");
        url = mysqlJdbcUrlHelper.getSqlConnUrlWithConfigProp();

        DruidDataSource mysqlTableSchemaDataSource = new DruidDataSource();
        mysqlTableSchemaDataSource.setUsername(username);
        mysqlTableSchemaDataSource.setPassword(password);
        mysqlTableSchemaDataSource.setDriverClassName(driverClassName);
        mysqlTableSchemaDataSource.setUrl(url);

        URL dbInitSqlFileURL = Resources.getResourceURL("database/db_init.sql");
        URL dbRestartInitSqlFileURL = Resources.getResourceURL("database/db_restart_init.sql");

        // 加载 sql URL附加属性
        Properties confProp = mysqlJdbcUrlHelper.getAdditionalParameters();
        // 浅拷贝一个Properties对象，删除其中的敏感数据，用以展示完整的JDBC URL
        Properties tmpConfProp = (Properties) confProp.clone();
        tmpConfProp.remove("user");
        tmpConfProp.remove("password");
        // 创建一个临时的数据库连接，完成数据库初始化
        // 构建一个不带附加参数的JDBC URL
        String tmpJdbcConnectionURL = mysqlJdbcUrlHelper.getSqlConnUrlWithConfigProp(MyConst.INFORMATION_SCHEMA_TABLE_NAME, tmpConfProp);
        log.info("Temporary JDBC URL=" + tmpJdbcConnectionURL);
        // 创建一个临时连接，用于试探MySQL数据库
        Connection conn2 = mysqlTableSchemaDataSource.getConnection();
        log.info("Database server connected.Checking database.");
        // 检查 MySQL中 某个数据库是否存在（其它数据库暂未适配，所以这个 dataSource 并非万能）
        PreparedStatement ps = conn2.prepareStatement("select * from information_schema.SCHEMATA where SCHEMA_NAME = ?");
        ps.setString(1, mysqlJdbcUrlHelper.getDatabaseName());
        log.info(MyBatisSqlUtils.getSql(ps));
        ResultSet rs = ps.executeQuery();

        InputStream urlInputStream = null;
        if (rs.next()) {
            log.info("Database is exist!");
            try {
                urlInputStream = dbRestartInitSqlFileURL.openConnection().getInputStream();
                log.info("Executing db_restart_init_sql_file: " + dbRestartInitSqlFileURL.toString());
                try {
                    executeSqlFile(conn2, urlInputStream);
                    log.info("Database initiated!");
                } catch (IOException exception) {
                    log.error(exception.getMessage());
                }
            } catch (IOException exception) {
                log.info("Initiation will not process.Because file not found");
            }
        } else {
            log.info("Database is not exist!");
            try {
                urlInputStream = dbInitSqlFileURL.openConnection().getInputStream();
                log.info("Executing db_init_sql_file: " + dbInitSqlFileURL.toString());
                try {
                    executeSqlFile(conn2, urlInputStream);
                    log.info("Database initiated!");
                } catch (IOException exception) {
                    log.error(exception.getMessage());
                }
            } catch (IOException exception) {
                log.info("Initiation will not process.Because file not found");
            }
        }
        if (urlInputStream != null) {
            urlInputStream.close();
        }
        conn2.close();
        mysqlTableSchemaDataSource.close();
    }

    public static void executeSqlFile(Connection connection, InputStream sqlFileInputStream) throws IOException, SQLException {
        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        Resources.setCharset(StandardCharsets.UTF_8); //设置字符集,不然中文乱码插入错误
        InputStreamReader read = new InputStreamReader(sqlFileInputStream, StandardCharsets.UTF_8);
        ScriptRunner runner = new ScriptRunner(connection);
        runner.setLogWriter(null);//设置是否输出日志
        runner.runScript(read);
        connection.commit();
        read.close();
    }
}
