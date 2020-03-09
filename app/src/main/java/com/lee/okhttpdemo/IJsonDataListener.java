package com.lee.okhttpdemo;

public interface IJsonDataListener<T> {

    void onSuccess(T t);

    void onFailure();
}
