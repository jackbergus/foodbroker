package it.giacomobergami.io;

import org.gradoop.common.model.impl.pojo.Edge;
import org.gradoop.common.model.impl.pojo.GraphTransaction;
import org.gradoop.common.model.impl.pojo.Vertex;

import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * Each node/edge having a specific label is written into a distinct file
 */
public class NewFileWriter {
    public HashMap<String, BufferedFile> map;
    private final NewCSVFormatter formatter;
    private final String lineSeparator;
    private final int thread;
    private final boolean multithreaded;

    public NewFileWriter(String directory) {
        this(directory, 0, false);
    }

    public NewFileWriter(String directory, int thread, boolean multithreaded) {
        this.formatter = new NewCSVFormatter(directory, ",");
        this.lineSeparator = System.getProperty("line.separator");;
        this.thread = thread;
        map = new HashMap<>();
        this.multithreaded = multithreaded;
    }

    private BufferedFile generate(String x) {
        String filename = formatter.getDirectory();
        filename += (multithreaded ? (thread+"_") : "") +x+formatter.getFileExtension();
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

    public void writeGraph(GraphTransaction gt) {
        for (Vertex v : gt.getVertices()) {
            writeVertex(v);
        }
        for (Edge e : gt.getEdges()) {
            writeEdge(e);
        }
    }

    protected boolean writeVertex(Vertex object) {
        String ogl = object.getLabel();
        map.computeIfAbsent(ogl, this::generate).write(formatter.format(object) + lineSeparator);
        return true;
    }

    protected boolean writeEdge(Edge object) {
        String ogl = object.getLabel();
        map.computeIfAbsent(ogl, this::generate).write(formatter.format(object) + lineSeparator);
        return true;
    }

    public void close() {
        for (BufferedFile w : map.values()) {
            w.close();
        }
        map.clear();
    }

}
