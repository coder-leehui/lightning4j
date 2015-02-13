package com.hql.lightning.core;

import com.hql.lightning.util.ModuleUtil;
import com.hql.lightning.util.ProReaderUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 服务初始化类
 *
 * @author lee
 *         2015-1-30
 */
public class ServerInit {

    private static Logger logger = Logger.getLogger(ServerInit.class);

    private static ServerInit instance = new ServerInit();

    public static ServerInit getInstance() {
        return instance;
    }

    /**
     * 初始化log4j
     */
    public void initLog4j() {
        try {
            PropertyConfigurator.configure(ProReaderUtil.getInstance().getLog4jPro());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 初始化逻辑工作线程
     */
    public void initGameWorkers() {
        GameWorkerManager.getInstance().init();
    }

    /**
     * 初始化配置文件根路径
     */
    public void initConfPath(String path) {
        ProReaderUtil.getInstance().setConfPath(path);
    }

    /**
     * 初始化游戏业务模块
     */
    public void initModules() {
        ModuleUtil.getInstance().init();
        logger.info("The loaded business modules:\n" + ModuleUtil.getInstance().getModuleInfo());
    }
}
