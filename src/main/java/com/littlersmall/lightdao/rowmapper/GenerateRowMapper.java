package com.littlersmall.lightdao.rowmapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sigh on 2016/1/20.
 */
public class GenerateRowMapper {
    public RowMapper mapRow(final Class<?> clazz) {
        return new RowMapper() {
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                Object instance = null;

                try {
                    instance = clazz.newInstance();


                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                return instance;
            }
        };
    }
}
