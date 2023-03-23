package com.innowise.accountingsystem.dao;

import com.innowise.accountingsystem.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class TestConnectionConfig {

    private static final String SQL_SCHEMA_PATH = "test-db-structure.sql";
    private static final String SQL_DATA_PATH = "test-db-data.sql";
    private static final String PROPERTIES_FILE_NAME = "test-db.properties";


    private static final String DB_URL_KEY = "url";
    private static final String DB_USERNAME_KEY = "username";
    private static final String DB_PASSWORD_KEY = "password";
    private static final String DB_DRIVER_KEY = "driver";
    private static final Properties PROPERTIES = new Properties();

    private static final String DB_URL;
    private static final String DB_PASSWORD;
    private static final String DB_USERNAME;
    private static final String DB_DRIVER;

    static {
        try (InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME)) {
            PROPERTIES.load(inputStream);
            DB_URL = PROPERTIES.getProperty(DB_URL_KEY);
            DB_USERNAME = PROPERTIES.getProperty(DB_USERNAME_KEY);
            DB_PASSWORD = PROPERTIES.getProperty(DB_PASSWORD_KEY);
            DB_DRIVER = PROPERTIES.getProperty(DB_DRIVER_KEY);
        } catch (IOException e) {
            log.error("error loading properties", e);
            throw new RuntimeException(e);
        }

        loadDriver();
    }

    Connection getConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            ClassLoader classLoader = TestConnectionConfig.class.getClassLoader();
            URL url = classLoader.getResource(SQL_SCHEMA_PATH);
            RunScript.execute(connection, new FileReader(url.getPath()));

        } catch (SQLException | FileNotFoundException e) {
            log.error("can not get connection to test db", e);
            throw new RuntimeException(e);
        }
        return connection;
    }

    void updateDatabase(Connection connection) {
        try {
            ClassLoader classLoader = TestConnectionConfig.class.getClassLoader();
            URL url = classLoader.getResource(SQL_DATA_PATH);
            RunScript.execute(connection, new FileReader(url.getPath()));
        } catch (SQLException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            log.error("cannot load database driver", e);
            throw new RuntimeException(e);
        }
    }
}
