package com.tang_tzb.sorm.pool;

import com.tang_tzb.sorm.core.DBManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 连接池的类
 * @author Tang_tzb
 */
public class DBConnPool {
    /**
     * 连接池存储connetion对象
     */
    private List<Connection> pool;
    /**
     * 连接池最大连接数
     */
    private final static int POOL_MAX_SIZE = DBManager.getConf().getPoolMaxSize();
    /**
     * 连接池最小连接数
     */
    private final static int POOL_MIN_SIZE = DBManager.getConf().getPoolMinSize();

    /**
     * 初始化连接池至最小连接数
     */
    public void initPool(){
        if(pool == null){
            pool = new ArrayList<>();
        }
        while (pool.size()<POOL_MIN_SIZE){
            pool.add(DBManager.creatConn());
        }
        System.out.println("初始化连接池，连接池数量："+ pool.size());
    }
    public DBConnPool(){
        initPool();
    }

    /**
     * 从连接池中获取连接
     * @return
     */
    public synchronized Connection getConnection(){
        if(pool.size()==0){
            initPool();
        }
        int lastIndex = pool.size()-1;
        Connection connection = pool.get(lastIndex);
        pool.remove(connection);
        return connection;
    }

    /**
     * 关闭连接，将连接添加到连接池中
     * @param conn
     */
    public synchronized void close(Connection conn){
        if (pool.size()>=POOL_MAX_SIZE){
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        pool.add(conn);
    }


}
