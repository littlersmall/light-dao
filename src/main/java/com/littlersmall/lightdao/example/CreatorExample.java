package com.littlersmall.lightdao.example;

import com.littlersmall.lightdao.creator.DdlCreator;
import com.littlersmall.lightdao.creator.QueryCreator;
import com.littlersmall.lightdao.creator.UpdateCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * Created by littlersmall on 16/12/22.
 */
@Service
public class CreatorExample {
    @Autowired
    @Qualifier("MyDbDataSource")
    DataSource myDataSource;

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        CreatorExample creatorExample = ac.getBean(CreatorExample.class);

        DdlCreator ddlCreator = new DdlCreator(creatorExample.myDataSource);
        QueryCreator queryCreator = new QueryCreator(creatorExample.myDataSource);
        UpdateCreator updateCreator = new UpdateCreator(creatorExample.myDataSource);

        ddlCreator.execute("create table info (id int, information varchar, user_id int)");

        System.out.println(updateCreator.insert("info")
                .values(new InfoDao.Info(1, "littlersmall", 2))
                .execute());

        System.out.println(queryCreator.select(InfoDao.Info.class)
                .from("info")
                .execute());

        queryCreator.clear();

        System.out.println(queryCreator.select(InfoDao.Info.class)
                .from("info")
                .where("id = 1")
                .and("user_id = 2")
                .groupBy("id")
                .orderBy("user_id")
                .limit(1)
                .execute());
    }
}
