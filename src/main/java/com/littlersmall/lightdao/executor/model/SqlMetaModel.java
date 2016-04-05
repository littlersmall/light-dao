package com.littlersmall.lightdao.executor.model;

import com.littlersmall.lightdao.enums.SqlType;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 * Created by sigh on 2016/2/18.
 */
@Data
public class SqlMetaModel {
    String sql;
    List<Object[]> argsList;
    SqlType sqlType;
    RowMapper rowMapper;
    boolean isReturnList;
}
