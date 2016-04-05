package com.littlersmall.lightdao.executor;

import com.littlersmall.lightdao.annotation.*;
import com.littlersmall.lightdao.enums.SqlType;
import com.littlersmall.lightdao.exception.ExecutorException;
import com.littlersmall.lightdao.executor.model.MethodMetaModel;
import com.littlersmall.lightdao.utils.ReflectTool;
import lombok.extern.java.Log;
import org.springframework.jdbc.core.RowMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sigh on 2016/1/21.
 */
//静态构造一个Dao方法的元信息
@Log
public class MethodMetaModelBuilder {
    private final Method method;

    public MethodMetaModelBuilder(Method method) {
        this.method = method;
    }

    //1 构造原始sql，和sqlType
    //2 构造ParamMap
    //3 构造返回类型
    //4 构建RowMapper
    public MethodMetaModel build() {
        MethodMetaModel methodMetaModel = new MethodMetaModel();

        //1
        conSqlAndType(methodMetaModel);
        //2
        conParamMap(methodMetaModel);
        //3
        conReturnType(methodMetaModel);
        //4
        conRowMapper(methodMetaModel);

        log.info("method name: " + method.getName() + " meta data: " + methodMetaModel);

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
            } else if (annotation instanceof BatchUpdate) {
                sqlType = SqlType.BATCH_UPDATE;
                rawSql = ((BatchUpdate) annotation).value();
            }
        }

        if (rawSql == null || sqlType == null) {
            throw new ExecutorException("@Select or @Update or @Execute use error");
        }

        methodMetaModel.setSqlType(sqlType);
        methodMetaModel.setRawSql(rawSql);
    }

    private void conParamMap(MethodMetaModel methodMetaModel) {
        Map<String, Integer> sqlParam = new HashMap<String, Integer>();
        Map<String, Integer> stringParam = new HashMap<String, Integer>();
        Annotation[][] annotations = method.getParameterAnnotations();

        for (int index = 0; index < annotations.length; index++) {
            for (Annotation annotation : annotations[index]) {
                if (annotation instanceof SqlParam) {
                    sqlParam.put(((SqlParam) annotation).value(), index);
                } else if (annotation instanceof StringParam) {
                    stringParam.put(((StringParam) annotation).value(), index);
                }
            }
        }

        methodMetaModel.setSqlParamMap(sqlParam);
        methodMetaModel.setStringParamMap(stringParam);
    }

    private void conReturnType(MethodMetaModel methodMetaModel) {
        //由于反射的擦除，所以相同, List<T>.class == List.class == List<U>.class
        boolean isReturnList = List.class.equals(method.getReturnType());
        Class<?> returnType;

        if (isReturnList) {
            returnType = ReflectTool.getActualClass(method.getGenericReturnType());
        } else {
            returnType = method.getReturnType();
        }

        methodMetaModel.setReturnList(isReturnList);
        methodMetaModel.setReturnType(returnType);
    }

    private void conRowMapper(MethodMetaModel methodMetaModel) {
        RowMapper rowMapper = RowMapperGenerator.mapRow(methodMetaModel.getReturnType());

        methodMetaModel.setRowMapper(rowMapper);
    }
}
