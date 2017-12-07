package ca.mcgill.ecse420.a3;

import java.util.concurrent.atomic.AtomicInteger;

public class BoundedLockFreeQueue<T> {
    private T[] items;

    private AtomicInteger head = new AtomicInteger(0);
    private AtomicInteger tail = new AtomicInteger(0);
    private AtomicInteger tailValue = new AtomicInteger(0);
    private AtomicInteger remainingSpace;

    public BoundedLockFreeQueue(int capacity) {
        items = (T[]) new Object[capacity];
        remainingSpace = new AtomicInteger(capacity);
    }

    public void enqueue(T item) throws InterruptedException {
        int rS = remainingSpace.get();
        while (rS <= 0 || !remainingSpace.compareAndSet(rS, rS - 1)) 
        	rS = remainingSpace.get();
        
        int t = tail.getAndIncrement();
        items[t % items.length] = item;

        while (tailValue.compareAndSet(t, t + 1)) { };
    }

    public T dequeue() throws InterruptedException {
        int h = head.getAndIncrement();
        while (h >= tailValue.get()) { };
        T item = items[h % items.length];
        remainingSpace.incrementAndGet();

        return item;
    }
}

