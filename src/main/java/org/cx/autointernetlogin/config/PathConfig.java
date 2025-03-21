package org.cx.autointernetlogin.config;

import org.cx.autointernetlogin.util.PathUtils;

import java.nio.file.Path;

public class PathConfig {

    /**
     * 项目配置文件路径
     */
    public static final String CONFIG_PATH;

    /**
     * 日志存放路径
     */
    public static final String LOG_PATH;

    static {
        String path = PathUtils.getJarParentPath();
        CONFIG_PATH = Path.of(path, "config.yaml").toString();
        LOG_PATH = Path.of(path, "log").toString();
    }

}
