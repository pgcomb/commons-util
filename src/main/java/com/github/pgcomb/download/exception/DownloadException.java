package com.github.pgcomb.download.exception;

/**
 * Title: DownloadException <br>
 * Description: DownloadException <br>
 * Date: 2018年09月13日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class DownloadException extends BolterException {

    private DownloadErr err;

    public DownloadException(Throwable cause, DownloadErr err) {
        super(err.getMsg(), cause);
        this.err = err;
    }

    public DownloadException(DownloadErr err) {
        super(err.getMsg());
        this.err = err;
    }

    public DownloadErr getErr() {
        return err;
    }
}
