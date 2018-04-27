package org.biiig.foodbroker.stores;

import org.biiig.foodbroker.concurrent.BufferedFile;
import org.biiig.foodbroker.formatter.Formatter;
import org.biiig.foodbroker.model.DataObject;
import org.biiig.foodbroker.model.Relationship;

import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * Each node/edge having a specific label is written into a distinct file
 */
public class FileWriter {

    public HashMap<String, BufferedFile> map;
    private final Formatter formatter;
    private final String lineSeparator;
    private final int thread;
    private final boolean multithreaded;

    public FileWriter(Formatter formatter, String lineSeparator, int thread, boolean multithreaded) {
        this.formatter = formatter;
        this.lineSeparator = lineSeparator;
        this.thread = thread;
        map = new HashMap<>();
        this.multithreaded = multithreaded;
    }

    private BufferedFile generate(String x, boolean isNode) {
        String filename = formatter.getDirectory();
        filename += (multithreaded ? (thread+"") : "") + "_" + x+formatter.getFileExtension();
        try {
            return new BufferedFile(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDirectory() {
        return formatter.getDirectory();
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
        for (BufferedFile w : map.values()) {
            w.close();
        }
        map.clear();
    }

}
