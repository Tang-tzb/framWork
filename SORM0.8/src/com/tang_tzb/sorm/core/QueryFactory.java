package com.tang_tzb.sorm.core;

/**
 * 负责根据配置信息创建query对象
 * @author Tang_tzb
 */
public class QueryFactory {
    /**
     * 用于克隆的原型Query对象
     */
    private static Query prototypeObj;
    static {
        try {
            Class c = Class.forName(DBManager.getConf().getQueryClass());
            prototypeObj = (Query) c.newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 克隆并返回一个Query对象
     * @return Query对象
     */
    public static Query creatQuery(){
        try {
            return (Query) prototypeObj.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
