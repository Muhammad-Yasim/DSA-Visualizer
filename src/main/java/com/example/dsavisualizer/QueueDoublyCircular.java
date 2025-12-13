package com.example.dsavisualizer;

public class QueueDoublyCircular {
        private int front = -1;
        private int rear = -1;
        private int[] arr;
        private int size = 0;

        public QueueDoublyCircular(int capacity) {
            arr = new int[capacity];
        }

        public void enqueueFront(int value) {
            if (size == arr.length)
                System.out.println("Deque is full");
            else {
                if (front == -1) {
                    front = rear = 0;
                } else {
                    front = (front - 1 + arr.length) % arr.length;
                }
                arr[front] = value;
                size++;
            }
        }

        public void enqueueBack(int value) {
            if (size == arr.length)
                System.out.println("Deque is full");
            else {
                if (rear == -1) {
                    front = rear = 0;
                } else {
                    rear = (rear + 1) % arr.length;
                }
                arr[rear] = value;
                size++;
            }
        }

        public int dequeueFront() {
            if (size == 0) {
                System.out.println("Deque is empty");
                return -1;
            }
            int val = arr[front];
            front = (front + 1) % arr.length;
            size--;
            if (size == 0)
                front = rear = -1;
            return val;
        }

        public int dequeueBack() {
            if (size == 0) {
                System.out.println("Deque is empty");
                return -1;
            }
            int val = arr[rear];
            rear = (rear - 1 + arr.length) % arr.length;
            size--;
            if (size == 0)
                front = rear = -1;
            return val;
        }

    public int size() {
        return size;
    }

    public int frontIndex() {
        return front;
    }

    public int rearIndex() {
        return rear;
    }

}
