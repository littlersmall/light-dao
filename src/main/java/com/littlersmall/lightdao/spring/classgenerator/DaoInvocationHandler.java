package com.littlersmall.lightdao.spring.classgenerator;

import com.littlersmall.lightdao.dataaccess.DataSourceHolder;
import com.littlersmall.lightdao.executor.SqlExecutor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sigh on 2016/1/18.
 */
public class DaoInvocationHandler implements InvocationHandler {
    private Map<Method, SqlExecutor> sqlExecutorMap = new ConcurrentHashMap<Method, SqlExecutor>();
    private DataSourceHolder dataSourceHolder;

    public DaoInvocationHandler(DataSourceHolder dataSourceHolder) {
        this.dataSourceHolder = dataSourceHolder;
    }

    //对应于Dao接口里的函数
    //将每一个函数映射为一个SqlExecutor
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!sqlExecutorMap.containsKey(method)) {
            synchronized (method) {
                if (!sqlExecutorMap.containsKey(method)) {
                    SqlExecutor sqlExecutor = new SqlExecutor.Builder(method, args, dataSourceHolder.getLightTemplate()).build();

                    sqlExecutorMap.put(method, sqlExecutor);
                }
            }
        }

        return sqlExecutorMap.get(method).execute();
    }
}
