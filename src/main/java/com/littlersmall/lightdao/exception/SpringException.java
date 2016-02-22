package com.littlersmall.lightdao.exception;

/**
 * Created by sigh on 2016/2/19.
 */
public class SpringException extends RuntimeException {
    public SpringException(Exception e) {
        super(e.getMessage());
    }
}
