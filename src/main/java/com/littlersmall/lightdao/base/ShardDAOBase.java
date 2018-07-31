package com.littlersmall.lightdao.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import com.google.common.base.CaseFormat;
import com.littlersmall.lightdao.annotation.PrimaryKey;
import com.littlersmall.lightdao.utils.ReflectUtils;

/**
 * Created by littlersmall on 2017/8/30.
 */
public interface ShardDAOBase<T> {
    int DEFAULT_SHARD_COUNT = 100;
    Map<Class<?>, Type> CLAZZ_MAPPER_MAP = new ConcurrentHashMap<>();
    Map<Class, RowMapper> ROW_MAPPER_MAP = new ConcurrentHashMap<>();
    Map<Class, String> TABLE_NAME_MAP = new ConcurrentHashMap<>();
    Map<Class, String> PRIMARY_KEY_NAME_MAP = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    default Class<T> getClazz() {
        return (Class<T>) CLAZZ_MAPPER_MAP.computeIfAbsent(this.getClass(), thisClazz -> {
            Type[] superInterfaces = thisClazz.getGenericInterfaces();

            return Arrays.stream(superInterfaces)
                    .filter(clazz -> clazz instanceof ParameterizedType)
                    .filter(clazz -> DAOBase.class.isAssignableFrom((Class<?>)((ParameterizedType) clazz).getRawType()))
                    .findAny()
                    .map(clazz -> ((ParameterizedType) clazz).getActualTypeArguments()[0])
                    .get();
        });
    }

    @SuppressWarnings("unchecked")
    default RowMapper<T> getRowMapper() {
        return ROW_MAPPER_MAP.computeIfAbsent(getClazz(), BeanPropertyRowMapper::new);
    }

    default int shardCount() {
        return DEFAULT_SHARD_COUNT;
    }

    default String getTableName(long shardId) {
        String name = TABLE_NAME_MAP.computeIfAbsent(getClazz(), (clazz) -> {
            String tableName = clazz.getSimpleName();
            tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, tableName);

            return tableName;
        });

        return name + "_" + shardId % shardCount();
    }

    default String getPrimaryKeyName() {
        return PRIMARY_KEY_NAME_MAP.computeIfAbsent(getClazz(), (clazz) ->
                Arrays.stream(clazz.getDeclaredFields())
                        .filter(field ->
                                field.getDeclaredAnnotation(PrimaryKey.class) != null)
                        .findAny()
                        .get()
                        .getName());
    }

    default long getPrimaryKey(Object model) {
        return (long) ReflectUtils.getField(model, getPrimaryKeyName());
    }
}
