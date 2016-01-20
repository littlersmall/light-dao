package com.littlersmall.lightdao.annotation;

import java.lang.annotation.*;

/**
 * Created by sigh on 2016/1/18.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DAO {
}
