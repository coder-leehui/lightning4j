package com.hql.lightning.core;

import org.apache.log4j.Logger;

import com.hql.lightning.buffer.GameUpBuffer;
import com.hql.lightning.handler.GameHandler;
import com.hql.lightning.handler.GameHandlerManager;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 逻辑处理工作线程类
 *
 * @author lee
 *         2015-2-3
 */
public class GameWorker implements Runnable {

    private static Logger logger = Logger.getLogger(GameWorker.class);

    /**
     * 线程运行标志
     */
    private volatile boolean isRunning = true;

    /**
     * 停止线程
     */
    public void closeWorker() {
        isRunning = false;
    }

    /**
     * 消息队列
     */
    Queue<GameUpBuffer> msgQueue = new ConcurrentLinkedQueue<GameUpBuffer>();

    /**
     * 放置消息到队列中
     *
     * @param data
     */
    public void pushUpstreamBuffer(GameUpBuffer data) {
        if (isRunning) {
            synchronized (msgQueue) {
                msgQueue.add(data);
                msgQueue.notifyAll();
            }
        }
    }

    /**
     * 队列消息是否处理完毕
     *
     * @return
     */
    public boolean isDone() {
        return msgQueue.isEmpty();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (msgQueue) {
                if (msgQueue.isEmpty()) {
                    try {
                        msgQueue.wait();
                    } catch (InterruptedException e) {
                        logger.info("receive an interrupted msg, now the state is " + isRunning);
                        if (!isRunning) {
                            return;
                        }
                    }
                }
            }

            GameUpBuffer c = null;
            while ((c = msgQueue.poll()) != null) {
                String cmd = c.getCmd();
                try {
                    GameHandler handler = GameHandlerManager.getInstance().getHandler(cmd);
                    if (handler != null) {
                        //记录执行时间过长的指令
                        long startTime = System.currentTimeMillis();
                        handler.process(c.getConnection(), c.getData());
                        long endTime = System.currentTimeMillis();

                        if ((startTime - endTime) > 500) {
                            logger.error("process too long time, cmd:" + cmd);
                        }
                    } else {
                        logger.error("error cmd = " + cmd);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }
}
