package org.cx.autointernetlogin.view;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.cx.autointernetlogin.HelloApplication;
import org.cx.autointernetlogin.config.PathConfig;
import org.cx.autointernetlogin.constant.CheckIntervalEnum;
import org.cx.autointernetlogin.service.LoginInterNet;
import org.cx.autointernetlogin.service.ReloginCheck;
import org.cx.autointernetlogin.util.LogUtils;
import org.cx.autointernetlogin.util.TextAreaHandler;
import org.cx.autointernetlogin.util.YamlUtils;
import org.cx.autointernetlogin.view.control.TTrayIcon;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

public class MainView implements Initializable {

    //region 控件列表
    @FXML
    private Label currentStatus_Info_Lb;

    @FXML
    private Label currentStatus_Lb;

    @FXML
    private ComboBox<String> enableCheck_Interval_CB;

    @FXML
    private Label enableCheck_Interval_Lb;

    @FXML
    private CheckBox enableCheck_Relogin_CB;

    @FXML
    private Label logInfo_Lb;

    @FXML
    private TextArea logInfo_TA;

    @FXML
    private Label loginPwd_Lb;

    @FXML
    private TextField loginPwd_TF;

    @FXML
    private Label loginUserName_Lb;

    @FXML
    private TextField loginUserName_TF;

    @FXML
    private Button login_Btn;

    @FXML
    private Button logout_Btn;
    //endregion


    //region 变量
    // 系统托盘
    private SystemTray systemTray;
    private TTrayIcon myTrayIcon;
    // 托盘图标
    private java.awt.Image trayIcon;
    private java.awt.Image trayIcon_red;
    //endregion


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var pj = HelloApplication.getProjectConfig();
        initTrayIcon();

        // 初始化用户名和密码
        loginUserName_TF.setText(pj.getUsername());
        loginUserName_TF.textProperty().addListener(this::loginUserName_TF_TextChanged);
        loginPwd_TF.setText(pj.getPassword());
        loginPwd_TF.textProperty().addListener(this::loginPwd_PF_TextChanged);

        // 掉线检查
        enableCheck_Relogin_CB.setSelected(pj.isReloginCheck());
        enableCheck_Relogin_CB.selectedProperty().addListener(this::enableCheck_Relogin_CB_SelectedChanged);
        // 检测间隔
        enableCheck_Interval_CB.getItems().addAll(Arrays.stream(CheckIntervalEnum.values()).map(CheckIntervalEnum::getText).collect(Collectors.toList()));
        var selectedValue = CheckIntervalEnum.query(pj.getReloginCheck_Interval());
        enableCheck_Interval_CB.setValue(Objects.requireNonNullElse(selectedValue, CheckIntervalEnum.ONE_MIN).getText());
        enableCheck_Interval_CB.valueProperty().addListener(this::enableCheck_Interval_CB_ValueChanged);

        // 登录按钮
        login_Btn.setOnMouseClicked(this::login_Btn_MouseClicked);
        // 登出按钮
        logout_Btn.setOnMouseClicked(this::logout_Btn_MouseClicked);

        // 添加日志显示处理
        TextAreaHandler handler = new TextAreaHandler(logInfo_TA);
        handler.setFormatter(new SimpleFormatter());
        LogUtils.getLogger().addHandler(handler);

        // 启动检测线程
        ReloginCheck rc = new ReloginCheck();
        rc.UpdateNetworkStatus = (networked) -> {
            Platform.runLater(() -> {
                if (networked) {
                    currentStatus_Info_Lb.setText("网络已连接！");
                    currentStatus_Info_Lb.setTextFill(Color.GREEN);
                    myTrayIcon.setImage(trayIcon);
                } else {
                    currentStatus_Info_Lb.setText("网络未连接！");
                    currentStatus_Info_Lb.setTextFill(Color.RED);
                    myTrayIcon.setImage(trayIcon_red);
                }
            });
        };
        rc.start();

