package com.littlersmall.lightdao.example;

import java.util.stream.Collectors;

import com.littlersmall.lightdao.annotation.PrimaryKey;
import com.littlersmall.lightdao.base.DAOBaseGet;
import com.littlersmall.lightdao.base.DAOBaseInsert;
import com.littlersmall.lightdao.creator.DdlCreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

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
    @Qualifier("MyDbDataSource")
    DataSource myDataSource;

    @Override
    public Class<Info> getClazz() {
        return Info.class;
    }

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
        creatorExample.insert(new Info(2, "2", 22));

        System.out.println(creatorExample.listAllDesc().collect(Collectors.toList()));
    }
}
