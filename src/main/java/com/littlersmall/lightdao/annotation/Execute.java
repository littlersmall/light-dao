package com.littlersmall.lightdao.annotation;

import java.lang.annotation.*;

/**
 * Created by sigh on 2016/1/19.
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Execute {
    String value();
}
