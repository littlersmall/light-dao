package com.littlersmall.lightdao.example;

import com.littlersmall.lightdao.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by sigh on 2016/2/22.
 */
@Dao(dbName = "my_db")
public interface InfoDao {
    String TABLE_NAME = " info ";
    String ALL_COLUMN = " id, information, user_id ";
    String ALL_VALUES = " {param.id}, {param.information}, {param.userId} ";

    @Data
    @AllArgsConstructor
    class Info {
        int id;
        String information;
        int userId;

        public Info() {}
    }

    @Execute("create table " + TABLE_NAME + " (id int, information varchar, user_id int)")
    void create();

    @Update("insert into " + TABLE_NAME + "(" +  ALL_COLUMN + ")" + " values(" + ALL_VALUES + ")")
    int insert(@SqlParam("param") Info info);

    @Data
    @AllArgsConstructor
    class UserInfo {
        String information;
        String name;

        public UserInfo() {};
    }

    //跨表查询示例
    @Select("select information, name from info, user where info.user_id = user.id")
    List<UserInfo> selectUserInfo();
}
