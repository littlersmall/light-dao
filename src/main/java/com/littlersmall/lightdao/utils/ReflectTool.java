package com.littlersmall.lightdao.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.ArrayList;
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

    public static Object getNamedField(Object target, String fieldName) {
        Object value = null;

        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, target.getClass());
            //通过get方法获得value
            Method getMethod = propertyDescriptor.getReadMethod();

            if (null != getMethod) {
                getMethod.setAccessible(true);
                value = getMethod.invoke(target);
            }

        } catch (NoSuchFieldException e) {
            //todo
        } catch (IntrospectionException e) {
            //todo
        } catch (InvocationTargetException e) {
            //todo
        } catch (IllegalAccessException e) {
            //todo
        }

        return value;
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
