package org.cx.autointernetlogin.util;

import org.cx.autointernetlogin.config.PathConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 日志记录帮助类
 */
public class LogUtils {

    private static Logger logger = null;

    /**
     * 获取日志帮助类
     *
     * @return 日志帮助类
     */
    public static Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger(LogUtils.class.getName());
            logger.setLevel(Level.ALL);

            try {
                // 日志生成方式
                File dir = new File(PathConfig.LOG_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileHandler fileHandler = new FileHandler(
                        Path.of(PathConfig.LOG_PATH, "log%g.log").toString(), // %g 已轮转的方式记录日志, 最新的编号为 0, 上一个为 1, 以此类推...
                        1048576, // 每份日志文件最大大小(1MB)
                        5, // 日志文件限制数量
                        true);
                fileHandler.setEncoding("utf-8");
                fileHandler.setLevel(Level.ALL);
                fileHandler.setFormatter(new SimpleFormatter());  // 日志输出格式

                logger.addHandler(fileHandler);
                logger.setUseParentHandlers(false); // 不使用父 Logger 的 handler
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return logger;
    }

}
