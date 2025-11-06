package org.example.test;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueue<T> {

    private LinkedList<T> queue;
    private final int maxSize;
    private ReentrantLock emptyLock;
    private ReentrantLock fullLock;

    public BoundedQueue(int maxSize) {
        this.maxSize = maxSize;
        queue = new LinkedList<>();
    }

    void enqueue(T element) throws InterruptedException {
        if (queue.size() >= maxSize) {
            fullLock.wait();
        }
        queue.add(element);
        emptyLock.notify();
        emptyLock.newCondition()
    }

    T dequeue() throws InterruptedException {
        if (queue.isEmpty()) {
            emptyLock.wait();
        }
        T element = queue.remove();
        fullLock.notify();
        return element;
    }
}
