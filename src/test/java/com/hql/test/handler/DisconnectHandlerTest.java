package com.hql.test.handler;

import com.alibaba.fastjson.JSONObject;
import com.hql.lightning.core.Connection;
import com.hql.lightning.handler.GameHandler;

/**
 * 离线事件处理测试
 */
public class DisconnectHandlerTest implements GameHandler {

    @Override
    public void process(final Connection connection, JSONObject data) throws Exception {
        System.out.println("client disconnect.");
    }
}
