package com.incrys;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by User on 7/10/2017.
 */
public class JDBConnectionFactory {
    public static String JDBC_URL;
    public static String JDBC_USER;
    public static String JDBC_PWD;

    static {
        Properties properties = new Properties();
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream("db.properties");
            properties.load(inputStream);

            JDBC_URL=properties.getProperty("JDBC_URL");
            JDBC_USER=properties.getProperty("JDBC_USER");
            JDBC_PWD=properties.getProperty("JDBC_PWD");
            System.out.println("Connected to "+JDBC_URL);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if( inputStream!=null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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