package org.cx.autointernetlogin.service;

import org.cx.autointernetlogin.HelloApplication;
import org.cx.autointernetlogin.constant.CheckIntervalEnum;
import org.cx.autointernetlogin.util.LogUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

/**
 * 重连检测
 */
public class ReloginCheck extends Thread {

    /**
     * 更新网络状态
     */
    public Consumer<Boolean> UpdateNetworkStatus;

    /**
     * 连续重连的请求上限
     */
    private final int requestLimit = 30;

    @Override
    public void run() {
        // 程序启动时启动, 直到程序终止; 循环间隔 1 秒.
        int timeCount = 0, requestCount = 0, checkUrl_Index = 0;
        String[] checkUrl = new String[]{
                "https://www.baidu.com/",
                "https://www.sina.com.cn/",
                "https://www.sohu.com/",
                "https://www.qq.com/",
                "https://www.163.com/",
                "https://www.youku.com/",
                "https://ai.taobao.com/",
                "https://weibo.com/",
                "https://s.click.taobao.com/",
                "https://www.tmall.com/"
        };
        while (true) {
            // 检测当前是否可以访问网络
            boolean networked;
            try {
                if (checkUrl_Index >= checkUrl.length) {
                    checkUrl_Index = 0;
                }
                URL url = new URL(checkUrl[checkUrl_Index]);
                checkUrl_Index++;
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD");
                connection.setConnectTimeout(3000); // 3 秒超时
                connection.setReadTimeout(3000);
                int responseCode = connection.getResponseCode();
                networked = (200 <= responseCode && responseCode <= 399);
            } catch (IOException e) {
                networked = false;
            }

            // 通知界面刷新状态
            if (UpdateNetworkStatus != null) {
                UpdateNetworkStatus.accept(networked);
            }

            // 检测是否勾选了掉线重连
            // 或当前是否无法访问网络
            // 或是否到达重连计数的上限
            boolean needRelogin = HelloApplication.getProjectConfig().isReloginCheck()
                    && HelloApplication.getProjectConfig().getUsername() != null && !HelloApplication.getProjectConfig().getUsername().isEmpty()
                    && HelloApplication.getProjectConfig().getPassword() != null && !HelloApplication.getProjectConfig().getPassword().isEmpty();
            if (!networked && needRelogin
                    && requestCount < requestLimit) {
                // 检测的间隔
                var interval = CheckIntervalEnum.query(HelloApplication.getProjectConfig().getReloginCheck_Interval());
                if (interval == null)
                    interval = CheckIntervalEnum.ONE_MIN;
                // 到达指定间隔时间后执行重连
                if (timeCount * 1000L >= interval.getMillis()) {
                    LogUtils.getLogger().info("正在尝试自动重新登录...");
                    LoginInterNet.submitLogin(HelloApplication.getProjectConfig().getUsername(), HelloApplication.getProjectConfig().getPassword());
                    requestCount++;
                }
            }

            if (networked) {
                requestCount = 0; // 连上网后重置计数
                timeCount = 0; // 重置计数
            } else {
                if (requestCount == requestLimit) {
                    LogUtils.getLogger().warning("已到达重连计数的上限(30次)！后续将不再自动尝试重新登录！手动登录成功后将接触这一限制。");
                }
            }

            // 睡眠
            try {
                Thread.sleep(1000L);
                if (networked)
                    timeCount++;
                else
                    timeCount += 4; // 算上检测网络时的等待超时3秒+循环时的1秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
