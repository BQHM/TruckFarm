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
            String driverClassName = getConfig(props, "jdbc.driver", "TRUCKFARM_JDBC_DRIVER");
            String url = getConfig(props, "jdbc.url", "TRUCKFARM_JDBC_URL");
            String user = getConfig(props, "jdbc.user", "TRUCKFARM_JDBC_USER");
            String pwd = getConfig(props, "jdbc.pwd", "TRUCKFARM_JDBC_PASSWORD");

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


    private static String resolvePlaceholders(String value) {
        if (value == null) {
            return null;
        }
        String result = value;
        int start = result.indexOf("${");
        while (start >= 0) {
            int end = result.indexOf("}", start);
            if (end < 0) {
                break;
            }
            String expression = result.substring(start + 2, end);
            String[] parts = expression.split(":", 2);
            String envValue = System.getenv(parts[0]);
            String replacement = envValue != null ? envValue : (parts.length > 1 ? parts[1] : "");
            result = result.substring(0, start) + replacement + result.substring(end + 1);
            start = result.indexOf("${", start + replacement.length());
        }
        return result;
    }
    private static String getConfig(Properties props, String propertyName, String envName) {
        String envValue = System.getenv(envName);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue;
        }
        return resolvePlaceholders(props.getProperty(propertyName));
    }
}



