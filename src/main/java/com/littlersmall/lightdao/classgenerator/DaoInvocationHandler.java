package com.littlersmall.lightdao.classgenerator;

import com.littlersmall.lightdao.dataaccess.GetJdbcTemplate;
import com.littlersmall.lightdao.sqlgenerator.SqlExecutor;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sigh on 2016/1/18.
 */
public class DaoInvocationHandler implements InvocationHandler {
    Map<Method, SqlExecutor> sqlExecutorMap = new ConcurrentHashMap<Method, SqlExecutor>();

    ListableBeanFactory beanFactory;
    String dbName;

    public DaoInvocationHandler(ListableBeanFactory beanFactory, String dbName) {
        this.beanFactory = beanFactory;
        this.dbName = dbName;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!sqlExecutorMap.containsKey(method)) {
            synchronized (method) {
                if (!sqlExecutorMap.containsKey(method)) {
                    JdbcTemplate jdbcTemplate = GetJdbcTemplate.getJdbcTemplate(beanFactory, dbName);
                    SqlExecutor sqlExecutor = new SqlExecutor(method, args, jdbcTemplate);
                    sqlExecutor.build();

                    sqlExecutorMap.put(method, sqlExecutor);
                }
            }
        }

        return sqlExecutorMap.get(method).execute();
    }
}
