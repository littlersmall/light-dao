package com.littlersmall.lightdao.utils;

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


}
