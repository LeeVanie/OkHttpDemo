package com.lee.okhttpdemo;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    String url = "https://v.juhe.cn/laohuangli/d?date=2014-09-11&key=5c846f4f21a0fd5ba61d3aef456beedd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendRequest();
    }


    public void sendRequest(){
        NetUtils.sendJsonRequest(url, null, ResponseClass.class, new IJsonDataListener<ResponseClass>() {
            @Override
            public void onSuccess(ResponseClass o) {
                Log.d("#####", o.getResult().toString());
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
