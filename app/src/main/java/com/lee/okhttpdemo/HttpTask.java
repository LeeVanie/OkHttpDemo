package com.lee.okhttpdemo;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class HttpTask<T> implements Runnable, Delayed {

    private IHttpRequest mHttpRequest;

    public HttpTask(String url, T requestData, IHttpRequest httpRequest, CallbackListener callbackListener) {
        mHttpRequest = httpRequest;
        httpRequest.setUrl(url);
        httpRequest.setListener(callbackListener);
        String content = JSON.toJSONString(requestData);
        try {
            httpRequest.setData(content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            mHttpRequest.excute();
        } catch (Exception e){
            ThreadPoolManager.getInstance().addDelayTask(this);
        }

    }

    private long delayTime;//延迟时间
    private int retryCount; //重试次数

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        //设置delayTIme要跟当前时间相加
        this.delayTime = System.currentTimeMillis() + delayTime;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public long getDelay(@NonNull TimeUnit unit) {
        return unit.convert(this.delayTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(@NonNull Delayed o) {
        return 0;
    }
}
