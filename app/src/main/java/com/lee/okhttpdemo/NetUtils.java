package com.lee.okhttpdemo;

import java.util.concurrent.ThreadPoolExecutor;

public class NetUtils {

    public static<T, M> void sendJsonRequest(String url, T requestData, Class<M> response, IJsonDataListener listener){
        IHttpRequest request = new JsonHttpRequest();
        CallbackListener callbackListener = new JsonCallbakListener<>(response, listener);
        HttpTask task = new HttpTask(url, requestData, request, callbackListener);
        ThreadPoolManager.getInstance().addTask(task);
    }
}
