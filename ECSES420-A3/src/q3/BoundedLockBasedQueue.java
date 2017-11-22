package q3;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedLockBasedQueue<T> {
    private T[] items;

    private int head = 0;
    private int tail = 0;

    private Lock lockHead = new ReentrantLock();
    private Lock lockTail = new ReentrantLock();

    private Condition notEmpty = lockHead.newCondition();
    private Condition notFull  = lockTail.newCondition();


    @SuppressWarnings("unchecked")
	public BoundedLockBasedQueue(int capacity) {
        items = (T[]) new Object[capacity];
    }

    public void enqueue(T item) {
        lockTail.lock();
        try {
            while (tail - head == items.length) {
                try { notFull.await(); } catch (InterruptedException ie) {}
            }
            items[tail % items.length] = item;

            tail++;

            if (tail - head == 1)
                notEmpty.signal();
        } finally {
            lockTail.unlock();
        }
    }

    public T dequeue() {
        lockHead.lock();
        try {
            while (tail - head == 0) {
                try { notEmpty.await(); } catch (InterruptedException ie) {}
            }
            T x = items[head % items.length];
            head++;
            if (tail - head == items.length - 1)
                notFull.signal();
            return x;
        } finally {
            lockHead.unlock();
        }

    }

}