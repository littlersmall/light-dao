package com.littlersmall.lightdao.utils;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.littlersmall.lightdao.example.InfoDao;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sigh on 2015/7/21.
 */
public class DAOHelper {
    //构造表名
    public static String conTableName(Class clazz) {
        String tableName = clazz.getSimpleName();

        if (tableName.startsWith("T")) {
            tableName = tableName.substring(1);
        }

        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, tableName);
    }

    //构造列名字符串，以','分隔
    public static final String conColumnNames(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<String> fieldList = new ArrayList<String>();

        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                fieldList.add(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName()));
            }
        }

        return Joiner.on(", ").join(fieldList);
    }

    //构造insert时的values字段
    public final static String conColumnParamNames(Class clazz) {
         Field[] fields = clazz.getDeclaredFields();
        List<String> fieldList = new ArrayList<String>();

        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                fieldList.add("{param." + field.getName() + "}");
            }
        }

        return Joiner.on(", ").join(fieldList);
    }

    public static void main(String[] args) {
        Class clazz = InfoDao.Info.class;

        System.out.println("String TABLE_NAME = " +  "\" " + conTableName(clazz) + " \";");
        System.out.println("String ALL_COLUMN = " + "\" " + conColumnNames(clazz) + " \";");
        System.out.println("String ALL_VALUES = " + "\" " + conColumnParamNames(clazz) + " \";");
    }
}
