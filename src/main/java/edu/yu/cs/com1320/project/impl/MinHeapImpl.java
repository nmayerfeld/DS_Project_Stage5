package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.MinHeap;

import java.util.Arrays;

public class MinHeapImpl<E extends Comparable<E>> extends MinHeap<E> {
    public MinHeapImpl(){
        this.elements= (E[]) new Comparable[10];
    }
    /*public int getLength() {
        return this.elements.length;
    }*/

    /**
     * @param element
     * ensures that E element is in the proper position in the MinHeap
     */
    public void reHeapify(E element) {
        if(element==null) {
            throw new IllegalArgumentException("element is null");
        }
        int index=0;
        try{
            index=this.getArrayIndex(element);
        }catch(IllegalArgumentException e){return;}
        this.downHeap(index);
        try{
            index=this.getArrayIndex(element);
        }catch(IllegalArgumentException e){return;}
        this.upHeap(index);
    }

    /**
     * @param element
     * @return the index of the array where element is stored
     * @throws IllegalArgumentException if element is not in the array
     */
    public int getArrayIndex(E element) {
        int index=-1;
        for(int i=1;i<this.count+1;i++) {
            E el=(E)(this.elements[i]);
            if(el.equals(element)) {
                index=i;
                break;
            }
        }
        if(index==-1) {
            throw new IllegalArgumentException("element not in array");
        }
        return index;
    }

    /**
     * doubles the size of the array
     */
    protected void doubleArraySize()
    {
        this.elements= Arrays.copyOf(this.elements,this.elements.length*2);
    }
}
