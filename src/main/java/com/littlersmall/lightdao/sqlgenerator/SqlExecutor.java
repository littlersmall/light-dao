package com.littlersmall.lightdao.sqlgenerator;

import com.littlersmall.lightdao.dataaccess.LightTemplate;
import com.littlersmall.lightdao.enums.SqlType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Method;

/**
 * Created by sigh on 2016/1/29.
 */
public class SqlExecutor {
    SqlMetaData sqlMetaData;
    LightTemplate lightTemplate;

    public SqlExecutor(Method method, Object[] rawArgs, JdbcTemplate jdbcTemplate) {
        sqlMetaData = new SqlMetaData(method, rawArgs);
        lightTemplate = new LightTemplate(jdbcTemplate);

        System.out.println(method.getName());
    }

    public void build() {
        sqlMetaData.build();
    }

    public Object execute() {
        SqlMetaData.SqlModel sqlModel = sqlMetaData.getSqlModel();
        Object result = null;

        switch (sqlModel.getSqlType()) {
        case EXECUTE:
            lightTemplate.execute(sqlModel.getSql());
            result = null;
            break;
        case SELECT:
            if (sqlModel.isReturnList()) {
                result = lightTemplate.select(sqlModel.getSql(), sqlModel.getArgs(), sqlModel.getRowMapper());
            } else {
                result = lightTemplate.select(sqlModel.getSql(), sqlModel.getArgs(), sqlModel.getRowMapper()).get(0);
                //todo
            }
            break;
        case UPDATE:
            result = lightTemplate.update(sqlModel.getSql(), sqlModel.getArgs());
            break;
        }

        return result;
    }
}
