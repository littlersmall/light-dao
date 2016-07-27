package com.littlersmall.lightdao.executor.model;

import com.littlersmall.lightdao.enums.SqlType;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.util.Arrays;
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("SqlMetaModel is {");
        stringBuilder.append(sql);
        stringBuilder.append(" ");
        stringBuilder.append("[");

        for (Object[] objects : argsList) {
            stringBuilder.append("(");
            stringBuilder.append(Arrays.toString(objects));
            stringBuilder.append(") ");
        }

        stringBuilder.append("]");
        stringBuilder.append("}");

        return stringBuilder.toString();
    }
}
