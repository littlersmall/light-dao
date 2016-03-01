package com.littlersmall.lightdao.annotation;

import java.lang.annotation.*;

/**
 * Created by sigh on 2016/1/19.
 *
 * 该注解标记所有的写语句，比如insert into, update, insert into select 等等
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Update {
    String value();
}
