package com.littlersmall.lightdao.spring;

import com.littlersmall.lightdao.spring.classgenerator.LightDaoFactoryBean;
import com.littlersmall.lightdao.dataaccess.DataSourceHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import org.springframework.context.annotation.ScannedGenericBeanDefinition;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by sigh on 2016/1/29.
 */
public class LightDaoBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    //1 扫描所有bean,找到符合要求的Dao类(返回BeanDefinition)
    //2 按LightDaoFactoryBean的标准进一步构建BeanDefinition
    //3 将构建好的Bean注册到spring的beanFactory中
    public final void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
        try {
            //1
            List<BeanDefinitionGenerator.BeanDefinitionWithDbName> definitionWithDbNames = BeanDefinitionGenerator.findBeanDefinitions();

            for (BeanDefinitionGenerator.BeanDefinitionWithDbName definitionWithDbName : definitionWithDbNames) {
                //2
                BeanDefinition beanDefinition = conFactoryBean(beanFactory, definitionWithDbName);
                //3
                DefaultListableBeanFactory defaultBeanFactory = (DefaultListableBeanFactory) beanFactory;
                defaultBeanFactory.registerBeanDefinition(beanDefinition.getBeanClassName(), beanDefinition);
            }
        } catch (IOException e) {
            //todo
        }
    }

    //1 构建DataSourceHolder(数据库访问对象)
    //2 构建LightDaoFactoryBean
    private BeanDefinition conFactoryBean(ConfigurableListableBeanFactory beanFactory,
            BeanDefinitionGenerator.BeanDefinitionWithDbName beanDefinitionWithDbName) {
        //1
        DataSourceHolder dataSourceHolder = new DataSourceHolder(beanFactory, beanDefinitionWithDbName.dbName);

        //2
        BeanDefinition beanDefinition = beanDefinitionWithDbName.getBeanDefinition();
        final String daoClassName = beanDefinition.getBeanClassName();
        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();


        //后续spring在加载class时根据objectType的type(Class<?>)，通过forName(objectType)获得真正的类型
        propertyValues.addPropertyValue("objectType", daoClassName);
        propertyValues.addPropertyValue("dataSourceHolder", dataSourceHolder);

        ScannedGenericBeanDefinition scannedBeanDefinition = (ScannedGenericBeanDefinition) beanDefinition;
        scannedBeanDefinition.setPropertyValues(propertyValues);
        scannedBeanDefinition.setBeanClass(LightDaoFactoryBean.class);

        return beanDefinition;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        LightDaoBeanFactoryPostProcessor lightDaoBeanFactoryPostProcessor = new LightDaoBeanFactoryPostProcessor();

        lightDaoBeanFactoryPostProcessor.postProcessBeanFactory(null);
    }
}
