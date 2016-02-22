package com.littlersmall.lightdao.spring;

import com.littlersmall.lightdao.annotation.Dao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sigh on 2016/2/15.
 */
@Log
public class BeanDefinitionGenerator {
    static PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
    static SimpleMetadataReaderFactory simpleMetadataReaderFactory = new CachingMetadataReaderFactory(pathMatchingResourcePatternResolver);

    @Data
    @AllArgsConstructor
    public static class BeanDefinitionWithDbName {
        BeanDefinition beanDefinition;
        String dbName;
    }

    //1 遍历classpath下的所有*Dao.class
    //2 获得ScannedGenericBeanDefinition
    //3 如果满足条件，则添加该ScannedGenericBeanDefinition
    public static List<BeanDefinitionWithDbName> findBeanDefinitions() throws IOException {
        List<BeanDefinitionWithDbName> beanDefinitions = new ArrayList<BeanDefinitionWithDbName>();
        //1
        Resource[] resources = pathMatchingResourcePatternResolver.getResources("classpath*:**/**Dao.class");

        for (Resource resource : resources) {
            if (resource.isReadable()) {
                log.info("resource is: " + resource);
                //2
                MetadataReader metadataReader = simpleMetadataReaderFactory.getMetadataReader(resource);
                ScannedGenericBeanDefinition scannedGenericBeanDefinition = new ScannedGenericBeanDefinition(metadataReader);

                scannedGenericBeanDefinition.setResource(resource);
                scannedGenericBeanDefinition.setSource(resource);

                //3
                String dbName = getDbName(scannedGenericBeanDefinition.getMetadata());

                if (null != dbName) {
                    log.info("beanId: " + scannedGenericBeanDefinition.getBeanClassName() + " dbName: " + dbName);
                    beanDefinitions.add(new BeanDefinitionWithDbName(scannedGenericBeanDefinition, dbName));
                }
            }
        }

        return beanDefinitions;
    }

    private static String getDbName(AnnotationMetadata annotationMetadata) {
        String dbName = null;
        String daoClassName = Dao.class.getName();

        if (annotationMetadata.isInterface()
            && annotationMetadata.hasAnnotation(daoClassName)) {
                dbName = (String) annotationMetadata.getAnnotationAttributes(daoClassName).get("dbName");
        }

        return dbName;
    }
}
