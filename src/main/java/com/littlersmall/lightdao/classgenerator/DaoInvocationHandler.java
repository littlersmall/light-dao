package com.littlersmall.lightdao.classgenerator;

import com.littlersmall.lightdao.sqlgenerator.SqlExecutor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by sigh on 2016/1/18.
 */
public class DaoInvocationHandler implements InvocationHandler {
    SqlExecutor sqlExecutor;
    JdbcTemplate jdbcTemplate;

    public DaoInvocationHandler(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        synchronized (method) {
            if (null == sqlExecutor) {
                sqlExecutor = new SqlExecutor(method, args, jdbcTemplate);
            }
        }

        return sqlExecutor.execute();
    }
}
