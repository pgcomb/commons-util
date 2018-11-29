package com.github.pgcomb.download.ftp;

/**
 * Title: SFtpProp <br>
 * Description: SFtpProp <br>
 * Date: 2018年09月12日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public interface FtpProp {
    String ip();

    int port();

    String username();

    String password();

    String encode();

    boolean localActive();
}
