package com.tang_tzb.sorm.core;

import com.tang_tzb.sorm.bean.Configuration;
import com.tang_tzb.sorm.pool.DBConnPool;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * 根据配置信息维持连接对象的管理（增加连接池功能）
 * @author Tang_tzb
 */
public class DBManager {
    private static Configuration conf;
    private static DBConnPool pool;
    static {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        conf = new Configuration();
        conf.setPoolMaxSize(Integer.parseInt(properties.getProperty("poolMaxSize")));
        conf.setPoolMinSize(Integer.parseInt(properties.getProperty("poolMinSize")));
        conf.setQueryClass(properties.getProperty("queryClass"));
        conf.setDriver(properties.getProperty("driver"));
        conf.setPoPackage(properties.getProperty("poPackage"));
        conf.setPwd(properties.getProperty("pwd"));
        conf.setSrcPath(properties.getProperty("srcPath"));
        conf.setUrl(properties.getProperty("url"));
        conf.setUsingDB(properties.getProperty("usingDB"));
        conf.setUser(properties.getProperty("user"));

        try {
            Class.forName("com.tang_tzb.sorm.core.TableContext");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Configuration getConf() {
        return conf;
    }

    public static void setConf(Configuration conf) {
        DBManager.conf = conf;
    }

    public static Connection getConn(){
        if (pool == null){
            pool = new DBConnPool();
        }
        return pool.getConnection();
    }

    /**
     * 生成一个连接对象
     * @return 连接对象
     */
    public static Connection creatConn(){
        try {
            Class.forName(conf.getDriver());
            return DriverManager.getConnection(conf.getUrl(),
                    conf.getUser(),conf.getPwd());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭结果集，预编译，连接
     * @param rs 结果集
     * @param ps 预编译
     * @param conn 连接
     */
    public static void close(ResultSet rs, PreparedStatement ps, Connection conn){
        if (rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        close(ps,conn);
    }

    /**
     * 关闭预编译，连接
     * @param ps 预编译
     * @param conn 连接
     */
    public static void close( PreparedStatement ps,Connection conn){
        if(ps != null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        close(conn);
    }

    /**
     * 关闭连接
     * @param conn 连接
     */
    public static void close( Connection conn){
        pool.close(conn);
    }

    /**
     * 关闭Statement
     * @param statement
     */
    public static void close( Statement statement){
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}


