package com.littlersmall.lightdao.classgenerator;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Proxy;

/**
 * Created by sigh on 2016/2/2.
 */
public class LightDaoFactoryBean implements FactoryBean, InitializingBean {
    protected Class<?> objectType;
    protected ListableBeanFactory beanFactory;
    protected String dbName;

    public void setObjectType(Class<?> clazz) {
        objectType = clazz;
    }

    public Class<?> getObjectType() {
        return objectType;
    }

    public void setBeanFactory(ListableBeanFactory  beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.isTrue(objectType.isInterface(), "not a interface class: " + objectType.getName());
        // cacheProvider可以null，不做assert.notNull判断
    }

    public Object getObject() {
        DaoInvocationHandler daoInvocationHandler = new DaoInvocationHandler(beanFactory, dbName);

        return Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(), new Class[] { objectType }, daoInvocationHandler);

        //try catch todo
    }
}
