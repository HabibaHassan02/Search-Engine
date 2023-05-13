package dev.se.SSE;

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
}
