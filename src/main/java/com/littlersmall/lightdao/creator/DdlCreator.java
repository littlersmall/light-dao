package com.littlersmall.lightdao.creator;

import com.littlersmall.lightdao.dataaccess.LightTemplate;

import javax.sql.DataSource;

/**
 * Created by littlersmall on 16/12/22.
 */
public class DdlCreator {
    private LightTemplate lightTemplate;

    public DdlCreator(DataSource dataSource) {
        lightTemplate = new LightTemplate(dataSource);
    }

    public void execute(String sql) {
       lightTemplate.execute(sql);
    }
}
