package com.littlersmall.lightdao.spring;

import javafx.util.Pair;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by sigh on 2016/2/1.
 */
public class DaoScan {
    private PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
    private SimpleMetadataReaderFactory simpleMetadataReaderFactory = new CachingMetadataReaderFactory(pathMatchingResourcePatternResolver);

    public Set<Pair<BeanDefinition, String>> findBeanDefinitions() throws IOException, URISyntaxException {
        Set<Pair<BeanDefinition, String>> beanDefinitions = new HashSet<Pair<BeanDefinition, String>>();

        for (String url : getAllDaoUrls()) {
            Resource[] resources = pathMatchingResourcePatternResolver.getResources(url + "**/**.class"); // + "**/*DAO.class");

            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = simpleMetadataReaderFactory.getMetadataReader(resource);

                    ScannedGenericBeanDefinition scannedGenericBeanDefinition = new ScannedGenericBeanDefinition(
                            metadataReader);

                    scannedGenericBeanDefinition.setResource(resource);
                    scannedGenericBeanDefinition.setSource(resource);

                    //System.out.println(scannedGenericBeanDefinition.getMetadata().getClassName());
                    if (scannedGenericBeanDefinition.getMetadata().isInterface()) {
                        String daoAnnotation = hasDao(scannedGenericBeanDefinition.getMetadata().getAnnotationTypes());

                        if (null != daoAnnotation) {
                            String dbName = (String) scannedGenericBeanDefinition.getMetadata().getAnnotationAttributes(daoAnnotation).get("dbName");
                            System.out.println(dbName);
                            beanDefinitions.add(new Pair(scannedGenericBeanDefinition, dbName));
                        }
                    }
                }
            }
        }

        return beanDefinitions;
    }

    private String hasDao(Set<String> annotationTypes) {
        for (String annotation : annotationTypes) {
            if (annotation.contains("Dao")) {
                return annotation;
            }
        }

        return null;
    }

    public List<String> getAllDaoUrls() throws IOException, URISyntaxException {
        List<String> urls = new ArrayList<String>();

        for (Resource resource : getResources()) {
            String urlStr = resource.getURL().toString();

            //if (urlStr.contains("dao") || urlStr.contains("DAO")) {
            File resourceFile = resource.getFile();
            if (resourceFile.isFile()) {
                urls.add("jar:file:" + resourceFile.toURI().getPath()
                        + ResourceUtils.JAR_URL_SEPARATOR);
            } else if (resourceFile.isDirectory()) {
                urls.add(resourceFile.toURI().toString());
            }
            //}
        }

        return urls;
    }

    private List<Resource> getResources() throws IOException, URISyntaxException {
        Set<Resource> classResources = getClassResources();
        Set<Resource> jarResources = getJarResources();

        classResources.addAll(jarResources);

        List<Resource> allResources = new ArrayList<Resource>();
        allResources.addAll(classResources);

        return allResources;
    }

    private Set<Resource> getClassResources() throws IOException, URISyntaxException {
        Set<Resource> resources = new HashSet<Resource>();
        Enumeration<URL> founds = pathMatchingResourcePatternResolver.getClassLoader().getResources("");

        while (founds.hasMoreElements()) {
            URL urlObject = founds.nextElement();

            if ("file".equals(urlObject.getProtocol())) {
                File file = new File(urlObject.toURI());

                if (!file.isFile()) {
                    Resource resource = new FileSystemResource(file);

                    resources.add(resource);
                }
            }
        }

        return resources;
    }

    private Set<Resource> getJarResources() throws IOException {
        Set<Resource> resources = new HashSet<Resource>();
        Resource[] metaInfResources = pathMatchingResourcePatternResolver.getResources("classpath*:/META-INF/");

        for (Resource metaInfResource : metaInfResources) {
            URL urlObject = metaInfResource.getURL();
            if (ResourceUtils.isJarURL(urlObject)) {
                String path = URLDecoder.decode(urlObject.getPath(), "UTF-8"); // fix 20%
                if (path.startsWith("file:")) {
                    path = path.substring("file:".length(), path.lastIndexOf('!'));
                } else {
                    path = path.substring(0, path.lastIndexOf('!'));
                }

                Resource resource = new FileSystemResource(path);

                resources.add(resource);
            }
        }

        return resources;
    }

    private List<Class<?>> getDaoInterfaces() {
        return null;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        /*
        LightDaoBeanFactoryPostProcessor lightDaoBeanFactoryPostProcessor = new LightDaoBeanFactoryPostProcessor();
        Set<BeanDefinition> beanDefinitions = lightDaoBeanFactoryPostProcessor.findBeanDefinitions();

        for (BeanDefinition beanDefinition : beanDefinitions) {
            //System.out.println(beanDefinition);
        }
        */
    }
}
