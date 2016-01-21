package com.littlersmall.lightdao.classgenerator;

import com.littlersmall.lightdao.annotation.Select;
import com.littlersmall.lightdao.annotation.SqlParam;
import com.littlersmall.lightdao.annotation.Update;
import com.littlersmall.lightdao.enums.SqlType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by sigh on 2016/1/21.
 */
public class MethodMetaData {
    private final Method method;
    private String rawSql;
    private SqlType sqlType;

    public MethodMetaData(Method method) {
        this.method = method;
    }

    public Class<?> getReturnTypeForRowMapper() {

    }

    public String getSqlForPrepareStatement() {

    }

    public SqlType getSqlType() {

    }

    public Object[] getArgsForPrepareStatement() {

    }

    private Annotation getSqlAnnotation() {
        Annotation[] annotations = method.getAnnotations();

        if (annotations.length != 1) {
            //todo
        }

        return annotations[0];
    }
}
