package com.example.dsavisualizer;

public class ListDoubly {
    private NodeListDoubly head;
    private int length = 0;

    // ---------- INSERT AT END ----------
    public void insertAtEnd(int data) {
        NodeListDoubly newNode = new NodeListDoubly(data);

        if (head == null) {
            head = newNode;
        } else {
            NodeListDoubly current = head;

            while (current.next != null)
                current = current.next;

            current.next = newNode;
            newNode.prev = current;
        }
        length++;
    }

    // ---------- INSERT AT BEGINNING ----------
    public void insertAtBeginning(int data) {
        NodeListDoubly newNode = new NodeListDoubly(data);

        if (head == null) {
            head = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }

        length++;
    }

    // ---------- DELETE FROM BEGINNING ----------
    public int deleteFromBeginning() {
        if (head == null) {
            System.out.println("List is empty.");
            return -1;
        }

        int removed = head.data;
        head = head.next;

        if (head != null)
            head.prev = null;

        length--;
        return removed;
    }

    // ---------- DELETE FROM END ----------
    public int deleteFromEnd() {
        if (head == null) {
            System.out.println("List is empty.");
            return -1;
        }

        if (head.next == null) {  // only one node
            int removed = head.data;
            head = null;
            length--;
            return removed;
        }

        // otherwise traverse to last node
        NodeListDoubly current = head;
        while (current.next != null)
            current = current.next;

        int removed = current.data;
        current.prev.next = null;
        length--;
        return removed;
    }

    // ---------- INSERT AT LOCATION ----------
    public void insertAtLocation(int data, int index) {
        if (index == 0) {
            insertAtBeginning(data);
            return;
        }

        if (index < 0 || index > length) {
            System.out.println("Cant Insert.Index out of bounds");
            return;
        }

        NodeListDoubly current = head;

        for (int i = 0; i < index - 1; i++)
            current = current.next;

        NodeListDoubly newNode = new NodeListDoubly(data);
        newNode.next = current.next;
        newNode.prev = current;

        if (current.next != null)
            current.next.prev = newNode;

        current.next = newNode;

        length++;
    }

    // ---------- DELETE AT LOCATION ----------
    public int deleteAtLocation(int index) {
        if (head == null) {
            System.out.println("List is empty.");
            return -1;
        }

        if (index < 0 || index >= length) {
            System.out.println("Cant Delete.Index out of bounds");
            return -1;
        }

        if (index == 0)
            return deleteFromBeginning();

        NodeListDoubly current = head;

        for (int i = 0; i < index - 1; i++)
            current = current.next;

        NodeListDoubly toDelete = current.next;
        int removed = toDelete.data;

        current.next = toDelete.next;

        if (toDelete.next != null)
            toDelete.next.prev = current;

        length--;
        return removed;
    }

    public int size() { return length; }
    public boolean isEmpty() { return head == null; }

    public int get(int index) {

        if (head == null) {
            System.out.println("List is empty");
            return -1;
        }

        if (index < 0 || index >= length) {
            System.out.println("Index out of bounds");
            return -1;
        }

        NodeListDoubly current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.data;
    }

}
