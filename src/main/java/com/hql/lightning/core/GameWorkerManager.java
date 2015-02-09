package com.hql.lightning.core;

import com.hql.lightning.buffer.GameUpBuffer;
import com.hql.lightning.util.ProReaderUtil;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * 逻辑处理工作线程管理器类
 *
 * @author lee
 *         2015-2-5
 */
public class GameWorkerManager {

    private static Logger logger = Logger.getLogger(GameWorkerManager.class);

    private static GameWorkerManager instance = new GameWorkerManager();

    /**
     * 工作线程集合
     */
    private HashMap<String, GameWorker> workers = new HashMap<String, GameWorker>();

    public static GameWorkerManager getInstance() {
        return instance;
    }

    /**
     * 初始化工作线程
     */
    public void init() {
        String[] workers = ProReaderUtil.getInstance().getWorkersPro().split(",");
        for (String w : workers) {
            final GameWorker worker = new GameWorker();
            setWorker(w, worker);
        }
    }

    /**
     * 启动工作线程
     *
     * @param module
     * @param worker
     */
    private void setWorker(String module, GameWorker worker) {
        workers.put(module, worker);
        new Thread(worker).start();
        logger.info("work thread:" + module + " has been started...");
    }

    /**
     * 获取工作线程
     *
     * @param module
     * @return
     */
    private GameWorker getWorker(String module) {
        return workers.get(module);
    }

    /**
     * 放置消息到指定工作线程
     *
     * @param buffer
     */
    public void pushDataToWorker(GameUpBuffer buffer) {
        if (buffer.getModule() != null) {
            GameWorker worker = getWorker(buffer.getModule());
            if (worker != null) {
                worker.pushUpstreamBuffer(buffer);
            } else {
                logger.error("module worker thread:" + buffer.getModule() + " is not found.");
            }
        } else {
            logger.error("module field:" + buffer.getModule() + " is not found.");
        }
    }
}
