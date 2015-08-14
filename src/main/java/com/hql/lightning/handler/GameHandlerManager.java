package com.hql.lightning.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务逻辑管理类
 *
 * @author lee
 *         2015-2-3
 */
public class GameHandlerManager {

    private static GameHandlerManager instance = new GameHandlerManager();

    /**
     * 业务逻辑集合
     */
    private Map<String, GameHandler> handlerMap = new HashMap<String, GameHandler>();

    public static GameHandlerManager getInstance() {
        return instance;
    }

    /**
     * 注册业务逻辑
     *
     * @param cls
     * @param cmd
     * @param module
     * @throws Exception
     */
    public void register(GameHandler cls, String cmd, String module) throws Exception {
        handlerMap.put(cmd + "-" + module, cls);
    }

    /**
     * 获取业务逻辑
     *
     * @param cmd
     * @return
     */
    public GameHandler getHandler(String cmd) {
        return handlerMap.get(cmd);
    }
}
