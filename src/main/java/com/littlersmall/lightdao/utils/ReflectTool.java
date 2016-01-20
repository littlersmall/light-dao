package com.littlersmall.lightdao.utils;

import com.google.common.base.CaseFormat;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sigh on 2016/1/20.
 */
public class ReflectTool {
    private static final String SETTER_PREFIX = "set";

    public static List<String> getPropertyNames(Class<?> clazz) {
        List<String> names = new ArrayList<String>();

        try {
            BeanInfo targetBeanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors = targetBeanInfo.getPropertyDescriptors();

            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String name = propertyDescriptor.getName();
                Method targetGet = propertyDescriptor.getReadMethod();
                Method targetSet = propertyDescriptor.getWriteMethod();

                if (null != targetGet && null != targetSet) {
                    String formatName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_UNDERSCORE, name);

                    names.add(formatName);
                }
            }
        } catch (Exception e) {

        }

        return names;
    }

    
}
