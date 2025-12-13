package com.example.dsavisualizer;

public class ListCircularDoubly {

    private NodeListDoubly last;

    public void addToEmpty(int data){
        if(last != null)
            return;

        NodeListDoubly temp = new NodeListDoubly(data);

        last = temp;
        temp.next = temp;
        temp.prev = temp;   // ✅ maintain prev
    }

    public void insertAtBeginning(int data){
        if(last == null){
            addToEmpty(data);
            return;
        }

        NodeListDoubly temp = new NodeListDoubly(data);

        temp.next = last.next;
        temp.prev = last;

        last.next.prev = temp;   // ✅
        last.next = temp;
    }

    public void insertAtEnd(int data){
        if(last == null){
            addToEmpty(data);
            return;
        }

        NodeListDoubly temp = new NodeListDoubly(data);

        temp.next = last.next;
        temp.prev = last;

        last.next.prev = temp;   // ✅
        last.next = temp;
        last = temp;
    }

    public int deleteFromBeginning() {
        if (last == null)
            throw new RuntimeException("List not existed");

        int deletedData;

        if (last.next == last) {
            deletedData = last.data;
            last = null;
            return deletedData;
        }

        NodeListDoubly first = last.next;
        deletedData = first.data;

        last.next = first.next;
        first.next.prev = last;

        return deletedData;
    }

    public int deleteFromEnd() {
        if (last == null)
            throw new RuntimeException("List not existed");

        int deletedData;

        if (last.next == last) {
            deletedData = last.data;
            last = null;
            return deletedData;
        }

        deletedData = last.data;

        NodeListDoubly secondLast = last.prev;
        secondLast.next = last.next;
        last.next.prev = secondLast;

        last = secondLast;

        return deletedData;
    }


    public void insertAfter(int data, int item){    //data is to be added.Item is to be compared
        if(last == null)
            return;

        NodeListDoubly p = last.next;

        do {
            if (p.data == item) {
                NodeListDoubly temp = new NodeListDoubly(data);

                temp.next = p.next;
                temp.prev = p;

                p.next.prev = temp;   // ✅
                p.next = temp;

                if(p == last)
                    last = temp;

                return;
            }
            p = p.next;
        } while (p != last.next);

        System.out.println("Node Not found");
    }

    public int deleteByData(int item) {
        if (last == null)
            throw new RuntimeException("List not existed");

        // single node case
        if (last.next == last && last.data == item) {
            int deletedData = last.data;
            last = null;
            return deletedData;
        }

        NodeListDoubly p = last.next;

        do {
            if (p.data == item) {

                int deletedData = p.data;

                p.prev.next = p.next;
                p.next.prev = p.prev;

                if (p == last)
                    last = p.prev;

                return deletedData;
            }
            p = p.next;
        } while (p != last.next);

        throw new RuntimeException("Node Not found");
    }



    public boolean isEmpty() {
        return last == null;
    }

    public int size() {
        if (last == null) return 0;

        int count = 0;
        NodeListDoubly temp = last.next;
        do {
            count++;
            temp = temp.next;
        } while (temp != last.next);

        return count;
    }

    public int get(int index) {
        if (last == null)
            throw new IndexOutOfBoundsException("List is empty");

        if (index < 0)
            throw new IndexOutOfBoundsException("Invalid index");

        NodeListDoubly temp = last.next;
        int i = 0;

        do {
            if (i == index)
                return temp.data;
            temp = temp.next;
            i++;
        } while (temp != last.next);

        throw new IndexOutOfBoundsException("Index out of range");
    }

}
