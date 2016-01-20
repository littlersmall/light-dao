package com.littlersmall.lightdao.annotation;

import java.lang.annotation.*;

/**
 * Created by sigh on 2016/1/18.
 */
@Target( { ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SQLParam {
    String value();
}
