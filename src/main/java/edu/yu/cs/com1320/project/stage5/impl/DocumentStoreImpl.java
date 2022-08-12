package edu.yu.cs.com1320.project.stage5.impl;
import edu.yu.cs.com1320.project.BTree;
import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.impl.*;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import com.google.gson.Gson;
import java.io.File;
import java.lang.System.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

public class DocumentStoreImpl implements DocumentStore {
    class URIComparator implements Comparator<URI> {
        private String word;
        private boolean thisIsAPrefixCompare;
        private BTreeImpl<URI,Document> bTree;

        public URIComparator(String word,Boolean prefixCompare,BTreeImpl<URI,Document> bTree) {
            this.word=word.toUpperCase();
            this.thisIsAPrefixCompare=prefixCompare;
            this.bTree=bTree;
        }

        /**
         * @param u1
         * @param u2
         * @return an int indicating which document has more occurrences of this word
         */
        public int compare(URI u1, URI u2) {
            Document d1=this.bTree.get(u1);
            Document d2=this.bTree.get(u2);
            if(!thisIsAPrefixCompare) {
                return d2.wordCount(this.word)-d1.wordCount(this.word);
            }
            int numTimesInDocOne=0;
            int numTimesInDocTwo=0;
            for(String w:d1.getWords()) {
                if(w.indexOf(this.word)==0) {
                    numTimesInDocOne+= d1.wordCount(w);
                }
            }
            for(String word:d2.getWords()) {
                if(word.indexOf(this.word)==0) {
                    numTimesInDocTwo+= d2.wordCount(word);
                }
            }
            return numTimesInDocTwo-numTimesInDocOne;
        }
    }

    class URICompare implements Comparable<URICompare> {
        private URI uri;
        private BTree<URI,Document> bTree;

        /**
         * @param uri
         * @param bTree
         */
        public URICompare(URI uri, BTree<URI,Document> bTree) {
            this.uri=uri;
            this.bTree=bTree;
        }

        protected Document URItoDocConvert()
        {
            return this.bTree.get(this.uri);
        }

        /**
         * @param timeInNanoseconds
         */
        protected void setLastUseTimeOfDocWIthThisURICompare(long timeInNanoseconds) {
            this.bTree.get(this.uri).setLastUseTime(timeInNanoseconds);
        }

        protected long getLastUseTimeOfDocWithThisURICompare()
        {
            return this.bTree.get(this.uri).getLastUseTime();
        }

        @Override
        public boolean equals(Object obj) {
            //see if it's the same object
            if(this == obj) {
                return true;
            }
            //see if it's null
            if(obj == null) {
                return false;
            }
            //see if they're from the same class
            if(getClass()!=obj.getClass()) {
                return false;
            }
            URICompare other=(URICompare) obj;
            return this.uri.equals(other.uri);
        }

        protected URI getURI()
        {
            return this.uri;
        }

        @Override
        public int compareTo (URICompare u2) {
            return (int)(this.bTree.get(this.uri).getLastUseTime()-u2.bTree.get(u2.uri).getLastUseTime());
        }
    }

    /**
     * the two document formats supported by this document store.
     * Note that TXT means plain text, i.e. a String.
     */
    private TrieImpl<URI> myTrie;
    private StackImpl<Undoable> commandStack;
    private MinHeapImpl<URICompare> minHeap;
    private BTreeImpl<URI,Document> btree;
    private long origNanoTime;
    private int numDocs;
    private int byteCount;
    int maxDocs=Integer.MAX_VALUE;
    int maxBytes=Integer.MAX_VALUE;
    private Set<Document> docsInMemory;

    public DocumentStoreImpl() {
        this.myTrie=new TrieImpl<>();
        this.commandStack=new StackImpl<>();
        this.minHeap=new MinHeapImpl<>();
        this.btree=new BTreeImpl<>();
        this.btree.setPersistenceManager(new DocumentPersistenceManager(new File(System.getProperty("user.dir"))));
        this.origNanoTime=System.nanoTime(); //to set doc to for all deletes- this will be lower than all subsequent nanotimes of all docs
        this.numDocs=0;
        this.byteCount=0;
        this.docsInMemory=new HashSet<>();
    }

