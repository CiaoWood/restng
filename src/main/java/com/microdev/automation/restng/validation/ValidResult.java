package com.microdev.automation.restng.validation;

/**
 * @author wuchao
 * @date 2017/3/27
 */
public class ValidResult {
    private boolean result;
    private String msg;

    public ValidResult(boolean r, String message) {
        result = r;
        msg = message;
    }

    public ValidResult(boolean r) {
        result = r;
    }

    public boolean result() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ValidResult{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                '}';
    }
}
