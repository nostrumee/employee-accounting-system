package com.innowise.accountingsystem.model.connection;

import com.innowise.accountingsystem.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {

    private static ConnectionPool instance;
    private static final Lock lock = new ReentrantLock();
    private static final AtomicBoolean isInstanceInitialized = new AtomicBoolean(false);

    private static final String DB_URL_KEY = "db.url";
    private static final String DB_USERNAME_KEY = "db.username";
    private static final String DB_PASSWORD_KEY = "db.password";
    private static final String DB_DRIVER_KEY = "db.driver";

    private static final String DB_URL;
    private static final String DB_USERNAME;
    private static final String DB_PASSWORD;
    private static final String DB_DRIVER;
    private static final int POOL_SIZE = 8;

    private final BlockingQueue<ProxyConnection> connections;

    static {
        DB_URL = PropertiesUtil.get(DB_URL_KEY);
        DB_USERNAME = PropertiesUtil.get(DB_USERNAME_KEY);
        DB_PASSWORD = PropertiesUtil.get(DB_PASSWORD_KEY);
        DB_DRIVER = PropertiesUtil.get(DB_DRIVER_KEY);
        loadDriver();
    }

    private ConnectionPool() {
        connections = new LinkedBlockingQueue<>();
        init();
    }

    private void init() {
        try {
            for (int i = 0; i < POOL_SIZE; i++) {
                createConnection();
            }
        } catch (InterruptedException | SQLException e) {
            //log.error
            throw new RuntimeException(e);
        }
    }

    public static ConnectionPool getInstance() {
        if (!isInstanceInitialized.get()) {
            try {
                lock.lock();
                if (instance == null) {
                    instance = new ConnectionPool();
                    isInstanceInitialized.set(true);
                }
            } finally {
                lock.unlock();
            }
        }

        return instance;
    }

    public Connection getConnection() {
        ProxyConnection connection = null;

        try {
            connection = connections.take();
        } catch (InterruptedException e) {
            //logger.error("Interrupted exception in get connection method", e);
            Thread.currentThread().interrupt();
        }

        return connection;
    }

    void releaseConnection(Connection connection) {
        if (connection instanceof ProxyConnection proxyConnection) {
            try {
                connections.put(proxyConnection);
            } catch (InterruptedException e) {
                //logger.error("Interrupted exception in release connection method", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    public void closePool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                connections.take().reallyClose();
            } catch (InterruptedException e) {
                //log.error("Interrupted exception in destroy pool method", e);
                Thread.currentThread().interrupt();
            } catch (SQLException e) {
                //logger.error("SQL exception in get destroy pool method", e);
            }
        }
    }

    private void createConnection() throws SQLException, InterruptedException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        ProxyConnection proxyConnection = new ProxyConnection(connection);
        connections.put(proxyConnection);
    }

    private static void loadDriver() {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            //log.error
            throw new RuntimeException(e);
        }
    }
}
