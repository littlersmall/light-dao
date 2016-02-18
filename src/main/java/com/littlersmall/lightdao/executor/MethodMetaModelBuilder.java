package com.littlersmall.lightdao.executor;

import com.littlersmall.lightdao.annotation.*;
import com.littlersmall.lightdao.enums.SqlType;
import com.littlersmall.lightdao.executor.model.MethodMetaModel;
import com.littlersmall.lightdao.utils.ReflectTool;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sigh on 2016/1/21.
 */
//构造一个Dao方法的元信息
public class MethodMetaModelBuilder {
    private final Method method;

    public MethodMetaModelBuilder(Method method) {
        this.method = method;
    }

    //1 构造原始sql，和sqlType
    //2 构造sqlParamMap
    //3 构造stringParamMap
    //4 构造返回类型
    public MethodMetaModel build() {
        MethodMetaModel methodMetaModel = new MethodMetaModel();

        //1
        conSqlAndType(methodMetaModel);
        //2
        conSqlParam(methodMetaModel);
        //3
        conStringParam(methodMetaModel);
        //4
        conReturnType(methodMetaModel);

        return methodMetaModel;
    }

    void conSqlAndType(MethodMetaModel methodMetaModel) {
        Annotation[] annotations = method.getAnnotations();
        String rawSql = null;
        SqlType sqlType = null;

        for (Annotation annotation : annotations) {
            if (annotation instanceof Select) {
                sqlType = SqlType.SELECT;
                rawSql = ((Select) annotation).value();
                break;
            } else if (annotation instanceof Update) {
                sqlType = SqlType.UPDATE;
                rawSql = ((Update) annotation).value();
                break;
            } else if (annotation instanceof Execute) {
                sqlType = SqlType.EXECUTE;
                rawSql = ((Execute) annotation).value();
                break;
            }
        }

        methodMetaModel.setSqlType(sqlType);
        methodMetaModel.setRawSql(rawSql);
    }

    private void conSqlParam(MethodMetaModel methodMetaModel) {
        Map<String, Integer> sqlParam = new HashMap<String, Integer>();
        Annotation[][] annotations = method.getParameterAnnotations();

        for (int index = 0; index < annotations.length; index++) {
            for (Annotation annotation : annotations[index]) {
                if (annotation instanceof SqlParam) {
                    sqlParam.put(((SqlParam) annotation).value(), index);
                }
            }
        }

        methodMetaModel.setSqlParamMap(sqlParam);
    }

    private void conStringParam(MethodMetaModel methodMetaModel) {
        Map<String, Integer> stringParam = new HashMap<String, Integer>();
        Annotation[][] annotations = method.getParameterAnnotations();

        for (int index = 0; index < annotations.length; index++) {
            for (Annotation annotation : annotations[index]) {
                if (annotation instanceof StringParam) {
                    stringParam.put(((StringParam) annotation).value(), index);
                }
            }
        }

        methodMetaModel.setStringParamMap(stringParam);
    }

    private void conReturnType(MethodMetaModel methodMetaModel) {
        boolean isReturnList = method.getReturnType().isAssignableFrom(List.class);
        Class<?> returnType;

        if (isReturnList) {
             returnType = ReflectTool.getActualClass(method.getGenericReturnType());
        } else {
            returnType = method.getReturnType();
        }

        methodMetaModel.setReturnList(isReturnList);
        methodMetaModel.setReturnType(returnType);
    }
}
