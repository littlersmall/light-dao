package com.littlersmall.lightdao.annotation;

import java.lang.annotation.*;

/**
 * Created by sigh on 2016/1/18.
 *
 * 该注解用于标记参数，可以是基本类型或是Bean
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SqlParam {
    String value();
}
