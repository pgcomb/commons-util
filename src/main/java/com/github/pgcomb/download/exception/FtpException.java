package com.github.pgcomb.download.exception;

/**
 * Title: FtpException <br>
 * Description: FtpException <br>
 * Date: 2018年09月12日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class FtpException extends BolterException {
    public FtpException() {
    }

    public FtpException(String message) {
        super(message);
    }

    public FtpException(String message, Throwable cause) {
        super(message, cause);
    }

    public FtpException(Throwable cause) {
        super(cause);
    }

    public FtpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
