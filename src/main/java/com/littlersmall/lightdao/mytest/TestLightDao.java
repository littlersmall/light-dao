package com.littlersmall.lightdao.mytest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Created by sigh on 2016/2/3.
 */
@Service
public class TestLightDao {
    @Autowired
    MyTest1 myTest1;

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        TestLightDao testLightDao = ac.getBean(TestLightDao.class);

        testLightDao.myTest1.createTable();
        System.out.println(testLightDao.getClass().getName());

        MyTest1.User user = new MyTest1.User(27, "littlersmall");

        System.out.println(testLightDao.myTest1.insertTable(user));

        System.out.println(testLightDao.myTest1.selectTable(27));
        System.out.println(testLightDao.myTest1.selectTable());
    }
}
