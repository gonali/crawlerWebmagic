package com.gonali.crawler.utils;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by TianyuanPan on 5/25/16.
 */
public class MySqlPoolUtils extends LoggerUtil {


    private static volatile MySqlPoolUtils pool;
    private MysqlDataSource dataSource;
    private Map<Connection, Boolean> connMap;

    private String url;
    private String username;
    private String password;
    private int initPoolSize = 10;
    private int maxPoolSize = 1000;
    private int waitTime = 100;

    private MySqlPoolUtils(ConfigUtils configUtils) {
        this.url = "jdbc:mysql://" + configUtils.getHostname() + ":" + configUtils.getPort() +
                "/" + configUtils.getDbName();
        this.username = configUtils.getUsername();
        this.password = configUtils.getPassword();
        init();
    }

    private MySqlPoolUtils(String hostname, int port, String dbName, String usrname, String password) {
        this.url = "jdbc:mysql://" + hostname + ":" + port +
                "/" + dbName;
        this.username = usrname;
        this.password = password;
        init();
    }

    public static MySqlPoolUtils getMySqlPoolUtils(ConfigUtils configUtils) {
        if (pool == null) {
            synchronized (MySqlPoolUtils.class) {
                if (pool == null) {
                    pool = new MySqlPoolUtils(configUtils);
                }
            }
        }
        return pool;
    }

    public static MySqlPoolUtils getMySqlPoolUtils(String hostname, int port, String dbName, String usrname, String password) {
        if (pool == null) {
            synchronized (MySqlPoolUtils.class) {
                if (pool == null) {
                    pool = new MySqlPoolUtils(hostname, port, dbName, usrname, password);
                }
            }
        }
        return pool;
    }


    private void init() {

        logger.info("connection url : " + this.url);
        logger.info("username: " + this.username + ", password: " + this.password);

        try {
            dataSource = new MysqlDataSource();
            dataSource.setUrl(url);
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setCacheCallableStmts(true);
            dataSource.setConnectTimeout(1000);
            dataSource.setLoginTimeout(2000);
            dataSource.setUseUnicode(true);
            dataSource.setEncoding("UTF-8");
            dataSource.setZeroDateTimeBehavior("convertToNull");
            dataSource.setMaxReconnects(5);
            dataSource.setAutoReconnect(true);
            connMap = new HashMap<Connection, Boolean>();
            for (int i = 0; i < initPoolSize; i++) {
                connMap.put(getNewConnection(), true);
            }
        } catch (Exception e) {
            logger.error("Init Mysql Pool Catch a exception, Message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Connection getNewConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("getNewConnection() SQL Exception, Message: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public synchronized Connection getConnection() {
        Connection conn = null;
        try {
            for (Entry<Connection, Boolean> entry : connMap.entrySet()) {
                if (entry.getValue()) {
                    conn = entry.getKey();
                    connMap.put(conn, false);
                    break;
                }
            }
            if (conn == null) {
                if (connMap.size() < maxPoolSize) {
                    conn = getNewConnection();
                    connMap.put(conn, false);
                } else {
                    wait(waitTime);
                    conn = getConnection();
                }
            }
        } catch (Exception e) {
            logger.error("getConnection() Exception, Message: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    public void releaseConnection(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            if (connMap.containsKey(conn)) {
                if (conn.isClosed()) {
                    connMap.remove(conn);
                } else {
                    if (!conn.getAutoCommit()) {
                        conn.setAutoCommit(true);
                    }
                    connMap.put(conn, true);
                }
            } else {
                conn.close();
            }
        } catch (SQLException e) {
            logger.error("releaseConnection() Exception, Message: " + e.getMessage());
            e.printStackTrace();
        }
    }

/*    private static volatile int a;
    private synchronized static void incr() {
        a++;
    }
    public static void main(String[] args) throws InterruptedException {
        int times = 1000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            new Thread(new Runnable() {

                @Override
                public void run() {

                    MySqlPoolUtils pool = MySqlPoolUtils.getMySqlPoolUtils(
                            "108.108.108.8", 3306, "wifi_ac", "builder", "builder"
                    );                    Connection conn = pool.getConnection();
                    Statement stmt = null;
                    ResultSet rs = null;
                    try {
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery("select * from tb_crawler");
                        while (rs.next()) {
                            System.out.println(rs.getInt(1) + ", "
                                    + rs.getString(2));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        incr();
                        if (rs != null) {
                            try {
                                rs.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (stmt != null) {
                            try {
                                stmt.close();
                            } catch (SQLException e) {
                            }
                        }
                        pool.releaseConnection(conn);
                    }
                }
            }).start();
        }
        while (true) {
            if (a == times) {
                System.out.println("finished, time:"
                        + (System.currentTimeMillis() - start));
                break;
            }
            Thread.sleep(100);
        }
    }

    */

}
