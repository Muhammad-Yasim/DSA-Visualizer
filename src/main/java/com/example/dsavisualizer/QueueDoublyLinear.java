package com.example.dsavisualizer;

public class QueueDoublyLinear {
        private int front = -1;
        private int rear = -1;
        private int[] arr;
        private int size=0;

        public QueueDoublyLinear(int size) {
            arr = new int[size];
        }

        // ---------------- ENQUEUE FRONT ----------------
        public void enqueueFront(int value) {     //Only this is changed,otherwise-all same
            if (isFull() )
                System.out.println("Deque is full");
            else if (front == -1) { // empty queue
                front = rear = 0;
                arr[front] = value;
                size++;
            } else {
                // shift elements right
                for (int i = rear; i >= front; i--)
                    arr[i + 1] = arr[i];
                arr[front] = value;
                rear++;
                size++;
            }
        }

        // ---------------- ENQUEUE BACK ----------------
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

        // ---------------- DEQUEUE FRONT ----------------
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

        // ---------------- DEQUEUE BACK ----------------
        public int dequeueBack() {
            if (front == -1 || front > rear) {
                System.out.println("Deque is empty");
                return -1;
            }
            int val = arr[rear--];    //rear instead of front
            if (front > rear)
                front = rear = -1;
            size--;
            return val;
        }


    public int frontIndex() {
        return front;
    }

    // ---------------- GET REAR INDEX ----------------
    public int rearIndex() {
        return rear;
    }

    public int getValueAt(int index) {
        //if (index < 0 || index >= size) return -1;  // or throw exception
        return arr[index];
    }  // ---------------- GET FRONT INDEX ----------------



    public boolean isFull() {
        return (rear == arr.length - 1);
    }


    public void display() {
            if (front == -1) {
                System.out.println("Deque is empty");
                return;
            }
            System.out.print("Deque (Front → Back): ");
            for (int i = front; i <= rear; i++)
                System.out.print(arr[i] + " ");
            System.out.println();
        }
    }
