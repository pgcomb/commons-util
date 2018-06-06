package com.github.pgcomb.file;

import java.io.File;

/**
 * 文件工具
 * @author 王东旭
 */
public class FileUtils {

    /**
     * 删除文件夹或文件，同时向上清除空的文件夹
     *
     * @param file 待删除的<code>File</code>
     * @param stopPath 清除空文件夹的位置<code>File</code>
     */
    public static void delIncludePath(File file, File stopPath) {
        org.apache.commons.io.FileUtils.deleteQuietly(file);
        upRm(file, stopPath != null ? stopPath.getAbsolutePath() : null);
    }

    /**
     * 删除问文件{@link #delIncludePath(File, File)},不限制位置
     *
     * @param file 待删除<code>File</code>
     */
    public static void delIncludePath(File file) {
        delIncludePath(file,null);
    }

    /**
     * 向上删除空的空的文件夹
     *
     * @param file 待删除<code>File</code>
     * @param stopPath stopPath 清除空文件夹的位置<code>String</code>
     */
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
