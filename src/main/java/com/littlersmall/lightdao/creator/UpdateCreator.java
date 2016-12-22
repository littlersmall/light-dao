package com.littlersmall.lightdao.creator;

import com.littlersmall.lightdao.dataaccess.LightTemplate;

import javax.sql.DataSource;

/**
 * Created by littlersmall on 16/12/21.
 */
public class UpdateCreator extends SqlCreator<Integer> {
    private LightTemplate lightTemplate;

    public UpdateCreator(DataSource dataSource) {
        lightTemplate = new LightTemplate(dataSource);
    }

    @Override
    public Integer execute() {
        return lightTemplate.update(sql.toString());
    }
}