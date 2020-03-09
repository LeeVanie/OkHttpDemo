package com.lee.okhttpdemo;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonHttpRequest implements IHttpRequest {

    private String url;
    private byte[] data;
    private CallbackListener listener;
    private HttpURLConnection urlConnection;

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void setListener(CallbackListener callbackListener) {
        this.listener = callbackListener;
    }

    @Override
    public void excute() {
        URL url = null;
        try {
            url = new URL(this.url);
            urlConnection = (HttpURLConnection) url.openConnection(); //打开HTTP链接
            urlConnection.setConnectTimeout(6000);//设置连接时间
            urlConnection.setUseCaches(false);//不使用缓存
            urlConnection.setInstanceFollowRedirects(true);//仅作用于当前函数，设置这个连接是否可以被重定向
            urlConnection.setRequestMethod("GET");//设置请求方式
            urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
            urlConnection.setReadTimeout(3000);//响应超时时间
            urlConnection.setRequestProperty("Content-type","application/json;charset=UTF-8");//设置消息类型
            urlConnection.connect();//连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个服务器与TCP的连接
            /***  使用字节流发送数据 ***/
            OutputStream out = urlConnection.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(out);
            bos.write(data);
            bos.flush();
            out.close();
            bos.close();
            /***   字符流写入数据  ***/
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){//服务器的返回码是否连接成功
                InputStream in = urlConnection.getInputStream();
                listener.onSuccess(in);
                //这里就把我们的数据返回到框架里了
            } else {
                throw new RuntimeException("请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("请求失败");
        } finally {
            urlConnection.disconnect();
        }
    }
}
