package com.hql.lightning.channel;

import java.util.HashMap;
import java.util.Map;

/**
 * 游戏频道管理类
 *
 * @author lee
 *         2015-2-5
 */
public class GameChannelManager {

    private Map<String, GameChannel> channelMap = new HashMap<String, GameChannel>();

    private static final GameChannelManager instance = new GameChannelManager();

    public static final GameChannelManager getInstance() {
        return instance;
    }

    public GameChannel getChannel(String name) {
        synchronized (channelMap) {
            return channelMap.get(name);
        }
    }

    public GameChannel addChannel(String name) {
        synchronized (channelMap) {
            GameChannel channel = channelMap.get(name);
            if (channel != null)
                return channel;
            else {
                channel = new GameChannel(name);
                channelMap.put(name, channel);
                return channel;
            }
        }
    }
}
