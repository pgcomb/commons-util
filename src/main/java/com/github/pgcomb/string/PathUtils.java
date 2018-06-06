package com.github.pgcomb.string;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 王东旭
 * @date 2018-06-06
 */
public class PathUtils {

    private static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    public static String joinPath(String... paths) {
        List<String> filterPath = Arrays.stream(paths)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());
        return replaceSeparator(StringUtils.join(filterPath, FILE_SEPARATOR))
                .replace(FILE_SEPARATOR + FILE_SEPARATOR, FILE_SEPARATOR);
    }

    private static String replaceSeparator(String path) {
        if (FILE_SEPARATOR.equals("/")) {
            return path.replace("\\", "/");
        } else {
            return path.replace("/", "\\");
        }
    }
}
