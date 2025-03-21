package org.cx.autointernetlogin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.cx.autointernetlogin.config.PathConfig;
import org.cx.autointernetlogin.config.ProjectConfig;
import org.cx.autointernetlogin.util.LogUtils;
import org.cx.autointernetlogin.util.YamlUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;

public class HelloApplication extends Application {

    //region 变量与属性
    // 项目配置
    private static ProjectConfig projectConfig;
    // 主界面
    private static Stage primaryStage;
    // 是否为 debug 模式启动
    private static boolean isDebugMod;

    /**
     * 获取全局项目配置
     *
     * @return 全局项目配置
     */
    public static ProjectConfig getProjectConfig() {
        return projectConfig;
    }

    /**
     * 获取主界面
     *
     * @return 主界面实例
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * 获取是否是 debug 模式启动的标识
     *
     * @return debug 模式启动的标识
     */
    public static boolean isIsDebugMod() {
        return isDebugMod;
    }
    //endregion

    @Override
    public void start(Stage stage) throws IOException {
        // 默认情况下 javafx 运行时会在最后一个 stage close(或hide)后自动关闭, 除非通过Platform.setImplicitExit(false);取消这个默认行为
        // 参考: https://blog.csdn.net/aliaichidantong/article/details/94402963
        Platform.setImplicitExit(false);
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/cx/autointernetlogin/view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("自动上网登录 v1.0");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/RiUserVoiceFill.png"))));
        stage.setScene(scene);
        stage.setOnCloseRequest(this::formClosing);
        stage.show();
    }

    public static void main(String[] args) {
        initConfig();
        launch(args);
    }

    /**
     * 初始化配置
     */
    private static void initConfig() {
        // 获取启动模式, 其值来自 pom.xml profile 的定义
        Properties prop = new Properties();
        try (InputStream input = HelloApplication.class.getClassLoader()
                .getResourceAsStream("app-config.properties")) {
            prop.load(input);
            isDebugMod = Boolean.parseBoolean(prop.getProperty("debug.mode"));
        } catch (Exception e) {
            throw new RuntimeException("无法加载 app-config.properties!", e);
        }

        LogUtils.getLogger().info("程序启动-日志保存路径为：" + PathConfig.LOG_PATH);

        // 加载项目配置
        try {
            projectConfig = YamlUtils.getProjectConfig(PathConfig.CONFIG_PATH);
        } catch (FileNotFoundException e) {
            LogUtils.getLogger().log(Level.CONFIG, "当前目录下无项目配置文件，将采用默认项目配置。", e);
            projectConfig = new ProjectConfig();

            try {
                YamlUtils.saveProjectConfig(PathConfig.CONFIG_PATH, projectConfig);
            } catch (IOException ex) {
                LogUtils.getLogger().log(Level.WARNING, "无法保存项目配置文件到本地目录！", ex);
            }
        }
    }

    /**
     * 窗体关闭
     *
     * @param event 窗体事件
     */
    private void formClosing(WindowEvent event) {
        event.consume(); // 阻止窗口关闭
        Platform.runLater(() -> {
            primaryStage.hide();
        });
    }

}