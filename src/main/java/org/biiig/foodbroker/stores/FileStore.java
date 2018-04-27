package org.biiig.foodbroker.stores;

import org.biiig.foodbroker.formatter.Formatter;
import org.biiig.foodbroker.model.DataObject;
import org.biiig.foodbroker.model.Relationship;

/**
 * Created by peet on 25.11.14.
 */
public class FileStore extends AbstractStore {

    //private String nodeFilePath;
    //private BufferedWriter nodeFileWriter;
    //private String edgeFilePath;
    //private BufferedWriter edgeFileWriter;
    private int thread;
    private String lineSeparator;
    private FileWriter fs;
    private final boolean multithreaded;

    public FileStore(Formatter formatter, boolean multithreaded){
        this(formatter, 0, multithreaded);
    }

    public FileStore(Formatter formatter, int thread, boolean multithreaded){
        this.formatter = formatter;
        this.thread = thread;
        this.lineSeparator = System.getProperty("line.separator");
        this.multithreaded = multithreaded;
    }

    @Override
    public void open() {
        fs = new FileWriter(formatter, lineSeparator, thread, multithreaded);
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

    public String getDirectory() {
        return fs.getDirectory();
    }
}
