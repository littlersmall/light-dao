package com.littlersmall.lightdao.utils;

import com.littlersmall.lightdao.exception.ReflectException;

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
                //getter
                Method targetGet = propertyDescriptor.getReadMethod();
                //setter
                Method targetSet = propertyDescriptor.getWriteMethod();

                if (null != targetGet && null != targetSet) {
                    propertyInfos.add(new PropertyInfo(propertyDescriptor, target));
                }
            }
        } catch (Exception e) {
            throw new ReflectException(e);
        }

        return propertyInfos;
    }

    //获得集合中的泛型
    //比如List<String>，返回String.class
    @SuppressWarnings({ "unchecked" })
    public static  Class<?> getActualClass(Type genericType) {
        Class<?> actualClass = null;

        if (genericType instanceof  ParameterizedType) {
            Type actualType = ((ParameterizedType) genericType).getActualTypeArguments()[0];

            if (actualType instanceof  Class<?>) {
                actualClass = (Class<?>) actualType;
            }
        } else {
            throw new ReflectException("Not a ParameterizedType");
        }

        return actualClass;
    }

    public static Object getFieldByName(Object target, String fieldName) {
        Object value = null;

        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, target.getClass());
            //通过get方法获得value
            Method getMethod = propertyDescriptor.getReadMethod();

            if (null != getMethod) {
                getMethod.setAccessible(true);
                value = getMethod.invoke(target);
            }

        } catch (IntrospectionException e) {
            throw new ReflectException(e);
        } catch (InvocationTargetException e) {
            throw new ReflectException(e);
        } catch (IllegalAccessException e) {
            throw new ReflectException(e);
        }

        return value;
    }

    /* for test */
    public static void main(String[] args) throws NoSuchMethodException {
        class MyClass {
            public List<String> getList() {
                return new ArrayList<String>();
            }
        }

        System.out.println(getActualClass(MyClass.class.getMethods()[0].getGenericReturnType()));
    }
}
