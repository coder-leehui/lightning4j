package com.hql.lightning.buffer;

/**
 * 下行消息类
 *
 * @author lee
 *         2015-2-5
 */
public class GameDownBuffer {

    /**
     * 数据vo
     */
    private Object data;

    /**
     * 指令
     */
    private String cmd;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
