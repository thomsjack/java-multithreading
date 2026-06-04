package com.yfy.multithreading.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Executors;

public class CompletableFutureDemo {

    public static void main(String[] args) throws Exception {
        // 建议在生产环境中为 CompletableFuture 提供自定义的线程池，避免使用默认的公共 ForkJoinPool 造成任务竞争
        ExecutorService customThreadPool = new ThreadPoolExecutor(
                2,
                4,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(50),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        try {
            // 同步运行（不带返回值），传入自定义线程池
            CompletableFuture<Void> completableFuture1 = CompletableFuture.runAsync(() -> {
                System.out.println(Thread.currentThread().getName() + "\t completableFuture1");
            }, customThreadPool);
            completableFuture1.get();

            // 异步回调（带返回值），传入自定义线程池
            CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName() + "\t completableFuture2");
                int i = 10 / 0; // 模拟抛出异常
                return 1024;
            }, customThreadPool);

            // 用 whenComplete 和 exceptionally 处理结果/异常
            completableFuture2.whenComplete((t, u) -> {
                System.out.println("whenComplete -------t=" + t); // 正常返回的结果
                System.out.println("whenComplete -------u=" + u); // 抛出的异常
            }).exceptionally(f -> {
                System.out.println("exceptionally -----exception:" + f.getMessage());
                return 444; // 异常发生时的默认返回值
            }).get();

            System.out.println("----------------------------------------");

            // 演示使用 handle() 处理结果和异常（比 exceptionally 更强大，支持转换返回值类型）
            CompletableFuture<String> completableFuture3 = CompletableFuture.supplyAsync(() -> {
                return "hello";
            }, customThreadPool).handle((res, ex) -> {
                if (ex != null) {
                    return "error handler: " + ex.getMessage();
                }
                return res + " world";
            });
            System.out.println("handle result: " + completableFuture3.get());

        } finally {
            customThreadPool.shutdown();
        }
    }
}
