package com.hql.lightning.util.jdbc;

import com.hql.lightning.util.ProReaderUtil;

import java.util.HashMap;

/**
 * jdbc配置类
 */
public class DbConfig {

    private String driver;

    private String url;

    private String userName;

    private String password;

    public DbConfig() {
        try {
            HashMap<String, String> conf = ProReaderUtil.getInstance().getJdbcPro();
            this.driver = conf.get("driver");
            this.url = conf.get("url");
            this.userName = conf.get("userName");
            this.password = conf.get("password");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
