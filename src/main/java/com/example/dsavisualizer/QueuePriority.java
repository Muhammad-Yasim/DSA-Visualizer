package com.example.dsavisualizer;

public class QueuePriority {
    private int[] queue;
    private int front = 0;
    private int rear = -1;
    private int size = 0;

    public QueuePriority() {
        queue = new int[7];
    }

    public void enqueue(int element) {
        if (size == queue.length) {
            System.out.println("Queue Full");
            return;
        }

        int i = rear;
        while (i >= front && queue[i] < element) {
            queue[i + 1] = queue[i];
            i--;
        }

        queue[i + 1] = element;

        rear++;
        size++;
    }

    public int dequeue() {
        if (size == 0) {
            System.out.println("Queue Empty");
            return -1;
        }

        int element = queue[front++];
        size--;

        if (size == 0) { //Reset when empty
            front = 0;
            rear = -1;
        }

        return element;
    }
}