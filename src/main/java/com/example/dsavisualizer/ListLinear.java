package com.example.dsavisualizer;

public class ListLinear {
    private NodeListLinear head;
    private int length=0;

    public void insertAtEnd(int data) {
        NodeListLinear newNode = new NodeListLinear(data);

        if (head == null)
            head = newNode;

        else{
            NodeListLinear current = head;

            while (current.next != null)
                current = current.next;

            current.next = newNode;
        }
        length++;
    }

    // Insert at the beginning
    public void insertAtBeginning(int data) {

        NodeListLinear newNode = new NodeListLinear(data);

        if (head == null)
            head = newNode;

        else {
            newNode.next = head;
            head = newNode;
        }
        length++;
    }

    // Delete from the beginning
    public int deleteFromBeginning() {

        if (head == null) {
            System.out.println("List is empty.");
            return -1;   // or throw exception
        }

        int removedValue = head.data;   // save the data
        head = head.next;               // move head
        length--;                       // decrease size
        return removedValue;            // return data
    }


    // Delete from end
    public int deleteFromEnd() {

        if (head == null) {
            System.out.println("List is empty.");
            return -1;
        }

        // Only one node
        if (head.next == null) {
            int removed = head.data;
            head = null;
            length--;
            return removed;
        }

        // More than one node
        NodeListLinear current = head;

        while (current.next.next != null) {   // go to second last
            current = current.next;
        }

        int removed = current.next.data;      // data of last node
        current.next = null;                  // remove last
        length--;
        return removed;
    }


    public void insertAtLocation(int index,int data) {

        if (head == null)
            System.out.println("List is empty");
        else if (index > length)
            System.out.println("Cant Insert.Index out of bounds");

        else if(index==0){
            insertAtBeginning(data);
        }

        else {
            NodeListLinear current = head;
            for (int i = 0; i < index - 1; i++)
                current = current.next;

            NodeListLinear newNode = new NodeListLinear(data);
            newNode.next = current.next;
            current.next = newNode;

            length++;
        }
    }


    public int deleteAtLocation(int index) {

        if (head == null) {
            System.out.println("List is empty");
            return -1;
        }

        if (index < 0 || index >= length) {
            System.out.println("Cant Delete. Index out of bounds");
            return -1;
        }

        // Delete from beginning
        if (index == 0) {
            return deleteFromBeginning();
        }

        NodeListLinear current = head;

        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }

        int removed = current.next.data;       // node to delete
        current.next = current.next.next;      // bypass node
        length--;

        return removed;
    }



    public int get(int index) {

        if (head == null) {
            System.out.println("List is empty");
            return -1;
        }

        if (index < 0 || index >= length) {
            System.out.println("Index out of bounds");
            return -1;
        }

        NodeListLinear current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.data;
    }



    public int size() {
        return length;
    }

    public boolean isEmpty() {
        return head == null;
    }



}