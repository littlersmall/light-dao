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
 * Created by littlersmall on 2017/8/30.
 */
public interface ShardDAOBaseInsert<T> extends ShardDAOBase<T> {

    NamedParameterJdbcTemplate getWriter();

    default int insert(long shardId, T model) {
        return insert(shardId, model, null);
    }

    default int insert(long shardId, T model, NamedParameterJdbcTemplate template) {
        if (template == null) {
            template = getWriter();
        }

        return template.update(
                new SqlUpdateBuilder()
                        .insert(getTableName(shardId))
                        .columns(DAOUtils.toColumnStr(getClazz()))
                        .values(DAOUtils.toValueStr(getClazz()))
                        .build(),
                DAOUtils.toParameterSource(model));
    }

    //注意，replace 语句如有成功更新，返回2(not 1, 底层实现是先delete 后 insert，所以是2条)
    default int replace(long shardId, T model) {
        return replace(shardId, model, null);
    }

    default int replace(long shardId, T model, NamedParameterJdbcTemplate template) {
        if (template == null) {
            template = getWriter();
        }

        return template.update(
                new SqlUpdateBuilder()
                        .replace(getTableName(shardId))
                        .columns(DAOUtils.toColumnStr(getClazz()))
                        .values(DAOUtils.toValueStr(getClazz()))
                        .build(),
                DAOUtils.toParameterSource(model));
    }

    default long insertReturnKey(long shardId, T model) {
        return insertReturnKey(shardId, model, null);
    }

    default long insertReturnKey(long shardId, T model, NamedParameterJdbcTemplate template) {
        if (template == null) {
            template = getWriter();
        }

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        if (1 == template.update(
                new SqlUpdateBuilder()
                        .insert(getTableName(shardId))
                        .columns(DAOUtils.toColumnStr(getClazz()))
                        .values(DAOUtils.toValueStr(getClazz()))
                        .build(),
                DAOUtils.toParameterSource(model), keyHolder)) {
            return keyHolder.getKey().longValue();
        } else {
            throw new RuntimeException("cannot get id, " + getTableName(shardId));
        }
    }

    default int insertOnDuplicate(long shardId, T model, String... conditions) {
        return insertOnDuplicate(shardId, model, Arrays.asList(conditions));
    }

    default int insertOnDuplicate(long shardId, T model, List<String> conditions) {
        return insertOnDuplicate(shardId, model, null, conditions);
    }

    default int insertOnDuplicate(long shardId, T model, NamedParameterJdbcTemplate template, String... conditions) {
        return insertOnDuplicate(shardId, model, template, Arrays.asList(conditions));
    }

    default int insertOnDuplicate(long shardId, T model, NamedParameterJdbcTemplate template, List<String> conditions) {
        if (template == null) {
            template = getWriter();
        }

        return template.update(
                new SqlUpdateBuilder()
                        .insert(getTableName(shardId))
                        .columns(DAOUtils.toColumnStr(getClazz()))
                        .values(DAOUtils.toValueStr(getClazz()))
                        .onDuplicate(conditions)
                        .build(),
                DAOUtils.toParameterSource(model));
    }

    default <Key> int update(long shardId, long primaryKey, String keyName, Key key) {
        return update(shardId, primaryKey, keyName, key, null);
    }

    default <Key> int update(long shardId, long primaryKey, String keyName, Key key, NamedParameterJdbcTemplate template) {
        List<Map.Entry<String, Object>> entryList = new ArrayList<>();
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName, key));

        return update(shardId, primaryKey, entryList, template);
    }

    // CHECKSTYLE:OFF
    default <Key1, Key2> int update(long shardId, long primaryKey, String keyName1, Key1 key1, String keyName2, Key2 key2) {
        return update(shardId, primaryKey, keyName1, key1, keyName2, key2, null);
    }

    default <Key1, Key2> int update(long shardId, long primaryKey, String keyName1, Key1 key1, String keyName2, Key2 key2,
                                    NamedParameterJdbcTemplate template) {
        List<Map.Entry<String, Object>> entryList = new ArrayList<>();
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName1, key1));
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName2, key2));

        return update(shardId, primaryKey, entryList, template);
    }

    default <Key1, Key2, Key3> int update(long shardId, long primaryKey, String keyName1, Key1 key1, String keyName2, Key2 key2,
                                          String keyName3, Key3 key3) {
        return update(shardId, primaryKey, keyName1, key1, keyName2, key2, keyName3, key3, null);
    }

    default <Key1, Key2, Key3> int update(long shardId, long primaryKey, String keyName1, Key1 key1, String keyName2, Key2 key2,
                                          String keyName3, Key3 key3, NamedParameterJdbcTemplate template) {
        List<Map.Entry<String, Object>> entryList = new ArrayList<>();
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName1, key1));
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName2, key2));
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName3, key3));

        return update(shardId, primaryKey, entryList, template);
    }

    default <Key1, Key2, Key3, Key4> int update(long shardId, long primaryKey, String keyName1, Key1 key1, String keyName2, Key2 key2,
                                                String keyName3, Key3 key3, String keyName4, Key4 key4) {
        return update(shardId, primaryKey, keyName1, key1, keyName2, key2, keyName3, key3, keyName4, key4, null);
    }

    default <Key1, Key2, Key3, Key4> int update(long shardId, long primaryKey, String keyName1, Key1 key1, String keyName2, Key2 key2,
                                                String keyName3, Key3 key3, String keyName4, Key4 key4,
                                                NamedParameterJdbcTemplate template) {
        List<Map.Entry<String, Object>> entryList = new ArrayList<>();
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName1, key1));
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName2, key2));
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName3, key3));
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName4, key4));

        return update(shardId, primaryKey, entryList, template);
    }
    // CHECKSTYLE:ON

    default int update(long shardId, long primaryKey, List<Map.Entry<String, Object>> entryList, NamedParameterJdbcTemplate template) {
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
                        .update(getTableName(shardId))
                        .set(conditions)
                        .where(format("%s=:%s", dbPrimaryKeyName, getPrimaryKeyName()))
                        .build(),
                mapSqlParameterSource.addValue(getPrimaryKeyName(), primaryKey));
    }
}
