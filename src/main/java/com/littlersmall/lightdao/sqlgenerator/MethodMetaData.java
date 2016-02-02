package com.littlersmall.lightdao.sqlgenerator;

import com.littlersmall.lightdao.annotation.SqlParam;
import com.littlersmall.lightdao.annotation.StringParam;
import com.littlersmall.lightdao.utils.ReflectTool;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sigh on 2016/1/21.
 */
public class MethodMetaData {
    private final Method method;

    public MethodMetaData(Method method) {
        this.method = method;
    }

    public Annotation getSqlAnnotation() {
        Annotation[] annotations = method.getAnnotations();

        if (annotations.length != 1) {
            //todo
        }

        return annotations[0];
    }

    public Map<String, Integer> getSqlParam() {
        Map<String, Integer> sqlParam = new HashMap<String, Integer>();
        Annotation[][] annotations = method.getParameterAnnotations();

        for (int index = 0; index < annotations.length; index++) {
            for (Annotation annotation : annotations[index]) {
                if (annotation instanceof SqlParam) {
                    sqlParam.put(((SqlParam) annotation).value(), index);
                }
            }
        }

        return sqlParam;
    }

    public Map<String, Integer> getStringParam() {
        Map<String, Integer> stringParam = new HashMap<String, Integer>();
        Annotation[][] annotations = method.getParameterAnnotations();

        for (int index = 0; index < annotations.length; index++) {
            for (Annotation annotation : annotations[index]) {
                if (annotation instanceof StringParam) {
                    stringParam.put(((StringParam) annotation).value(), index);
                }
            }
        }

        return stringParam;
    }

    public boolean isReturnList() {
        return method.getReturnType().isAssignableFrom(List.class);
    }

    public Class<?> getReturnType() {
        if (isReturnList()) {
            return ReflectTool.getActualClass(method.getGenericReturnType());
        } else {
            return method.getReturnType();
        }
    }
}
