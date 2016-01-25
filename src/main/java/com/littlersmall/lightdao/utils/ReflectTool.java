package com.littlersmall.lightdao.utils;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sigh on 2016/1/20.
 */
public class ReflectTool {
    public static List<PropertyInfo> getPropertyNames(Object target) {
        List<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();

        try {
            BeanInfo targetBeanInfo = Introspector.getBeanInfo(target.getClass());
            PropertyDescriptor[] propertyDescriptors = targetBeanInfo.getPropertyDescriptors();

            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Method targetGet = propertyDescriptor.getReadMethod();
                Method targetSet = propertyDescriptor.getWriteMethod();

                if (null != targetGet && null != targetSet) {
                    propertyInfos.add(new PropertyInfo(propertyDescriptor, target));
                }
            }
        } catch (Exception e) {
            //todo
        }

        return propertyInfos;
    }

    public static Class<?> getActualClass(Type genericType) {
        Class<?> actualClass = null;

        if (genericType instanceof  ParameterizedType) {
            Type actualType = ((ParameterizedType) genericType).getActualTypeArguments()[0];

            if (actualType instanceof  Class<?>) {
                actualClass = (Class<?>) actualType;
            }
        } else {
            //todo
        }

        return actualClass;
    }

    public static void main(String[] args) {
        class A {
            List<String> myList;
        }

        List<Integer> testA = new ArrayList<Integer>();
        List<Long> testB = new ArrayList<Long>();
        List<A> testC = new ArrayList<A>();

        System.out.println(getActualClass(A.class.getDeclaredFields()[0].getGenericType()));
    }
}
