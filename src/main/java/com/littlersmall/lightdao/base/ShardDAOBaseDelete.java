package com.littlersmall.lightdao.base;

import static java.lang.String.format;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.google.common.base.CaseFormat;
import com.littlersmall.lightdao.creator.SqlUpdateBuilder;

/**
 * Created by littlersmall on 2017/11/23.
 */
public interface ShardDAOBaseDelete <T> extends ShardDAOBase<T> {
    NamedParameterJdbcTemplate getWriter();

    default int delete(long shardId, long primaryKey) {
        return delete(shardId, primaryKey, null);
    }

    default int delete(long shardId, long primaryKey, NamedParameterJdbcTemplate template) {
        if (template == null) {
            template = getWriter();
        }

        final String dbPrimaryKeyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
                getPrimaryKeyName());

        return template.update(
                new SqlUpdateBuilder()
                        .delete(getTableName(shardId))
                        .where(format("%s=:%s", dbPrimaryKeyName, getPrimaryKeyName()))
                        .build(),
                new MapSqlParameterSource().addValue(getPrimaryKeyName(), primaryKey));
    }
}
