package com.littlersmall.lightdao.rowmapper;

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
    private final Object target;

    public GenerateRowMapper(Object target) {
        this.target = target;
    }

    public RowMapper mapRow() {
        return new RowMapper() {
            public Object mapRow(ResultSet resultSet, int rowNum) {
                Object instance = null;

                try {
                    for (PropertyInfo propertyInfo : ReflectTool.getPropertyNames(target)) {
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
                }

                return instance;
            }
        };
    }

    private Object getValue(String valueName, Class<?> valueType, ResultSet resultSet) throws SQLException {
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
