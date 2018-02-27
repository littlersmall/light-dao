package com.littlersmall.lightdao.creator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;

import lombok.NonNull;

/**
 * Created by littlersmall on 2017/10/30.
 */
public class SqlUpdateBuilder {

    private StringBuilder sql = new StringBuilder();

    public SqlUpdateBuilder insert(@NonNull String tableName) {
        sql.append("insert into ");
        sql.append(tableName);

        return this;
    }

    public SqlUpdateBuilder replace(@NonNull String tableName) {
        sql.append("replace into ");
        sql.append(tableName);

        return this;
    }

    public SqlUpdateBuilder update(@NonNull String tableName) {
        sql.append("update ");
        sql.append(tableName);

        return this;
    }

    //very dangerous
    public SqlUpdateBuilder delete(@NonNull String tableName) {
        sql.append("delete from ");
        sql.append(tableName);

        return this;
    }

    public SqlUpdateBuilder columns(@NonNull String... columns) {
        if (columns.length > 0) {
            sql.append(" ( ");
            sql.append(Joiner.on(",").join(columns));
            sql.append(" ) ");
        }

        return this;
    }

    public SqlUpdateBuilder columns(@NonNull String columnsStr) {
        sql.append(" ( ");
        sql.append(columnsStr);
        sql.append(" ) ");

        return this;
    }

    public SqlUpdateBuilder onDuplicate(@NonNull String... conditions) {
        return onDuplicate(Arrays.asList(conditions));
    }

    public SqlUpdateBuilder onDuplicate(@NonNull List<String> conditions) {
        if (!conditions.isEmpty()) {
            sql.append(" on duplicate key update ");
            sql.append(Joiner.on(",").join(conditions));
        }

        return this;
    }

    public SqlUpdateBuilder values(@NonNull String... valueParams) {
        if (valueParams.length > 0) {
            sql.append(" values( ");
            sql.append(Joiner.on(",").join(
                    Arrays.stream(valueParams)
                            .map(valueParam -> ":" + valueParam)
                            .collect(Collectors.toList())));
            sql.append(" ) ");
        }

        return this;
    }

    public SqlUpdateBuilder values(@NonNull String valueParamsStr) {
        sql.append(" values( ");
        sql.append(valueParamsStr);
        sql.append(" ) ");

        return this;
    }

    public SqlUpdateBuilder set(@NonNull String... conditions) {
        return set(Arrays.asList(conditions));
    }

    public SqlUpdateBuilder set(@NonNull List<String> conditions) {
        if (!conditions.isEmpty()) {
            sql.append(" set ");
            sql.append(Joiner.on(",").join(conditions));
        }

        return this;
    }

    public SqlUpdateBuilder and(@NonNull String... conditions) {
        if (conditions.length > 0) {
            sql.append(" and ( ");
            sql.append(Joiner.on(" and ").join(conditions));
            sql.append(" ) ");
        }

        return this;
    }

    public SqlUpdateBuilder or(@NonNull String... conditions) {
        if (conditions.length > 0) {
            sql.append(" or ( ");
            sql.append(Joiner.on(" or ").join(conditions));
            sql.append(" ) ");
        }

        return this;
    }

    public SqlUpdateBuilder where(@NonNull String... conditions) {
        return where(Arrays.asList(conditions));
    }

    public SqlUpdateBuilder where(@NonNull List<String> conditions) {
        if (!conditions.isEmpty()) {
            sql.append(" where ( ");
            sql.append(Joiner.on(" and ").join(conditions));
            sql.append(" ) ");
        }

        return this;
    }

    public SqlUpdateBuilder whereOr(@NonNull String... conditions) {
        return whereOr(Arrays.asList(conditions));
    }

    public SqlUpdateBuilder whereOr(@NonNull List<String> conditions) {
        if (!conditions.isEmpty()) {
            sql.append(" where ( ");
            sql.append(Joiner.on(" or ").join(conditions));
            sql.append(" ) ");
        }

        return this;
    }

    public String build() {
        return sql.toString();
    }
}
