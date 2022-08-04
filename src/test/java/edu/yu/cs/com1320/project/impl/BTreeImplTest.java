package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage5.impl.DocumentPersistenceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class BTreeImplTest {
    private BTreeImpl<URI, Document> bTree;
    private DocumentPersistenceManager pm;
    private URI uri1;
    private URI uri2;
    private URI uri3;
    private URI uri4;
    private String s1;
    private String s2;
    private String s3;
    private String s4;
    private Document d1;
    private Document d2;
    private Document d3;
    private Document d4;

    @BeforeEach
    public void init() throws Exception {
        this.uri1 = new URI("http://edu.yu.cs/com1320/project/document111");
        this.s1 = "This is the text of doc1, in plain text. No fancy file format - just plain old String hi";
        this.d1=new DocumentImpl(uri1,s1, null);
        //init possible values for doc2
        this.uri2 = new URI("http://edu.yu.cs/com1320/project/document222");
        this.s2 = "Text for doc2. A plain old String string. hi";
        this.d2=new DocumentImpl(uri2,s2,null);
        this.uri3 = new URI("http://edu.yu.cs/com1320/project/document333");
        this.s3 = "Text for doc3. A plain old String. document";
        this.d3=new DocumentImpl(uri3,s3.getBytes());
        this.uri4 = new URI("http://edu.yu.cs/com1320/project/document444");
        this.s4 = "Text for doc4. A plain old String. text noam";
        this.d4=new DocumentImpl(uri4,s4.getBytes());
        this.bTree=new BTreeImpl<>();
        this.pm=new DocumentPersistenceManager(new File(System.getProperty("user.dir")));
        bTree.setPersistenceManager(this.pm);
        this.bTree.put(uri4,d4);
        this.bTree.moveToDisk(uri4);
    }
    @Test
    public void textASinglePutAndGet()
    {
        this.bTree.put(uri1,d1);
        assert this.bTree.get(uri1)==d1;
    }
    @Test
    public void testAMoveToDiskText() throws Exception {
        this.bTree.put(uri1,d1);
        this.bTree.moveToDisk(uri1);
        //i checked that the file was actually created
    }
    @Test
    public void testAMoveToDiskBinary() throws Exception {
        this.bTree.put(uri3,d3);
        this.bTree.moveToDisk(uri3);
        //i checked that the file was actually created
    }
    @Test
    public void testThatGetOnDocumentInMemoryReturnsItToMemoryAndDeletesTheFile() throws Exception {
        this.bTree.put(uri1,d1);
        this.bTree.moveToDisk(uri1);
        this.bTree.put(uri3,d3);
        this.bTree.moveToDisk(uri3);
        Document document1=this.bTree.get(uri1);
        Document document3=this.bTree.get(uri3);
        assertEquals(document1.getKey(),d1.getKey());
        assertEquals(document1.getDocumentTxt(),d1.getDocumentTxt());
        assertEquals(document1.getWordMap(),d1.getWordMap());
        assertEquals(document3.getKey(),d3.getKey());
        for(int i=0;i<document3.getDocumentBinaryData().length;i++)
        {
            assertEquals(document3.getDocumentBinaryData()[i],d3.getDocumentBinaryData()[i]);
        }
    }
}
