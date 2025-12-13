package com.example.dsavisualizer;

public class ListCircularLinear {

    private NodeListLinear last;

    public void addToEmpty(int data){
        if(last!=null)    //It checks if it is really null or not.It not,then no point of adding Node
            return;

        NodeListLinear temp=new NodeListLinear(data);

        last=temp;
        temp.next=last;
    }

    public void insertAtBeginning(int data){
        if(last==null) {   //It checks if list is already empty
            addToEmpty(data);
            return;
        }

        NodeListLinear temp=new NodeListLinear(data);

        temp.next=last.next;
        last.next=temp;
    }

    public void insertAtEnd(int data){
        if(last==null) {   //It checks if list is already empty
            addToEmpty(data);
            return;
        }

        NodeListLinear temp=new NodeListLinear(data);

        temp.next=last.next;
        last.next=temp;
        last=temp;
    }

    public int deleteFromBeginning() {
        if (last == null) {
            System.out.println("List not existed");
            return -1;
        }

        int deleted;

        if (last.next == last) {
            deleted = last.data;
            last = null;
            return deleted;
        }

        deleted = last.next.data;      // head node
        last.next = last.next.next;    // bypass head
        return deleted;
    }


    public int deleteFromEnd() {
        if (last == null) {
            System.out.println("List not existed");
            return -1;
        }

        int deleted;

        if (last.next == last) {
            deleted = last.data;
            last = null;
            return deleted;
        }

        NodeListLinear temp = last.next;

        while (temp.next != last) {
            temp = temp.next;
        }

        deleted = last.data;
        temp.next = last.next;
        last = temp;

        return deleted;
    }



    public void insertAfter(int data,int item){    //Data is to be added,item is to be search for after which we have to delete
        if(last==null)
            return;

        NodeListLinear p=last.next;
        do {
            if (p.data == item) {
                NodeListLinear temp = new NodeListLinear(data);
                temp.next=p.next;
                p.next=temp;

                if(p==last)   //For handling if entered at last of the list
                    last=temp;

                return;
            }
            p=p.next;
        }while (p!=last.next);   //For checking if it really exists.If it looped and turned back to where it started,it means it is not found.

        System.out.println("Node Not found");
    }

    public int deleteByData(int item) {
        if (last == null) {
            System.out.println("List not existed");
            return -1;
        }

        if (last.next == last && last.data == item) {
            int deleted = last.data;
            last = null;
            return deleted;
        }

        NodeListLinear p = last;

        do {
            if (p.next.data == item) {

                int deleted = p.next.data;

                // if deleting last node
                if (p.next == last) {
                    p.next = last.next;
                    last = p;
                } else {
                    p.next = p.next.next;
                }
                return deleted;
            }

            p = p.next;
        } while (p != last);

        System.out.println("Node Not found");
        return -1;
    }

    public boolean isEmpty() {
        return last == null;
    }

    public int get(int index) {
        if (last == null)
            throw new IndexOutOfBoundsException("List is empty");

        int size = size();
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Invalid index: " + index);

        NodeListLinear temp = last.next; // head
        int i = 0;

        while (i < index) {
            temp = temp.next;
            i++;
        }

        return temp.data;
    }

    public int size() {
        if (last == null)
            return 0;

        int count = 1;
        NodeListLinear temp = last.next; // head

        while (temp != last) {
            count++;
            temp = temp.next;
        }
        return count;
    }


}