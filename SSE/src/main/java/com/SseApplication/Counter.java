package com.SseApplication;

public class Counter {
    private int count;

    public Counter() {
        count = 0;
    }

    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
    public synchronized void setCount(int set){
        this.count=set;

    }
}
