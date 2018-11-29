package com.github.pgcomb.download.ftp;

import com.github.pgcomb.download.api.ObjectFactory;
import com.github.pgcomb.download.api.ObjectPeptic;
import com.github.pgcomb.download.exception.BolterException;
import com.github.pgcomb.download.exception.FtpException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;

/**
 * Title: FtpClientFactory <br>
 * Description: FtpClientFactory <br>
 * Date: 2018年09月12日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class FtpClientFactory implements ObjectFactory<FTPClient> {

    private static final Logger log = LoggerFactory.getLogger(FtpClientFactory.class);

    public FtpProp ftpProp;

    public FtpClientFactory(FtpProp ftpProp) {
        this.ftpProp = ftpProp;
    }

    @Override
    public FTPClient get() throws BolterException {

        FTPClientConfig config = new FTPClientConfig();
        FTPClient client = new FTPClient();
        client.configure(config);
        client.setBufferSize(10240);
        client.setConnectTimeout(5000);
        client.setDefaultTimeout(5000);
        String encoding = ftpProp.encode();
        if (encoding != null) {
            client.setControlEncoding(encoding);
        }
        try {
            client.connect(ftpProp.ip(), ftpProp.port());
            int reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                client.disconnect();
                throw new FtpException("failed to connect server" + ftpProp.ip() + ":" + ftpProp.port());
            }
            if (ftpProp.username() != null && ftpProp.password() != null) {
                client.login(ftpProp.username(), ftpProp.password());
                reply = client.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    client.logout();
                    client.disconnect();
                    throw new FtpException("failed to login server" + ftpProp.ip() + ":" + ftpProp.port());
                }
            }
            client.setControlEncoding(ftpProp.encode());
            boolean localActive = ftpProp.localActive();
            if (localActive) {
                client.enterLocalActiveMode();
            } else {
                client.enterLocalPassiveMode();
            }
            client.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (SocketException e) {
            throw new FtpException("socket exception: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new FtpException("io exception: " + e.getMessage(), e);
        }
        return client;
    }

    @Override
    public ObjectPeptic<FTPClient> peptic() {
        return new ObjectPeptic<FTPClient>() {
            @Override
            public void destroyObject(FTPClient p) throws Exception {
                p.logout();
                p.disconnect();
            }

            @Override
            public boolean validateObject(FTPClient p) {
                if(p != null) {
                    try {
                        int reply = p.noop();
                        return FTPReply.isPositiveCompletion(reply);
                    } catch (IOException e) {
                        log.error("FtpClientFactory#validateObject",e);
                    }
                }
                return false;
            }
        };
    }
}
