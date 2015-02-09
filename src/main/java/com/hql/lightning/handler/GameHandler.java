package com.hql.lightning.handler;

import com.alibaba.fastjson.JSONObject;
import com.hql.lightning.core.Connection;

/**
 * 消息处理接口
 *
 * @author lee
 *         2015-1-30
 */
public interface GameHandler {

    /**
     * 处理服务器接收到的消息
     *
     * @param connection
     * @param data
     * @throws Throwable
     */
    public abstract void process(final Connection connection, JSONObject data) throws Exception;
}
