package com.example.dsavisualizer;

public class QueueDoublyCircular {
        private int front = -1;
        private int rear = -1;
        private int[] arr;
        private int size = 0;

        public QueueDoublyCircular(int capacity) {
            arr = new int[capacity];
        }

        // ---------------- ENQUEUE FRONT ----------------
        public void enqueueFront(int value) {
            if (size == arr.length)
                System.out.println("Deque is full");
            else {
                if (front == -1) { // empty queue
                    front = rear = 0;
                } else {
                    front = (front - 1 + arr.length) % arr.length;
                }
                arr[front] = value;
                size++;
            }
        }

        // ---------------- ENQUEUE BACK ----------------
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

        // ---------------- DEQUEUE FRONT ----------------
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

        // ---------------- DEQUEUE BACK ----------------
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

    // ---------------- GET FRONT INDEX ----------------
    public int frontIndex() {
        return front;
    }

    // ---------------- GET REAR INDEX ----------------
    public int rearIndex() {
        return rear;
    }

        public void display() {
            if (size == 0) {
                System.out.println("Deque is empty");
                return;
            }
            System.out.print("Deque (Front → Back): ");
            int i = front;
            for (int count = 0; count < size; count++) {
                System.out.print(arr[i] + " ");
                i = (i + 1) % arr.length;
            }
            System.out.println();
        }
    }
