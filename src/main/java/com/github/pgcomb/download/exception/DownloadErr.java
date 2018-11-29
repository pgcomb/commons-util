package com.github.pgcomb.download.exception;

/**
 * Title: DownloadErr <br>
 * Description: DownloadErr <br>
 * Date: 2018年09月13日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public enum DownloadErr {

    /**
     * 文件找不到
     */
    NOT_FOUND_FILE("Can't find the remote file"),

    /**
     * 下载过程意外中断
     */
    BREAK_DOWNLOAD("Download process unexpectedly"),

    /**
     * 连接异常
     */
    CONNECTION_EXCEPTION("ftp connection exception");

    private String msg;

    DownloadErr(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
