package com.tang_tzb.sorm.core;

/**
 * 将Java的数据类型和数据库的数据类型进行互相转换
 * @author Tang_tzb
 */
public interface TypeConvertor {
    /**
     * 将数据库的数据类型转换成Java的数据类型
     * @param columnType 数据库的数据类型
     * @return java的数据类型
     */
    public String dataBaseType2JavaType(String columnType);
    /**
     * 将Java的数据类型转换成数据库的数据类型
     * @param javaDataType java的数据类型
     * @return 数据库的数据类型
     */
    public String JavaType2DataBaseType(String javaDataType);
}