package cc.xfl12345.mybigdata.server.pojo;

import com.mysql.cj.PreparedQuery;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.DisposableBean;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MyBatisSql implements DisposableBean {
    /**
     * 生肉SQL语句
     */
    protected BoundSql boundSql;

    /**
     * toString 方法里，用来构造 PrepareStatement 对象
     */
    protected SqlSessionFactory sqlSessionFactory;

    protected ParameterHandler parameterHandler;
    protected Configuration configuration;
    protected MappedStatement mappedStatement;
    protected PreparedStatement preparedStatement;

    public MyBatisSql(
        SqlSessionFactory sqlSessionFactory,
        Class<?> daoClass,
        String methodId,
        Object parameterObject) throws Exception {
        this(sqlSessionFactory, daoClass.getName() + "." + methodId, parameterObject);
    }

    public MyBatisSql(
        SqlSessionFactory sqlSessionFactory,
        String mappedStatementId,
        Object parameterObject) throws Exception {
        this.sqlSessionFactory = sqlSessionFactory;
        configuration = sqlSessionFactory.getConfiguration();
        mappedStatement = configuration.getMappedStatement(mappedStatementId);
        setParameterObject(parameterObject);
    }

    /**
     * 获取 生肉 SQL语句，包含占位符
     *
     * @return 含占位符的生肉 SQL语句 字符串
     */
    public String getRawSql() {
        return boundSql.getSql();
    }

    protected String commonCompleteSqlGenerator() {
        String sql;
        try {
            Method method = preparedStatement.getClass().getMethod("asSql");
            sql = (String) method.invoke(preparedStatement);
        } catch (Exception exception) {
            sql = preparedStatement.toString();
        }
        return sql;
    }

    /**
     * 获取 可以直接拿去执行的熟肉 SQL语句
     *
     * @return 可以直接拿去执行的熟肉 SQL语句 字符串
     */
    public String getCompleteSql() {
        String sql;
        if (preparedStatement instanceof com.mysql.cj.jdbc.ClientPreparedStatement) {
            try {
                sql = ((PreparedQuery) preparedStatement.unwrap(com.mysql.cj.jdbc.ClientPreparedStatement.class).getQuery()).asSql();
            } catch (SQLException e) {
                sql = commonCompleteSqlGenerator();
            }
        } else {
            sql = commonCompleteSqlGenerator();
        }
        return sql;
    }

    /**
     * 更新数据库对象，但不重新生成 PreparedStatement
     *
     * @param parameterObject 数据库表中一行的对象
     */
    public void updateParameterObjectButNoChangeField(Object parameterObject) throws SQLException {
        boundSql = mappedStatement.getBoundSql(parameterObject);
        parameterHandler = configuration.newParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler.setParameters(preparedStatement);
    }

    public Object getParameterObject() {
        return boundSql.getParameterObject();
    }

    /**
     * 更新数据库对象，会根据数据库对象重新生成 PreparedStatement
     *
     * @param parameterObject 数据库表中一行的对象
     */
    public void setParameterObject(Object parameterObject) throws SQLException {
        if (preparedStatement != null && !preparedStatement.isClosed()) {
            preparedStatement.close();
        }
        boundSql = mappedStatement.getBoundSql(parameterObject);
        parameterHandler = configuration.newParameterHandler(mappedStatement, parameterObject, boundSql);
        Connection connection = sqlSessionFactory.openSession().getConnection();
        preparedStatement = connection.prepareStatement(boundSql.getSql());
        connection.close();
        parameterHandler.setParameters(preparedStatement);
    }


    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) throws SQLException {
        this.sqlSessionFactory = sqlSessionFactory;
        configuration = sqlSessionFactory.getConfiguration();
        mappedStatement = configuration.getMappedStatement(mappedStatement.getId());
        setParameterObject(boundSql.getParameterObject());
    }

    /**
     * 获取可以直接在数据库中执行的sql语句
     *
     * @return 最终成品级sql语句
     */
    @Override
    public String toString() {
        return getCompleteSql();
    }

    @Override
    public void destroy() throws Exception {
        dispose();
    }

    public void dispose() throws SQLException {
        preparedStatement.close();
    }
}
