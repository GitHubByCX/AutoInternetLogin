# 自动网络登录

## 一. 项目介绍

本项目用于ZDNK-陈家桥办公室电脑的自动上网登录。

包含的功能包括上网登录和定时检测当前是否可以访问网络，当无法访问时将自动重新登录。
</br>

## 二. 使用说明

![img01](https://github.com/GitHubByCX/Picture/blob/master/workspace/AutoInternetLogin/readme/img01.png)

(1) 必要的配置

首先需要在主界面中填写自己的登录用户名，第二个输入框填写登录密钥；

关于登录密钥需要从登录服务器获取，获取步骤如下:

![img02](https://github.com/GitHubByCX/Picture/blob/master/workspace/AutoInternetLogin/readme/img02.png)

首先打开浏览器，启动F12开发者工具，切换到’网络‘项（记得筛选条件设置为全部），然后填写自己的用户名和密码，点击登录；

![img03](https://github.com/GitHubByCX/Picture/blob/master/workspace/AutoInternetLogin/readme/img03.png)

登录成功后，选择目标服务器（10.10.11.2），查看负载，复制密钥即可。
</br>

## 三. 通过项目生成程序(exe)

(1) 首先克隆并通过 IntelliJ IDEA 打开项目，再重新加载 Maven，等待 Maven 检查和下载依赖。

![img04](https://github.com/GitHubByCX/Picture/blob/master/workspace/AutoInternetLogin/readme/img04.png)

(2) 通过 Maven 生成项目，或者执行命令 'mvn clean compile package -P release'

![img05](https://github.com/GitHubByCX/Picture/blob/master/workspace/AutoInternetLogin/readme/img05.png)

(3) 此时打包好的程序文件应该就在 ’..\项目根目录\target\AutoInternetLogin\‘ 下了。

![img06](https://github.com/GitHubByCX/Picture/blob/master/workspace/AutoInternetLogin/readme/img06.png)

执行exe即可启动程序
</br>

## 四. 设置开机自启

(1) 为主程序创建一个快捷方式

![img07](https://github.com/GitHubByCX/Picture/blob/master/workspace/AutoInternetLogin/readme/img07.png)

(2) 按Win+R键，输入 ’shell:startup‘ 打开自启文件夹，并将刚刚创建的快捷方式放入即可。

![img08](https://github.com/GitHubByCX/Picture/blob/master/workspace/AutoInternetLogin/readme/img08.png)
