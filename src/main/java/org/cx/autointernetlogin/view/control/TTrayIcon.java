package org.cx.autointernetlogin.view.control;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;

/**
 * 自定义托盘图标
 */
public class TTrayIcon extends TrayIcon {

    /**
     * 双击托盘图标时触发
     */
    public BiConsumer<Object, MouseEvent> mouseDoubleClickEvent;

    //设置面板和布局为静态变量
    private Stage stage = new Stage();
    private StackPane pane = new StackPane();

    // 实例初始化块
    {
        stage.setScene(new Scene(pane));
        // 去除面板标题栏
        stage.initStyle(StageStyle.TRANSPARENT);
        // 监听面板焦点
        stage.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                // 失去焦点时隐藏面板
                stage.hide();
            }
        });
    }

    public TTrayIcon(Image image, String tooltip, Region menu) {
        super(image, tooltip);

        // 设置系统托盘图标为自适应
        this.setImageAutoSize(true);
        // 添加组件到面板中
        pane.getChildren().add(menu);
        // 设置面板的宽高
        stage.setWidth(menu.getPrefWidth());
        stage.setHeight(menu.getPrefHeight());
        // 添加鼠标事件
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                // e.getButton(): 1 左键, 2 中键, 3 右键
                if (e.getButton() == 1) {
                    // 左键双击
                    if (e.getClickCount() == 2 && mouseDoubleClickEvent != null) {
                        mouseDoubleClickEvent.accept(this, e);
                    }
                } else if (e.getButton() == 3) {
                    // 非 javafx 线程使用 javafx 控件需通过 Platform.runLater 调用
                    Platform.runLater(() -> {
                        // 托盘面板显示位置
                        stage.setX(e.getX() - (stage.getWidth() / 2));
                        stage.setY(e.getY() - stage.getHeight());
                        // 将托盘面板置顶
                        stage.setAlwaysOnTop(true);
                        // 切换显示/隐藏
                        if (!stage.isShowing()) {
                            stage.show();
                        } else {
                            stage.hide();
                        }
                    });
                }
            }
        });
    }

}
