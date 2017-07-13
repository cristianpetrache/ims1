package com.incrys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by User on 7/10/2017.
 */
public class JDBConnectionFactory {
    public static final String JDBC_URL = "jdbc:mysql://192.168.1.195:3306/t1database";
    public static final String JDBC_USER="voinic";
    public static final String JDBC_PWD="1234";

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection connection = null;

        try {
            connection= DriverManager.getConnection(JDBC_URL,JDBC_USER,JDBC_PWD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
}