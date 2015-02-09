package com.hql.test.handler;

import com.alibaba.fastjson.JSONObject;
import com.hql.lightning.buffer.GameDownBuffer;
import com.hql.lightning.channel.GameChannelManager;
import com.hql.lightning.core.Connection;
import com.hql.lightning.handler.GameHandler;
import com.hql.test.vo.UserInfo;

/**
 * 游戏测试逻辑
 */
public class HandlerTest implements GameHandler {

    @Override
    public void process(final Connection connection, JSONObject data) throws Exception {
        int id = data.getInteger("id");
        String name = data.getString("name");

        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setName(name);

        GameDownBuffer buf = new GameDownBuffer();
        buf.setCmd("testCmd");
        buf.setData(userInfo);

        //connection.write(buf);
        GameChannelManager.getInstance().getChannel("allUser").broadcast(buf);//广播消息
    }
}
