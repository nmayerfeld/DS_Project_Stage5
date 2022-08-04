package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.stage5.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class DocumentPersistenceManagerTest {
    private URI uri1;
    private URI uri2;
    private String txt1;
    private String txt2;
    private byte[] bytes;
    private Document d1;
    private Document d2;
    private DocumentPersistenceManager dpm;

    @BeforeEach
    public void init() throws Exception {
        //init possible values for doc1
        this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        this.txt1 = "This is the text of doc1, in plain text. No fancy file format - just plain old String hi";

        //init possible values for doc2
        this.uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        this.txt2 = "Text for doc2. A plain old String string. hi";

        this.d1=new DocumentImpl(uri1,txt1, null);
        this.d2=new DocumentImpl(uri2,txt2.getBytes());

        this.dpm=new DocumentPersistenceManager(new File(System.getProperty("user.dir")));
    }
    @Test
    public void testSerializeAByteDocument() throws IOException {
        System.out.println(d2);
        System.out.println(this.d2.getKey());
        System.out.println(new String(d2.getDocumentBinaryData(),StandardCharsets.UTF_8));
        this.dpm.serialize(uri2,d2);
        Document d=this.dpm.deserialize(uri2);
        System.out.println(d);
        System.out.println(d.getKey());
        System.out.println(new String(d2.getDocumentBinaryData(),StandardCharsets.UTF_8));
    }
    @Test
    public void testSerializeATextDocument() throws IOException {
        System.out.println(d1);
        System.out.println(this.d1.getKey());
        System.out.println(d1.getDocumentTxt());
        this.dpm.serialize(uri1,d1);
        Document d=this.dpm.deserialize(uri1);
        System.out.println(d);
        System.out.println(d.getKey());
        System.out.println(d1.getDocumentTxt());
        assert d1.getWordMap().equals(d.getWordMap());
    }
    @Test
    public void testDeleteMethodViaASerializationAndThenAManualCheckOfTheFile() throws IOException {
        this.dpm.serialize(uri1,d1);
        assertEquals(this.dpm.delete(uri1),true);
    }

}
