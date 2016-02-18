package com.littlersmall.lightdao.executor;

import com.littlersmall.lightdao.dataaccess.LightTemplate;
import com.littlersmall.lightdao.executor.model.SqlMetaModel;

import java.lang.reflect.Method;

/**
 * Created by sigh on 2016/1/29.
 */
//Dao接口函数的实际执行体
public class SqlExecutor {
    private SqlMetaModel sqlMetaModel;
    private LightTemplate lightTemplate;

    public static class Builder {
        final private Method method;
        final private Object[] rawArgs;
        final LightTemplate lightTemplate;

        public Builder(Method method, Object[] rawArgs, LightTemplate lightTemplate) {
            this.method = method;
            this.rawArgs = rawArgs;
            this.lightTemplate = lightTemplate;
        }

        public SqlExecutor build() {
            return new SqlExecutor(this);
        }
    }

    //1 构建SqlMetaData,sql元数据(sql语句，注解信息等)
    //2 获得数据库执行体
    public SqlExecutor(Builder builder) {
        //1
        sqlMetaModel = new SqlMetaModelBuilder(builder.method, builder.rawArgs).build();
        //2
        lightTemplate = builder.lightTemplate;
    }

    //Dao中函数的实际执行体
    //1 获得sql类型(select or update or execute)
    //2 调用jdbc执行sql
    public Object execute() {
        //1
        Object result = null;

        //2
        switch (sqlMetaModel.getSqlType()) {
        case EXECUTE:
            lightTemplate.execute(sqlMetaModel.getSql());
            result = null;
            break;
        case SELECT:
            if (sqlMetaModel.isReturnList()) {
                result = lightTemplate.select(sqlMetaModel.getSql(), sqlMetaModel.getArgs(), sqlMetaModel.getRowMapper());
            } else {
                result = lightTemplate.select(sqlMetaModel.getSql(), sqlMetaModel.getArgs(), sqlMetaModel.getRowMapper()).get(0);
                System.out.println(result);
                //todo
            }
            break;
        case UPDATE:
            result = lightTemplate.update(sqlMetaModel.getSql(), sqlMetaModel.getArgs());
            break;
        }

        return result;
    }
}
