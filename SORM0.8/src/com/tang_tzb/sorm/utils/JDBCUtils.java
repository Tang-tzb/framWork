package com.tang_tzb.sorm.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 封装了 JDBC查询常用操作
 * @author Tang_tzb
 */
public class JDBCUtils {
    /**
     * 设置sql语句的参数
     * @param ps 预编译
     * @param params 参数
     */
    public static void handleParams(PreparedStatement ps , Object[] params){
        if(params != null){
          for(int i = 0 ; i < params.length ;i++){
              try {
                  ps.setObject(i+1,params[i]);
              } catch (SQLException e) {
                  e.printStackTrace();
              }
          }
        }
    }
}
