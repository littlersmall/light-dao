package com.littlersmall.lightdao.base;

import static java.lang.String.format;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.github.phantomthief.util.CursorIterator;
import com.google.common.base.CaseFormat;
import com.littlersmall.lightdao.creator.SqlQueryBuilder;

/**
 * Created by littlersmall on 2017/4/20.
 */
public interface DAOBaseGet<T> extends DAOBase<T> {

    int BUFFER_SIZE = 200;

    NamedParameterJdbcTemplate getReader();

    default List<T> queryBySql(String sql, MapSqlParameterSource mapSqlParameterSource) {
        return getReader().query(sql, mapSqlParameterSource, getRowMapper());
    }

    //通过主键查询
    default <Key> T get(Key key) {
        return getByKey(getPrimaryKeyName(), key);
    }

    //通过唯一key获得行
    default <Key> T getByKey(String keyName, Key key) {
        try {
            final String dbKeyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, keyName);

            return getReader().queryForObject(
                    new SqlQueryBuilder()
                            .select("*")
                            .from(getTableName())
                            .where(format("%s=:%s", dbKeyName, keyName))
                            .build(),
                    new MapSqlParameterSource(keyName, key), getRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    //通过key获得行
    default <Key1, Key2> T getByKey(String keyName1, Key1 key1, String keyName2, Key2 key2) {
        try {
            final String dbKeyName1 = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, keyName1);
            final String dbKeyName2 = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, keyName2);


            return getReader().queryForObject(
                    new SqlQueryBuilder()
                            .select("*")
                            .from(getTableName())
                            .where(format("%s=:%s", dbKeyName1, keyName1), format("%s=:%s", dbKeyName2, keyName2))
                            .build(),
                    new MapSqlParameterSource(keyName1, key1).addValue(keyName2, key2),
                    getRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    default <Key> Stream<T> listByKeyDesc(String keyName, Key key) {
        List<Map.Entry<String, Object>> entryList = new ArrayList<>();
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName, key));

        return listByKeyDesc(entryList);
    }

    default <Key1, Key2> Stream<T> listByKeyDesc(String keyName1, Key1 key1, String keyName2, Key2 key2) {
        List<Map.Entry<String, Object>> entryList = new ArrayList<>();
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName1, key1));
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName2, key2));

        return listByKeyDesc(entryList);
    }

    default <Key1, Key2, key3> Stream<T> listByKeyDesc(String keyName1, Key1 key1, String keyName2, Key2 key2,
                                                       String keyName3, key3 key3) {
        List<Map.Entry<String, Object>> entryList = new ArrayList<>();
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName1, key1));
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName2, key2));
        entryList.add(new AbstractMap.SimpleImmutableEntry<>(keyName3, key3));

        return listByKeyDesc(entryList);
    }

    default Stream<T> listByKeyDesc(List<Map.Entry<String, Object>> entryList) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        List<String> conditions = new ArrayList<>();

        entryList.forEach(entry -> {
            conditions.add(format(" %s=:%s ", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entry.getKey()),
                    entry.getKey()));
            mapSqlParameterSource.addValue(entry.getKey(), entry.getValue());
        });

        final String dbPrimaryKeyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
                getPrimaryKeyName());

        return CursorIterator.<Long, T> newGenericBuilder().bufferSize(BUFFER_SIZE)
                .start(Long.MAX_VALUE).cursorExtractor(this::getPrimaryKey)
                .build((cursor, limit) -> getReader().query(
                        new SqlQueryBuilder()
                                .select("*")
                                .from(getTableName())
                                .where(conditions)
                                .and(format("%s<=:cursor", dbPrimaryKeyName))
                                .orderBy(dbPrimaryKeyName)
                                .desc()
                                .limit(":limit")
                                .build(),
                        mapSqlParameterSource.addValue("cursor", cursor)
                                .addValue("limit", limit),
                        getRowMapper()))
                .stream();
    }

    default <Key> Stream<T> listOppositeByKeyDesc(String keyName, Key key) {
        final String dbKeyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, keyName);
        final String dbPrimaryKeyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
                getPrimaryKeyName());

        return CursorIterator.<Long, T> newGenericBuilder().bufferSize(BUFFER_SIZE)
                .start(Long.MAX_VALUE).cursorExtractor(this::getPrimaryKey)
                .build((cursor, limit) -> getReader().query(
                        new SqlQueryBuilder().select("*")
                                .from(getTableName())
                                .where(format("%s!=:%s", dbKeyName, keyName))
                                .and(format("%s<=:cursor", dbPrimaryKeyName))
                                .orderBy(dbPrimaryKeyName)
                                .desc()
                                .limit(":limit")
                                .build(),
                        new MapSqlParameterSource(keyName, key).addValue("cursor", cursor)
                                .addValue("limit", limit),
                        getRowMapper()))
                .stream();
    }

    default Stream<T> listAllDesc() {
        final String dbPrimaryKeyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
                getPrimaryKeyName());
        return CursorIterator.<Long, T> newGenericBuilder().bufferSize(BUFFER_SIZE)
                .start(Long.MAX_VALUE).cursorExtractor(this::getPrimaryKey)
                .build((cursor, limit) -> getReader().query(
                        new SqlQueryBuilder()
                                .select("*")
                                .from(getTableName())
                                .where(format("%s<=:cursor", dbPrimaryKeyName))
                                .orderBy(dbPrimaryKeyName)
                                .desc()
                                .limit(":limit")
                                .build(),
                        new MapSqlParameterSource("cursor", cursor)
                                .addValue("limit", limit),
                        getRowMapper()))
                .stream();
    }

    default List<T> listAllDesc(long offset, int limit) {
        final String dbPrimaryKeyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
                getPrimaryKeyName());

        return getReader().query(
                new SqlQueryBuilder()
                        .select("*")
                        .from(getTableName())
                        .orderBy(dbPrimaryKeyName)
                        .desc()
                        .limit(":offset, :limit")
                        .build(),
                new MapSqlParameterSource("offset", offset).addValue("limit", limit),
                getRowMapper());
    }

    default <Key> List<T> listByKeyDesc(String keyName, Key key, long offset, int limit) {
        final String dbKeyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, keyName);
        final String dbPrimaryKeyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
                getPrimaryKeyName());

        return getReader().query(
                new SqlQueryBuilder()
                        .select("*")
                        .from(getTableName())
                        .where(format("%s=:%s", dbKeyName, keyName))
                        .orderBy(dbPrimaryKeyName)
                        .desc()
                        .limit(":offset,:limit")
                        .build(),
                new MapSqlParameterSource(keyName, key).addValue("offset", offset).addValue("limit",
                        limit),
                getRowMapper());
    }

    default <Key> List<T> listByKeySetDesc(String keyName, Collection<Key> keys) {
        final String dbKeyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, keyName);
        final String dbPrimaryKeyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
                getPrimaryKeyName());

        return getReader().query(
                new SqlQueryBuilder()
                        .select("*")
                        .from(getTableName())
                        .where(format("%s in (:%s)", dbKeyName, keyName))
                        .orderBy(dbPrimaryKeyName)
                        .desc()
                        .build(),
                new MapSqlParameterSource(keyName, keys),
                getRowMapper());
    }

    default <Key> long countByKey(String keyName, Key key) {
        final String dbKeyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, keyName);

        return getReader().queryForObject(
                new SqlQueryBuilder()
                        .select("count(1)")
                        .from(getTableName())
                        .where(format("%s=:%s", dbKeyName, keyName))
                        .build(),
                new MapSqlParameterSource(keyName, key),
                Long.class);
    }

    default <Key1, Key2> long countByKey(String keyName1, Key1 key1, String keyName2, Key2 key2) {
        final String dbKeyName1 = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, keyName1);
        final String dbKeyName2 = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, keyName2);

        return getReader().queryForObject(
                new SqlQueryBuilder()
                        .select("count(1)")
                        .from(getTableName())
                        .where(format("%s=:%s and %s=:%s", dbKeyName1, keyName1, dbKeyName2, keyName2))
                        .build(),
                new MapSqlParameterSource(keyName1, key1).addValue(keyName2, key2),
                Long.class);
    }
}
