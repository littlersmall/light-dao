package com.littlersmall.lightdao.example;

import com.littlersmall.lightdao.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by sigh on 2016/2/3.
 */
@Dao(dbName = "my_db")
public interface UserDao {
    String TABLE_NAME = "user";

    @Data
    @AllArgsConstructor
    class User {
        int id;
        String name;

        public User() {};
    }

    @Execute("create table " + TABLE_NAME + " (id int, name varchar(200));")
    void create();

    @Update("insert into " + TABLE_NAME + "(id, name) values ({user.id}, {user.name});")
    int insert(@SqlParam("user") User user);

    @Select("select id, name from " + TABLE_NAME + " where id = {id}")
    User select(@SqlParam("id") int id);

    @Select("select id, name from " + TABLE_NAME + " where name = {name}")
    List<User> select(@SqlParam("name") String name);

    @Select("select id, name from " + TABLE_NAME)
    List<User> select();

    @Select("select sum(id) from " + TABLE_NAME)
    int selectSum();

    @Data
    class TableInfo {
        String tableName;
        String tableSchema;
    }

    @Select("show tables")
    List<TableInfo> showTables();
}
