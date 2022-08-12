package edu.yu.cs.com1320.project.impl;
import edu.yu.cs.com1320.project.Trie;

import java.util.*;

public class TrieImpl<Value> implements Trie<Value> {
    private TrieNode root;
    private static final int alphaNumeric = 36; // large enough for all capital letters and 0-9

    public static class TrieNode<Value> {
        protected Set<Value> val=new HashSet<>();
        protected TrieNode[] links = new TrieNode[TrieImpl.alphaNumeric];
    }

    public TrieImpl()
    {
        this.root=new TrieNode<Value>();
    }

    /**
     * @param x
     * @param key
     * @param d
     * @return
     */
    private TrieNode get(TrieNode x, String key, int d) {
        //link was null - return null, indicating a miss
        if (x == null) {
            return null;
        }
        //we've reached the last node in the key,
        //return the node
        if (d == key.length()) {
            return x;
        }
        //proceed to the next node in the chain of nodes that
        //forms the desired key
        char c = Character.toUpperCase(key.charAt(d));;
        int index=0;
        if((int)c>57) {
            index=(int)c-65;
        }
        else {
            index=(int)c-22; //because 0-9 map to ascii 48-57 and i want the #s stored after 26 letters in alphabet so this will start #s at index 26
        }
        return this.get(x.links[index], key, d + 1);
    }

    /**
     * add the given value at the given key
     * @param key
     * @param val
     */
    public void put(String key, Value val) {
        if(key==null) {
            throw new IllegalArgumentException("the key was null");
        }
        //deleteAll the value from this key
        if (val == null) {
            this.deleteAll(key);
        }
        else {
            this.root = put(this.root, key, val, 0);
        }
    }

    /**
     *
     * @param x
     * @param key
     * @param val
     * @param d
     * @return
     */
    private TrieNode put(TrieNode x, String key, Value val, int d) {
        //create a new node
        if (x == null) {
            x = new TrieNode();
        }
        //we've reached the last node in the key,
        //set the value for the key and return the node
        if (d == key.length()) {
            x.val.add(val);
            return x;
        }
        //proceed to the next node in the chain of nodes that
        //forms the desired key
        char c = Character.toUpperCase(key.charAt(d));
        int index=0;
        if((int)c>57) {
            index=(int)c-65;
        }
        else {
            index=(int)c-22; //because 0-9 map to ascii 48-57 and i want the #s stored after 26 letters in alphabet so this will start #s at index 26
        }
        x.links[index] = this.put(x.links[index], key, val, d + 1);
        return x;
    }


    /**
     * get all exact matches for the given key, sorted in descending order.
     * Search is CASE INSENSITIVE.
     * @param key
     * @param comparator used to sort  values
     * @return a List of matching Values, in descending order
     */
    public List<Value> getAllSorted(String key, Comparator<Value> comparator) {
        if(comparator==null) {
            throw new IllegalArgumentException("comparator is null");
        }
        TrieNode x = this.get(this.root, key, 0);
        if (x == null) {
            return new ArrayList<Value>();
        }
        List sortedDocumentsContainingWord= new ArrayList<>();
        if(x.val!=null) {
            sortedDocumentsContainingWord.addAll(x.val);
        }
        Collections.sort(sortedDocumentsContainingWord,comparator);
        return sortedDocumentsContainingWord;
    }

