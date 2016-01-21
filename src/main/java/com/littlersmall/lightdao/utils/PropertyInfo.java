package com.littlersmall.lightdao.utils;

import com.google.common.base.CaseFormat;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by sigh on 2016/1/20.
 */
public class PropertyInfo {
    private final PropertyDescriptor propertyDescriptor;
    private final Object target;

    public PropertyInfo(PropertyDescriptor propertyDescriptor, Object target) {
        this.propertyDescriptor = propertyDescriptor;
        this.target = target;
    }

    public String getName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, propertyDescriptor.getName());
    }

    public Class<?> getType() {
        return propertyDescriptor.getPropertyType();
    }

    public void setValue(Object value) throws InvocationTargetException, IllegalAccessException {
        Method targetWriteMethod = propertyDescriptor.getWriteMethod();

        setValue(targetWriteMethod, new Object[] {value});
    }

    private void setValue(Method method, Object[] args) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        if (method == null || args == null) {
            //todo
        }

        if (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            method.setAccessible(true);
        }

        method.invoke(target, args);
    }
}
