package com.littlersmall.lightdao.dataaccess;

import com.google.common.base.CaseFormat;
import com.littlersmall.lightdao.exception.DataAccessException;
import org.springframework.beans.factory.ListableBeanFactory;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sigh on 2016/2/2.
 */
public class JdbcTemplateGenerator {
    static final String dataSourceSuffix = "DataSource";
    static Map<String, LightTemplate> jdbcTemplateMap = new ConcurrentHashMap<String, LightTemplate>();

    //1 生成BeanId，(dbName + "DataSource")
    //2 查找该Datasource，并生成LightTemplate
    public static LightTemplate getOrCreateJdbcTemplate(ListableBeanFactory beanFactory, String dbName) {
        //1
        String beanId = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, dbName.toLowerCase()) + dataSourceSuffix;

        //2
        if (!jdbcTemplateMap.containsKey(beanId)) {
            synchronized (JdbcTemplateGenerator.class) {
                if (!jdbcTemplateMap.containsKey(beanId)) {
                    if (beanFactory.containsBeanDefinition(beanId)) {
                        DataSource dataSource = beanFactory.getBean(beanId, DataSource.class);

                        jdbcTemplateMap.put(beanId, new LightTemplate(dataSource));
                    } else {
                        throw new DataAccessException("no database :" + dbName);
                    }
                }
            }
        }

        return jdbcTemplateMap.get(beanId);
    }
}
