package com.hql.lightning.buffer;

import com.hql.lightning.core.Connection;
import com.alibaba.fastjson.JSONObject;

/**
 * 上行消息类
 *
 * @author lee
 *         2015-2-3
 */
public class GameUpBuffer {

    /**
     * 客户端连接
     */
    private Connection connection;

    /**
     * 接收数据包
     */
    private JSONObject buffer;

    public GameUpBuffer(JSONObject buffer, Connection connection) {
        this.connection = connection;
        this.buffer = buffer;
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * 获取应放入的逻辑工作线程
     *
     * @return
     */
    public String getModule() {
        return buffer.getString("module");
    }

    /**
     * 获取指令
     *
     * @return
     */
    public String getCmd() {
        return buffer.getString("cmd");
    }

    /**
     * 获取数据参数
     *
     * @return
     */
    public JSONObject getData() {
        return buffer.containsKey("data") ? buffer.getJSONObject("data") : null;
    }
}
