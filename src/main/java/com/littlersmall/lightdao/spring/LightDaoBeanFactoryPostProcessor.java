package com.littlersmall.lightdao.spring;

import com.littlersmall.lightdao.exception.SpringException;
import com.littlersmall.lightdao.spring.classgenerator.LightDaoFactoryBean;
import com.littlersmall.lightdao.dataaccess.DataSourceHolder;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by sigh on 2016/1/29.
 */
@Log
@Service
public class LightDaoBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    //1 扫描所有bean,找到符合要求的Dao类(返回BeanDefinition)
    //2 按LightDaoFactoryBean的标准进一步构建BeanDefinition并将构建好的Bean注册到spring的beanFactory中
    //3 将构建好的Bean注册到spring的beanFactory中
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
        try {
            log.info("start post process bean factory");
            //1
            List<BeanDefinitionGenerator.BeanDefinitionWithDbName> definitionWithDbNames = BeanDefinitionGenerator.findBeanDefinitions();

            for (BeanDefinitionGenerator.BeanDefinitionWithDbName definitionWithDbName : definitionWithDbNames) {
                String beanId = definitionWithDbName.beanDefinition.getBeanClassName();

                log.info("dao beanId is: " + beanId);

                //2
                BeanDefinition beanDefinition = conAndRegisterFactoryBean(beanFactory, definitionWithDbName);
                //3
                DefaultListableBeanFactory defaultBeanFactory = (DefaultListableBeanFactory) beanFactory;
                defaultBeanFactory.registerBeanDefinition(beanId, beanDefinition);
            }

            log.info("end post process bean factory");
        } catch (IOException e) {
            throw new SpringException(e);
        }
    }

    //1 构建DataSourceHolder(数据库访问对象)
    //2 构建LightDaoFactoryBean
    private BeanDefinition conAndRegisterFactoryBean(ConfigurableListableBeanFactory beanFactory,
            BeanDefinitionGenerator.BeanDefinitionWithDbName beanDefinitionWithDbName) {
        //1
        DataSourceHolder dataSourceHolder = new DataSourceHolder(beanFactory, beanDefinitionWithDbName.dbName);

        //2
        BeanDefinition beanDefinition = beanDefinitionWithDbName.getBeanDefinition();
        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();

        try {
            //获得Dao接口的Class对象
            Class<?> objectType = Class.forName(beanDefinition.getBeanClassName());
            propertyValues.addPropertyValue("objectType", objectType);
        } catch (ClassNotFoundException e) {
            throw new SpringException(e);
        }

        propertyValues.addPropertyValue("dataSourceHolder", dataSourceHolder);
        ScannedGenericBeanDefinition scannedBeanDefinition = (ScannedGenericBeanDefinition) beanDefinition;
        scannedBeanDefinition.setPropertyValues(propertyValues);
        scannedBeanDefinition.setBeanClass(LightDaoFactoryBean.class);

        return beanDefinition;
    }
}
