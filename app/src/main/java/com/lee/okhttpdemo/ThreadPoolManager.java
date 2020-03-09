package com.lee.okhttpdemo;

import android.util.Log;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {

    private static ThreadPoolManager instance = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return instance;
    }

    // 创建线程池
    private ThreadPoolExecutor threadPoolExecutor;
    public ThreadPoolManager() {
        threadPoolExecutor = new ThreadPoolExecutor(3, 10, 15,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        //将拒绝的线程重新放回队列
                        addTask(r);
                    }
                });
        threadPoolExecutor.execute(coreThread);
        threadPoolExecutor.execute(delayThread);
    }

    //创建队列   将网络请求任务添加到队列中
    private LinkedBlockingDeque<Runnable> mQueue = new LinkedBlockingDeque<>();

    public void addTask(Runnable runnable){
        if (runnable != null){
            try {
                mQueue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //创建“叫号员”线程，监听线程池中是否存在空闲，不停的从队列中获取任务加入到线程池
    public Runnable coreThread = new Runnable() {
        Runnable runn = null;
        @Override
        public void run() {
            //死循环，不停的获取
            while (true){
                try {
                    runn = mQueue.take(); //队列中获取线程
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                threadPoolExecutor.execute(runn);   //加入到线程池
            }
        }
    };

    //创建延迟队列
    private DelayQueue<HttpTask> delayQueue = new DelayQueue<>();

    public void addDelayTask(HttpTask httpTask){
        if (httpTask != null){
            httpTask.setDelayTime(3000);
            delayQueue.offer(httpTask);
        }
    }

    public Runnable delayThread = new Runnable() {
        HttpTask ht = null;
        @Override
        public void run() {
            //死循环，不停的获取
            while (true){
                try {
                    ht = delayQueue.take(); //队列中获取线程
                    if (ht.getRetryCount() < 3){
                        threadPoolExecutor.execute(ht);   //加入到线程池
                        ht.setDelayTime(ht.getRetryCount() + 1);
                        Log.d("====重试机制====", ht.getRetryCount() + "    " + System.currentTimeMillis());
                    } else {
                        Log.d("====重试机制====", "重试超过三次，直接放弃");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
