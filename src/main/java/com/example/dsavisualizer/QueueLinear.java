package com.example.dsavisualizer;

public class QueueLinear {

    private int front=-1;
    private int rear=-1;
    private int arr[];

    public QueueLinear(){
        this.arr=new int[7];
    }

    public void enqueue(int value) {
        if (rear==arr.length - 1)
            System.out.println("Queue is full");
        else {
            arr[++rear]=value;
            if (front==-1)
                front=0;
        }
    }

    public int dequeue(){
        if(front==-1 || front>rear) {
            System.out.println("Queue is empty");
            return -1;
        }
        int value= arr[front++];
        if(front>rear) {
            front=rear=-1;
        }
        return value;
    }

    public int size(){
        int size;
        size=rear-front+1;
        return size;
    }

    public int frontIndex() {
        return front;
    }

    public int rearIndex() {
        return rear;
    }

    public boolean isEmpty() {
        return front == -1;
    }

    public boolean isFull() {
        if(rear==arr.length-1)
            return true;
        return false;
    }

}
