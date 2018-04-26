package org.biiig.foodbroker.stores;

import org.biiig.foodbroker.concurrent.ConcurrentBufferedFile;
import org.biiig.foodbroker.formatter.Formatter;
import org.biiig.foodbroker.model.DataObject;
import org.biiig.foodbroker.model.Relationship;

import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * Each node/edge having a specific label is written into a distinct file
 */
public class FileWriter {

    public HashMap<String, ConcurrentBufferedFile> map;
    private final Formatter formatter;
    private final String lineSeparator;
    private final int thread;

    public FileWriter(Formatter formatter, String lineSeparator, int thread) {
        this.formatter = formatter;
        this.lineSeparator = lineSeparator;
        this.thread = thread;
        map = new HashMap<>();
    }

    private ConcurrentBufferedFile generate(String x, boolean isNode) {
        try {
            return new ConcurrentBufferedFile(formatter.getDirectory()+x+formatter.getFileExtension());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean writeVertex(DataObject object) {
        String ogl = object.getLabel();
        map.computeIfAbsent(ogl, x -> generate(x, true)).write(formatter.format(object) + lineSeparator);
        return true;
    }

    public boolean writeEdge(Relationship object) {
        String ogl = object.getLabel();
        map.computeIfAbsent(ogl, x -> generate(x, false)).write(formatter.format(object) + lineSeparator);
        return true;
    }

    public void close() {
        for (ConcurrentBufferedFile w : map.values()) {
            w.close();
        }
        map.clear();
    }

}
