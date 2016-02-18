package com.littlersmall.lightdao.dataaccess;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by sigh on 2016/1/19.
 */
//对应三种注解
// select -> @Select
// update -> @Update
// execute -> @Execute
public class LightTemplate {
    private JdbcTemplate jdbcTemplate;

    public LightTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
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

    //根据sql(已将参数替换成?)和args生成Prepare语句
    private PreparedStatementCreator getPreparedCreator(final String sql, final Object[] args) {
        return new PreparedStatementCreator() {
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
    }
}
