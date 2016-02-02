package com.littlersmall.lightdao.annotation;

/**
 * Created by sigh on 2016/1/29.
 */

@Dao(dbName = "abc")
public interface TestClass {

    @Select("select * from abc")
    int getAbc();
}
