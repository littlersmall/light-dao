package com.littlersmall.lightdao.base;

import static java.lang.String.format;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.google.common.base.CaseFormat;
import com.littlersmall.lightdao.creator.SqlUpdateBuilder;
import com.littlersmall.lightdao.utils.DAOUtils;

/**
 * Created by littlersmall on 2017/4/21.
 */
public interface DAOBaseInsert<T> extends DAOBase<T> {
    NamedParameterJdbcTemplate getWriter();

    /*
     insert into TABLE(column1, column2...) values (value1, value2...);
     */
    default int insert(T model) {
        return insert(model, null);
    }

    default int insert(T model, NamedParameterJdbcTemplate template) {
        if (template == null) {
            template = getWriter();
        }

        return template.update(
                new SqlUpdateBuilder()
                        .insert(getTableName())
                        .columns(DAOUtils.toColumnStr(getClazz()))
                        .values(DAOUtils.toValueStr(getClazz()))
                        .build(),
                DAOUtils.toParameterSource(model));
    }

    //注意，replace 语句如有成功更新，返回2(not 1, 底层实现是先delete 后 insert，所以是2条)
    //如果只是插入，或者该行数据完全无修改，返回1
    /*
     replace into TABLE(column1, column2...) values (value1, value2...);
     */
    default int replace(T model) {
        return replace(model, null);
    }

    default int replace(T model, NamedParameterJdbcTemplate template) {
        if (template == null) {
            template = getWriter();
        }

        return template.update(
                new SqlUpdateBuilder()
                        .replace(getTableName())
                        .columns(DAOUtils.toColumnStr(getClazz()))
                        .values(DAOUtils.toValueStr(getClazz()))
                        .build(),
                DAOUtils.toParameterSource(model));
    }

    default long insertReturnKey(T model) {
        return insertReturnKey(model, null);
    }

    default long insertReturnKey(T model, NamedParameterJdbcTemplate template) {
        if (template == null) {
            template = getWriter();
        }

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        if (1 == template.update(
                new SqlUpdateBuilder()
                        .insert(getTableName())
                        .columns(DAOUtils.toColumnStr(getClazz()))
                        .values(DAOUtils.toValueStr(getClazz()))
                        .build(),
                DAOUtils.toParameterSource(model), keyHolder)) {
            return keyHolder.getKey().longValue();
        } else {
            throw new RuntimeException("cannot get id, " + getTableName());
        }
    }

    /*
     insert into TABLE(column1, column2...) values(value1, value2...) on duplicate key update conditions
     */
    default int insertOnDuplicate(T model, String... conditions) {
        return insertOnDuplicate(model, Arrays.asList(conditions));
    }

    default int insertOnDuplicate(T model, List<String> conditions) {
        return insertOnDuplicate(model, null, conditions);
    }

    default int insertOnDuplicate(T model, NamedParameterJdbcTemplate template, String... conditions) {
        return insertOnDuplicate(model, template, Arrays.asList(conditions));
    }

    default int insertOnDuplicate(T model, NamedParameterJdbcTemplate template, List<String> conditions) {
        if (template == null) {
            template = getWriter();
        }

        return template.update(
                new SqlUpdateBuilder()
                        .insert(getTableName())
                        .columns(DAOUtils.toColumnStr(getClazz()))
                        .values(DAOUtils.toValueStr(getClazz()))
                        .onDuplicate(conditions)
                        .build(),
                DAOUtils.toParameterSource(model));
    }

    /*
     update TABLE set key_name = key where primary_key = primaryKey
     */
    default <Key> int update(long primaryKey, String keyName, Key key) {
        return update(primaryKey, keyName, key, null);
    }

    default <Key> int update(long primaryKey, String keyName, Key key, NamedParameterJdbcTemplate template) {
        List<Map.Entry<String, Object>> entryList = new ArrayList<>();
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName, key));

        return update(primaryKey, entryList, template);
    }

    // CHECKSTYLE:OFF
    default <Key1, Key2> int update(long primaryKey, String keyName1, Key1 key1, String keyName2, Key2 key2) {
        return update(primaryKey, keyName1, key1, keyName2, key2, null);
    }

    default <Key1, Key2> int update(long primaryKey, String keyName1, Key1 key1, String keyName2, Key2 key2,
                                    NamedParameterJdbcTemplate template) {
        List<Map.Entry<String, Object>> entryList = new ArrayList<>();
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName1, key1));
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName2, key2));

        return update(primaryKey, entryList, template);
    }

    default <Key1, Key2, Key3> int update(long primaryKey, String keyName1, Key1 key1, String keyName2, Key2 key2,
                                          String keyName3, Key3 key3) {
        return update(primaryKey, keyName1, key1, keyName2, key2, keyName3, key3, null);
    }

    default <Key1, Key2, Key3> int update(long primaryKey, String keyName1, Key1 key1, String keyName2, Key2 key2,
                                          String keyName3, Key3 key3, NamedParameterJdbcTemplate template) {
        List<Map.Entry<String, Object>> entryList = new ArrayList<>();
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName1, key1));
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName2, key2));
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName3, key3));

        return update(primaryKey, entryList, template);
    }

    default <Key1, Key2, Key3, Key4> int update(long primaryKey, String keyName1, Key1 key1, String keyName2, Key2 key2,
                                                String keyName3, Key3 key3, String keyName4, Key4 key4) {
        return update(primaryKey, keyName1, key1, keyName2, key2, keyName3, key3, keyName4, key4, null);
    }

    default <Key1, Key2, Key3, Key4> int update(long primaryKey, String keyName1, Key1 key1, String keyName2, Key2 key2,
                                                String keyName3, Key3 key3, String keyName4, Key4 key4,
                                                NamedParameterJdbcTemplate template) {
        List<Map.Entry<String, Object>> entryList = new ArrayList<>();
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName1, key1));
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName2, key2));
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName3, key3));
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName4, key4));

        return update(primaryKey, entryList, template);
    }
    // CHECKSTYLE:ON

    default int update(long primaryKey, List<Map.Entry<String, Object>> entryList, NamedParameterJdbcTemplate template) {
        if (template == null) {
            template = getWriter();
        }

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        List<String> conditions = new ArrayList<>();

        entryList.forEach(entry -> {
            conditions.add(format(" %s=:%s ", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entry.getKey()),
                    entry.getKey()));
            mapSqlParameterSource.addValue(entry.getKey(), entry.getValue());
        });

        final String dbPrimaryKeyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
                getPrimaryKeyName());

        return template.update(
                new SqlUpdateBuilder()
                        .update(getTableName())
                        .set(conditions)
                        .where(format("%s=:%s", dbPrimaryKeyName, getPrimaryKeyName()))
                        .build(),
                mapSqlParameterSource.addValue(getPrimaryKeyName(), primaryKey));
    }
}
