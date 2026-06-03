package com.yfy.multithreading.basic.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPoolDemo {
    public static void main(String[] args) {
        // 使用 ThreadPoolExecutor 自定义线程池，规避 Executors.newCachedThreadPool() 的无界线程数带来的 OOM 风险
        ExecutorService threadPool = new ThreadPoolExecutor(
                2,                                 // 核心线程数
                5,                                 // 最大线程数
                60L,                               // 线程空闲存活时间
                TimeUnit.SECONDS,                  // 时间单位
                new LinkedBlockingQueue<>(10),     // 阻塞队列，指定容量
                Executors.defaultThreadFactory(),  // 线程工厂
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略：调用者运行
        );

        Future<String> future = threadPool.submit(new MyCallable());
        if (!future.isDone()) {
            System.out.println("task has not finished, please wait!");
        }
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}
