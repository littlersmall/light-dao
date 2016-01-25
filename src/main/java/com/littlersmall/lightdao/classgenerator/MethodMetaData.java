package com.littlersmall.lightdao.classgenerator;

import com.littlersmall.lightdao.annotation.SqlParam;
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

    public Map<Integer, String> getSqlParam() {
        Map<Integer, String> sqlParam = new HashMap<Integer, String>();
        Annotation[][] annotations = method.getParameterAnnotations();

        for (int index = 0; index < annotations.length; index++) {
            for (Annotation annotation : annotations[index]) {
                if (annotation instanceof SqlParam) {
                    sqlParam.put(index, ((SqlParam) annotation).value());
                }
            }
        }

        return sqlParam;
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
