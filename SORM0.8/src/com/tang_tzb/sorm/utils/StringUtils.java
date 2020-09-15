package com.tang_tzb.sorm.utils;

/**
 * 封装了字符串常用操作
 * @author Tang_tzb
 */
public class StringUtils {
    /**
     * 将字符串的首字母转换成大写
     * @param str
     * @return
     */
    public static String initial2UpperCase(String str){
        return str.toUpperCase().substring(0,1)+str.substring(1);
    }
}
