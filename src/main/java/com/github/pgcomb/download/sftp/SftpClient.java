package com.github.pgcomb.download.sftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * sftp 客户端
 * @author 王东旭
 */
public class SftpClient {
    private static final Logger log = LoggerFactory.getLogger(SftpClient.class);
    private JSch jSch;
    private ChannelSftp sftp;
    private Session sshSession;

    private SftpClient(JSch jSch, ChannelSftp sftp, Session sshSession) {
        this.jSch = jSch;
        this.sftp = sftp;
        this.sshSession = sshSession;
    }

    public static SftpClient createSftpClient(String host, String username, String password) {
        return createSftpClient(host,22, username, password);
    }

    public static SftpClient createSftpClient(String host,Integer port, String username, String password ) {
        Session sshSession;
        ChannelSftp sftp;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            sshSession = jsch.getSession(username, host, port);
            if (log.isInfoEnabled()) {
                log.info("Session created.");
            }
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            if (log.isInfoEnabled()) {
                log.info("Session connected.");
            }
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            if (log.isInfoEnabled()) {
                log.info("Opening Channel.");
            }
            sftp = (ChannelSftp) channel;
            if (log.isInfoEnabled()) {
                log.info("Connected to " + host + ".");
            }
            return new SftpClient(jsch,sftp,sshSession);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect()
    {
        if (this.sftp != null)
        {
            if (this.sftp.isConnected())
            {
                this.sftp.disconnect();
                if (log.isInfoEnabled())
                {
                    log.info("sftp is closed already");
                }
            }
        }
        if (this.sshSession != null)
        {
            if (this.sshSession.isConnected())
            {
                this.sshSession.disconnect();
                if (log.isInfoEnabled())
                {
                    log.info("sshSession is closed already");
                }
            }
        }
    }

    public ChannelSftp getSftp() {
        return sftp;
    }

    public Session getSshSession() {
        return sshSession;
    }
}
