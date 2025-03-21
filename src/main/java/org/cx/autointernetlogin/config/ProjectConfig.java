package org.cx.autointernetlogin.config;

import org.cx.autointernetlogin.constant.CheckIntervalEnum;

/**
 * 项目配置
 */
public class ProjectConfig {

    private String username;
    private String password;
    private boolean showPassword = false;
    private boolean reloginCheck = true;
    private int reloginCheck_Interval = CheckIntervalEnum.ONE_MIN.getIndex();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isShowPassword() {
        return showPassword;
    }

    public void setShowPassword(boolean showPassword) {
        this.showPassword = showPassword;
    }

    public boolean isReloginCheck() {
        return reloginCheck;
    }

    public void setReloginCheck(boolean reloginCheck) {
        this.reloginCheck = reloginCheck;
    }

    public int getReloginCheck_Interval() {
        return reloginCheck_Interval;
    }

    public void setReloginCheck_Interval(int reloginCheck_Interval) {
        this.reloginCheck_Interval = reloginCheck_Interval;
    }

}
