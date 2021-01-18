package util.response;

import util.DateUtil;

/**
 * @program: markdown
 * @description:
 * @author: Zhang
 * @create: 2021-01-18 10:28
 **/
public class JsonMessage {
    private int status;
    private Object msg;
    private String serverTime;

    public JsonMessage(){
    }

    public JsonMessage(int status, Object msg, String serverTime){
        this.status = status;
        this.msg = msg;
        this.serverTime = serverTime;
    }

    public JsonMessage(int status, Object msg){
        this(status, msg, DateUtil.getSysDateTimeString());
    }


    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public Object getMsg() {
        return msg;
    }
    public void setMsg(Object msg) {
        this.msg = msg;
    }
    public String getServerTime() {
        return serverTime;
    }
    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

}
