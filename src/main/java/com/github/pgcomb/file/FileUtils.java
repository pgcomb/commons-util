package com.github.pgcomb.file;

import java.io.File;

/**
 * @author 王东旭
 * @date 2018-06-06
 */
public class FileUtils {

    public static void delIncludePath(File file, File stopPath) {
        org.apache.commons.io.FileUtils.deleteQuietly(file);
        upRm(file, stopPath != null ? stopPath.getAbsolutePath() : null);
    }
    public static void delIncludePath(File file) {
        delIncludePath(file,null);
    }

    private static void upRm(File file, String stopPath) {
        if (file.isFile()) {
            file.delete();
        }
        while (!file.getAbsolutePath().equals(file.getParentFile().getAbsolutePath()) && !file.getAbsolutePath().equals(stopPath)) {
            File[] files = file.listFiles();
            if (files == null || files.length != 0 || !file.delete()) {
                if (file.exists()) {
                    return;
                } else {
                    file = file.getParentFile();
                }
            } else {
                file = file.getParentFile();
            }
        }
    }
}
