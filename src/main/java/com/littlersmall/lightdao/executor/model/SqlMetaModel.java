package com.littlersmall.lightdao.executor.model;

import com.littlersmall.lightdao.enums.SqlType;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

/**
 * Created by sigh on 2016/2/18.
 */
@Data
public class SqlMetaModel {
    String sql;
    SqlType sqlType;
    Object[] args;
    RowMapper rowMapper;
    boolean isReturnList;
}
