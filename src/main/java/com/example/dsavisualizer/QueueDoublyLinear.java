package com.example.dsavisualizer;

public class QueueDoublyLinear {
        private int front = -1;
        private int rear = -1;
        private int[] arr;
        private int size=0;

        public QueueDoublyLinear(int size) {
            arr = new int[size];
        }

        public void enqueueFront(int value) {     //Only this is changed,otherwise-all same
            if (isFull() )
                System.out.println("Deque is full");
            else if (front == -1) {
                front = rear = 0;
                arr[front] = value;
                size++;
            } else {
                for (int i = rear; i >= front; i--)
                    arr[i + 1] = arr[i];
                arr[front] = value;
                rear++;
                size++;
            }
        }

        public void enqueueBack(int value) {
            if (isFull())
                System.out.println("Deque is full");
            else {
                if (front == -1)
                    front = 0;
                arr[++rear] = value;
                size++;
            }
        }

        public int dequeueFront() {
            if (front == -1 || front > rear) {
                System.out.println("Deque is empty");
                return -1;
            }
            int val = arr[front++];
            if (front > rear)
                front = rear = -1;
            size--;
            return val;
        }

        public int dequeueBack() {
            if (front == -1 || front > rear) {
                System.out.println("Deque is empty");
                return -1;
            }
            int val = arr[rear--];
            if (front > rear)
                front = rear = -1;
            size--;
            return val;
        }


    public int frontIndex() {
        return front;
    }

    public int rearIndex() {
        return rear;
    }

    public int getValueAt(int index) {
        return arr[index];
    }

    public boolean isFull() {
        return (rear == arr.length - 1);
    }
}
