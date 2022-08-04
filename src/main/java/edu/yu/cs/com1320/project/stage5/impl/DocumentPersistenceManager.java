package edu.yu.cs.com1320.project.stage5.impl;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import jakarta.xml.bind.DatatypeConverter;

/**
 * created by the document store and given to the BTree via a call to BTree.setPersistenceManager
 */
public class DocumentPersistenceManager implements PersistenceManager<URI, Document> {
    private File baseDir;
    public DocumentPersistenceManager(File baseDir){
        if(baseDir!=null)
        {
            this.baseDir=baseDir;
        }
        else
        {
            this.baseDir=new File(System.getProperty("user.dir")+File.separator);
        }
        baseDir.mkdir();
    }

    @Override
    public void serialize(URI uri, Document val) throws IOException {
        String json;
        String documentLocation="";
        String temps=uri.getSchemeSpecificPart();
        for (int i=0;i<2;i++)
        {
            if(temps.substring(0,1).equals("/"))
            {
                temps=temps.substring(1);
            }
        }
        String[] words=temps.split("/");
        File oldFile=this.baseDir;
        for(int i=0;i<words.length-1;i++) //excludes the last one which is the final file name
        {
            File ff=new File(oldFile,words[i]);
            ff.mkdir();
            oldFile=ff;
        }
        File f=new File(oldFile.getAbsolutePath(),words[words.length-1]+".json");
        f.createNewFile();
        FileOutputStream fileOut = new FileOutputStream(f);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        if(val.getDocumentBinaryData()!=null)
        {
            JsonSerializer<Document> jd=new JsonSerializer<Document>() {
                @Override
                public JsonElement serialize(Document document, Type type, JsonSerializationContext jsonSerializationContext) {
                    JsonObject object = new JsonObject();
                    object.addProperty("uri", String.valueOf(val.getKey()));
                    object.addProperty("contents binary",DatatypeConverter.printBase64Binary(val.getDocumentBinaryData()));
                    return object;
                }
            };
            Gson gson=new GsonBuilder().registerTypeAdapter(DocumentImpl.class,jd).create();
            json=gson.toJson(val);
        }
        else
        {
            Gson gson=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            json=gson.toJson(val);
        }

        out.writeObject(json);
        out.close();
        fileOut.close();
    }

    @Override
    public Document deserialize(URI uri) throws IOException {
        //need to figure out how to delete the file and how to close it
        String temps=uri.getSchemeSpecificPart();
        for (int i=0;i<2;i++)
        {
            if(temps.substring(0,1).equals("/"))
            {
                temps=temps.substring(1);
            }
        }
        String[] words=temps.split("/");
        File oldFile=this.baseDir;
        for(int i=0;i<words.length-1;i++) //excludes the last one which is the final file name
        {
            File ff=new File(oldFile,words[i]);
            ff.mkdir();
            oldFile=ff;
        }
        String s=words[words.length-1]+".json";
        File f=new File(oldFile.getAbsolutePath(),s);
        if(!f.exists())
        {
            throw new IllegalArgumentException("filepath specified in uri doesn't exist");
        }
        FileInputStream fileIn = new FileInputStream(f);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        JsonDeserializer<Document> jd=new JsonDeserializer<Document>(){
            public Document deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                DocumentImpl ddd;
                JsonObject j= json.getAsJsonObject();
                try {
                    String myUriTemp= String.valueOf(j.get("uri"));
                    if(myUriTemp.equals("null"))
                    {
                        String s=String.valueOf(j.get("documentURI"));
                        String sFinal=s.substring(1,s.length()-1);
                        String text=String.valueOf(j.get("documentText"));
                        String textFinal=text.substring(1,text.length()-1);
                        Map<String, Integer> map = new Gson().fromJson(j.get("words"), new TypeToken<Map<String, Integer>>() {}.getType());
                        ddd=new DocumentImpl(new URI(sFinal),textFinal,map);
                        ddd.setWordMap(map);
                    }
                    else
                    {
                        String myUri=myUriTemp.substring(1,myUriTemp.length()-1);
                        URI uri=new URI(myUri);
                        ddd=new DocumentImpl(uri,DatatypeConverter.parseBase64Binary(String.valueOf(j.get("contents binary"))));
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    ddd=null;
                }
                return ddd;
            };
        };
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DocumentImpl.class,jd)
                .create();
        Document d=null;
        try {
            d = gson.fromJson((String) in.readObject(), DocumentImpl.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        in.close();
        fileIn.close();
        f.delete();
        return d;
    }

    @Override
    public boolean delete(URI uri) throws IOException {
        String temps=uri.getSchemeSpecificPart();
        for (int i=0;i<2;i++)
        {
            if(temps.substring(0,1).equals("/"))
            {
                temps=temps.substring(1);
            }
        }
        String[] words=temps.split("/");
        File oldFile=this.baseDir;
        for(int i=0;i<words.length-1;i++) //excludes the last one which is the final file name
        {
            File ff=new File(oldFile,words[i]);
            ff.mkdir();
            oldFile=ff;
        }
        File f=new File(oldFile.getAbsolutePath(),words[words.length-1]+".json");
        return f.delete();
    }
}

