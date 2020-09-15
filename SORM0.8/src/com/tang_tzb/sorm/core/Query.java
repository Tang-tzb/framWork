package com.tang_tzb.sorm.core;

import com.tang_tzb.sorm.bean.ColumnInfo;
import com.tang_tzb.sorm.bean.TableInfo;
import com.tang_tzb.sorm.utils.JDBCUtils;
import com.tang_tzb.sorm.utils.ReflectUtils;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责查询（对外提供服务）
 * @author Tang_tzb
 */
@SuppressWarnings("all")
public abstract class Query implements Cloneable{
    /**
     * 直接执行一个DML语句
     * @param sql 所要执行的语句
     * @param params 语句中对应的参数
     * @return 执行后返回受影响的行数
     */
    public int executeDML(String sql, Object[] params){
        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;
        try {
            conn = DBManager.getConn();
            ps = conn.prepareStatement(sql);
            JDBCUtils.handleParams(ps,params);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBManager.close(ps,conn);
        }

        return count;
    }

    public Object executeQueryTemplate(String sql,Object[] params,Class clazz,CallBack back){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConn();
            ps = conn.prepareStatement(sql);
            JDBCUtils.handleParams(ps,params);//填充参数
            rs = ps.executeQuery();
            return back.doExecute(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBManager.close(rs, ps, conn);
        }
        return null;
    }

    /**
     * 将一个对象存储到数据库中
     * @param object 所要存储的对象
     * @return
     */
    public void insert(Object object){
        Class c = object.getClass();
        List<Object> params = new ArrayList<>();
        TableInfo table = TableContext.poClassTableMap.get(c);
        int countNotNullField = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("insert into "+table.getTname()+" (");
        Field[] fields = c.getDeclaredFields();
        for (Field field:fields){
            String fieldName = field.getName();
            Object fieldValue = ReflectUtils.invokeGet(fieldName,object);
            if (fieldValue != null){
                sql.append(fieldName+",");
                params.add(fieldValue);
                countNotNullField++;
            }
        }
        sql.setCharAt(sql.length()-1,')');

        sql.append(" values(");
        for (int i=0 ; i < countNotNullField ; i++){
            sql.append("?,");
        }
        sql.setCharAt(sql.length()-1,')');
        executeDML(sql.toString(),params.toArray());
    }

    /**
     * 删除对象在数据库中的记录（对象所在的类对应到表，对象的主键值对应到记录）
     * @param object 所要删除的对象
     */
    public void delete(Object object){
        Class clazz = object.getClass();
        TableInfo table = TableContext.poClassTableMap.get(clazz);
        ColumnInfo onlyPrikey = table.getOnlyPriKey();
        //通过反射调用属性的get方法
        Object priKeyValue = ReflectUtils.invokeGet(onlyPrikey.getName(),object);
        delete(clazz,priKeyValue);

    }

    /**
     * 删除clazz表示类对应在表中的记录（对应id的记录）
     * @param clazz 跟表对应类的class对象
     * @param priKey 主键的值
     */
    public void delete(Class clazz,Object priKey){
        TableInfo table = TableContext.poClassTableMap.get(clazz);
        ColumnInfo onlyPrikey = table.getOnlyPriKey();
        String sql = "delete from "+table.getTname()+" where "+ onlyPrikey.getName() +"=?";
        executeDML(sql,new Object[]{priKey});
    }

    /**
     * 更新对象，且只修改对应属性的值
     * @param object 所要更新的对象
     * @param fieldNames 所要更新的字段名
     * @return
     */
    public int update(Object object,String[] fieldNames){
        Class c = object.getClass();
        TableInfo table = TableContext.poClassTableMap.get(c);
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("update "+table.getTname()+" set ");
        for(String fieldName:fieldNames){
            Object fieldValue = ReflectUtils.invokeGet(fieldName,object);
            params.add(fieldValue);
            sql.append(fieldName + "=?,");
        }
        sql.setCharAt(sql.length()-1,' ');
        String priKey = table.getOnlyPriKey().getName();
        sql.append("where "+priKey+" = ?");
        params.add(ReflectUtils.invokeGet(priKey,object));
        executeDML(sql.toString(),params.toArray());
        return 0;
    }

    /**
     *  查询返回多行记录，并将多行记录封装到clazz指定的类的对象中
     * @param clazz 封装数据的Javabean类的Class对象
     * @param sql 查询语句
     * @param params 参数
     * @return
     */
    public List queryRows(final Class clazz,String sql,Object[] params){
       return (List) executeQueryTemplate(sql, params, clazz, new CallBack() {
            @Override
            public Object doExecute(ResultSet rs) {
                List list = null;
                try{
                  ResultSetMetaData metaData = rs.getMetaData();
                  while (rs.next()) {
                      if (list == null) {
                          list = new ArrayList();
                      }
                      Object rowObj = clazz.newInstance();//调用无参构造器
                      for (int i = 0; i < metaData.getColumnCount(); i++) {//获取每个列的值
                          String columnName = metaData.getColumnLabel(i + 1);
                          Object columnValue = rs.getObject(i + 1);
                          if (i == 2) {
                              int j = 0;
                          }
                          ReflectUtils.invokeSet(columnName, columnValue, rowObj);//通过反射调用set方法填充数据
                      }
                      list.add(rowObj);
                  }

              } catch (Exception e) {
                  e.printStackTrace();
              }
                return list;


            }
        });

    }

    /**
     * 查询返回一行记录，并将该值记录到clzz指定的类的对象中
     * @param clazz 封装数据的Javabean类的Class对象
     * @param sql 查询语句
     * @param params 参数
     * @return
     */
    public Object queryUniqueRow(Class clazz,String sql,Object[] params){
        List list = new MySqlQuery().queryRows(clazz,sql,params);
        return (list != null && list.size()>0)?list.get(0):null;
    }

    /**
     * 查询并返回一个值（一行一列），并将该值返回
     * @param sql 查询语句
     * @param params 参数
     * @return 查询到的结果
     */
    public Object queryValue(String sql,Object[] params) {
        return executeQueryTemplate(sql, params, null, new CallBack() {
            @Override
            public Object doExecute(ResultSet rs) {
                Object value = null;
                try {
                    while (rs.next()) {
                        value = rs.getObject(1);
                    }
                }catch (Exception e){
                    e.getStackTrace();
                    return null;
                }
                return value;
            }
        });
    }
    /**
     * 查询并返回一个数字（一行一列），并将该数字返回
     * @param sql 查询语句
     * @param params 参数
     * @return 查询到的数字
     */
    public Number queryNumber(String sql,Object[] params){

        return (Number)(new MySqlQuery().queryValue(sql, params));
    }

    /**
     * 分页查询
     * @param pageNum 第几页数据
     * @param Size 每页显示多少数据
     * @return 查询的结果
     */
    public abstract Object queryPageNate(int pageNum,int Size);

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
