package com.github.pgcomb.download.sftp;

import com.github.pgcomb.data.DownloadSpeedUtil;
import com.github.pgcomb.date.DateLog;
import com.github.pgcomb.download.exception.DownloadErr;
import com.github.pgcomb.download.exception.DownloadException;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Title: SftpDownloader <br>
 * Description: SftpDownloader <br>
 * Date: 2018年09月13日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class SftpDownloader {

    private static final Logger log = LoggerFactory.getLogger(SftpDownloader.class);

    public static void download(SftpClient sftpClient, String src, File target) throws DownloadException {

        if (!target.getParentFile().exists()) {
            target.getParentFile().mkdirs();
        }

        SftpATTRS lstat;
        try {
            lstat = sftpClient.getSftp().lstat(src);
        } catch (SftpException e) {
            throw new DownloadException(e, DownloadErr.CONNECTION_EXCEPTION);
        }
        long remoteSize = lstat == null ? 0 : lstat.getSize();
        long localSize = target.exists() ? target.length() : 0;

        if (remoteSize < localSize) {
            target.delete();
            localSize = 0;
        }

        if (remoteSize == 0) {
            log.debug("sftp|remote file[{}] not found", src, target.getAbsolutePath());
            throw new DownloadException(DownloadErr.NOT_FOUND_FILE);
        } else if (remoteSize == localSize) {
            log.debug("sftp|remote file[{}] has been downloaded[{}]", src, target.getAbsolutePath());
            return;
        } else {
            log.debug("sftp|remote file[{}][size:{}] to [{}] from offset[{}]", src, remoteSize, target.getAbsolutePath(), localSize);
        }
        try (OutputStream fos = new BufferedOutputStream(new FileOutputStream(target))) {
            DateLog.record();
            sftpClient.getSftp().get(src, fos, null, ChannelSftp.APPEND, localSize);
            long finalLocalSize = localSize;
            DateLog.record(duration -> log.info("ftp|download success ->{}",DownloadSpeedUtil.speed(remoteSize-finalLocalSize,duration)));
        } catch (IOException | SftpException e) {
            throw new DownloadException(e,DownloadErr.BREAK_DOWNLOAD);
        }
    }
}
