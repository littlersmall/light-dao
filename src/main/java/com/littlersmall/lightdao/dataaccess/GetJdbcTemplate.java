package com.littlersmall.lightdao.dataaccess;

import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sigh on 2016/2/2.
 */
public class GetJdbcTemplate {
    static final String dataSourceSuffix = "DataSource";
    static Map<String, JdbcTemplate> jdbcTemplateMap = new ConcurrentHashMap<String, JdbcTemplate>();

    public static JdbcTemplate getJdbcTemplate(ListableBeanFactory beanFactory, String dbName) {
        String beanId =  CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, dbName.toLowerCase()) + dataSourceSuffix;

        if (!jdbcTemplateMap.containsKey(beanId)) {
            if (beanFactory.containsBeanDefinition(beanId)) {
                DataSource dataSource = beanFactory.getBean(beanId, DataSource.class);

                jdbcTemplateMap.put(beanId, new JdbcTemplate(dataSource));
            }
        }

        return jdbcTemplateMap.get(beanId);
    }
}
