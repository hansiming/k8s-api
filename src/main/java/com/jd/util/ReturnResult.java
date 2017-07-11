package com.jd.util;

/**
 * Created by yuanshuaiming on 2016/7/7.
 */
public class ReturnResult extends ReturnMessage {
    private Object data;

    public ReturnResult() {
        this.setStatus(false);
        this.setMessage("");
        this.setData(null);
    }

    public ReturnResult(Boolean status, String message, Object data) {
        this.setStatus(status);
        this.setMessage(message);
        this.setData(data);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
