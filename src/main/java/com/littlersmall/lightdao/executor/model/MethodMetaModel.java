package com.littlersmall.lightdao.executor.model;

import com.littlersmall.lightdao.enums.SqlType;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.util.Map;

/**
 * Created by sigh on 2016/2/18.
 */
@Data
public class MethodMetaModel {
    String rawSql;
    SqlType sqlType;
    boolean isReturnList;
    Class<?> returnType;
    Map<String, Integer> sqlParamMap;
    Map<String, Integer> stringParamMap;
    RowMapper<?> rowMapper;
}