    /**
     * get all matches which contain a String with the given prefix, sorted in descending order.
     * For example, if the key is "Too", you would return any value that contains "Tool", "Too", "Tooth", "Toodle", etc.
     * Search is CASE INSENSITIVE.
     * @param prefix
     * @param comparator used to sort values
     * @return a List of all matching Values containing the given prefix, in descending order
     */
    public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator) {
        if(comparator==null) {
            throw new IllegalArgumentException("comparator is null");
        }
        Set<Value> allWithPrefix=new HashSet<>();
        TrieNode startingNode=this.get(this.root,prefix,0);
        if(startingNode==null) {
            return new ArrayList<Value>();
        }
        if(startingNode.val!=null) {
            allWithPrefix.addAll(startingNode.val);
        }
        this.getAllWithPrefixSorted(startingNode,allWithPrefix);
        List<Value> results=new ArrayList<>();
        results.addAll(allWithPrefix);
        Collections.sort(results,comparator);
        return results;
    }

    /**
     * @param current
     * @param allDocsContainingPrefix
     */
    private void getAllWithPrefixSorted(TrieNode current,Collection<Value> allDocsContainingPrefix) {
        for(TrieNode tn: current.links) {
            if(tn!=null) {
                if(tn.val!=null) {
                    allDocsContainingPrefix.addAll(tn.val);
                }
                this.getAllWithPrefixSorted(tn,allDocsContainingPrefix);
            }
        }
    }

    /**
     * Delete the subtree rooted at the last character of the prefix.
     * Search is CASE INSENSITIVE.
     * @param prefix
     * @return a Set of all Values that were deleted.
     */
    public Set<Value> deleteAllWithPrefix(String prefix) {
        //get all the values that will be deleted
        Set<Value> allDeleted=new HashSet<>();
        TrieNode startingNode=this.get(this.root,prefix,0);
        if(startingNode.val!=null) {
            allDeleted.addAll(startingNode.val);
        }
        this.getAllWithPrefixSorted(startingNode,allDeleted);
        //delete the subtree by setting all of the pointers in the node to null and the value located at the last node of the prefix to null
        for(int i=0;i<startingNode.links.length;i++) {
            startingNode.links[i]=null;
        }
        startingNode.val=null;
        return allDeleted;
    }

    /**
     * Delete all values from the node of the given key (do not remove the values from other nodes in the Trie)
     * @param key
     * @return a Set of all Values that were deleted.
     */
    public Set<Value> deleteAll(String key) {
        Set<Value> mySet=new HashSet<>();
        this.root = deleteAll(this.root, key, 0,mySet);
        return mySet;
    }

    /**
     * @param x
     * @param key
     * @param d
     * @param mySet
     * @return
     */
    private TrieNode deleteAll(TrieNode x, String key, int d, Set<Value> mySet) {
        if (x == null) {
            return null;
        }
        //we're at the node to del - set the val to null
        if (d == key.length()) {
            mySet.addAll(x.val);
            x.val = null;
        }
        //continue down the trie to the target node
        else {
            char c = Character.toUpperCase(key.charAt(d));
            int index=0;
            if((int)c>57) {
                index=(int)c-65;
            }
            else {
                index=(int)c-22; //because 0-9 map to ascii 48-57 and i want the #s stored after 26 letters in alphabet so this will start #s at index 26
            }
            x.links[index] = this.deleteAll(x.links[index], key, d + 1,mySet);
        }
        //this node has a val – do nothing, return the node
        if (x.val != null) {
            return x;
        }
        //remove subtrie rooted at x if it is completely empty
        for (int c = 0; c <x.links.length; c++) {
            if (x.links[c] != null) {
                return x; //not empty
            }
        }
        //empty - set this link to null in the parent
        return null;
    }

    /**
     * Remove the given value from the node of the given key (do not remove the value from other nodes in the Trie)
     * @param key
     * @param val
     * @return the value which was deleted. If the key did not contain the given value, return null.
     */
    public Value delete(String key, Value val) {
        TrieNode x=this.get(this.root,key,0);
        Boolean wasPresent = x.val.contains(val);
        this.root=this.delete(this.root,key,0,val);
        if(wasPresent&&!x.val.contains(val)) {
            return val;
        }
        return null;
    }

    /**
     * @param x
     * @param key
     * @param d
     * @param toBeDeleted
     * @return
     */
    private TrieNode delete(TrieNode x, String key, int d, Value toBeDeleted) {
        if (x == null) {
            return null;
        }
        //we're at the node to del - set the val to null
        if (d == key.length()) {
            if(x.val.contains(toBeDeleted)) {
                x.val.remove(toBeDeleted);
            }
        }
        //continue down the trie to the target node
        else {
            char c = Character.toUpperCase(key.charAt(d));
            int index=0;
            if((int)c>57) {
                index=(int)c-65;
            }
            else{
                index=(int)c-22; //because 0-9 map to ascii 48-57 and i want the #s stored after 26 letters in alphabet so this will start #s at index 26
            }
            x.links[index] = this.delete(x.links[index], key, d + 1,toBeDeleted);
        }
        //this node has a val – do nothing, return the node
        if (x.val != null) {
            return x;
        }
        //remove subtrie rooted at x if it is completely empty
        for (int c = 0; c <x.links.length; c++) {
            if (x.links[c] != null) {
                return x; //not empty
            }
        }
        //empty - set this link to null in the parent
        return null;
    }
}
