package org.cx.autointernetlogin.util;

import org.cx.autointernetlogin.HelloApplication;

import java.io.File;
import java.nio.file.Path;

/**
 * 路径帮助类
 */
public class PathUtils {

    /**
     * 获取项目 jar 包所处文件夹路径
     *
     * @return 文件夹路径
     */
    public static String getJarParentPath() {
        if (HelloApplication.isIsDebugMod()) {
            String path = Path.of(System.getProperty("user.dir"), "\\target\\classes").toString();
            System.out.println("debug:" + path);
            return path;
        } else {
            String path = System.getProperty("java.class.path");
            int firstIndex = path.lastIndexOf(File.pathSeparator) + 1;
            int lastIndex = path.lastIndexOf(File.separator) + 1;
            path = path.substring(firstIndex, lastIndex);
            System.out.println("release: " + path);
            return path;
        }
    }

}
