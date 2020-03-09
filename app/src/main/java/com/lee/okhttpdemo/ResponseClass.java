package com.lee.okhttpdemo;

public class ResponseClass {

    private String reason;
    private String result;

    public ResponseClass() {
    }

    public ResponseClass(String reason, String result) {
        this.reason = reason;
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
