package org.cx.autointernetlogin.service;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.cx.autointernetlogin.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class LoginInterNet {

    public static boolean submitLogin(String username, String password) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建 POST 请求
            HttpPost httpPost = new HttpPost("http://10.10.11.2:8888");

            // 设置请求头，模拟浏览器行为
            httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate");
            httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
            httpPost.setHeader("Cache-Control", "max-age=0");
            httpPost.setHeader("Connection", "keep-alive");
            httpPost.setHeader("Host", "10.10.11.2:8888");
            httpPost.setHeader("Upgrade-Insecure-Requests", "1");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36 Edg/134.0.0.0");

            // 设置表单数据
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("language", "1"));
            params.add(new BasicNameValuePair("actionType", "umlogin"));
            params.add(new BasicNameValuePair("userIpMac", ""));
            params.add(new BasicNameValuePair("authorization", password));
            params.add(new BasicNameValuePair("redirectUrl", ""));
            params.add(new BasicNameValuePair("password", ""));
            params.add(new BasicNameValuePair("userName", username));

            // 设置请求体
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            // 发送请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String responseString = new String(entity.getContent().readAllBytes(), "gb2312");
                    LogUtils.getLogger().info("Login Response: " + responseString);
                    System.out.println("Login Response: " + responseString);
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            LogUtils.getLogger().log(Level.WARNING, "登录失败！", e);
            return false;
        }
    }

    public static boolean logout(String username, String password) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建 POST 请求
            HttpPost httpPost = new HttpPost("http://10.10.11.2:8888");

            // 设置请求头，模拟浏览器行为
            httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate");
            httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
            httpPost.setHeader("Cache-Control", "max-age=0");
            httpPost.setHeader("Connection", "keep-alive");
            httpPost.setHeader("Host", "10.10.11.2:8888");
            httpPost.setHeader("Upgrade-Insecure-Requests", "1");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36 Edg/134.0.0.0");

            // 设置表单数据
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("language", "1"));
            params.add(new BasicNameValuePair("actionType", "umlogout"));
            params.add(new BasicNameValuePair("userIpMac", ""));
            params.add(new BasicNameValuePair("authorization", password));
            params.add(new BasicNameValuePair("redirectUrl", ""));
            params.add(new BasicNameValuePair("password", ""));
            params.add(new BasicNameValuePair("userName", username));

            // 设置请求体
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            // 发送请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String responseString = new String(entity.getContent().readAllBytes(), "gb2312");
                    LogUtils.getLogger().info("Logout Response: " + responseString);
                    System.out.println("Logout Response: " + responseString);
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.getLogger().log(Level.WARNING, "登出失败！", e);
            return false;
        }
    }

}
