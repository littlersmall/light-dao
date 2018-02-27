package com.littlersmall.lightdao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by littlersmall on 2018/2/23.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {
    /*
    在一个model中，比如
    class Info {
        @PrimaryKey
        private long infoId;
        private String name;
    }
    如果id为注解，则标记上@PrimaryKey注解，这样在框架层会自动产生
    1 info_id 为主键名(注意驼峰会自动转换为下划线模式) 无需再在 DAO 中实现 getPrimaryKeyName 方法
    2 在使用listAllDesc 等等方法时，会使用主键值作为limit条件，具体可看DAOBaseGet的实现

    注意：
    1 主键必须和数据库的主键名一致
    2 主键必须为long(or Long or int or Integer)类型

    本次升级为兼容性升级，不影响原来的PrimaryKey接口使用，但PrimaryKey接口会逐渐废弃。
     */
}
