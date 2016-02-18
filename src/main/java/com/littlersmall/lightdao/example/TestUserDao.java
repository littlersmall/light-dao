package com.littlersmall.lightdao.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sigh on 2016/2/3.
 */
@Service
public class TestUserDao {
    @Autowired
    UserDao userDao;

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");

        TestUserDao testUserDao = ac.getBean(TestUserDao.class);

        testUserDao.userDao.createTable();
        System.out.println(testUserDao.getClass().getName());

        UserDao.User user = new UserDao.User(27, "littlersmall");

        System.out.println(testUserDao.userDao.insertTable(user));

        System.out.println(testUserDao.userDao.selectTable(27));
        System.out.println(testUserDao.userDao.selectTable());
        System.out.println(testUserDao.userDao.selectSum());
        System.out.println(testUserDao.userDao.showTables());
    }
}
