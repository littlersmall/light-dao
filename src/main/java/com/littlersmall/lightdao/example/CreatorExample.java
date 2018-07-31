package com.littlersmall.lightdao.example;

import java.util.Date;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Range;
import com.littlersmall.lightdao.annotation.PrimaryKey;
import com.littlersmall.lightdao.base.DAOBaseGet;
import com.littlersmall.lightdao.base.DAOBaseInsert;
import com.littlersmall.lightdao.creator.DdlCreator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by littlersmall on 16/12/22.
 */
@Service
public class CreatorExample implements DAOBaseGet<CreatorExample.Info>, DAOBaseInsert<CreatorExample.Info> {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Info {
        @PrimaryKey
        int id;
        String information;
        int userId;
    }

    @Autowired
    @Qualifier("myDbDataSource")
    private DataSource myDataSource;

    /*
    @Override
    public Class<Info> getClazz() {
        return Info.class;
    }
    */

    @Override
    public NamedParameterJdbcTemplate getReader() {
        return new NamedParameterJdbcTemplate(myDataSource);
    }

    @Override
    public NamedParameterJdbcTemplate getWriter() {
        return new NamedParameterJdbcTemplate(myDataSource);
    }

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        CreatorExample creatorExample = ac.getBean(CreatorExample.class);

        DdlCreator ddlCreator = new DdlCreator(creatorExample.myDataSource);

        ddlCreator.execute("create table info (id int, information varchar, user_id int)");

        creatorExample.insert(new Info(1, "1", 11));
        creatorExample.insert(new Info(2, "1", 22));
        creatorExample.insert(new Info(3, "3", 33));

        //System.out.println(creatorExample.listAllDesc().collect(Collectors.toList()));

        System.out.println(creatorExample.listByRangeDesc("id", Range.open(1, 3)).collect(Collectors.toList()));
        System.out.println(creatorExample.sumByKey("id", 2, "userId"));
        System.out.println(creatorExample.listByKeyDesc("information", 1, "userId", 11).collect(Collectors.toList()));

        Date date = new Date(1000);

        java.sql.Date date1 = new java.sql.Date(1000);

        System.out.println(date.equals(date1));
        System.out.println(date1.equals(date));

    }
}
