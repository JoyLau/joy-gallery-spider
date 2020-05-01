package cn.joylau.code.utils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by joylau on 2020/5/1.
 * cn.joylau.code.utils
 * 2587038142.liu@gmail.com
 */
public class Utils {

    public static String rootPath(String savePath) {
        return savePath.endsWith("/") ? savePath.substring(0, savePath.length() - 2) : savePath;
    }

    public static String fileName() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS")) + ".jpeg";
    }

    public static void init(File path) {
        if (!path.exists()) {
            if (!path.mkdirs()) {
                throw new RuntimeException("======== path [" + path.getPath() + "] unable to create!! ========");
            }
            if (!path.canWrite()) {
                throw new RuntimeException("======== path [" + path.getPath() + "] unable to write!! ========");
            }
            if (path.getFreeSpace() < (1024 * 1024 * 1024)) {
                throw new RuntimeException("======== disk capacity is less than 1G!! ========");
            }
        }
    }
}
