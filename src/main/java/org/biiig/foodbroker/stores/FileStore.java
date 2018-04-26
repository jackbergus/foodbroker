package org.biiig.foodbroker.stores;

import org.biiig.foodbroker.formatter.Formatter;
import org.biiig.foodbroker.model.DataObject;
import org.biiig.foodbroker.model.Relationship;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by peet on 25.11.14.
 */
public class FileStore extends AbstractStore{

    //private String nodeFilePath;
    //private BufferedWriter nodeFileWriter;
    //private String edgeFilePath;
    //private BufferedWriter edgeFileWriter;
    private int thread;
    private String lineSeparator;
    private FileWriter fs;

    public FileStore(Formatter formatter){
        this(formatter, 0);
    }

    public FileStore(Formatter formatter, int thread){
        this.formatter = formatter;
        this.thread = thread;
        this.lineSeparator = System.getProperty("line.separator");
    }

    @Override
    public void open() {
        fs = new FileWriter(formatter, lineSeparator, thread);
    }

    @Override
    public void store(DataObject dataObject) {
        fs.writeVertex(dataObject);
        if (formatter.hasSeparateRelationshipHandling()) {
            for (String key : dataObject.getNestedRelationshipKeys()) {
                store(dataObject.getNestedRelationship(key));
            }
        }
    }

    @Override
    public void store(Relationship relationship) {
        fs.writeEdge(relationship);
    }

    @Override
    public void close() {
        fs.close();
    }

    public String getNodeFilePath() {
        return null;
    }

    public String getEdgeFilePath() {
        return null;
    }
}
