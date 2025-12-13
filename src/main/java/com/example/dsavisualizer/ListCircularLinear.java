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

    public void addAtBeginning(int data){
        if(last==null) {   //It checks if list is already empty
            addToEmpty(data);
            return;
        }

        NodeListLinear temp=new NodeListLinear(data);

        temp.next=last.next;
        last.next=temp;
    }

    public void deleteAtBeginning() {
        if (last == null){
            System.out.println("List not existed");
            return;
        }
        else if(last.next==last){
            last=null;
            return;
        }

        last.next=last.next.next;
    }


    public void addAtEnd(int data){
        if(last==null) {   //It checks if list is already empty
            addToEmpty(data);
            return;
        }

        NodeListLinear temp=new NodeListLinear(data);

        temp.next=last.next;
        last.next=temp;
        last=temp;
    }


    public void deleteAtEnd(){
        if (last == null){
            System.out.println("List not existed");
            return;
        }
        else if(last.next==last){
            last=null;
            return;
        }

        NodeListLinear temp=last.next;

        while(temp.next!=last){
            temp=temp.next;
        }

        temp.next=last.next;
        last=temp;

    }


    public void addAfter(int data,int item){    //Data is to be deleted,item is to be search for after which we have to delete
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


    public void deleteByData(int item){
        if (last == null){
            System.out.println("List not existed");
            return;
        }
        else if (last.next == last && last.data == item) {   //For single node case
            last = null;
            return;
        }

        NodeListLinear p=last;
        do {
            if (p.next.data == item) {

                if(p.next==last) {  //In case its last node itself
                    p.next = last.next;
                    last=p;
                }
                else {
                    p.next = p.next.next;
                }
                return;
            }

            p=p.next;
        }while (p!=last.next);   //For checking if it really exists.If it looped and turned back to where it started,it means it is not found.

        System.out.println("Node Not found");
    }

}