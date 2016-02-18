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
    String table_name = "user";

    @Data
    @AllArgsConstructor
    class User {
        int id;
        String name;

        public User() {};
    }

    @Execute("create table " + table_name + " (id int, name varchar(200));")
    void createTable();

    @Update("insert into " + table_name + "(id, name) values ({user.id}, {user.name});")
    int insertTable(@SqlParam("user") User user);

    @Select("select id, name from " + table_name + " where id = {id}")
    User selectTable(@SqlParam("id") int id);

    @Select("select sum(id) from " + table_name)
    int selectSum();

    @Data
    class TableInfo {
        String tableName;
        String tableSchema;
    }

    @Select("show tables")
    List<TableInfo> showTables();

    @Select("select id, name from " + table_name)
    List<User> selectTable();
}
