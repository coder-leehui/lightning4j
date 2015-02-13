package com.hql.lightning.util;

import com.hql.lightning.handler.GameHandler;
import com.hql.lightning.handler.GameHandlerManager;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 模块加载工具类
 *
 * @author lee
 *         2015-2-12
 */
public class ModuleUtil {

    private static Logger logger = Logger.getLogger(ModuleUtil.class);

    /**
     * 模块信息集合(moduleName->(handlerName->className))
     */
    private Map<String, HashMap<String, String>> moduleInfo = new HashMap<String, HashMap<String, String>>();

    private static ModuleUtil instance = new ModuleUtil();

    public static ModuleUtil getInstance() {
        return instance;
    }

    /**
     * 初始化所有模块
     */
    public void init() {
        try {
            parseModuleConf();
            Set<Map.Entry<String, HashMap<String, String>>> sets = moduleInfo.entrySet();
            for (Map.Entry<String, HashMap<String, String>> entry : sets) {
                String jarPath = ProReaderUtil.getInstance().getConfPath() + "/modules/" + entry.getKey() + ".jar";
                URLClassLoader loader = new URLClassLoader(new URL[]{new File(jarPath).toURI().toURL()});
                registerHandlersFromModule(entry.getValue(), loader);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 解析模块配置文件
     */
    private void parseModuleConf() {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(ProReaderUtil.getInstance().getModulePro());
            Element root = doc.getRootElement();

            List list = root.elements();

            for (Iterator its = list.iterator(); its.hasNext(); ) {
                Element module = (Element) its.next();
                moduleInfo.put(module.getName(), getHandlerInfo(module));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 获取模块handler集合
     *
     * @param ele
     * @return
     * @throws Exception
     */
    private HashMap<String, String> getHandlerInfo(Element ele) throws Exception {
        HashMap<String, String> handlerInfo = new HashMap<String, String>();

        List list = ele.elements();

        for (Iterator itr = list.iterator(); itr.hasNext(); ) {
            Element chileEle = (Element) itr.next();
            handlerInfo.put(chileEle.getName().trim(), chileEle.getText().trim());
        }

        return handlerInfo;
    }

    /**
     * 更新/添加模块
     *
     * @param moduleName
     */
    public void updateModule(String moduleName) {
        HashMap<String, String> module;
        String jarPath = ProReaderUtil.getInstance().getConfPath() + "/modules/" + moduleName + ".jar";
        URLClassLoader loader;

        try {
            module = moduleInfo.get(moduleName);
            loader = new URLClassLoader(new URL[]{new File(jarPath).toURI().toURL()});

            if (module == null) {
                SAXReader reader = new SAXReader();
                Document doc = reader.read(ProReaderUtil.getInstance().getModulePro());
                Element root = doc.getRootElement();

                List list = root.elements();

                for (Iterator itr = list.iterator(); itr.hasNext(); ) {
                    Element chileEle = (Element) itr.next();
                    if (chileEle.getName().equals(moduleName)) {
                        module = getHandlerInfo(chileEle);
                        moduleInfo.put(moduleName, module);
                        break;
                    }
                }
            }

            registerHandlersFromModule(module, loader);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 根据模块信息注册handler
     *
     * @param module
     * @param loader
     */
    private void registerHandlersFromModule(HashMap<String, String> module, URLClassLoader loader) {
        try {
            Set<Map.Entry<String, String>> childSets = module.entrySet();
            for (Map.Entry<String, String> childEntry : childSets) {
                Class<?> cls = loader.loadClass(childEntry.getValue());
                GameHandler handler = (GameHandler) cls.newInstance();
                GameHandlerManager.getInstance().register(handler, childEntry.getKey());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 获取模块信息
     *
     * @return
     */
    public String getModuleInfo() {
        String rs = "";
        Set<Map.Entry<String, HashMap<String, String>>> sets = moduleInfo.entrySet();
        for (Map.Entry<String, HashMap<String, String>> entry : sets) {
            rs += entry.getKey() + ":\n";

            Set<Map.Entry<String, String>> childSets = entry.getValue().entrySet();
            for (Map.Entry<String, String> childEntry : childSets) {
                rs += ("∟" + childEntry.getKey() + ":" + childEntry.getValue() + "\n");
            }
        }

        return rs;
    }
}
