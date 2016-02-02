package com.littlersmall.lightdao.spring;

import com.littlersmall.lightdao.annotation.TestClass;
import com.littlersmall.lightdao.classgenerator.LightDaoFactoryBean;
import com.littlersmall.lightdao.dataaccess.GetJdbcTemplate;
import javafx.util.Pair;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by sigh on 2016/1/29.
 */
public class LightDaoBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    DaoScan daoScan = new DaoScan();

    public final void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
            throws BeansException {

        doPostBeanFactory(beanFactory);
    }

    private void doPostBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        try {
            Set<Pair<BeanDefinition, String>> beanDefinitions = daoScan.findBeanDefinitions();

            for (Pair<BeanDefinition, String> beanDefinitionStringPair : beanDefinitions) {
                registerBean(beanFactory, beanDefinitionStringPair.getKey(), beanDefinitionStringPair.getValue());
            }
        } catch (IOException e) {
            //todo
        } catch (URISyntaxException e) {
            //todo
        }
    }

    private void registerBean(ConfigurableListableBeanFactory beanFactory, BeanDefinition beanDefinition, String dbName) {
        final String daoClassName = beanDefinition.getBeanClassName();
        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();

        JdbcTemplate jdbcTemplate = GetJdbcTemplate.getJdbcTemplate(beanFactory, dbName);
        propertyValues.addPropertyValue("objectType", daoClassName);
        propertyValues.addPropertyValue("jdbcTemplate", jdbcTemplate);

        ScannedGenericBeanDefinition scannedBeanDefinition = (ScannedGenericBeanDefinition) beanDefinition;
        scannedBeanDefinition.setPropertyValues(propertyValues);
        scannedBeanDefinition.setBeanClass(LightDaoFactoryBean.class);

        System.out.println(daoClassName);
        System.out.println(beanDefinition);

        DefaultListableBeanFactory defaultBeanFactory = (DefaultListableBeanFactory) beanFactory;
        defaultBeanFactory.registerBeanDefinition(daoClassName, beanDefinition);
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        LightDaoBeanFactoryPostProcessor lightDaoBeanFactoryPostProcessor = new LightDaoBeanFactoryPostProcessor();

        lightDaoBeanFactoryPostProcessor.doPostBeanFactory(null);
    }
}
