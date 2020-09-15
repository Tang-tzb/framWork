package com.tang_tzb.sorm.utils;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 封装了反射常用操作
 * @author Tang_tzb
 */
public class ReflectUtils {
    /**
     * 调用object的属性对应的get方法
     * @param fieldName 属性名
     * @param object 映射表的类的对象
     * @return
     */
    public static Object invokeGet(String fieldName,Object object){
        Class c = object.getClass();
        try {
            Method m = c.getMethod("get"+StringUtils.initial2UpperCase(fieldName),null);
            Object priKeyValue = m.invoke(object,null);
            return priKeyValue;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void invokeSet(String columnName,Object columbValue,Object object){
        Class clazz = object.getClass();
        try {
            if(columbValue != null){
                Method m = clazz.getMethod("set" + StringUtils.initial2UpperCase(columnName), columbValue.getClass());
                m.invoke(object, columbValue);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
