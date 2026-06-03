package com.yfy.multithreading.basic.thread;

import java.util.concurrent.atomic.AtomicInteger;

public class CASCase {
    // 方式一：传统的同步锁
    public volatile int value;

    public synchronized void add() {
        value++;
    }

    // 方式二：使用 AtomicInteger，其内部基于 CAS（无锁自旋）
    public AtomicInteger atomicValue = new AtomicInteger(0);

    public void atomicAdd() {
        atomicValue.incrementAndGet();
    }

    // 方式三：手动模拟 CAS 的自旋重试逻辑，展示 Compare-And-Swap 的底层的机制
    public void compareAndSwapAdd() {
        int oldValue;
        int newValue;
        do {
            oldValue = atomicValue.get();
            newValue = oldValue + 1;
            // 只有当期望的 oldValue 与当前内存中的值相同时，才更新为 newValue。否则 compareAndSet 返回 false，继续循环自旋。
        } while (!atomicValue.compareAndSet(oldValue, newValue));
    }
}
