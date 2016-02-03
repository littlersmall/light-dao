package com.littlersmall.lightdao.enums;

/**
 * Created by sigh on 2016/1/21.
 */
public enum SqlType {
    EXECUTE(1, "ddl语句等"),
    SELECT(2, "query语句"),
    UPDATE(3, "insert or update 语句");

    int id;
    String desc;

    SqlType(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
