package com.littlersmall.lightdao.exception;

/**
 * Created by sigh on 2016/2/19.
 */
public class DataAccessException extends RuntimeException {
    public DataAccessException(Exception e) {
        super(e.getMessage());
    }

    public DataAccessException(String message) {
        super(message);
    }
}
