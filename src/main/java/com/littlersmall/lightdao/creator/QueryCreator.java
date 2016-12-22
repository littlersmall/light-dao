package com.littlersmall.lightdao.creator;

import com.littlersmall.lightdao.dataaccess.LightTemplate;
import com.littlersmall.lightdao.executor.RowMapperGenerator;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by littlersmall on 16/12/20.
 */
public class QueryCreator extends SqlCreator<List> {
    private LightTemplate lightTemplate;

    public QueryCreator(DataSource dataSource) {
        lightTemplate = new LightTemplate(dataSource);
    }

    @Override
    public List<?> execute() {
        if (returnType != null) {
            RowMapper<?> rowMapper = RowMapperGenerator.mapRow(returnType);

            return lightTemplate.select(sql.toString(), rowMapper);
        } else {
            return lightTemplate.select(sql.toString());
        }
    }
}
