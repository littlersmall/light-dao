package com.littlersmall.lightdao.exception;

/**
 * Created by sigh on 2016/2/19.
 */
public class ExecutorException extends RuntimeException {
    public ExecutorException(Exception e) {
        super(e.getMessage());
    }

    public ExecutorException(String message) {
        super(message);
    }
}
