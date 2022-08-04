package edu.yu.cs.com1320.project.stage5.impl;
import com.google.gson.annotations.Expose;
import edu.yu.cs.com1320.project.stage5.Document;

import java.net.URI;
import java.util.*;

public class DocumentImpl implements Document
{
    @Expose
    private String documentText;
    private byte[] documentBinaryData;
    @Expose
    private URI documentURI;
    @Expose
    private Map<String, Integer> words;
    private long timeStamp;
    public DocumentImpl(URI uri, String txt, Map<String, Integer> wordCountMap)
    {
        if(uri==null||uri.toASCIIString().isBlank()||txt==null||txt.equals(""))
        {
            throw new IllegalArgumentException("one of the fields in the constructor was null");
        }
        this.documentURI=uri;
        this.documentText=txt;
        this.timeStamp=System.nanoTime();
        if(wordCountMap==null)
        {
            this.words=new HashMap<>();
            String txtUpperCase=this.documentText.toUpperCase();
            String[] wordsInDocument=txtUpperCase.split(" ");
            List<String> wordsAfterSplit=new ArrayList<>();
            for(String s: wordsInDocument)
            {
                s=s.replaceAll("[^a-zA-Z0-9]", "");
                wordsAfterSplit.add(s);
            }
            for(String w: wordsAfterSplit)
            {
                //ensuring that there were no "words" that were only non-alphanumeric bc they would b empty strings at this pt
                if(!w.equals(""))
                {
                    if(words.get(w)==null)
                    {
                        words.put(w,1);
                    }
                    else
                    {
                        words.put(w,words.get(w)+1);
                    }
                }
            }
        }
        else
        {
            this.words=wordCountMap;
        }
    }
    public DocumentImpl(URI uri, byte[] binaryData)
    {
        if(uri==null||uri.toString().length()==0||binaryData==null||binaryData.length==0)
        {
            throw new IllegalArgumentException("one of the fields in the constructor was null");
        }
        this.documentURI=uri;
        this.documentBinaryData=binaryData;
        this.timeStamp=System.nanoTime();
    }
    /**
     * @return content of text document
     */
    public String getDocumentTxt()
    {
        return this.documentText;
    }

    /**
     * @return content of binary data document
     */
    public byte[] getDocumentBinaryData()
    {
        return this.documentBinaryData;
    }

    /**
     * @return URI which uniquely identifies this document
     */
    public URI getKey()
    {
        return this.documentURI;
    }
    /**
     * @param word
     * @return the number of times word appears in the document
     */
    public int wordCount(String word)
    {
        if(word==null||this.documentBinaryData!=null)
        {
            throw new IllegalArgumentException("word was null or this is a binary doc");
        }
        String myWord=word.toUpperCase();
        if(this.words.get(myWord) == null)
        {
            return 0;
        }
        return this.words.get(myWord);
    }
    /**
     * @return the list of words in the document
     */
    public Set<String> getWords()
    {
        return this.words.keySet();
    }
    @Override
    public int hashCode()
    {
        int result = this.documentURI.hashCode();
        result = 31 * result + (this.documentText != null ? this.documentText.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(this.documentBinaryData);
        return result;
    }
    @Override
    public boolean equals(Object obj)
    {
        //see if it's the same object
        if(this == obj)
        {
            return true;
        }
        //see if it's null
        if(obj == null)
        {
            return false;
        }
        //see if they're from the same class
        if(getClass()!=obj.getClass())
        {
            return false;
        }
        DocumentImpl other=(DocumentImpl)obj;
        return this.hashCode()==other.hashCode();
    }
    /**
     * return the last time this document was used, via put/get or via a search result
     * (for stage 4 of project)
     */
    public long getLastUseTime()
    {
        return this.timeStamp;
    }
    /**
     * @param timeInNanoseconds
     * sets lastUseTime to timeInNanoseconds
     */
    public void setLastUseTime(long timeInNanoseconds)
    {
        this.timeStamp=timeInNanoseconds;
    }
    /**
     * @return a Map of words to the number of times they appear
     */
    public Map<String,Integer> getWordMap()
    {
        Map<String,Integer> copyOfMap=new HashMap<>();
        copyOfMap.putAll(this.words);
        return copyOfMap;
    }
    /**
     * @param wordMap
     */
    public void setWordMap(Map<String,Integer> wordMap)
    {
        this.words=wordMap;
    }
    @Override
    public int compareTo (Document d2)
    {
        return (int)(this.getLastUseTime()-d2.getLastUseTime());
    }
}
