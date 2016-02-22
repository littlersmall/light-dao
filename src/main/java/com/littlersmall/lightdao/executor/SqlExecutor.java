package com.littlersmall.lightdao.executor;

import com.littlersmall.lightdao.dataaccess.LightTemplate;
import com.littlersmall.lightdao.exception.ExecutorException;
import com.littlersmall.lightdao.executor.model.MethodMetaModel;
import com.littlersmall.lightdao.executor.model.SqlMetaModel;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by sigh on 2016/1/29.
 */
//Dao接口函数的实际执行体
public class SqlExecutor {
    private MethodMetaModel methodMetaModel;
    private LightTemplate lightTemplate;

    public static class Builder {
        final private Method method;
        final LightTemplate lightTemplate;

        public Builder(Method method, LightTemplate lightTemplate) {
            this.method = method;
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
        methodMetaModel = new MethodMetaModelBuilder(builder.method).build();
        //2
        lightTemplate = builder.lightTemplate;
    }

    //Dao中函数的实际执行体
    //1 动态构造sqlMetaModel
    //2 根据sql类型调用jdbc执行sql
    public Object execute(Object[] rawArgs) {
        //1
        SqlMetaModel sqlMetaModel = new SqlMetaModelBuilder(methodMetaModel, rawArgs).build();
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
                List<?> resultList = lightTemplate.select(sqlMetaModel.getSql(), sqlMetaModel.getArgs(), sqlMetaModel.getRowMapper());

                if (resultList.size() == 0) {
                    result = null;
                } else if (resultList.size() == 1) {
                    result = resultList.get(0);
                } else {
                    throw new ExecutorException("result size is: " + resultList.size());
                }
            }
            break;
        case UPDATE:
            result = lightTemplate.update(sqlMetaModel.getSql(), sqlMetaModel.getArgs());
            break;
        }

        return result;
    }
}
