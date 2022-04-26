package cc.xfl12345.mybigdata.model.jdbc;

import com.mysql.cj.conf.ConnectionUrl;
import com.mysql.cj.conf.HostInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

public class MysqlJdbcUrlParameter {
    protected String sqlServerDriverName = com.mysql.cj.jdbc.Driver.class.getCanonicalName();

    protected String sqlProtocal = "jdbc";
    protected String sqlSubProtocal = "mysql";

    protected String sqlServerAddress;
    protected Integer sqlServerPort;

    protected String databaseName;

    public MysqlJdbcUrlParameter() {
    }

    public MysqlJdbcUrlParameter(Properties properties) {
        loadFromProperties(properties);
    }

    public MysqlJdbcUrlParameter(ConnectionUrl connectionUrl) {
        HostInfo hostInfo = connectionUrl.getMainHost();
        if (hostInfo == null) {
            sqlServerAddress = connectionUrl.getDefaultHost();
            sqlServerPort = connectionUrl.getDefaultPort();
            databaseName = "information_schema";
        } else {
            sqlServerAddress = hostInfo.getHost();
            sqlServerPort = hostInfo.getPort();
            databaseName = hostInfo.getDatabase();
        }
    }

    /**
     * 通过Properties对象和Java反射机制来完成赋值，
     * 将已有成员变量名作为键去获取对应的值并完成赋值操作。
     *
     * @param properties 一个包含 属性名=值 的Properties对象
     */
    private void loadFromProperties(Properties properties) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                //跳过final修饰的属性
                if (Modifier.isFinal(f.getModifiers()))
                    continue;
                String key = f.getName();
                //完成赋值任务
                if (properties.containsKey(key)) {
                    if (f.getType().equals(String.class)) {
                        f.set(this, properties.get(key));
                    } else if (f.getType().equals(Integer.class)) {
                        f.set(this, Integer.valueOf((String) properties.get(key)));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过克隆来构造对象
     *
     * @param base 同一个类的对象
     */
    public MysqlJdbcUrlParameter(MysqlJdbcUrlParameter base) {
        this.sqlServerDriverName = base.getSqlServerDriverName();
        this.sqlProtocal = base.getSqlProtocal();
        this.sqlSubProtocal = base.getSqlSubProtocal();
        this.sqlServerAddress = base.getSqlServerAddress();
        this.sqlServerPort = base.getSqlServerPort();
        this.databaseName = base.getDatabaseName();
    }

    public String getSqlServerDriverName() {
        return sqlServerDriverName;
    }

    public void setSqlServerDriverName(String sqlServerDriverName) {
        this.sqlServerDriverName = sqlServerDriverName;
    }

    public String getSqlProtocal() {
        return sqlProtocal;
    }

    public void setSqlProtocal(String sqlProtocal) {
        this.sqlProtocal = sqlProtocal;
    }

    public String getSqlSubProtocal() {
        return sqlSubProtocal;
    }

    public void setSqlSubProtocal(String sqlSubProtocal) {
        this.sqlSubProtocal = sqlSubProtocal;
    }

    public String getSqlServerAddress() {
        return sqlServerAddress;
    }

    public void setSqlServerAddress(String sqlServerAddress) {
        this.sqlServerAddress = sqlServerAddress;
    }

    public Integer getSqlServerPort() {
        return sqlServerPort;
    }

    public void setSqlServerPort(Integer sqlServerPort) {
        this.sqlServerPort = sqlServerPort;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

}
