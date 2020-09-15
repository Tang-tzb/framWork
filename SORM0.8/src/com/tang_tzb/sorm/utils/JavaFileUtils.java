package com.tang_tzb.sorm.utils;

import com.tang_tzb.sorm.bean.ColumnInfo;
import com.tang_tzb.sorm.bean.JavaFieldGetSet;
import com.tang_tzb.sorm.bean.TableInfo;
import com.tang_tzb.sorm.core.DBManager;
import com.tang_tzb.sorm.core.MySqlTypeConvertor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 封装了Java文件（源代码）常用操作
 * @author Tang_tzb
 */
public class JavaFileUtils {
    /**
     * 生成数据库字段对应的Java属性和set、get方法的源码
     * @param column 字段名
     * @param convertor 类型转换器
     * @return 返回封装好属性和方法的JavaFieldGetSet对象
     */
    public static JavaFieldGetSet creatFieldGetSet(ColumnInfo column, MySqlTypeConvertor convertor){
        JavaFieldGetSet jfgs = new JavaFieldGetSet();
        String javaFieldType = convertor.dataBaseType2JavaType(column.getDataType());
        //private int userId;
        jfgs.setFieldInfo("\tprivate "+javaFieldType+" "+column.getName()+";\n");
        //public int getUserId(){return userId;}
        StringBuilder getSrc = new StringBuilder();
        getSrc.append("\tpublic "+javaFieldType+" get"+StringUtils.initial2UpperCase(column.getName())+"()"+"{\n");
        getSrc.append("\t\treturn "+column.getName()+";\n");
        getSrc.append("\t}\n");

        StringBuilder setSrc = new StringBuilder();
        setSrc.append("\tpublic void set"+StringUtils.initial2UpperCase(column.getName())+"("+javaFieldType+" "+column.getName() +")"+"{\n");
        setSrc.append("\t\tthis."+column.getName()+" = "+column.getName()+";\n");
        setSrc.append("\t}\n");
        jfgs.setGetInfo(getSrc.toString());
        jfgs.setSetInfo(setSrc.toString());
        return jfgs;
    }

    /**
     * 生成对应表的java类源代码
     * @param table 表信息的对象
     * @param convertor 类型转换器
     * @return java类源代码
     */
    public static String creatJavaSrc(TableInfo table, MySqlTypeConvertor convertor){
        Map<String,ColumnInfo> columns = table.getColumns();
        List<JavaFieldGetSet> fields = new ArrayList<>();
        for (ColumnInfo column:columns.values()){
            fields.add(JavaFileUtils.creatFieldGetSet(column,convertor));
        }
        StringBuilder src = new StringBuilder();
        //生成package语句
        src.append("package "+ DBManager.getConf().getPoPackage()+";\n\n");
        //生成import语句
        src.append("import java.util.*;\n");
        src.append("import java.sql.*;\n\n");
        //生成声明语句
        src.append("public class "+StringUtils.initial2UpperCase(table.getTname())+"{\n");
        //生成属性列
        for(JavaFieldGetSet f : fields){
            src.append(f.getFieldInfo());
        }
        //生成get方法
        for(JavaFieldGetSet f : fields){
            src.append(f.getGetInfo());
        }
        //生成set方法
        for(JavaFieldGetSet f : fields){
            src.append(f.getSetInfo());
        }
        //生成结束语句
        src.append("}");
        return src.toString();
    }

    /**
     * 在po包下生成表对应Java类
     * @param table 表的信息的对象
     * @param convertor 类型转换器
     */
    public static void createJavaPOFile(TableInfo table, MySqlTypeConvertor convertor){

        String src = creatJavaSrc(table,convertor);
        String srcPath = DBManager.getConf().getSrcPath()+"\\";
        String packagePath = DBManager.getConf().getPoPackage().replaceAll("\\.","/");
        File file = new File(srcPath + packagePath);
        if(!file.exists()){
            file.mkdirs();
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()+"\\"+
                StringUtils.initial2UpperCase(table.getTname())+".java"))) {
            bw.write(src);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {

    }
}
