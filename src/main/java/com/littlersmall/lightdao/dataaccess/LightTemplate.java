package com.littlersmall.lightdao.dataaccess;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by sigh on 2016/1/19.
 */
public class LightTemplate {
    private JdbcTemplate jdbcTemplate;

    public LightTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public <T> List<T> select(String sql, Object[] args, RowMapper rowMapper) {
        PreparedStatementCreator preparedStatementCreator = getPreparedCreator(sql, args);

        return (List<T>) jdbcTemplate.query(preparedStatementCreator, new RowMapperResultSetExtractor(rowMapper));
    }

    public int update(String sql, Object[] args) {
        PreparedStatementCreator preparedStatementCreator = getPreparedCreator(sql, args);

        return jdbcTemplate.update(preparedStatementCreator);
    }

    public void execute(String sql) throws DataAccessException {
        jdbcTemplate.execute(sql);
    }

    private PreparedStatementCreator getPreparedCreator(final String sql, final Object[] args) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                if (null != args) {
                    for (int i = 0; i < args.length; i++) {
                        Object arg = args[i];

                        if (arg instanceof SqlParameterValue) {
                            SqlParameterValue paramValue = (SqlParameterValue) arg;
                            StatementCreatorUtils.setParameterValue(preparedStatement, i + 1, paramValue, paramValue.getValue());
                        } else {
                            StatementCreatorUtils.setParameterValue(preparedStatement, i + 1, SqlTypeValue.TYPE_UNKNOWN, arg);
                        }
                    }
                }

                return preparedStatement;
            }
        };

        return preparedStatementCreator;
    }
}
