package com.littlersmall.lightdao.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sigh on 2016/2/3.
 */
@Service
public class UserDaoExample {
    @Autowired
    UserDao userDao;

    @Autowired
    InfoDao infoDao;

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");

        UserDaoExample userDaoExample = ac.getBean(UserDaoExample.class);

        UserDao userDao2 = ac.getBean(UserDao.class);

        System.out.println(userDao2 == userDaoExample.userDao);


        //创建user表
        userDaoExample.userDao.create();

        UserDao.User user1 = new UserDao.User(1, "tester1");
        UserDao.User user2 = new UserDao.User(2, "tester2");

        System.out.println(userDaoExample.userDao.insert(user1));
        System.out.println(userDaoExample.userDao.insert(user2));

        UserDao.User user3 = new UserDao.User(3, "tester3");
        UserDao.User user4 = new UserDao.User(4, "tester4");

        List<UserDao.User> users = new ArrayList<UserDao.User>();
        users.add(user3);
        users.add(user4);

        System.out.println(Arrays.toString(userDaoExample.userDao.insert(users)));

        System.out.println(userDaoExample.userDao.select(1));
        System.out.println(userDaoExample.userDao.select());
        System.out.println(userDaoExample.userDao.select("123"));
        System.out.println(userDaoExample.userDao.selectSum());
        System.out.println(userDaoExample.userDao.showTables());

        //创建info表
        userDaoExample.infoDao.create();

        InfoDao.Info info1 = new InfoDao.Info(1, "tester1's info", 1);
        InfoDao.Info info2 = new InfoDao.Info(1, "tester2's info", 2);

        System.out.println(userDaoExample.infoDao.insert(info1));
        System.out.println(userDaoExample.infoDao.insert(info2));
        System.out.println(userDaoExample.infoDao.selectUserInfo());
    }
}
