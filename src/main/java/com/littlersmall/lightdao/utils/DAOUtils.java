package com.littlersmall.lightdao.utils;

import static java.lang.String.format;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.collect.Range;

import lombok.extern.java.Log;

/**
 * Created by sigh on 2015/7/21.
 */
@Log
public class DAOUtils {
    public static <T, R> R withNull(T object, Function<T, R> f) {
        if (object == null) {
            return null;
        }
        return f.apply(object);
    }

    public static Long zeroToNull(long i) {
        return i == 0 ? null : i;
    }

    //只针对Model
    public static <T> MapSqlParameterSource toParameterSource(T model) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        Field[] fields = model.getClass().getDeclaredFields();

        Arrays.stream(fields).filter(field -> !Modifier.isStatic(field.getModifiers()))
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        mapSqlParameterSource.addValue(field.getName(), field.get(model));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        log.info("access field error");
                    }
                });

        return mapSqlParameterSource;
    }

    //产生sql字符串, 类似" user, name, user_info ", xx_xx的命名方式
    public static String toColumnStr(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<String> fieldList = Arrays.stream(fields)
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(field -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
                        field.getName()))
                .collect(Collectors.toList());

        return Joiner.on(", ").join(fieldList);
    }

    //产生sql字符串，类似" :user, :name, :userInfo ", xxXXX的命名方式
    public static String toValueStr(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<String> fieldList = Arrays.stream(fields)
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(field -> ":" + field.getName()).collect(Collectors.toList());

        return Joiner.on(", ").join(fieldList);
    }

    public static <Key extends Comparable> List<String> buildRangeConditions(MapSqlParameterSource mapSqlParameterSource,
                                                                             String rangeKeyName, Range<Key> range) {
        List<String> conditions = new ArrayList<>();

        if (range.hasLowerBound()) {
            if (range.contains(range.lowerEndpoint())) { // >=
                conditions.add(format(" %s >= :%s ", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, rangeKeyName)
                        , rangeKeyName + "Low"));
            } else { // >
                conditions.add(format(" %s > :%s ", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, rangeKeyName)
                        , rangeKeyName + "Low"));
            }
            mapSqlParameterSource.addValue(rangeKeyName + "Low", range.lowerEndpoint());
        }

        if (range.hasUpperBound()) {
            if (range.contains(range.upperEndpoint())) { // <=
                conditions.add(format(" %s <= :%s ", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, rangeKeyName)
                        , rangeKeyName + "Up"));
            } else { // <
                conditions.add(format(" %s < :%s ", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, rangeKeyName)
                        , rangeKeyName + "Up"));
            }
            mapSqlParameterSource.addValue(rangeKeyName + "Up", range.upperEndpoint());
        }

        return conditions;
    }
}
