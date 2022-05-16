package cc.xfl12345.mybigdata.server.initializer;


import cc.xfl12345.mybigdata.server.appconst.MyConst;
import cc.xfl12345.mybigdata.server.model.SpringBeanAPI;
import cc.xfl12345.mybigdata.server.model.jdbc.MysqlJdbcUrlHelper;
import cc.xfl12345.mybigdata.server.utility.MyBatisSqlUtils;
import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.cj.conf.ConnectionUrl;
import lombok.extern.slf4j.Slf4j;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
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
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        MysqlJdbcUrlHelper mysqlJdbcUrlHelper = new MysqlJdbcUrlHelper(ConnectionUrl.getConnectionUrlInstance(url, null));
        String targetDatabaseName = mysqlJdbcUrlHelper.getDatabaseName();
        mysqlJdbcUrlHelper.setDatabaseName("information_schema");
        url = mysqlJdbcUrlHelper.getSqlConnUrlWithConfigProp();

        DruidDataSource mysqlTableSchemaDataSource = new DruidDataSource();
        mysqlTableSchemaDataSource.setUsername(username);
        mysqlTableSchemaDataSource.setPassword(password);
        mysqlTableSchemaDataSource.setDriverClassName(driverClassName);
        mysqlTableSchemaDataSource.setUrl(url);

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
        conn2.setAutoCommit(false);
        log.info("Database server connected.Checking database.");
        // 检查 MySQL中 某个数据库是否存在（其它数据库暂未适配，所以这个 dataSource 并非万能）
        PreparedStatement ps = conn2.prepareStatement("select * from information_schema.SCHEMATA where SCHEMA_NAME = ?");
        ps.setString(1, targetDatabaseName);
        log.info(MyBatisSqlUtils.getSql(ps));
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            log.info("Database is exist!");
            URL dbRestartInitSqlFileURL = classLoader.getResource("database/db_restart_init.sql");
            if (dbRestartInitSqlFileURL != null) {
                log.info("Executing db_restart_init_sql_file: " + dbRestartInitSqlFileURL.toString());
                try {
                    executeSqlFile(conn2, dbRestartInitSqlFileURL);
                    log.info("Database initiated!");
                } catch (IOException exception) {
                    log.error(exception.getMessage());
                }
            } else {
                log.info("Initiation will not process.Because file not found");
            }
        } else {
            log.info("Database is not exist!");
            URL dbInitSqlFileURL = classLoader.getResource("database/db_init.sql");
            if (dbInitSqlFileURL != null) {
                log.info("Executing db_init_sql_file: " + dbInitSqlFileURL.toString());
                try {
                    executeSqlFile(conn2, dbInitSqlFileURL);
                    log.info("Database initiated!");
                } catch (IOException exception) {
                    log.error(exception.getMessage());
                }
            } else  {
                log.info("Initiation will not process.Because file not found");
            }
        }
        conn2.close();
        mysqlTableSchemaDataSource.close();
    }

    public static void executeSqlFile(Connection connection, URL sqlFileURL) throws IOException, SQLException {
        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        ScriptUtils.executeSqlScript(
            connection,
            new EncodedResource(new UrlResource(sqlFileURL), StandardCharsets.UTF_8),
            false,
            false,
            new String[]{ScriptUtils.DEFAULT_COMMENT_PREFIX, "#"},
            ScriptUtils.DEFAULT_STATEMENT_SEPARATOR,
            ScriptUtils.DEFAULT_BLOCK_COMMENT_START_DELIMITER,
            ScriptUtils.DEFAULT_BLOCK_COMMENT_END_DELIMITER
        );
        new SQLExec().setSrc(new File(sqlFileURL.getFile()));
        connection.commit();
    }
}
