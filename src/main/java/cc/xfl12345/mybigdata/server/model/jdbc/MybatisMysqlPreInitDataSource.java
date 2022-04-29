package cc.xfl12345.mybigdata.server.model.jdbc;

import com.mysql.cj.conf.url.SingleConnectionUrl;
import cc.xfl12345.mybigdata.server.appconst.MyConst;
import cc.xfl12345.mybigdata.server.model.utility.MyBatisSqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
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

@Deprecated
@Slf4j
public class MybatisMysqlPreInitDataSource extends PooledDataSource implements ApplicationContextAware {
    protected File dbInitSqlFile;
    protected File dbRestartInitSqlFile;
    protected Properties confProp;
    protected String url;

    protected boolean isInitSucceed = false;
    protected boolean enableForceCloseAll = true;

    protected ApplicationContext applicationContext;

    public MybatisMysqlPreInitDataSource() {
        super();
    }

    public MybatisMysqlPreInitDataSource(boolean enableForceCloseAll) {
        super();
        this.enableForceCloseAll = enableForceCloseAll;
    }

    public MybatisMysqlPreInitDataSource(File dbInitSqlFile, File dbRestartInitSqlFile, MysqlJdbcUrlHelper mysqlJdbcUrlHelper) throws SQLException, IOException {
        this.dbInitSqlFile = dbInitSqlFile;
        this.dbRestartInitSqlFile = dbRestartInitSqlFile;
        initWithJava(mysqlJdbcUrlHelper);
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
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    public void initWithJava() throws SQLException, IOException {
        initWithJava(url);
    }

    public void initWithJava(String url) throws SQLException, IOException {
        initWithJava(new MysqlJdbcUrlHelper(
            SingleConnectionUrl.getConnectionUrlInstance(url, getDriverProperties())
        ));
    }

    public void initWithJava(MysqlJdbcUrlHelper mysqlJdbcUrlHelper) throws SQLException, IOException {
        enableForceCloseAll = false;
        String driverName = getDriver();
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
                getDriverProperties()
            );
        }

        // 加载 sql URL附加属性
        confProp = mysqlJdbcUrlHelper.getAdditionalParameters();
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
                    executeSqlFile(conn2, dbRestartInitSqlFile);
                    log.info("Database initiated!");
                } else {
                    log.info("Initiation will not process.Because file not found");
                }
            }
        } else {
            log.info("Database is not exist!");
            if (dbInitSqlFile != null) {
                if (dbInitSqlFile.exists()) {
                    log.info("Executing db_init_sql_file: " + dbInitSqlFile.toURI());
                    executeSqlFile(conn2, dbInitSqlFile);
                    log.info("Database initiated!");
                } else {
                    log.info("Initiation will not process.Because file not found");
                }
            }
        }
        conn2.close();
        log.info("Final JDBC URL=" + mysqlJdbcUrlHelper.getSqlConnUrlWithConfigProp(tmpConfProp));
        //配置连接池
        super.setDriver(mysqlJdbcUrlHelper.getSqlServerDriverName());
        super.setUrl(mysqlJdbcUrlHelper.getSqlConnectionUrl());
        super.setDriverProperties(confProp);
        super.setPoolMaximumActiveConnections(100);
        super.setPoolMaximumIdleConnections(20);
        isInitSucceed = true;
        enableForceCloseAll = true;
        forceCloseAll();
        log.info("Mybatis datasource ready!");
    }

    @Override
    public void forceCloseAll() {
        if (enableForceCloseAll) {
            super.forceCloseAll();
        }
    }

    public boolean isInitSucceed() {
        return isInitSucceed;
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
