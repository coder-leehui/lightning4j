package com.hql.lightning.core;

import com.alibaba.fastjson.JSONObject;
import com.hql.lightning.buffer.GameDownBuffer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * 客户端连接类
 *
 * @author lee
 *         2015-1-30
 */
public class Connection {

    /**
     * 连接标识id
     */
    private int id;

    /**
     * 连接上下文
     */
    private ChannelHandlerContext ctx;

    private volatile boolean isClosed = false;

    public Connection(int id, ChannelHandlerContext ctx) {
        this.id = id;
        this.ctx = ctx;
    }

    public int getId() {
        return id;
    }

    public Channel getChannel() {
        return ctx.channel();
    }

    /**
     * 往连接写入数据
     *
     * @param buffer
     */
    public void write(GameDownBuffer buffer) {
        TextWebSocketFrame frame = new TextWebSocketFrame(JSONObject.toJSONString(buffer));
        ctx.writeAndFlush(frame);
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (!isClosed) {
            isClosed = true;
            ctx.close();
        }
    }

    public boolean isClosed() {
        return isClosed;
    }
}
