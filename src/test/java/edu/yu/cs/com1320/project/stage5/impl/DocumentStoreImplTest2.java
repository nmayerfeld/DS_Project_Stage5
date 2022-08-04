package edu.yu.cs.com1320.project.stage5.impl;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import edu.yu.cs.com1320.project.stage5.impl.DocumentStoreImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class DocumentStoreImplTest2 {
    //variables to hold possible values for doc1
    private URI uri1;
    private String txt1;

    //variables to hold possible values for doc2
    private URI uri2;
    private String txt2;

    private URI uri3;
    private String txt3;

    private URI uri4;
    private String txt4;

    private URI uri5;
    private String txt5;

    private URI uri6;
    private String txt6;

    private URI uri7;
    private String txt7;

    private URI uri8;
    private String txt8;

    private URI uri9;
    private String txt9;

    private URI uri10;
    private String txt10;

    private URI uri11;
    private String txt11;

    private URI uri12;
    private String txt12;

    private URI uri13;
    private String txt13;

    private URI uri14;
    private String txt14;

    private URI uri15;
    private String txt15;

    private URI uri16;
    private String txt16;

    private URI uri17;
    private String txt17;

    private DocumentStoreImpl ds;
    @BeforeEach
    public void init() throws Exception {
        //init possible values for doc1
        this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        this.txt1 = "This is the text of doc1, in plain text. No fancy file format - just plain old String hi";

        //init possible values for doc2
        this.uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        this.txt2 = "Text for doc2. A plain old String string. hi";

        this.uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        this.txt3 = "Text for doc3. A plain old String. document";

        this.uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        this.txt4 = "Text for doc4. A plain old String. text noam";

        this.uri5 = new URI("http://edu.yu.cs/com1320/project/doc5");
        this.txt5 = "Text for doc5. A plain old String. docu document documber noam";

        this.uri6 = new URI("http://edu.yu.cs/com1320/project/doc6");
        this.txt6 = "Text blah blah blah shouldn't be deleted in delete all noam";

        this.uri7 = new URI("http://edu.yu.cs/com1320/project/doc7");
        this.txt7 = "this doc07 is one unit";

        this.uri8 = new URI("http://edu.yu.cs/com1320/project/doc8");
        this.txt8 = "this doc08 is one unit";

        this.uri9 = new URI("http://edu.yu.cs/com1320/project/doc9");
        this.txt9 = "this doc09 is one unit";

        this.uri10 = new URI("http://edu.yu.cs/com1320/project/doc10");
        this.txt10= "this doc10 is one unit";

        this.uri11 = new URI("http://edu.yu.cs/com1320/project/doc11");
        this.txt11 = "this doc11 is one unit";

        this.uri12 = new URI("http://edu.yu.cs/com1320/project/doc12");
        this.txt12 = "this doc12 is one unit";

        this.uri13 = new URI("http://edu.yu.cs/com1320/project/doc13");
        this.txt13 = "this doc13 is one unit";

        this.uri14 = new URI("http://edu.yu.cs/com1320/project/doc14");
        this.txt14 = "this doc14 is one unit";

        this.uri15 = new URI("http://edu.yu.cs/com1320/project/doc15");
        this.txt15 = "this doc15 is one unit";

        this.uri16 = new URI("http://edu.yu.cs/com1320/project/doc16");
        this.txt16 = "this doc16 is one unit";

        this.uri17 = new URI("http://edu.yu.cs/com1320/project/doc17");
        this.txt17 = "this doc17 is one unitthis doc17 is one unitthis doc17 is one unitthis doc17 is one unitthis doc17 is one unit";

        this.ds = new DocumentStoreImpl(new File("C:/Users/nmaye/OneDrive/forTestDPM"));
    }
    @Test
    public void testExceptionProperlyThrownWhenTryToUndoOnEmptyStack()
    {
        try {
            this.ds.undo();
            assert false;
        }catch(IllegalStateException e){
        }
    }
    @Test
    //public print method has been deleted for submission but this did work
    public void testSingleDocumentPutAndUndoAndSearch() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //ds.print();
        //assert ds.getCommandStackSize()==1;
        System.out.println();
        Document d=ds.getDocument(this.uri1);
        System.out.println(d.getDocumentTxt());
        assert ds.search("this").contains(d);
        assert ds.search("this").size()==1;
        assert ds.search("is").contains(d);
        assert ds.search("the").contains(d);
        assert ds.search("text").contains(d);
        assert ds.search("of").contains(d);
        assert ds.search("doc1").contains(d);
        assert ds.search("In").contains(d);
        assert ds.search("plain").contains(d);
        assert ds.search("Text").contains(d);
        assert ds.search("just").contains(d);
        assert ds.search("no").contains(d);
        assert ds.search("fancy").contains(d);
        assert ds.search("file").contains(d);
        assert ds.search("format").contains(d);
        assert ds.search("old").contains(d);
        assert ds.search("String").contains(d);
        ds.undo();
        assert ds.search("text").size()==0;
        //ds.print();
        //assert ds.getCommandStackSize()==0;
    }
    @Test
    public void testPutAndUndoBinary() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        //ds.print();
        //assert ds.getCommandStackSize()==1;
        ds.undo();
        System.out.println(ds.getDocument(uri1));
        assert ds.getDocument(uri1)==null;
    }
    @Test
    public void testThatComparatorProperlyOrdersObjectsInSetReturnedByASearch() throws IOException
    {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(this.uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(this.uri2);
        int i=0;
        for(Document dd:ds.search("String"))
        {
            if(i==0)
            {
                assert dd==d2;
            }
            else
            {
                assert dd==d1;
            }
            i++;
        }
    }
    @Test
    public void testThatSearchProperlyReturnsEmptySetWhenNoInstancesOfSaidWord()
    {
        assert ds.search("hello").size()==0;
    }
    @Test
    public void testPutDocumentWithMultipleDocsAndUndoWithSpecifiedURI() throws IOException {
        URI myUri=this.uri1;
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),myUri, DocumentStore.DocumentFormat.TXT);
        //assert ds.getCommandStackSize()==1;
        System.out.println();
        Document d=ds.getDocument(this.uri1);
        System.out.println(d);
        assert ds.search("this").contains(d);
        assert ds.search("this").size()==1;
        assert ds.search("is").contains(d);
        assert ds.search("the").contains(d);
        assert ds.search("text").contains(d);
        assert ds.search("of").contains(d);
        assert ds.search("doc1").contains(d);
        assert ds.search("In").contains(d);
        assert ds.search("plain").contains(d);
        assert ds.search("Text").contains(d);
        assert ds.search("just").contains(d);
        assert ds.search("no").contains(d);
        assert ds.search("fancy").contains(d);
        assert ds.search("file").contains(d);
        assert ds.search("format").contains(d);
        assert ds.search("old").contains(d);
        assert ds.search("String").contains(d);
        //ds.print();
        System.out.println();

        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document doc=ds.getDocument(this.uri2);
        System.out.println(doc.getDocumentTxt());
        assert ds.search("text").contains(doc);
        assert ds.search("text").size()==2;
        assert ds.search("doc2").contains(doc);
        assert ds.search("for").contains(doc);
        assert ds.search("plain").contains(doc);
        assert ds.search("old").contains(doc);
        assert ds.search("string").contains(doc);
        assert ds.search("string").contains(d);
        assert ds.search("string").size()==2;
        //assert ds.getCommandStackSize()==2;
        //ds.print();

        System.out.println();
        ds.undo(myUri);
        //assert ds.getCommandStackSize()==1;
        //ds.print();

        assert ds.search("text").contains(doc);
        assert !ds.search("text").contains(d);
        assert !ds.search("doc1").contains(d);
        System.out.println();
    }
    @Test
    private void testPutWithMultipleDocsAndUndoSpecifiedURIBinary() throws IOException {
        URI myUri=this.uri1;
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),myUri, DocumentStore.DocumentFormat.BINARY);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.BINARY);
        assert ds.getDocument(uri1)!=null;
        assert ds.getDocument(uri2)!=null;
        ds.undo(myUri);
        assertEquals(ds.getDocument(uri1),null);
    }
    @Test
    public void testModifySecondDocAfterTwoPuts() throws IOException
    {
        //put in the first
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //assert ds.getCommandStackSize()==1;
        System.out.println();
        Document d=ds.getDocument(this.uri1);
        //ds.print();
        System.out.println();
        //put in the second
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        //assert ds.getCommandStackSize()==2;
        System.out.println();
        Document doc=ds.getDocument(this.uri2);
        assert ds.search("doc2").contains(doc);
        System.out.println("\n\n\n\n\nsize serach string= "+ds.search("string").size());
        //ds.print();
        System.out.println();

        //modify the second
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        //assert ds.getCommandStackSize()==3;
        System.out.println();
        Document docu=ds.getDocument(this.uri2);
        assert docu.getWords().size()==8;
        //ds.print();
        System.out.println();
        //check that the words of the original second doc are no longer in the trie and that the words in third are
        assert ds.search("doc2").size()==0;
        System.out.println("size serach string= "+ds.search("string").size());
        for(Document docum:ds.search("string"))
        {
            System.out.println(docum.getKey());
        }
        assert ds.search("string").size()==2;
        assert ds.search("String").contains(docu);
        assert !ds.search("String").contains(doc);
    }
    @Test
    public void testModifySecondDocAfterTwoPutsBinary() throws IOException
    {
        //put in the first
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        //assert ds.getCommandStackSize()==1;
        System.out.println();
        //ds.print();
        System.out.println();
        //put in the second
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.BINARY);
        //assert ds.getCommandStackSize()==2;
        int origSize=this.txt2.getBytes().length;
        //modify the second
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri2, DocumentStore.DocumentFormat.BINARY);
        Document docu=ds.getDocument(this.uri2);
        assert docu.getDocumentBinaryData().length==this.txt3.getBytes().length;
        //ds.print();
    }
    @Test
    public void testUndoOfModifyDocumentWithSpecifiedURI() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //assert ds.getCommandStackSize()==1;
        //ds.print();
        System.out.println();
        Document d=ds.getDocument(this.uri1);
        System.out.println("D: "+d);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        //assert ds.getCommandStackSize()==2;
        //ds.print();
        System.out.println();
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document doc=ds.getDocument(this.uri1);
        System.out.println("DOC: "+doc);
        //assert ds.getCommandStackSize()==3;
        assert ds.search("doc3").contains(doc);
        assert !ds.search("doc 1").contains(d);
        //ds.print();
        System.out.println();
        ds.undo(this.uri1);
        //assert ds.getCommandStackSize()==2;
        //ds.print();
        System.out.println();
        //check that it undid the modify
        assert !ds.search("doc3").contains(doc);
        assert ds.search("doc1").contains(d);
        assert ds.search("string").size()==2;
        assert ds.search("string").contains(d);
        assert !ds.search("string").contains(doc);
    }
    @Test
    public void testUndoOfModifyDOcWIthSPecifiedURIBinary() throws IOException {
        //put in the first
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        //assert ds.getCommandStackSize()==1;
        int origSize=this.txt1.getBytes().length;
        System.out.println();
        //ds.print();
        System.out.println();
        //put in the second
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.BINARY);
        //assert ds.getCommandStackSize()==2;
        //modify the second
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        Document docu=ds.getDocument(this.uri1);
        assert docu.getDocumentBinaryData().length==this.txt3.getBytes().length;
        //ds.print();
        ds.undo(this.uri1);
        Document docum=ds.getDocument(this.uri1);
        assert docum.getDocumentBinaryData().length==origSize;
    }
    @Test
    public void testUndoOfDeleteDocumentWithSpecifiedURIUsingDeleteDocument() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //assert ds.getCommandStackSize()==1;
        //ds.print();
        System.out.println();
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d=ds.getDocument(this.uri2);
        //assert ds.getCommandStackSize()==2;
        //ds.print();
        System.out.println();
        //check that doc2 is in trie
        assert ds.search("doc2").contains(d);
        ds.deleteDocument(this.uri2);
        //ds.print();
        assert ds.search("doc2").size()==0;
        //assert ds.getCommandStackSize()==3;
        ds.undo(this.uri2);
        assert ds.search("doc2").contains(d);
        //assert ds.getCommandStackSize()==2;
        //ds.print();
        System.out.println();
    }
    @Test
    public void testUndoOfDeleteDocumentWithSpecifiedURIUsingDeleteDocumentBinary() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        //assert ds.getCommandStackSize()==1;
        //ds.print();
        System.out.println();
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.BINARY);
        ds.deleteDocument(uri2);
        assert ds.getDocument(uri2)==null;
        ds.undo(this.uri2);
        assert ds.getDocument(uri2)!=null;
    }
    @Test
    public void testValueChangeAndUndo() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //assert ds.getCommandStackSize()==1;
        //ds.print();
        System.out.println();
        //using input stream of txt 2 instead of txt 1- should replace the key
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //assert ds.getCommandStackSize()==2;
        //ds.print();
        System.out.println();

        ds.undo();
        //ds.print();
        //assert ds.getCommandStackSize()==1;
    }
    @Test
    public void testValueChangeFromBinaryToTxtAndUndo() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        //assert ds.getCommandStackSize()==1;
        //ds.print();
        System.out.println();
        //using input stream of txt 2 instead of txt 1- should replace the key
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d=ds.getDocument(this.uri1);
        //assert ds.getCommandStackSize()==2;
        //ds.print();
        System.out.println();
        //check that doc2 is in trie
        assert ds.search("doc2").contains(d);
        //assert ds.getCommandStackSize()==2;
        //ds.print();
        System.out.println();

        ds.undo();
        assert !ds.search("doc2").contains(d);
        //ds.print();
        //assert ds.getCommandStackSize()==1;
    }
    @Test
    public void testUndoOfDeleteUsingNullInputStream() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //assert ds.getCommandStackSize()==1;
        //ds.print();
        System.out.println();
        Document d=ds.getDocument(this.uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        //assert ds.getCommandStackSize()==2;
        //ds.print();
        System.out.println();
        assert ds.search("doc1").contains(d);
        assert ds.search("string").size()==2;
        ds.putDocument(null,this.uri1, DocumentStore.DocumentFormat.TXT);
        assert !ds.search("doc1").contains(d);
        //ds.print();
        System.out.println();
        //System.out.println(ds.getCommandStackSize());
        //assert ds.getCommandStackSize()==3;
        ds.undo();
        assert ds.search("doc1").contains(d);
        //ds.print();
        System.out.println();
        //assert ds.getCommandStackSize()==2;
    }
    @Test
    public void testUndoOfDeleteUsingNullInputStreamBinary() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        //assert ds.getCommandStackSize()==1;
        //ds.print();
        System.out.println();
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.BINARY);
        //assert ds.getCommandStackSize()==2;
        //ds.print();
        ds.putDocument(null,this.uri1, DocumentStore.DocumentFormat.BINARY);
        assertEquals(null, ds.getDocument(uri1));
        ds.undo();
        assert ds.getDocument(uri1)!=null;
    }
    @Test
    public void testUndoOfDeleteUsingDSIDelete() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //assert ds.getCommandStackSize()==1;
        //ds.print();
        System.out.println();
        Document d=ds.getDocument(this.uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        assert ds.search("string").size()==2;
        assert ds.search("doc1").contains(d);
        //assert ds.getCommandStackSize()==2;
        //ds.print();
        System.out.println();
        ds.deleteDocument(this.uri1);
        assert !ds.search("doc1").contains(d);
        assert ds.search("string").size()==1;
        //ds.print();
        System.out.println();
        //System.out.println(ds.getCommandStackSize());
        //assert ds.getCommandStackSize()==3;
        ds.undo();
        assert ds.search("string").size()==2;
        assert ds.search("doc1").contains(d);
        //ds.print();
        System.out.println();
        //assert ds.getCommandStackSize()==2;
    }
    @Test
    public void testUndoOfDeleteUsingDSIDeleteBinary() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        //assert ds.getCommandStackSize()==1;
        //ds.print();
        System.out.println();
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.BINARY);
        //assert ds.getCommandStackSize()==2;
        //ds.print();
        ds.deleteDocument(uri1);
        assertEquals(null,ds.getDocument(uri1));
        ds.undo();
        assert ds.getDocument(uri1)!=null;
    }
    @Test
    public void testThatPutAndUndoStillFunctionProperlyInACaseWhereHashTableWasDoubled() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //assert ds.getCommandStackSize()==1;
        assert ds.search("string").size()==1;
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        assert ds.search("string").size()==2;
        //assert ds.getCommandStackSize()==2;
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        assert ds.search("string").size()==3;
        //assert ds.getCommandStackSize()==3;
        ds.putDocument(new ByteArrayInputStream(this.txt4.getBytes()),this.uri4, DocumentStore.DocumentFormat.TXT);
        assert ds.search("string").size()==4;
        //assert ds.getCommandStackSize()==4;
        //ds.print();
        System.out.println();
        ds.putDocument(new ByteArrayInputStream(this.txt5.getBytes()),this.uri5, DocumentStore.DocumentFormat.TXT);
        assert ds.search("string").size()==5;
        //assert ds.getCommandStackSize()==5;
        //ds.print();
        System.out.println();
        ds.undo();
        assert ds.search("string").size()==4;
        //ds.print();
        //assert ds.getCommandStackSize()==4;
        ds.undo(this.uri3);
        System.out.println();
        //ds.print();
        System.out.println();
        //assert ds.getCommandStackSize()==3;
    }
    @Test
    public void testThatSearchByPrefixReturnsEmptyListWhenNoWordsWIthThatPrefix() throws IOException
    {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        assert ds.searchByPrefix("he").size()==0;
    }
    @Test
    public void testSearchByPrefix() throws IOException
    {
        //order for doc should b d5,d3 and then the rest
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(this.uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(this.uri2);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        Document d3=ds.getDocument(this.uri3);
        ds.putDocument(new ByteArrayInputStream(this.txt4.getBytes()),this.uri4, DocumentStore.DocumentFormat.TXT);
        Document d4=ds.getDocument(this.uri4);
        ds.putDocument(new ByteArrayInputStream(this.txt5.getBytes()),this.uri5, DocumentStore.DocumentFormat.TXT);
        Document d5=ds.getDocument(this.uri5);
        int i=0;
        for(Document dd: ds.searchByPrefix("doc"))
        {
            if(i==0)
            {
                assert dd==d5;
            }
            else if(i==1)
            {
                assert dd==d3;
            }
            System.out.println(dd);
            i++;
        }
        System.out.println();
        //checking for text
        int j=0;
        for(Document ddd:ds.searchByPrefix("tex"))
        {
            if(j==0||j==1)
            {
                assert ddd==d4||ddd==d1;
            }
            System.out.println(ddd);
            j++;
        }
    }
    @Test
    public void testDeleteAllOfOneDoc() throws IOException
    {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(this.uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(this.uri2);
        assert ds.searchByPrefix("doc").size()==2;
        //ds.print();
        ds.deleteAll("doc2");
        assert !ds.search("doc2").contains(d2);
        //ds.print();
    }
    @Test
    public void testDeleteAllAndUndoOfOneDoc() throws IOException
    {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(this.uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(this.uri2);
        assert ds.searchByPrefix("doc").size()==2;
        //ds.print();
        ds.deleteAll("doc2");
        assert !ds.search("doc2").contains(d2);
        //ds.print();
        ds.undo();
        //ds.print();
        assert ds.searchByPrefix("doc").size()==2;
    }
    @Test
    public void testDeleteAllOfMultipleDocs() throws IOException
    {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(this.uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(this.uri2);
        ds.putDocument(new ByteArrayInputStream(this.txt6.getBytes()),this.uri6, DocumentStore.DocumentFormat.TXT);
        Document d6=ds.getDocument(this.uri6);
        assert ds.searchByPrefix("text").size()==3;
        //ds.print();
        ds.deleteAll("String");
        //ds.print();
        assert !ds.search("String").contains(d2);
        assert !ds.search("doc1").contains(d1);
        assert ds.search("Text").size()==1;
        assert ds.search("text").contains(d6);
    }
    @Test
    public void testDeleteAllOfMultipleDocsAndUndo() throws IOException
    {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(this.uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(this.uri2);
        ds.putDocument(new ByteArrayInputStream(this.txt6.getBytes()),this.uri6, DocumentStore.DocumentFormat.TXT);
        Document d6=ds.getDocument(this.uri6);
        assert ds.searchByPrefix("text").size()==3;
        //ds.print();
        ds.deleteAll("String");
        //ds.print();
        assert !ds.search("String").contains(d2);
        assert !ds.search("doc1").contains(d1);
        assert ds.search("Text").size()==1;
        assert ds.search("text").contains(d6);
        ds.undo();
        //ds.print();
        assert ds.searchByPrefix("text").size()==3;
        assert ds.search("String").contains(d2);
        assert ds.search("doc1").contains(d1);
    }
    @Test
    public void TestDeleteAllWithPrefix() throws IOException
    {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(this.uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(this.uri2);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        Document d3=ds.getDocument(this.uri3);
        ds.putDocument(new ByteArrayInputStream(this.txt4.getBytes()),this.uri4, DocumentStore.DocumentFormat.TXT);
        Document d4=ds.getDocument(this.uri4);
        ds.putDocument(new ByteArrayInputStream(this.txt5.getBytes()),this.uri5, DocumentStore.DocumentFormat.TXT);
        Document d5=ds.getDocument(this.uri5);
        ds.putDocument(new ByteArrayInputStream(this.txt6.getBytes()),this.uri6, DocumentStore.DocumentFormat.TXT);
        Document d6=ds.getDocument(this.uri6);
        //ds.print();
        assert ds.searchByPrefix("doc").size()==5;
        ds.deleteAllWithPrefix("doc");
        //ds.print();
        assert ds.searchByPrefix("doc").size()==0;
        assert !ds.search("doc3").contains(d3);
    }
    @Test
    public void TestDeleteAllWithPrefixAndUndo() throws IOException
    {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(this.uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(this.uri2);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        Document d3=ds.getDocument(this.uri3);
        ds.putDocument(new ByteArrayInputStream(this.txt4.getBytes()),this.uri4, DocumentStore.DocumentFormat.TXT);
        Document d4=ds.getDocument(this.uri4);
        ds.putDocument(new ByteArrayInputStream(this.txt5.getBytes()),this.uri5, DocumentStore.DocumentFormat.TXT);
        Document d5=ds.getDocument(this.uri5);
        ds.putDocument(new ByteArrayInputStream(this.txt6.getBytes()),this.uri6, DocumentStore.DocumentFormat.TXT);
        Document d6=ds.getDocument(this.uri6);
        //ds.print();
        assert ds.searchByPrefix("doc").size()==5;
        ds.deleteAllWithPrefix("doc");
        //ds.print();
        assert ds.searchByPrefix("doc").size()==0;
        assert !ds.search("doc3").contains(d3);

        ds.undo();
        //ds.print();
        assert ds.searchByPrefix("doc").size()==5;
        assert ds.search("text").size()==6;
    }
    @Test
    public void testDeleteAllOnNonExistentWord() throws IOException
    {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(this.uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(this.uri2);
        ds.putDocument(new ByteArrayInputStream(this.txt6.getBytes()),this.uri6, DocumentStore.DocumentFormat.TXT);
        Document d6=ds.getDocument(this.uri6);
        assert ds.searchByPrefix("text").size()==3;
        //ds.print();
        assert ds.deleteAll("hello").size()==0;
        //ds.print();
    }
    @Test
    public void testDeleteAllWithPrefixWithNonExistentPrefix() throws IOException
    {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(this.uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(this.uri2);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        Document d3=ds.getDocument(this.uri3);
        ds.putDocument(new ByteArrayInputStream(this.txt4.getBytes()),this.uri4, DocumentStore.DocumentFormat.TXT);
        Document d4=ds.getDocument(this.uri4);
        ds.putDocument(new ByteArrayInputStream(this.txt5.getBytes()),this.uri5, DocumentStore.DocumentFormat.TXT);
        Document d5=ds.getDocument(this.uri5);
        ds.putDocument(new ByteArrayInputStream(this.txt6.getBytes()),this.uri6, DocumentStore.DocumentFormat.TXT);
        Document d6=ds.getDocument(this.uri6);
        //ds.print();
        assert ds.searchByPrefix("doc").size()==5;
        assert ds.deleteAllWithPrefix("bum").size()==0;
        //ds.print();
    }
    @Test
    public void testThatTheCommandSetIsPutBackOnTheStackIfIUndoOnlyOneOfTheDocumentsViaUndoingTheRest() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(this.uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(this.uri2);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        Document d3=ds.getDocument(this.uri3);
        ds.putDocument(new ByteArrayInputStream(this.txt4.getBytes()),this.uri4, DocumentStore.DocumentFormat.TXT);
        Document d4=ds.getDocument(this.uri4);
        ds.putDocument(new ByteArrayInputStream(this.txt5.getBytes()),this.uri5, DocumentStore.DocumentFormat.TXT);
        Document d5=ds.getDocument(this.uri5);
        ds.putDocument(new ByteArrayInputStream(this.txt6.getBytes()),this.uri6, DocumentStore.DocumentFormat.TXT);
        Document d6=ds.getDocument(this.uri6);
        //ds.print();
        assert ds.searchByPrefix("doc").size()==5;
        ds.deleteAll("noam");//should delete 4,5,6
        //ds.print();
        assert ds.searchByPrefix("noam").size()==0;
        assert !ds.search("doc4").contains(d4);
        ds.undo(d4.getKey()); //undo just d4
        //ds.print();
        System.out.println("should've put 4 back in");
        assert ds.search("doc4").contains(d4);
        assert ds.search("noam").size()==1;
        ds.undo(d6.getKey());
        //ds.print();
        System.out.println("should've put 6 back in");
        assert ds.search("blah").contains(d6);
        assert ds.search("doc5").size()==0;// check that doc5 is still deleted
    }
    @Test
    public void testThatUndoOfADeleteAllWithPrefixPutsTheRestBackOnTheStackForARegularUndoWithoutSpecificURI() throws IOException
    {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(this.uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(this.uri2);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        Document d3=ds.getDocument(this.uri3);
        ds.putDocument(new ByteArrayInputStream(this.txt4.getBytes()),this.uri4, DocumentStore.DocumentFormat.TXT);
        Document d4=ds.getDocument(this.uri4);
        ds.putDocument(new ByteArrayInputStream(this.txt5.getBytes()),this.uri5, DocumentStore.DocumentFormat.TXT);
        Document d5=ds.getDocument(this.uri5);
        ds.putDocument(new ByteArrayInputStream(this.txt6.getBytes()),this.uri6, DocumentStore.DocumentFormat.TXT);
        Document d6=ds.getDocument(this.uri6);
        //ds.print();
        assert ds.searchByPrefix("doc").size()==5;
        ds.deleteAll("noam");//should delete 4,5,6
        //ds.print();
        assert ds.searchByPrefix("noam").size()==0;
        assert !ds.search("doc4").contains(d4);
        ds.undo(d4.getKey()); //undo just d4
        //ds.print();
        System.out.println("should've put 4 back in");
        assert ds.search("doc4").contains(d4);
        assert ds.search("noam").size()==1;
        ds.undo(); //should put 5 and 6 back in
        //ds.print();
        System.out.println("should've put 5 and 6 back in");
        assert ds.search("blah").contains(d6);
        assert ds.search("doc5").size()==1;
        assert ds.search("text").size()==6;
    }
    @Test
    public void testThatUndoOfADeleteAllOnOneDocDoesNotPutAnEmptyCommandSetBackOnTheUndoStack() throws IOException
    {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(this.uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(this.uri2);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        Document d3=ds.getDocument(this.uri3);
        ds.putDocument(new ByteArrayInputStream(this.txt4.getBytes()),this.uri4, DocumentStore.DocumentFormat.TXT);
        Document d4=ds.getDocument(this.uri4);
        ds.putDocument(new ByteArrayInputStream(this.txt5.getBytes()),this.uri5, DocumentStore.DocumentFormat.TXT);
        Document d5=ds.getDocument(this.uri5);
        ds.putDocument(new ByteArrayInputStream(this.txt6.getBytes()),this.uri6, DocumentStore.DocumentFormat.TXT);
        Document d6=ds.getDocument(this.uri6);
        ////ds.print();
        assert ds.searchByPrefix("doc").size()==5;
        ds.deleteAll("doc5");//should delete 5
        ////ds.print();
        assert ds.searchByPrefix("doc5").size()==0;
        assert ds.search("doc4").contains(d4);
        ds.undo(d5.getKey()); //undo just d5
        ////ds.print();
        System.out.println("should've put 5 back in");
        assert ds.search("doc5").contains(d5);
        ds.undo(); //should remove doc 6 bc it was the last doc there
        ////ds.print();
        System.out.println("should've put 6 back in");
        assert !ds.search("text").contains(d6);
        assert ds.search("doc6").size()==0;// check that doc6 has been deleted
    }
    @Test
    public void testThrowsIAEIfPutGreaterThanLimitText() throws IOException {
        try{
            ds.setMaxDocumentBytes(txt1.getBytes().length-7);
            ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
            assert false;
        }catch(IllegalArgumentException e){}
    }
    @Test
    public void testThrowsIAEIfPutGreaterThanLimitBinary() throws IOException {
        try{
            ds.setMaxDocumentBytes(txt1.getBytes().length-7);
            ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
            assert false;
        }catch(IllegalArgumentException e){}
    }
    @Test
    public void testThrowsIAEIfUndoDeleteGreaterThanLimitWIthDeleteDocText() throws IOException {
        try{
            ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
            ds.deleteDocument(uri1);
            ds.setMaxDocumentBytes(txt1.getBytes().length-7);
            ds.undo();
            assert false;
        }catch(IllegalArgumentException e){}
    }
    @Test
    public void testThrowsIAEIfUndoDeleteGreaterThanLimitWIthDeleteDocBinary() throws IOException {
        try{
            ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
            ds.deleteDocument(uri1);
            ds.setMaxDocumentBytes(txt1.getBytes().length-7);
            ds.undo();
            assert false;
        }catch(IllegalArgumentException e){}
    }
    @Test
    public void testThrowsIAEIfUndoDeleteGreaterThanLimitWIthNullISText() throws IOException {
        try{
            ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
            ds.putDocument(null,uri1, DocumentStore.DocumentFormat.TXT);
            ds.setMaxDocumentBytes(txt1.getBytes().length-7);
            ds.undo();
            assert false;
        }catch(IllegalArgumentException e){}
    }
    @Test
    public void testThrowsIAEIfUndoDeleteGreaterThanLimitWIthNullISBinary() throws IOException {
        try{
            ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
            ds.putDocument(null,uri1, DocumentStore.DocumentFormat.BINARY);
            ds.setMaxDocumentBytes(txt1.getBytes().length-7);
            ds.undo();
            assert false;
        }catch(IllegalArgumentException e){}
    }
    @Test
    public void testThatDealsWithDeletingIfMaxBytesIsSetToNumberLowerThanWhatsCurrentlyContainedBinary() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        ds.setMaxDocumentBytes(txt1.getBytes().length-4); //should cause doc1 to be written to disk
        //I manually checked that it was written out to disk
    }
    @Test
    public void testThatDealsWithDeletingIfMaxBytesIsSetToNumberLowerThanWhatsCurrentlyContainedTXT() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        assert ds.searchByPrefix("doc").size()==1;
        ds.setMaxDocumentBytes(txt1.getBytes().length-4); //should cause doc1 to be deleted
        //make sure properly deleted from trie;
        assert ds.searchByPrefix("doc").size()==0;
        //checked manually that it was written out to disk
    }
    @Test
    public void testThatDealsWithDeletingIfMaxDocsIsSetToNumberLowerThanWhatsCurrentlyContainedBinary() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        ds.setMaxDocumentCount(0); //should cause doc1 to be deleted
    }
    @Test
    public void testThatDealsWithDeletingIfMaxDocsBytesIsSetToNumberLowerThanWhatsCurrentlyContainedTXT() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        assert ds.searchByPrefix("doc").size()==1;
        ds.setMaxDocumentCount(0); //should cause doc1 to be deleted
        //make sure properly deleted from trie;
        assert ds.searchByPrefix("doc").size()==0;
    }
    @Test
    public void testThatTwoPutsOrderProperlyBasedOnLastUsedTimeAndProperlyDealsWIthMaxWHenItsSet() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        ds.setMaxDocumentBytes(txt2.getBytes().length); //should cause doc1 to be written to disk
        //manually checked that doc1 was written to disk
        assert ds.searchByPrefix("doc").size()==1;
    }
    @Test
    public void testThatTwoPutsOrderProperlyBasedOnLastUsedTimeAndProperlyDealsWIthMaxWHenItsSetDeletingTXTFromTrie() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d=ds.getDocument(uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.BINARY);
        ds.setMaxDocumentBytes(txt2.getBytes().length); //should cause doc1 to be written to disk
       assert !ds.searchByPrefix("doc").contains(d);
    }
    @Test
    public void testThatGetProperlyResetsDocTime() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d=ds.getDocument(uri1);
        assert ds.searchByPrefix("doc").size()==2;
        ds.setMaxDocumentCount(1); //should cause doc2 to be deleted
        //make sure properly deleted from try;
        assert ds.searchByPrefix("doc").contains(d);
        assert ds.searchByPrefix("doc").size()==1;
    }
    @Test
    public void testThatSearchProperlyResetsDocTime() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        ds.search("doc1");
        assert ds.searchByPrefix("doc").size()==2;
        ds.setMaxDocumentCount(1); //should cause doc2 to be deleted
        //make sure properly deleted from try;
        assert ds.searchByPrefix("doc").contains(ds.getDocument(uri1));
        assert ds.searchByPrefix("doc").size()==1;
    }
    @Test
    public void testThatSearchPrefixResetsDocTime() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        ds.search("hi");
        assert ds.searchByPrefix("doc").size()==3;
        ds.setMaxDocumentBytes(txt1.getBytes().length+txt2.getBytes().length); //should cause doc3 to be deleted
        //make sure properly deleted from trie;
        assert ds.searchByPrefix("doc").size()==2;
    }
    @Test
    public void testThatUndoOfDeleteResetsDocTimeTXTformaxdoc() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        System.out.println("\n\n up to delete doc1");
        ds.deleteDocument(uri1);
        System.out.println("just deleted doc");
        ds.setMaxDocumentCount(2);
        ds.undo();//should cause doc2 to be written to disk
        System.out.println("just undid doc");

        //make sure properly deleted from trie;
        assertEquals(2,ds.searchByPrefix("doc").size());
    }
    @Test
    public void testThatUndoOfDeleteResetsDocTimeBinaryformaxdoc() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.BINARY);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.BINARY);
        System.out.println("\n\n up to delete doc1");
        ds.deleteDocument(uri1);
        System.out.println("just deleted doc");
        ds.setMaxDocumentCount(2);
        ds.undo();//should cause doc2 to be deleted
        System.out.println("just undid doc");
    }
    @Test
    public void testThatUndoOfDeleteResetsDocTimeMixedUsingMaxBytes() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        System.out.println("\n\n up to delete doc1");
        ds.deleteDocument(uri1);
        System.out.println("just deleted doc");
        System.out.println("byte size of doc1: "+txt1.getBytes().length);
        System.out.println("byte size of doc2: "+txt2.getBytes().length);
        System.out.println("byte size of doc3: "+txt3.getBytes().length);
        ds.setMaxDocumentBytes(this.txt2.getBytes().length+this.txt3.getBytes().length+8);
        ds.undo();//should cause doc2 to be deleted
        System.out.println("just undid doc");
         //has to delete both bc no room for doc 3 once doc 2 is there
        //WHY WAS NOTHING DELETED HERE-SEEMS LIKE UNDO OF DELETE NEVER REACHES HANDLE MAXIMUMS

        assertEquals(1,ds.searchByPrefix("doc").size());
        //MANUALLY CHECKED AND THEY WERE WRITTEN TO DISK
    }
    @Test
    public void testThatUndoOfPutWithNullResetsDocTimeTXTformaxdoc() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        System.out.println("\n\n up to delete doc1");
        ds.putDocument(null,uri1, DocumentStore.DocumentFormat.TXT);
        System.out.println("just deleted doc");
        ds.setMaxDocumentCount(2);
        ds.undo();//should cause doc2 to be deleted
        System.out.println("just undid doc");
        //manually checked that doc 2 was written to disk
        //make sure properly deleted from trie;
        assertEquals(2,ds.searchByPrefix("doc").size());
    }
    @Test
    public void testThatUndoOfPutWithNullResetsDocTimeBINARYformaxdoc() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.BINARY);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.BINARY);
        System.out.println("\n\n up to delete doc1");
        ds.putDocument(null,uri1, DocumentStore.DocumentFormat.BINARY);
        System.out.println("just deleted doc");
        ds.setMaxDocumentCount(2);
        ds.undo();//should cause doc2 to be deleted
        System.out.println("just undid doc");
        //checked that doc2 was written to disk
    }
    @Test
    public void testThatUndoOfPutWithNullResetsDocTimeTXTformaxBYTES() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        System.out.println("\n\n up to delete doc1");
        ds.putDocument(null,uri1, DocumentStore.DocumentFormat.TXT);
        System.out.println("just deleted doc");
        ds.setMaxDocumentBytes(140);
        ds.undo();//should cause doc2 to be deleted
        System.out.println("just undid doc");
        //manually checked that doc 2 was properly written to disk;
        //make sure properly deleted from trie;
        assertEquals(2,ds.searchByPrefix("doc").size());
    }
    @Test
    public void testThatUndoOfDeleteAllResetsDocTimeForMaxDoc() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        ds.deleteAll("hI"); //should write to disk docs 1 and 2
        ds.setMaxDocumentCount(2);
        ds.undo();//should cause doc3 to be written to disk
        //make sure properly deleted from trie;
        assertEquals(2,ds.searchByPrefix("hi").size());
    }
    @Test
    public void testThatUndoOfDeleteAllResetsDocTimeForMaxBytes() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        ds.deleteAll("hI"); //should write to disk docs 1 and 2
        ds.setMaxDocumentBytes(txt1.getBytes().length+txt2.getBytes().length);
        ds.undo();//should cause doc3 to be written to disk
        //make sure properly deleted from trie;
        assertEquals(2,ds.searchByPrefix("hi").size());
    }
    @Test
    public void testThatUndoOfDeleteAllPrefixResetsDocTimeForMaxDoc() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        ds.deleteAllWithPrefix("h"); //should delete docs 1 and 2
        ds.setMaxDocumentCount(2);
        ds.undo();//should cause doc3 to be written to disk
        //make sure properly deleted from trie;
        assertEquals(2,ds.searchByPrefix("hi").size());
    }
    @Test
    public void testThatUndoOfDeleteAllPrefixResetsDocTimeForMaxBytes() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        ds.deleteAllWithPrefix("h"); //should delete docs 1 and 2
        ds.setMaxDocumentBytes(txt1.getBytes().length+txt2.getBytes().length);
        ds.undo();//should cause doc3  to be written to disk
        //make sure properly deleted from trie;
        assertEquals(2,ds.searchByPrefix("hi").size());
    }
    @Test
    public void testThatDocumentIsRemovedFromUndoStackAsSingleCommandWHenDeletedBcMemoryOverflow() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        ds.getDocument(uri1); //makes doc1 most recently used
        ds.setMaxDocumentCount(2); //should delete doc 2
        try {
            ds.undo(uri2);
            assert false;
        }catch(IllegalStateException e){}
    }
    @Test
    public void testThatDocumentIsRemovedFromUndoStackAsSingleCommandWHenDeletedBcMemoryOverflowBinary() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.BINARY);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.BINARY);
        ds.getDocument(uri1); //makes doc1 most recently used
        ds.setMaxDocumentCount(2); //should delete doc 2
        try {
            ds.undo(uri2);
            assert false;
        }catch(IllegalStateException e){}
    }
    @Test
    public void testThatDocumentIsRemovedFromUndoStackAsSingleCommandWHenDeletedBcMemoryOverflowBytes() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
        ds.getDocument(uri1); //makes doc1 most recently used
        ds.setMaxDocumentBytes(txt1.getBytes().length+txt3.getBytes().length); //should delete doc 2
        try {
            ds.undo(uri2);
            assert false;
        }catch(IllegalStateException e){}
    }
    @Test
    public void testThatDocumentIsRemovedFromUndoStackAsPieceOfCommandSetWHenDeletedBcMemoryOverflow() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt7.getBytes()),this.uri7, DocumentStore.DocumentFormat.TXT);
        Document d7=ds.getDocument(uri7);
        ds.putDocument(new ByteArrayInputStream(this.txt8.getBytes()),this.uri8, DocumentStore.DocumentFormat.TXT);
        Document d8=ds.getDocument(uri8);
        ds.putDocument(new ByteArrayInputStream(this.txt9.getBytes()),this.uri9, DocumentStore.DocumentFormat.TXT);
        Document d9=ds.getDocument(uri9);
        ds.putDocument(new ByteArrayInputStream(this.txt10.getBytes()),this.uri10, DocumentStore.DocumentFormat.TXT);
        Document d10=ds.getDocument(uri10);
        ds.putDocument(new ByteArrayInputStream(this.txt11.getBytes()),this.uri11, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt12.getBytes()),this.uri12, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt13.getBytes()),this.uri13, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt14.getBytes()),this.uri14, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt15.getBytes()),this.uri15, DocumentStore.DocumentFormat.TXT);
        ds.setMaxDocumentBytes(10*txt7.getBytes().length);
        ds.putDocument(new ByteArrayInputStream(this.txt17.getBytes()),this.uri17, DocumentStore.DocumentFormat.TXT); //should cause 7-10 to be deleted
        Document d17=ds.getDocument(uri17);
        //check deleted from trie
        assert !ds.searchByPrefix("doc").contains(d7);
        assert !ds.searchByPrefix("doc").contains(d8);
        assert !ds.searchByPrefix("doc").contains(d9);
        assert !ds.searchByPrefix("doc").contains(d10);
        assert ds.searchByPrefix("doc").contains(d17);
        assertEquals(6,ds.searchByPrefix("doc").size());
        //manually checked that docs 7-10 were written to disk and that 11 and 17 weren't
    }
    @Test
    public void testThatDeleteOfDocInMemoryIsUndoneProperly() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(uri1);
        ds.deleteDocument(uri1);
        assertEquals(ds.search("doc1").size(),0);
        ds.undo();
        assertEquals(ds.search("doc1").size(),1);
    }
    @Test
    public void testThatReplacementOfDocInMemoryIsUndoneProperly() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(uri2);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        assertEquals(ds.search("doc3").size(),1);
        assertEquals(ds.search("doc1").size(),0);
        ds.undo();
        assertEquals(ds.search("doc3").size(),0);
        assertEquals(ds.search("doc1").size(),1);
    }
    @Test
    public void testThatUndoingADeleteOfADocOnDiskReturnsItToDisk() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(uri2);
        ds.setMaxDocumentCount(1); //doc1 moved to disk
        assertEquals(ds.search("doc1").size(),0);
        ds.deleteDocument(uri1);
        ds.undo();
        System.out.println(ds.search("doc1").size());
        assertEquals(ds.search("doc1").size(),0);
    }
    @Test
    public void testThatUndoingAReplacementOfADocOnDiskReturnsItToDisk() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(uri2);
        ds.setMaxDocumentCount(1); //doc1 moved to disk
        assertEquals(ds.search("doc1").size(),0);
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        assertEquals(ds.search("doc3").size(),1);
        ds.undo();
        assertEquals(ds.search("doc3").size(),0);
        assertEquals(ds.search("doc1").size(),0);
    }

    @Test
    public void testThatDocumentIsReSerializedByAGetTxt() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        ds.setMaxDocumentCount(1);
        assert ds.searchByPrefix("doc").size()==1;
        ds.getDocument(uri1);
        assert  ds.searchByPrefix("doc").size()==2;
    }
    @Test
    public void testThatDocumentIsReSerializedByAGetBinary() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()), this.uri1, DocumentStore.DocumentFormat.BINARY);
        Document d1 = ds.getDocument(uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()), this.uri2, DocumentStore.DocumentFormat.BINARY);
        ds.setMaxDocumentCount(1);
        ds.getDocument(uri1);
        //checked manually that back in memory
    }
    @Test
    public void testReplacementPutReplacingADocInMemoryText() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(uri2);
        ds.setMaxDocumentCount(1); //doc1 should be moved to memory
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        assert ds.search("doc3").size()==1;
        //manually checked that doc 1 was not on disk
    }
    @Test
    public void testReplacementPutReplacingADocInMemoryBinary() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        Document d1=ds.getDocument(uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.BINARY);
        Document d2=ds.getDocument(uri2);
        ds.setMaxDocumentCount(1); //doc1 should be moved to memory
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        //assert ds.search("doc3").size()==1;
        //manually checked that doc 1 was not on disk
    }
    @Test
    public void testReplacementPutReplacingADocInMemoryAndUndoGoesStraightToDiskText() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        Document d1=ds.getDocument(uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
        Document d2=ds.getDocument(uri2);
        ds.setMaxDocumentCount(1); //doc1 should be moved to memory
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        assert ds.search("doc3").size()==1;
        ds.undo();
        //manually checked that it is rewritten to disk
        assert ds.search("doc1").size()==0;
    }
    @Test
    public void testReplacementPutReplacingADocInMemoryAndUndoGoesStraightToDiskBinary() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        Document d1=ds.getDocument(uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.BINARY);
        Document d2=ds.getDocument(uri2);
        ds.setMaxDocumentCount(1); //doc1 should be moved to memory
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        ds.undo();
        //manually checked that doc 1 was on disk
    }
    @Test
    public void testThatPutDocumentOnSerializedDocumentDeletesIt() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
        Document d1=ds.getDocument(uri1);
        ds.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.BINARY);
        Document d2=ds.getDocument(uri2);
        ds.setMaxDocumentCount(1); //doc1 should be moved to memory
        ds.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri1, DocumentStore.DocumentFormat.BINARY);
    }
    @Test
    public void testWithABunchOfDocsThatProgramWorksProperly() throws IOException {
        ds.putDocument(new ByteArrayInputStream(this.txt7.getBytes()),this.uri7, DocumentStore.DocumentFormat.TXT);
        Document d7=ds.getDocument(uri7);
        ds.putDocument(new ByteArrayInputStream(this.txt8.getBytes()),this.uri8, DocumentStore.DocumentFormat.TXT);
        Document d8=ds.getDocument(uri8);
        ds.putDocument(new ByteArrayInputStream(this.txt9.getBytes()),this.uri9, DocumentStore.DocumentFormat.TXT);
        Document d9=ds.getDocument(uri9);
        ds.putDocument(new ByteArrayInputStream(this.txt10.getBytes()),this.uri10, DocumentStore.DocumentFormat.TXT);
        Document d10=ds.getDocument(uri10);
        ds.putDocument(new ByteArrayInputStream(this.txt11.getBytes()),this.uri11, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt12.getBytes()),this.uri12, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt13.getBytes()),this.uri13, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt14.getBytes()),this.uri14, DocumentStore.DocumentFormat.TXT);
        ds.putDocument(new ByteArrayInputStream(this.txt15.getBytes()),this.uri15, DocumentStore.DocumentFormat.TXT);
        ds.setMaxDocumentBytes(10*txt7.getBytes().length);
        ds.putDocument(new ByteArrayInputStream(this.txt17.getBytes()),this.uri17, DocumentStore.DocumentFormat.TXT); //should cause 7-10 to be deleted
        Document d17=ds.getDocument(uri17);
        //check deleted from trie
        assert !ds.searchByPrefix("doc07").contains(d7);
        assert !ds.searchByPrefix("doc08").contains(d8);
        assert !ds.searchByPrefix("doc09").contains(d9);
        assert !ds.searchByPrefix("doc10").contains(d10);
        assert ds.searchByPrefix("doc17").contains(d17);
        //trying a replacement put for doc 7
        ds.putDocument(new ByteArrayInputStream(this.txt9.getBytes()),this.uri7, DocumentStore.DocumentFormat.TXT); //should cause 11 to be deleted
        assert ds.search("doc09").size()==1;
        //trying a delete for put for document 8- already on disk
        ds.putDocument(null,this.uri8, DocumentStore.DocumentFormat.TXT);
        //trying a delete for a document in memory (doc 12)
        ds.deleteDocument(uri12);
        assert ds.search("doc12").size()==0;
        ds.undo(uri8);//should put it back in memory
        assert ds.search("doc08").size()==1;
        ds.undo(); //should put 12 back in
        assert ds.search("doc12").size()==1; //causes 13 to be sent to disk
        ds.undo(); //undoes text change on doc 7- should go back to disk
        assert ds.search("doc09").size()==0;
        assert ds.search("doc07").size()==0;
        //14 is the next document ready to be touched
        ds.getDocument(uri14);
        //this should now get rid of 15 and 17
        ds.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        assert ds.search("doc15").size()==0;
        assert ds.search("doc17").size()==0;
        ds.setMaxDocumentCount(1); //should kick out all remaining docs except doc1
        assert ds.search("doc14").size()==0;
        assert ds.search("doc1").size()==1;
        ds.putDocument(new ByteArrayInputStream(this.txt9.getBytes()),this.uri9, DocumentStore.DocumentFormat.TXT);
        assert ds.search("doc1").size()==0;
        assert ds.search("doc09").size()==1;
    }

}