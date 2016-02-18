package com.littlersmall.lightdao.spring.classgenerator;

import com.littlersmall.lightdao.dataaccess.DataSourceHolder;
import com.littlersmall.lightdao.example.UserDao;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.lang.reflect.Proxy;

/**
 * Created by sigh on 2016/2/2.
 */
public class LightDaoFactoryBean implements FactoryBean {
    private Class<?> objectType;
    private DataSourceHolder dataSourceHolder;

    public void setObjectType(Class<?> clazz) {
        objectType = clazz;
    }

    public Class<?> getObjectType() {
        return objectType;
    }

    public void setDataSourceHolder(DataSourceHolder dataSourceHolder) {
        this.dataSourceHolder = dataSourceHolder;
    }

    public boolean isSingleton() {
        return true;
    }

    //通过spring获得Dao bean时，实际调用此接口
    public Object getObject() {
        DaoInvocationHandler daoInvocationHandler = new DaoInvocationHandler(dataSourceHolder);

        return Proxy.newProxyInstance(objectType.getClassLoader(), new Class[] { objectType }, daoInvocationHandler);
    }
}
