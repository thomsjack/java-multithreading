package com.yfy.multithreading.basic.thread;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo implements Runnable {
    // false = 非公平锁 (默认)，容易造成线程饥饿（同一个线程释放锁后立即重新抢占成功）
    // true = 公平锁，按照线程等待锁的顺序来分配锁
    private static ReentrantLock lock = new ReentrantLock(false);

    @Override
    public void run() {
        while (true) {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " get lock");
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            
            // 优化点：释放锁后休眠/让出 CPU 资源一段时间，给其他阻塞等待的线程获取锁的机会，避免单线程饥饿和独占
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ReentrantLockDemo rtld = new ReentrantLockDemo();
        Thread thread1 = new Thread(rtld, "Thread-1");
        Thread thread2 = new Thread(rtld, "Thread-2");
        thread1.start();
        thread2.start();
    }
}
