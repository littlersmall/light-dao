package com.littlersmall.lightdao.executor;

import com.littlersmall.lightdao.exception.ExecutorException;
import com.littlersmall.lightdao.utils.PropertyInfo;
import com.littlersmall.lightdao.utils.ReflectTool;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.apache.commons.lang3.ClassUtils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sigh on 2016/1/20.
 */

public class RowMapperGenerator {
    //分两种情况
    //1 基础类型(int, long ,string, Integer 等等)
    //2 自定义类(例如自定义的User，or Thrift生成的Model类)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static RowMapper mapRow(final Class<?> clazz) {
        //1
        if (clazz.isPrimitive()) { //基本类型
            return new SingleColumnRowMapper(ClassUtils.primitiveToWrapper(clazz));
        } else if (ClassUtils.isPrimitiveWrapper(clazz)) { //包装类
            return new SingleColumnRowMapper(clazz);
        } else return new RowMapper() { //2
            public Object mapRow(ResultSet resultSet, int rowNum) {
                Object instance;

                try {
                    instance = clazz.newInstance();

                    for (PropertyInfo propertyInfo : ReflectTool.getPropertyNames(instance)) {
                        String name = propertyInfo.getName();
                        Class<?> type = propertyInfo.getType();
                        Object value = getValue(name, type, resultSet);

                        propertyInfo.setValue(value);
                    }
                } catch (IllegalAccessException e) {
                    throw new ExecutorException(e);
                } catch (InstantiationException e) {
                    throw new ExecutorException(e);
                }

                return instance;
            }
        };
    }

    static Object getValue(String valueName, Class<?> valueType, ResultSet resultSet) {
        Object value;

        try {
            if (valueType == String.class) {
                value = resultSet.getString(valueName);
            } else if (valueType == short.class
                    || valueType == Short.class) {
                value = resultSet.getShort(valueName);
            } else if (valueType == int.class
                    || valueType == Integer.class) {
                value = resultSet.getInt(valueName);
            } else if (valueType == long.class
                    || valueType == Long.class) {
                value = resultSet.getLong(valueName);
            } else if (valueType == float.class
                    || valueType == Float.class) {
                value = resultSet.getFloat(valueName);
            } else if (valueType == double.class
                    || valueType == Double.class) {
                value = resultSet.getFloat(valueName);
            } else if (valueType == Date.class) {
                value = resultSet.getDate(valueName);
            } else {
                throw new ExecutorException("the bean for return not legal " + valueType);
            }
        } catch (SQLException e) {
            throw new ExecutorException(e);
        }

        return value;
    }
}