        // 启动时尝试一次登录
        LoginInterNet.submitLogin(pj.getUsername(), pj.getPassword());
    }

    /**
     * 初始化托盘图标
     */
    private void initTrayIcon() {
        systemTray = SystemTray.getSystemTray();
        var mainStage = HelloApplication.getPrimaryStage();
        // 添加托盘图标
        VBox trayManu = new VBox();
        trayManu.setPrefWidth(80);
        trayManu.setPrefHeight(25);
        trayManu.alignmentProperty().setValue(Pos.CENTER);
        trayManu.setStyle("-fx-padding: 0");
        javafx.scene.control.Button exit_btn = new Button();
        exit_btn.setPrefWidth(80);
        exit_btn.setPrefHeight(25);
        exit_btn.setText("退出");
        exit_btn.setStyle("-fx-font-family: Microsoft YaHei; -fx-background-color: White;");
        exit_btn.setOnMouseClicked(mouseEvent -> {
            // 保存项目配置
            try {
                YamlUtils.saveProjectConfig(PathConfig.CONFIG_PATH, HelloApplication.getProjectConfig());
            } catch (IOException ex) {
                LogUtils.getLogger().log(Level.WARNING, "无法保存项目配置文件到本地目录！", ex);
            }
            // 强制退出
            System.exit(0);
        });
        trayManu.getChildren().add(exit_btn);
        trayIcon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/RiUserVoiceFill.png"));
        trayIcon_red = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/RiUserVoiceFill-Red.png"));
        try {
            myTrayIcon = new TTrayIcon(trayIcon, "自动上网登录 v1.0", trayManu);
            myTrayIcon.mouseDoubleClickEvent = (obj, mouseEvent) -> {
                Platform.runLater(() ->
                {
                    if (!mainStage.isShowing()) {
                        mainStage.show();
                        mainStage.toFront();
                    }
                });
            };
            systemTray.add(myTrayIcon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }


    //region 控件事件

    /**
     * 修改用户名事件
     *
     * @param observableValue 控件实例
     * @param oldValue        旧值
     * @param newValue        新值
     */
    private void loginUserName_TF_TextChanged(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        HelloApplication.getProjectConfig().setUsername(newValue);
    }

    /**
     * 修改密码事件
     *
     * @param observableValue 控件实例
     * @param oldValue        旧值
     * @param newValue        新值
     */
    private void loginPwd_PF_TextChanged(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        HelloApplication.getProjectConfig().setPassword(newValue);
    }

    /**
     * 启用或禁用掉线检测事件
     *
     * @param observableValue 控件实例
     * @param oldValue        旧值
     * @param newValue        新值
     */
    private void enableCheck_Relogin_CB_SelectedChanged(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
        HelloApplication.getProjectConfig().setReloginCheck(newValue);
    }

    /**
     * 修改检测间隔事件
     *
     * @param observableValue 控件实例
     * @param oldValue        旧值
     * @param newValue        新值
     */
    private void enableCheck_Interval_CB_ValueChanged(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        var selectedValue = CheckIntervalEnum.query(newValue);
        HelloApplication.getProjectConfig().setReloginCheck_Interval(selectedValue == null ? -1 : selectedValue.getIndex());
    }

    /**
     * 点击登录按钮事件
     *
     * @param mouseEvent 鼠标事件
     */
    private void login_Btn_MouseClicked(MouseEvent mouseEvent) {
        var pj = HelloApplication.getProjectConfig();
        LoginInterNet.submitLogin(pj.getUsername(), pj.getPassword());
    }

    /**
     * 点击登出按钮事件
     *
     * @param mouseEvent 鼠标事件
     */
    private void logout_Btn_MouseClicked(MouseEvent mouseEvent) {
        var pj = HelloApplication.getProjectConfig();
        LoginInterNet.logout(pj.getUsername(), pj.getPassword());
    }

    //endregion

}
