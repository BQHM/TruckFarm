package com.iweb.utils;

import com.alibaba.druid.pool.DruidDataSource;

import javax.servlet.ServletConfig;
import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

public class DataSourceUtil {

    private final static String datasourceName = "datasourceConfiguration";

    public static DataSource initDataSource(ServletConfig config) {
        DruidDataSource dataSource = null;

        try {

            String datasourcePath = config.getInitParameter(datasourceName);
            InputStream stream = DataSourceUtil.class.getClassLoader().getResourceAsStream(datasourcePath);
            Properties props = new Properties();
            props.load(stream);
            String driverClassName = props.getProperty("jdbc.driver");
            String url = props.getProperty("jdbc.url");
            String user = props.getProperty("jdbc.user");
            String pwd = props.getProperty("jdbc.pwd");

            // 初始化数据库连接池
            dataSource = new DruidDataSource();
            dataSource.setDriverClassName(driverClassName);
            dataSource.setUrl(url);
            dataSource.setUsername(user);
            dataSource.setPassword(pwd);
//            System.out.println(dataSource.getConnection());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSource;

    }

}
