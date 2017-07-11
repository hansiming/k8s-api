package com.jd.util;

/**
 * Created by hansiming on 2017/7/10.
 */
public class ReturnMessage {

    enum ErrorType {
        PARAMETER_INVALID, SERVICE_ERROR
    }

    private boolean status;
    private String message;

    public ReturnMessage() {
        this(false, "");
    }

    public ReturnMessage(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ReturnMessage success() {
        return new ReturnMessage(true, "success");
    }

    public static ReturnMessage success(String message) {
        return new ReturnMessage(true, message);
    }

    public static ReturnMessage error(String errorMessage) {
        return new ReturnMessage(false, errorMessage);
    }

    public static ReturnMessage paramError(String paramName) {
        return error(ErrorType.PARAMETER_INVALID + ":" + paramName);
    }

    public static ReturnMessage serviceError(String message) {
        return error(ErrorType.SERVICE_ERROR + ":" + message);
    }
}
