package com.example.dsavisualizer;

public class QueueCircular {
    private int front = -1;
    private int rear = -1;
    private int arr[];
    private int size = 0;

    public QueueCircular(int size) {
        arr = new int[size];
    }

    void enqueue(int val) {
        if (size == arr.length) {
            System.out.println("Queue is FULL");
        }
        else {
            rear = (rear + 1) % arr.length;
            arr[rear] = val;
            size++;
            if (front == -1)
                front = 0;
        }
    }

    int dequeue() {
        if (size == 0) {
            System.out.println("Empty");
            return -1;
        }

        int element = arr[front];
        front = (front + 1) % arr.length;
        size--;
        if (size==0) {
            front = -1;
            rear = -1;
        }

        return element;
    }

    public int size(){
        if (front == -1)  // queue empty
            return 0;
        int sizef;
        sizef=(rear-front+arr.length)%arr.length+1;

        return sizef;
    }

    public int frontIndex() {
        return front;
    }

    public int rearIndex() {
        return rear;
    }

    public boolean isEmpty() {
        if(size==0)
            return true;
        return false;
    }

    public boolean isFull() {
        if(size==arr.length)
            return true;
        return false;
    }
}