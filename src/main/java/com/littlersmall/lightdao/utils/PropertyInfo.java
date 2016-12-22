package com.littlersmall.lightdao.utils;

import com.google.common.base.CaseFormat;
import com.littlersmall.lightdao.exception.ReflectException;

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

    public void setValue(Object value) {
        Method targetWriteMethod = propertyDescriptor.getWriteMethod();

        setValue(targetWriteMethod, new Object[] { value });
    }

    public Object getValue() {
        Method targetReadMethod = propertyDescriptor.getReadMethod();
        Object value = null;

        try {
            targetReadMethod.setAccessible(true);
            value = targetReadMethod.invoke(target);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return value;
    }

    private void setValue(Method method, Object[] args) {
        if (method == null || args == null) {
            throw new ReflectException("method is null or args is null");
        }

        if (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            method.setAccessible(true);
        }

        try {
            method.invoke(target, args);
        } catch (IllegalAccessException e) {
            throw new ReflectException(e);
        } catch (InvocationTargetException e) {
            throw new ReflectException(e);
        }
    }
}
