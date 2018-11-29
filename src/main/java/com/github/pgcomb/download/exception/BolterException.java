package com.github.pgcomb.download.exception;

/**
 * Title: BolterException <br>
 * Description: BolterException <br>
 * Date: 2018年09月12日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class BolterException extends Exception{
    public BolterException() {
    }

    public BolterException(String message) {
        super(message);
    }

    public BolterException(String message, Throwable cause) {
        super(message, cause);
    }

    public BolterException(Throwable cause) {
        super(cause);
    }

    public BolterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
