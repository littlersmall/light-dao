package com.littlersmall.lightdao.sqlgenerator;

import com.littlersmall.lightdao.utils.PropertyInfo;
import com.littlersmall.lightdao.utils.ReflectTool;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sigh on 2016/1/20.
 */
public class GenerateRowMapper {
    public static <T> RowMapper mapRow(final Class<T> clazz) {
        return new RowMapper() {
            public Object mapRow(ResultSet resultSet, int rowNum) {
                Object instance = null;

                try {
                    instance = clazz.newInstance();

                    for (PropertyInfo propertyInfo : ReflectTool.getPropertyNames(instance)) {
                        String name = propertyInfo.getName();
                        Class<?> type = propertyInfo.getType();
                        Object value = getValue(name, type, resultSet);

                        propertyInfo.setValue(value);
                    }
                } catch (IllegalAccessException e) {
                    //todo
                } catch (InvocationTargetException e) {
                    //todo
                } catch (SQLException e) {
                    //todo
                } catch (InstantiationException e) {
                    //todo
                }

                return instance;
            }
        };
    }

    static <T> Object getValue(String valueName, Class<T> valueType, ResultSet resultSet) throws SQLException {
        Object value;

        if (valueType == String.class) {
            value = resultSet.getString(valueName);
        } else if (valueType == short.class
                || valueType == Short.class) {
           value = resultSet.getShort(valueName);
        }  else if (valueType == int.class
                || valueType == Integer.class) {
           value = resultSet.getInt(valueName);
        } else if (valueType == long.class
                || valueType == Long.class) {
           value = resultSet.getLong(valueName);
        } else {
            value = null;
            //todo
        }

        return value;
    }
}
