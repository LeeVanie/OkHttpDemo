package com.lee.okhttpdemo;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonCallbakListener<T> implements CallbackListener {

    private Class<T> responseClass;
    private Handler handler = new Handler(Looper.getMainLooper());
    private IJsonDataListener jsonDataListener;

    public JsonCallbakListener(Class<T> responseClass, IJsonDataListener jsonDataListener) {
        this.responseClass = responseClass;
        this.jsonDataListener = jsonDataListener;
    }

    @Override
    public void onSuccess(InputStream inputStream) {
        //将流转换成对应的T类型
        String response = getContent(inputStream);
        final T clazz = JSON.parseObject(response, responseClass);
        handler.post(new Runnable() {
            @Override
            public void run() {
                jsonDataListener.onSuccess(clazz);
            }
        });
    }

    private String getContent(InputStream inputStream) {
        String content = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                System.out.println("Error=" + e.toString());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("Error=" + e.toString());
                }

            }
            return sb.toString();
        } catch (Exception e){
            e.printStackTrace();
        }
        return content;

    }

    @Override
    public void onFailure() {

    }
}
