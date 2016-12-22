package com.littlersmall.lightdao.creator;

import com.google.common.base.Joiner;
import com.littlersmall.lightdao.utils.DAOHelper;
import com.littlersmall.lightdao.utils.ReflectTool;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by littlersmall on 16/12/20.
 */
public abstract class SqlCreator<T> {
    protected StringBuilder sql = new StringBuilder();
    protected Class<?> returnType;

    public SqlCreator select(String columns) {
        returnType = null;
        sql.append("select ");
        sql.append(columns);

        return this;
    }

    public SqlCreator select(Class<?> clazz) {
        returnType = clazz;
        sql.append("select ");
        sql.append(DAOHelper.conColumnNames(clazz));

        return this;
    }

    public SqlCreator update(String tableName) {
        sql.append("update ");
        sql.append(tableName);

        return this;
    }

    public SqlCreator set(String expression) {
        sql.append("set ");
        sql.append(expression);

        return this;
    }

    public SqlCreator insert(String tableName) {
        sql.append("insert into ");
        sql.append(tableName);

        return this;
    }

    public SqlCreator insert(String tableName, String columns) {
        sql.append("insert into ");
        sql.append(tableName);
        sql.append("(");
        sql.append(columns);
        sql.append(")");

        return this;
    }

    public SqlCreator values(Object... columns) {
        sql.append(" values ");
        sql.append("(");
        sql.append(Joiner.on(", ").join(Arrays.stream(columns)
                .map(column -> {
                    if (column instanceof String) {
                        return "'" + column + "'";
                    } else {
                        return column.toString();
                    }
                }).collect(Collectors.toList())));
        sql.append(")");

        return this;
    }

    public <V> SqlCreator values(V bean) {
        return values(ReflectTool.getValues(bean).toArray());
    }

    public SqlCreator from(String tableName) {
        sql.append(" from ");
        sql.append(tableName);

        return this;
    }

    public SqlCreator join(String tableName) {
        sql.append(" join ");
        sql.append(tableName);

        return this;
    }

    public SqlCreator on(String condition) {
        sql.append(" on ");
        sql.append(condition);

        return this;
    }

    public SqlCreator where(String condition) {
        sql.append(" where ");
        sql.append(condition);

        return this;
    }

    public SqlCreator and(String condition) {
        sql.append(" and ");
        sql.append(condition);

        return this;
    }

    public SqlCreator groupBy(String... fields) {
        sql.append(" group by ");
        sql.append(Joiner.on(", ").join(fields));

        return this;
    }

    public SqlCreator having(String condition) {
        sql.append(" having ");
        sql.append(condition);

        return this;
    }

    public SqlCreator orderBy(String field) {
        sql.append(" order by ");
        sql.append(field);

        return this;
    }

    public SqlCreator asc() {
        sql.append(" asc ");

        return this;
    }

    public SqlCreator desc() {
        sql.append(" desc ");

        return this;
    }

    public SqlCreator limit(int num) {
        sql.append(" limit ");
        sql.append(num);

        return this;
    }

    public SqlCreator offset(long offset) {
        sql.append(" offset ");
        sql.append(offset);

        return this;
    }

    public void clear() {
        sql = new StringBuilder();
    }

    public abstract T execute();
}
