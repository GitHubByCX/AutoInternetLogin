package org.cx.autointernetlogin;

public class Main {

    // 由于打包生成exe后, 执行exe会报缺少javafx运行时的问题, 所以这里额外新建了 Main 启动类来解决
    // 参考：https://github.com/javapackager/JavaPackager/issues/20#issuecomment-599055530
    public static void main(String[] args) {
        HelloApplication.main(args);
    }

}
