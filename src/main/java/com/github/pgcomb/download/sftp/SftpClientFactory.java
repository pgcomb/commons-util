package com.github.pgcomb.download.sftp;

import com.github.pgcomb.download.api.ObjectFactory;
import com.github.pgcomb.download.api.ObjectPeptic;
import com.github.pgcomb.download.exception.BolterException;
import com.jcraft.jsch.SftpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: SftpClientFactory <br>
 * Description: SftpClientFactory <br>
 * Date: 2018年09月13日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class SftpClientFactory implements ObjectFactory<SftpClient> {

    private static final Logger log = LoggerFactory.getLogger(SftpClientFactory.class);

    private SFtpProp sFtpProp;

    public SftpClientFactory(SFtpProp sFtpProp) {
        this.sFtpProp = sFtpProp;
    }

    @Override
    public SftpClient get() throws BolterException {
        return SftpClient.createSftpClient(sFtpProp.ip(), sFtpProp.port(), sFtpProp.username(), sFtpProp.password());
    }

    @Override
    public ObjectPeptic<SftpClient> peptic() {
        return new ObjectPeptic<SftpClient>() {
            @Override
            public void destroyObject(SftpClient p) throws Exception {
                if (p != null) {
                    p.disconnect();
                }
            }

            @Override
            public boolean validateObject(SftpClient p) {
                if (p != null) {
                    try {
                        p.getSftp().getServerVersion();
                        return true;
                    } catch (SftpException e) {
                        log.error("validate object is false:", e);
                    }
                }
                return false;
            }
        };
    }
}
