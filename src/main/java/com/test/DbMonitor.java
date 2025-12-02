package com.test;

import com.core.PropertyManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbMonitor {
    private static final Logger logger = Logger.getLogger(DbMonitor.class);
    public static void main(String[] args) {
        try {
            String querySql = "SELECT SYSDATE FROM DUAL";
            Class.forName(PropertyManager.getInstance().getProperty("resultdb.driver"));
            String url = PropertyManager.getInstance().getProperty("resultdb.Url");
            String user = PropertyManager.getInstance().getProperty("resultdb.User");
            String password = PropertyManager.getInstance().getProperty("resultdb.Pwd");
            logger.info("begin to get number");
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                while (true) {
                    try (Statement statement = connection.createStatement()) {
                        statement.setQueryTimeout(10);
                        ResultSet resultSet = statement.executeQuery(querySql);
                        while (resultSet.next()) {
                            logger.info(resultSet.getString(1));
                        }
                    } catch (Exception e) {
                        logger.error(e.toString(), e);
                    }
                    Thread.sleep(10 * 1000);
                }
            }
        }catch (Exception e){
            logger.error(e.toString(),e);
        }
    }
}
