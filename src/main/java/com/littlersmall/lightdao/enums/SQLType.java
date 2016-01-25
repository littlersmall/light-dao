package com.littlersmall.lightdao.enums;

/**
 * Created by sigh on 2016/1/21.
 */
public enum SqlType {
    EXECUTE(1, ""),
    SELECT(2, ""),
    UPDATE(3, "");

    int id;
    String desc;

    SqlType(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
