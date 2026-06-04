package com.yfy.multithreading.special;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//资源类 = 实例变量+实例方法
class Ticket {
    private int number = 30;
    // List list = new ArrayList();
    Lock lock = new ReentrantLock();

    public void sale() {
        lock.lock();
        try {
            if (number > 0) {
                System.out.println(Thread.currentThread().getName() + "\t卖出第：" + (number--) + "\t 还剩下： " + number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


}

/**
 * 题目：三个售票员         卖出          30张票
 * 笔记：如何编写企业级的多线程代码
 * 固定的编程套路+模板是什么？
 * <p>
 * 1 在高内聚低耦合的前提下，线程        操作      资源类
 * <p>
 * 1.1 一言不合，先创建一个资源类
 */
public class SaleTicketDemo01 {
    public static void main(String[] args)//主线程，一切程序的入口
    {
        Ticket ticket = new Ticket();


//
//        @FunctionalInterface
//        public interface Runnable {
//
//            public abstract void run();
//        }

        // 使用 Lambda 表达式创建并启动线程（推荐：简洁，高内聚）
        new Thread(() -> {
            for (int i = 1; i <= 40; i++) ticket.sale();
        }, "A").start();
        
        new Thread(() -> {
            for (int i = 1; i <= 40; i++) ticket.sale();
        }, "B").start();
        
        new Thread(() -> {
            for (int i = 1; i <= 40; i++) ticket.sale();
        }, "C").start();

        /*
         * 对比：下面是传统的匿名内部类写法（已注释，用于对比学习）
         * 
         * new Thread(new Runnable() {
         *     @Override
         *     public void run() {
         *         for (int i = 1; i <= 40; i++) {
         *             ticket.sale();
         *         }
         *     }
         * }, "A-Legacy").start();
         */
    }
}