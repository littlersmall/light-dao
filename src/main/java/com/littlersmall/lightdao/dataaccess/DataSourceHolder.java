package com.littlersmall.lightdao.dataaccess;

import org.springframework.beans.factory.ListableBeanFactory;

/**
 * Created by sigh on 2016/2/14.
 */
public class DataSourceHolder {
    final private ListableBeanFactory beanFactory;
    final private String dbName;

    public DataSourceHolder(ListableBeanFactory beanFactory, String dbName) {
        this.beanFactory = beanFactory;
        this.dbName = dbName;
    }

    public LightTemplate getLightTemplate() {
        return JdbcTemplateGenerator.getOrCreateJdbcTemplate(beanFactory, dbName);
    }
}
