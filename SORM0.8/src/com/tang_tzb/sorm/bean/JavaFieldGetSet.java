package com.tang_tzb.sorm.bean;

/**
 * 封装了Java的属性和set、get方法
 * @author Tang_tzb
 */
public class JavaFieldGetSet {
    /**
     * 属性的源码信息，如：private int userId;
     */
    private String fieldInfo;
    /**
     * get方法的源码信息，如：public int getUserId(){}
     */
    private String getInfo;
    /**
     * set方法的源码信息，如：public void setUserId(){}
     */
    private String setInfo;

    public String getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(String fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public String getGetInfo() {
        return getInfo;
    }

    public void setGetInfo(String getInfo) {
        this.getInfo = getInfo;
    }

    public String getSetInfo() {
        return setInfo;
    }

    public void setSetInfo(String setInfo) {
        this.setInfo = setInfo;
    }

    @Override
    public String toString() {
        System.out.println(fieldInfo);
        System.out.println(setInfo);
        System.out.println(getInfo);
        return super.toString();
    }
}
