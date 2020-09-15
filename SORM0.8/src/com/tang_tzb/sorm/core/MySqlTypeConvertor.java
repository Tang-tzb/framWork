package com.tang_tzb.sorm.core;



/**
 * 实现Mysql数据类型和java数据类型的转换
 * @author Tang_tzb
 */
public class MySqlTypeConvertor implements TypeConvertor {
    /**
     * 将Mysql数据库的数据类型转换成Java的数据类型
     * @param columnType Mysql数据库的数据类型
     * @return java的数据类型
     */
    @Override
    public String dataBaseType2JavaType(String columnType) {
        if("varchar".equalsIgnoreCase(columnType)||"char".equalsIgnoreCase(columnType)){

            return "String";
        }else if("int".equalsIgnoreCase(columnType)||"smallint".equalsIgnoreCase(columnType)
                ||"mediumint".equalsIgnoreCase(columnType)
                ||"tinyint".equalsIgnoreCase(columnType)
                ||"integer".equalsIgnoreCase(columnType)){
            return "Integer";
        }else if("bigint".equalsIgnoreCase(columnType)){
            return "Long";
        }else if("double".equalsIgnoreCase(columnType)||"float".equalsIgnoreCase(columnType)){
            return "Double";
        }else if("text".equalsIgnoreCase(columnType)){
            return "java.sql.Clob";
        }else if("blob".equalsIgnoreCase(columnType)){
            return "java.sql.Blob";
        } else if("date".equalsIgnoreCase(columnType)){
            return "java.sql.Date";
        }else if ("time".equalsIgnoreCase(columnType)){
            return "java.sql.Time";
        }else if("timestamp".equalsIgnoreCase(columnType)){
            return "java.sql.Timestamp";
        }
        return null;
    }
    /**
     * 将Java的数据类型转换成Mysql数据库的数据类型
     * @param javaDataType java的数据类型
     * @return Mysql数据库的数据类型
     */
    @Override
    public String JavaType2DataBaseType(String javaDataType) {
        if("String".equalsIgnoreCase(javaDataType)){

            return "varchar";
        }else if("Integer".equalsIgnoreCase(javaDataType)){
            return "int";
        }else if("Long".equalsIgnoreCase(javaDataType)){
            return "bigint";
        }else if("Double".equalsIgnoreCase(javaDataType)||"float".equalsIgnoreCase(javaDataType)){
            return "double";
        }else if("java.sql.Clob".equalsIgnoreCase(javaDataType)){
            return "text";
        }else if("java.sql.Blob".equalsIgnoreCase(javaDataType)){
            return "blob";
        } else if("java.sql.Date".equalsIgnoreCase(javaDataType)){
            return "date";
        }else if ("java.sql.Time".equalsIgnoreCase(javaDataType)){
            return "time";
        }else if("java.sql.Timestamp".equalsIgnoreCase(javaDataType)){
            return "timestamp";
        }
        return null;
    }
}
