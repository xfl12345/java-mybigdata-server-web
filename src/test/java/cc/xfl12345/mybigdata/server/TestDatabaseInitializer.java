package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.initializer.MyDatabaseInitializer;
import com.alibaba.druid.pool.DruidDataSource;

public class TestDatabaseInitializer {
    public static void main(String[] args) throws Exception {
        DruidDataSource dataSource = TestLoadDataSource.getDataSource();
        MyDatabaseInitializer databaseInitializer = new MyDatabaseInitializer();
        databaseInitializer.setUrl(dataSource.getUrl());
        databaseInitializer.setDriverClassName(dataSource.getDriverClassName());
        databaseInitializer.setUsername(dataSource.getUsername());
        databaseInitializer.setPassword(dataSource.getPassword());

        databaseInitializer.afterPropertiesSet();
    }

}
