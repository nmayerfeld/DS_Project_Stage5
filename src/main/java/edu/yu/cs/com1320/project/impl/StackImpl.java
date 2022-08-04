package edu.yu.cs.com1320.project.impl;
import edu.yu.cs.com1320.project.Stack;

public class StackImpl<T> implements Stack <T>
{
    class ListNode<T> {
        public T data;
        public ListNode next;

        public ListNode(T element) {
            this.next = null;
            this.data = element;
        }
    }

    class LinkedList<T> {
        private ListNode head;
        private ListNode last;

        LinkedList()
        {
            this.head = null;
            this.last = null;
        }
        /*public void print() {
            if (this.head == null) {
                System.out.print("this linkedList is empty");
            }
            ListNode myListNode = this.head;
            while (myListNode != null) {
                myListNode.print();
                myListNode = myListNode.next;
            }
        }*/

        //adds new listNode to the front of the list, and changes last accordingly
        private void add(T c) {
            ListNode newNode = new ListNode(c);
            //set up last- for the first element in
            if (this.head == null) {
                this.last = newNode;
            }
            newNode.next = this.head;
            this.head = newNode;
        }
        private T removeFront() throws IllegalStateException
        {
            if(this.head==null)
            {
                throw new IllegalStateException(("stack is empty"));
            }
            T myData= (T) this.head.data;
            this.head=this.head.next;
            return myData;
        }
        private T viewFront() throws IllegalStateException
        {
            if(this.head==null)
            {
                return null;
            }
            T myData= (T) this.head.data;
            return myData;
        }
    }
    
    private LinkedList elementList;
    private int numInStack;
    public StackImpl()
    {
        this.elementList=new LinkedList();
        this.numInStack=0;
    }
    /**
     * @param element
     */
    public void push (T element)
    {
        this.elementList.add(element);
        this.numInStack++;
    }
    /**
     * @return the T at the top of the stack, removing it in the process
     * @throws IllegalStateException if stack is empty
     */
    public T pop() throws IllegalStateException
    {
        if(numInStack==0)
        {
            return null;
        }
        this.numInStack--;
        return (T)this.elementList.removeFront();
    }
    /**
     * @return the T at the top of the stack
     * @throws IllegalStateException if stack is empty
     */
    public T peek() throws IllegalStateException
    {
        return (T)this.elementList.viewFront();
    }
    /**
     * @return the size of the stack
     */
    public int size()
    {
        return this.numInStack;
    }
}
