package com.github.pgcomb.download.ftp;

import com.github.pgcomb.data.DownloadSpeedUtil;
import com.github.pgcomb.date.DateLog;
import com.github.pgcomb.download.exception.DownloadErr;
import com.github.pgcomb.download.exception.DownloadException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Title: FtpDownloader <br>
 * Description: FtpDownloader <br>
 * Date: 2018年09月13日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class FtpDownloader {

    private static final Logger log = LoggerFactory.getLogger(FtpDownloader.class);

    public static void download(FTPClient ftpClient, String src, File target) throws DownloadException {

        if (!target.getParentFile().exists()) {
            target.getParentFile().mkdirs();
        }
        FTPFile[] ftpFiles;
        try {
            ftpFiles = ftpClient.listFiles(src);
        } catch (IOException e) {
            throw new DownloadException(DownloadErr.CONNECTION_EXCEPTION);
        }

        long remoteSize = ftpFiles == null ? 0 : ftpFiles.length != 1 ? 0 : ftpFiles[0].getSize();
        long localSize = target.exists() ? target.length() : 0;

        if (remoteSize < localSize) {
            target.delete();
            localSize = 0;
        }

        try (OutputStream fos = new BufferedOutputStream(new FileOutputStream(target,true))) {

            if (remoteSize == 0) {
                log.debug("ftp|remote file[{}] not found", src, target.getAbsolutePath());
                throw new DownloadException(DownloadErr.NOT_FOUND_FILE);
            } else if (remoteSize == localSize) {
                log.debug("ftp|remote file[{}] has been downloaded[{}]", src, target.getAbsolutePath());
                return;
            } else if (remoteSize > localSize && localSize != 0) {
                log.debug("ftp|remote file[{}][size:{}] to [{}] from offset[{}]", src, remoteSize, target.getAbsolutePath(), localSize);
                ftpClient.setRestartOffset(localSize);
            } else {
                log.debug("ftp|remote file[{}][size:{}] to [{}]", src, remoteSize, target.getAbsolutePath());
            }
            DateLog.record();
            if (!ftpClient.retrieveFile(src, fos)) {
                throw new DownloadException(DownloadErr.BREAK_DOWNLOAD);
            }
            long finalLocalSize = localSize;
            DateLog.record(duration -> log.info("ftp|download success ->{}",DownloadSpeedUtil.speed(remoteSize-finalLocalSize,duration)));
        } catch (IOException e) {
            throw new DownloadException(e, DownloadErr.BREAK_DOWNLOAD);
        }
    }
}
