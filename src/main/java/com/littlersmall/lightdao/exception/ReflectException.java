package com.littlersmall.lightdao.exception;

/**
 * Created by sigh on 2016/2/19.
 */
public class ReflectException extends RuntimeException {
    public ReflectException(Exception e) {
        super(e.getMessage());
    }

    public ReflectException(String message) {
        super(message);
    }
}