    /**
     * @param baseDir
     */
    public DocumentStoreImpl(File baseDir) {
        this.myTrie=new TrieImpl<>();
        this.commandStack=new StackImpl<>();
        this.minHeap=new MinHeapImpl<>();
        this.btree=new BTreeImpl<>();
        this.btree.setPersistenceManager(new DocumentPersistenceManager(baseDir));
        this.origNanoTime=System.nanoTime(); //to set doc to for all deletes- this will be lower than all subsequent nanotimes of all docs
        this.numDocs=0;
        this.byteCount=0;
        this.docsInMemory=new HashSet<>();
    }

    /**
     * @param input the document being put
     * @param uri unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return if there is no previous doc at the given URI, return 0. If there is a previous doc, return the hashCode of the previous doc. If InputStream is null, this is a delete, and thus return either the hashCode of the deleted doc or 0 if there is no doc to delete.
     * @throws IOException if there is an issue reading input
     * @throws IllegalArgumentException if uri or format are null
     */
    public int putDocument(InputStream input, URI uri, DocumentFormat format) throws IOException {
        Document d;
        Document document;
        byte[] bytes;
        if(input==null) {
            d=btree.get(uri);
            if(d!=null) {
                if(this.docsInMemory.contains(d)) {
                    this.removeFromMinHeap(d);
                    this.updateStorageTrackerForDelete(d);
                    if(this.isTxt(d)) {
                        this.deleteDocFromTrie(d);
                    }
                    this.docsInMemory.remove(d);
                }
                else {
                    d.setLastUseTime(System.nanoTime());
                }
                btree.put(uri,null);
                //undo puts doc back in memory
                this.commandStack.push(new GenericCommand<URI>(uri,(URI uri1)->{if(this.getDocSize(d)>this.maxBytes){throw new IllegalArgumentException("document to be undone exceeds max byte size");};this.btree.put(uri1,d);d.setLastUseTime(System.nanoTime());this.minHeap.insert(new URICompare(uri1,this.btree));if(d.getDocumentBinaryData()==null){this.addDocToTrie(d);}this.updateStorageTrackersForAdd(d);this.docsInMemory.add(d);this.handleMaximums();return true;}));
            }
        }
        else if(uri==null||format==null) {
            throw new IllegalArgumentException("format or uri is null");
        }
        else {
            try{
                bytes= input.readAllBytes();
            }catch (Exception e){
                throw new IOException("issue reading from InputStream");
            }
            if(format==DocumentFormat.TXT) {
                String txt=new String(bytes);
                document= (Document) new DocumentImpl(uri,txt,null);
                //put this document at every location in the Trie of a word contained within the document
            }
            else {
                document= (Document) new DocumentImpl(uri,bytes);
            }
            d=btree.get(uri);//should be whatever was there before (which will have been recalled from memory by btree.get if it was on disk), or null if it is a new put
            btree.put(uri,document); //replaces it with the new value
            Document finalD = d;
            //if it was a new put, create the undo function, and within it just delete all the words from the trie, and update storage trackers
            if(d==null) {
                if(this.getDocSize(document)>maxBytes) {
                    throw new IllegalArgumentException("doc is bigger than limit");
                }
                if(format==DocumentFormat.TXT) {
                    this.addDocToTrie(document);
                }
                this.docsInMemory.add(document);
                this.updateStorageTrackersForAdd(document);
                URICompare uc=new URICompare(document.getKey(),this.btree);
                this.minHeap.insert(uc);
                this.handleMaximums();
                this.commandStack.push(new GenericCommand<URI>(uri,(URI uri1)->{this.removeFromMinHeap(document);this.btree.put(uri1,finalD);this.updateStorageTrackerForDelete(document);this.docsInMemory.remove(document);if(this.isTxt(document)){this.deleteDocFromTrie(document);};return true;}));
            }
            //if it was a replacement, delete all the document references in the trie corresponding to each word in the old document, in the undo, add back the old words and remove the new ones
            else {
                boolean wasInMemory=false;
                if(this.docsInMemory.contains(d)) {
                    wasInMemory=true;
                    this.updateStorageTrackerForModify(d,document);
                    if(d.getDocumentBinaryData()==null) {
                        //remove all references for each word in the old doc, d
                        this.deleteDocFromTrie(d);
                    }
                    this.removeFromMinHeap(d);
                    this.docsInMemory.remove(d);
                }
                else {
                    this.updateStorageTrackersForAdd(document);
                }
                this.docsInMemory.add(document);
                //need to do this after deleting the old words from the trie bc they are stored by URI, so if i add the new words first, deleting from trie will delete both docs bc the newly modified doc has the same uri as the old one
                if(format==DocumentFormat.TXT) {
                    this.addDocToTrie(document);
                }
                this.minHeap.insert(new URICompare(document.getKey(),this.btree));
                //handle maximum if affected by the replacement
                this.handleMaximums();
                if(wasInMemory) {
                    if(format==DocumentFormat.TXT) {
                        // create the undo function which undoes the replacement, adds the words back from the old doc, finalD, and deletes the words from the new doc, document
                        this.commandStack.push(new GenericCommand<URI>(uri,(URI uri1)->{this.removeFromMinHeap(document);this.btree.put(uri1,finalD);finalD.setLastUseTime(System.nanoTime());this.docsInMemory.remove(document);this.docsInMemory.add(finalD);this.minHeap.insert(new URICompare(finalD.getKey(),this.btree));if(document.getDocumentBinaryData()==null){this.deleteDocFromTrie(document);}this.updateStorageTrackerForModify(document,finalD);if(finalD.getDocumentBinaryData()==null){this.addDocToTrie(finalD);}this.handleMaximums();return true;}));
                    }
                    else {
                        this.commandStack.push(new GenericCommand<URI>(uri,(URI uri1)->{this.removeFromMinHeap(document);this.btree.put(uri1,finalD);finalD.setLastUseTime(System.nanoTime());this.docsInMemory.remove(document);this.docsInMemory.add(finalD);this.minHeap.insert(new URICompare(finalD.getKey(),this.btree));this.updateStorageTrackerForModify(document,finalD);if(finalD.getDocumentBinaryData()==null){this.addDocToTrie(finalD);}this.handleMaximums();return true;}));
                    }
                }
                else { //if it wasn't in memory, the lambdas need to put the old doc back in memory
                    if(format==DocumentFormat.TXT) {
                        // create the undo function which undoes the replacement, adds the words back from the old doc, finalD, and deletes the words from the new doc, document
                        this.commandStack.push(new GenericCommand<URI>(uri,(URI uri1)->{this.removeFromMinHeap(document);this.btree.put(uri1,finalD);this.docsInMemory.remove(document);if(document.getDocumentBinaryData()==null){this.deleteDocFromTrie(document);}this.updateStorageTrackerForDelete(document);
                            try {
                                this.btree.moveToDisk(d.getKey());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            this.handleMaximums();return true;}));
                    }
                    else {
                        this.commandStack.push(new GenericCommand<URI>(uri,(URI uri1)->{this.removeFromMinHeap(document);this.btree.put(uri1,finalD);this.docsInMemory.remove(document);this.updateStorageTrackerForDelete(document);
                            try {
                                this.btree.moveToDisk(d.getKey());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            this.handleMaximums();return true;}));
                    }
                }

            }
        }
        if(d==null) {
            return 0;
        }
        else {
            return d.hashCode();
        }
    }

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document
     */
    public Document getDocument(URI uri) {
        Document d=this.btree.get(uri); //now in memory regardless
        if(d!=null) {
            d.setLastUseTime(System.nanoTime());
            this.minHeap.reHeapify(new URICompare(uri,this.btree));
            if(!this.docsInMemory.contains(d)) { //it was just recalled from disk
                if(this.isTxt(d)) {
                    this.addDocToTrie(d);
                }
                this.docsInMemory.add(d);
                this.handleMaximums();
            }
        }
        return d;
    }

    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    public boolean deleteDocument(URI uri) {
        Document d=btree.get(uri);
        if(d==null) {
            return false;
        }
        else {
            if(this.docsInMemory.contains(d)) { //if it was in memory before this call to delete
                if(d.getDocumentBinaryData()==null) {
                    this.deleteDocFromTrie(d);
                }
                //delete from minheap
                this.removeFromMinHeap(d);
                this.updateStorageTrackerForDelete(d);
                this.docsInMemory.remove(d);
                btree.put(uri,null);
                this.commandStack.push(new GenericCommand<URI>(uri,(URI uri1)->{if(this.getDocSize(d)>this.maxBytes){throw new IllegalArgumentException("document to be undone exceeds max byte size");};this.btree.put(uri1,d);d.setLastUseTime(System.nanoTime());this.minHeap.insert(new URICompare(d.getKey(),this.btree));this.updateStorageTrackersForAdd(d);this.docsInMemory.add(d);this.handleMaximums();if(d.getDocumentBinaryData()==null){this.addDocToTrie(d);}return true;}));
            }
            else { //it was on disk, delete it and set up undo to put it straight back on disk
                btree.put(uri,null);
                this.commandStack.push(new GenericCommand<URI>(uri,(URI uri1)->{;this.btree.put(uri1,d);try {this.btree.moveToDisk(d.getKey());} catch (Exception e) {e.printStackTrace();}return true;}));
            }
            return true;
        }
    }

    /**
     * undo the last put or delete command
     * @throws IllegalStateException if there are no actions to be undone, i.e. the command stack is empty
     */
    public void undo() throws IllegalStateException {
        if (commandStack.size()==0) {
            throw new IllegalStateException("stack is empty");
        }
        Undoable u= (Undoable) commandStack.pop();
        u.undo();
    }

    /**
     * undo the last put or delete that was done with the given URI as its key
     * @param uri
     * @throws IllegalStateException if there are no actions on the command stack for the given URI
     */
    public void undo(URI uri) throws IllegalStateException {
        if (commandStack.size()==0) {
            throw new IllegalStateException("stack is empty");
        }
        Boolean foundURI=false;
        int tries=0;
        StackImpl<Undoable> temp=new StackImpl<>();
        int size= commandStack.size();
        while(!foundURI&&tries<size) {
            tries++;
            Undoable u= (Undoable) commandStack.pop();
            if(u instanceof GenericCommand) {
                GenericCommand<URI> gc=(GenericCommand<URI>) (u);
                if(gc.getTarget()==uri) {
                    gc.undo();
                    foundURI=true;
                }
                else {
                    temp.push(u);
                }
            }
            else {
                CommandSet<URI> cs=(CommandSet<URI>)(u);
                if(cs.containsTarget(uri)) {
                    cs.undo(uri);
                    foundURI=true;
                    if(cs.size()!=0) {
                        temp.push(cs);
                    }
                }
                else {
                    temp.push(u);
                }
            }
        }
        if(!foundURI) {
            throw new IllegalStateException("given URI is not in any command on the stack");
        }
        while(temp.size()!=0) {
            Undoable undoable=temp.pop();
            commandStack.push(undoable);
        }
    }
    //relies on a public StackImpl.LinkedList class and element list (which have been made private after testing to fulfill requirements of this assignment)
    /*public void printStack() {
        StackImpl.LinkedList ll= this.commandStack.elementList;
        System.out.println("printing stack from top to bottom");
        StackImpl.ListNode current=ll.head;
        while(current!=null){
            Undoable command=(Undoable)(current.data);
            if(command instanceof GenericCommand) {
                System.out.println("This is a GenericCommand with URI: "+ ((GenericCommand<?>) command).getTarget());
            }
            else {
                System.out.println("This is a CommandSet with the following URI's");
                for(GenericCommand g: (CommandSet<URI>)(command)) {
                    System.out.println(g.getTarget());
                }
                System.out.println();
            }

            current=current.next;
        }
    }
    public void print() {
        this.store.print();
    }
    public int getCommandStackSize(){
        return this.commandStack.size();
    }*/

    /**
     * Retrieve all documents whose text contains the given keyword.
     * Documents are returned in sorted, descending order, sorted by the number of times the keyword appears in the document.
     * Search is CASE INSENSITIVE.
     * @param keyword
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    public List<Document> search(String keyword) {
        URIComparator comparator=new URIComparator(keyword.toUpperCase(),false,this.btree);
        List<URI> results=myTrie.getAllSorted(keyword.toUpperCase(),comparator);
        List<Document> finalResults=new ArrayList<>();
        //update timeStamps and add the documents to final results
        for(URI u: results) {
            Document d=this.btree.get(u); //now in memory whether it started there or not
            if(d!=null) {
                d.setLastUseTime(System.nanoTime());
            }
            this.minHeap.reHeapify(new URICompare(d.getKey(), this.btree));
            finalResults.add(d);
        }
        return finalResults;
    }

    /**
     * Retrieve all documents whose text starts with the given prefix
     * Documents are returned in sorted, descending order, sorted by the number of times the prefix appears in the document.
     * Search is CASE INSENSITIVE.
     * @param keywordPrefix
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    public List<Document> searchByPrefix(String keywordPrefix) {
        URIComparator comparator=new URIComparator(keywordPrefix,true,this.btree);
        List<URI> results=myTrie.getAllWithPrefixSorted(keywordPrefix.toUpperCase(),comparator);
        List<Document> finalResults=new ArrayList<>();
        //update timeStamps and add the documents to final results
        for(URI u: results) {
            Document d=this.btree.get(u);
            d.setLastUseTime(System.nanoTime());
            this.minHeap.reHeapify(new URICompare(d.getKey(),this.btree));
            finalResults.add(d);
        }
        return finalResults;
    }

    /**
     * Completely remove any trace of any document which contains the given keyword
     * Search is CASE INSENSITIVE.
     * @param keyword
     * @return a Set of URIs of the documents that were deleted.
     */
    public Set<URI> deleteAll(String keyword) {
        List<Document> documentsDeleted= this.search(keyword);
        Set<URI> results=new HashSet<>();
        CommandSet cs=new CommandSet();
        for(Document dd: documentsDeleted) {
            this.updateStorageTrackerForDelete(dd);
            this.docsInMemory.remove(dd);
            if(dd.getDocumentBinaryData()==null) {
                this.deleteDocFromTrie(dd);
            }
            this.removeFromMinHeap(dd);
            results.add(dd.getKey());
            //Document document=this.btree.get(dd.getKey());
            this.btree.put(dd.getKey(),null);
            GenericCommand gc=new GenericCommand<URI>(dd.getKey(),(URI uri1)->{if(this.getDocSize(dd)>this.maxBytes){throw new IllegalArgumentException("document to be undone exceeds max byte size");};this.btree.put(uri1,dd);this.updateStorageTrackersForAdd(dd);this.docsInMemory.add(dd);dd.setLastUseTime(System.nanoTime());this.minHeap.insert(new URICompare(dd.getKey(),this.btree));if(dd.getDocumentBinaryData()==null){this.addDocToTrie(dd);}this.handleMaximums();return true;});
            cs.addCommand(gc);
        }
        this.commandStack.push(cs);
        return results;
    }

    /**
     * Completely remove any trace of any document which contains a word that has the given prefix
     * Search is CASE INSENSITIVE.
     * @param keywordPrefix
     * @return a Set of URIs of the documents that were deleted.
     */
    public Set<URI> deleteAllWithPrefix(String keywordPrefix) {
        List<Document> documentsDeleted= this.searchByPrefix(keywordPrefix);
        Set<URI> results=new HashSet<>();
        CommandSet cs=new CommandSet();
        for(Document dd: documentsDeleted) {
            this.docsInMemory.remove(dd);
            this.updateStorageTrackerForDelete(dd);
            if(dd.getDocumentBinaryData()==null) {
                this.deleteDocFromTrie(dd);
            }
            this.removeFromMinHeap(dd);
            results.add(dd.getKey());
            //Document document=this.btree.get(dd.getKey());
            this.btree.put(dd.getKey(),null);
            GenericCommand gc=new GenericCommand<URI>(dd.getKey(),(URI uri1)->{if(this.getDocSize(dd)>this.maxBytes){throw new IllegalArgumentException("document to be undone exceeds max byte size");};this.btree.put(uri1,dd);this.updateStorageTrackersForAdd(dd);this.docsInMemory.add(dd);dd.setLastUseTime(System.nanoTime());this.minHeap.insert(new URICompare(dd.getKey(),this.btree));if(dd.getDocumentBinaryData()==null){this.addDocToTrie(dd);}this.handleMaximums();return true;});
            cs.addCommand(gc);
        }
        this.commandStack.push(cs);
        return results;
    }

    /**
     * set maximum number of documents that may be stored
     * @param limit
     */
    public void setMaxDocumentCount(int limit) {
        this.maxDocs=limit;
        this.handleMaximums();
    }

    /**
     * set maximum number of bytes of memory that may be used by all the documents in memory combined
     * @param limit
     */
    public void setMaxDocumentBytes(int limit) {
        this.maxBytes=limit;
        this.handleMaximums();
    }
    /**
     * update variables tracking storage for addition of a document to the database
     * @param d
     */
    private void updateStorageTrackersForAdd(Document d) {
        this.numDocs++;
        if(d.getDocumentBinaryData()==null) {
            this.byteCount+=d.getDocumentTxt().getBytes().length;
        }
        else {
            this.byteCount+=d.getDocumentBinaryData().length;
        }
    }

    /**
     * update variables tracking storage for removal of a document to the database
     * @param d
     */
    private void updateStorageTrackerForDelete(Document d) {
        this.numDocs--;
        if(d.getDocumentBinaryData()==null) {
            this.byteCount-=d.getDocumentTxt().getBytes().length;
        }
        else {
            this.byteCount-=d.getDocumentBinaryData().length;
        }
    }

    /**
     * update variables tracking storage for modification of a document to the database
     * @param oldDoc, newDoc
     */
    private void updateStorageTrackerForModify(Document oldDoc, Document newDoc) {
        updateStorageTrackerForDelete(oldDoc);
        updateStorageTrackersForAdd(newDoc);
    }

    /**
     * @param d
     * @return the size of d in bytes
     */
    private int getDocSize(Document d) {
        int size;
        if(d.getDocumentBinaryData()==null) {
            size=d.getDocumentTxt().getBytes().length;
        }
        else {
            size=d.getDocumentBinaryData().length;
        }
        return size;
    }

    /**
     * check whether storage exceeds maximums and deal with what needs to be done if it has
     */
    private void handleMaximums() {
        while(numDocs>maxDocs||byteCount>maxBytes) {
            //take minimum doc from heap
            URICompare uri=this.minHeap.remove();
            Document d=uri.URItoDocConvert(); //calls the getter method from b tree
            //write to disk
            try {
                this.btree.moveToDisk(d.getKey());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //remove all references by each word in trie if word doc
            if(d.getDocumentBinaryData()==null) {
                this.deleteDocFromTrie(d);
            }
            //remove it from everywhere in the command stack
            StackImpl<Undoable> temp= new StackImpl<>();
            while(this.commandStack.size()>0) {
                Undoable u=commandStack.pop();
                if(u instanceof GenericCommand) {
                    GenericCommand gc =(GenericCommand) (u);
                    if(!gc.getTarget().equals(d.getKey())) {
                        temp.push(u);
                    }
                }
                else {
                    CommandSet cs=(CommandSet) (u);
                    if(cs.containsTarget(d.getKey()))
                    {
                        cs.remove(d.getKey()); //need to check that this removes what i want it to
                    }
                    temp.push(cs); //THIS WAS ORIGINALLY IN THE IF
                }
            }
            while(temp.size()>0) {
                this.commandStack.push(temp.pop());
            }
            //subtract necessary stuff from docCount and ByteCount
            this.updateStorageTrackerForDelete(d);
            this.docsInMemory.remove(d);
        }
    }

    /**
     * deletes document from the trie
     * @param d
     */
    private void deleteDocFromTrie(Document d) {
        for(String w: d.getWords()) {
            this.myTrie.delete(w,d.getKey());
        }
    }

    /**
     * adds document to the trie
     * @param d
     */
    private void addDocToTrie(Document d) {
        for(String w: d.getWords()) {
            this.myTrie.put(w,d.getKey());
        }
    }

    /**
     * removes document from MinHeap
     * @param d
     */
    private void removeFromMinHeap(Document d) {
        d.setLastUseTime(this.origNanoTime);
        this.minHeap.reHeapify(new URICompare(d.getKey(),this.btree)); //inside reHeapify, i find the array index of that element using equals method (the URICompare equals method just checks to see if the uri is the same so my new instance will b eqaul to the old instance already in the minheap)
        this.minHeap.remove();
    }

    /**
     * @param d
     * @return true if document is a text document, false if it is binary
     */
    private boolean isTxt(Document d) {
        return d.getDocumentBinaryData()==null;
    }
}
