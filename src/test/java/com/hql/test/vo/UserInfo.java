package com.hql.test.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 测试数据包
 */
public class UserInfo {

    private int id;

    private String name;

    @JSONField(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
