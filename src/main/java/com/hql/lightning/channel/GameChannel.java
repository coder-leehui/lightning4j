package com.hql.lightning.channel;

import com.alibaba.fastjson.JSONObject;
import com.hql.lightning.buffer.GameDownBuffer;
import com.hql.lightning.core.Connection;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 游戏频道类
 *
 * @author lee
 *         2015-2-5
 */
public class GameChannel {

    /**
     * 客户端连接集合
     */
    private DefaultChannelGroup channel;

    public GameChannel(String name) {
        channel = new DefaultChannelGroup(name, GlobalEventExecutor.INSTANCE);
    }

    /**
     * 添加连接到频道
     *
     * @param c
     * @return
     */
    public boolean addConnection(Connection c) {
        return channel.add(c.getChannel());
    }

    /**
     * 从频道中删除连接
     *
     * @param c
     * @return
     */
    public boolean removeConnection(Connection c) {
        return channel.remove(c.getChannel());
    }

    /**
     * 向频道中的连接广播数据
     *
     * @param buffer
     * @throws Exception
     */
    public void broadcast(GameDownBuffer buffer) throws Exception {
        TextWebSocketFrame frame = new TextWebSocketFrame(JSONObject.toJSONString(buffer));
        channel.writeAndFlush(frame);
    }
}
