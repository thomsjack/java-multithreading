package com.yfy.multithreading.basic.thread;

public class SyncBlockAndMethod {
    public void syncsTask() {
        // 同步代码块（演示锁的可重入性：同一个线程可以多次获取同一个锁而不死锁）
        synchronized (this) {
            System.out.println("Hello");
            synchronized (this) {
                System.out.println("World");
            }
        }
    }

    public synchronized void syncTask() {
        System.out.println("Hello Again");
    }

}
