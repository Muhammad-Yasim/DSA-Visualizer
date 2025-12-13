package com.example.dsavisualizer;

class Stacks{

    int arr[];
    int trace;

    public Stacks(){
        trace=-1;
        arr=new int[7];
    }

    void push(int val){
        if(trace<arr.length-1)
            arr[++trace]=val;
        else
            System.out.println("Array is Full");
    }

    int pop(){
        if(trace==-1){
            System.out.print("Array is empty,");
            return -1;
        }
        return arr[trace--];
    }

    boolean isEmpty(){
        if(trace==-1)
            return true;

        return false;
    }

    boolean isFull(){
        if(trace==arr.length-1)
            return true;

        return false;
    }


    public int size(){
        return trace+1;
    }
}