package com.hql.lightning.core;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接管理器类
 *
 * @author lee
 *         2015-1-30
 */
public class ConnectionManager {

    /**
     * id生成器
     */
    private AtomicInteger idMaker = new AtomicInteger(0);

    /**
     * 连接存储集合
     */
    private Map<Integer, Connection> connections = new ConcurrentHashMap<Integer, Connection>();

    private static final ConnectionManager instance = new ConnectionManager();

    public static final ConnectionManager getInstance() {
        return instance;
    }

    /**
     * 添加一个连接到集合
     *
     * @param c
     */
    public void addConnection(Connection c) {
        connections.put(c.getId(), c);
    }

    /**
     * 根据连接上下文添加一个连接到集合
     *
     * @param ctx
     */
    public Connection addConnection(ChannelHandlerContext ctx) {
        final Connection c = new Connection(idMaker.incrementAndGet(), ctx);
        connections.put(c.getId(), c);
        return c;
    }

    /**
     * 从集合中获取一个连接
     *
     * @param id
     * @return
     */
    public Connection getConnection(int id) {
        return connections.get(id);
    }

    /**
     * 从集合中删除一个连接
     *
     * @param c
     */
    public void removeConnection(Connection c) {
        Connection conn = connections.remove(c.getId());
        conn.close();
    }
}
