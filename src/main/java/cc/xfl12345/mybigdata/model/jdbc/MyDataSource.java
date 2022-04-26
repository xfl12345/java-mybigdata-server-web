package cc.xfl12345.mybigdata.model.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.cj.conf.url.SingleConnectionUrl;
import cc.xfl12345.mybigdata.appconst.MyConst;
import cc.xfl12345.mybigdata.model.utility.MyBatisSqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class MyDataSource extends DruidDataSource implements ApplicationContextAware {
    protected File dbInitSqlFile;
    protected File dbRestartInitSqlFile;

    protected ApplicationContext applicationContext;

    public MyDataSource() {
        super();
    }

    public MyDataSource(File dbInitSqlFile, File dbRestartInitSqlFile, MysqlJdbcUrlHelper mysqlJdbcUrlHelper) throws SQLException, IOException {
        this.dbInitSqlFile = dbInitSqlFile;
        this.dbRestartInitSqlFile = dbRestartInitSqlFile;
        init(mysqlJdbcUrlHelper);
    }

    public static void executeSqlFile(Connection connection, File sqlFile) throws IOException, SQLException {
        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        Resources.setCharset(StandardCharsets.UTF_8); //设置字符集,不然中文乱码插入错误
        Reader read = new FileReader(sqlFile, StandardCharsets.UTF_8);
        ScriptRunner runner = new ScriptRunner(connection);
        runner.setLogWriter(null);//设置是否输出日志
        runner.runScript(read);
        connection.commit();
//        connection.close();
        read.close();
    }

    public void executeSqlFile(File sqlFile) throws IOException, SQLException {
        executeSqlFile(getConnection(), sqlFile);
    }

    @Override
    public void init() throws SQLException {
        if (inited) {
            return;
        }
        init(getUrl());
    }

    public void init(String url) throws SQLException {
        if (inited) {
            return;
        }
        init(new MysqlJdbcUrlHelper(
            SingleConnectionUrl.getConnectionUrlInstance(url, getConnectProperties())
        ));
    }

    public void init(MysqlJdbcUrlHelper mysqlJdbcUrlHelper) throws SQLException {
        if (inited) {
            return;
        }
        String driverName = getDriverClassName();
        if (driverName == null) {
            driverName = mysqlJdbcUrlHelper.sqlServerDriverName;
        }

        DataSource mysqlTableSchemaDataSource = null;
        if (applicationContext != null) {
            try {
                mysqlTableSchemaDataSource = applicationContext.getBean("mysqlTableSchemaDataSource", DataSource.class);
            } catch (Exception ignored) {
            }
        }
        if (mysqlTableSchemaDataSource == null) {
            log.info("bean id=mysqlTableSchemaDataSource not found.New one instead.");
            mysqlTableSchemaDataSource = new UnpooledDataSource(
                driverName,
                mysqlJdbcUrlHelper.getSqlConnectionUrl("information_schema"),
                getConnectProperties()
            );
        }

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
        if (rs.next()) {
            log.info("Database is exist!");
            if (dbRestartInitSqlFile != null) {
                if (dbRestartInitSqlFile.exists()) {
                    log.info("Executing db_restart_init_sql_file: " + dbRestartInitSqlFile.toURI());
                    try {
                        executeSqlFile(conn2, dbRestartInitSqlFile);
                        log.info("Database initiated!");
                    } catch (IOException exception) {
                        log.error(exception.getMessage());
                    }
                } else {
                    log.info("Initiation will not process.Because file not found");
                }
            }
        } else {
            log.info("Database is not exist!");
            if (dbInitSqlFile != null) {
                if (dbInitSqlFile.exists()) {
                    log.info("Executing db_init_sql_file: " + dbInitSqlFile.toURI());
                    try {
                        executeSqlFile(conn2, dbInitSqlFile);
                        log.info("Database initiated!");
                    } catch (IOException exception) {
                        log.error(exception.getMessage());
                    }
                    log.info("Database initiated!");
                } else {
                    log.info("Initiation will not process.Because file not found");
                }
            }
        }
        conn2.close();
        log.info("Final JDBC URL=" + mysqlJdbcUrlHelper.getSqlConnUrlWithConfigProp(tmpConfProp));
        super.setUrl(mysqlJdbcUrlHelper.getSqlConnectionUrl());
        super.init();
        log.info("Mybatis datasource ready!");
    }

    public File getDbInitSqlFile() {
        return dbInitSqlFile;
    }

    public void setDbInitSqlFile(File dbInitSqlFile) {
        this.dbInitSqlFile = dbInitSqlFile;
    }

    public File getDbRestartInitSqlFile() {
        return dbRestartInitSqlFile;
    }

    public void setDbRestartInitSqlFile(File dbRestartInitSqlFile) {
        this.dbRestartInitSqlFile = dbRestartInitSqlFile;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
