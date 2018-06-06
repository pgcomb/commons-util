package com.github.pgcomb.string;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 路径工具
 * @author 王东旭
 */
public class PathUtils {

    private static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    /**
     * 拼接路径
     *
     * @param paths 路径片段
     * @return 返回路径
     */
    public static String joinPath(String... paths) {
        List<String> filterPath = Arrays.stream(paths)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());
        return replaceSeparator(StringUtils.join(filterPath, FILE_SEPARATOR))
                .replace(FILE_SEPARATOR + FILE_SEPARATOR, FILE_SEPARATOR);
    }

    /**
     * 替换路径分割符
     *
     * @param path 路径
     * @return 分割后的路径
     */
    private static String replaceSeparator(String path) {
        if (FILE_SEPARATOR.equals("/")) {
            return path.replace("\\", "/");
        } else {
            return path.replace("/", "\\");
        }
    }
}
