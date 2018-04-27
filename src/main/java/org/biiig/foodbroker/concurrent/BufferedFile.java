package org.biiig.foodbroker.concurrent;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class BufferedFile {
    private final BufferedWriter writer;

    public BufferedFile(String path) throws FileNotFoundException {
        writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(path), Charset.forName("UTF-8")
                )
        );
    }

    public boolean write(String element) {
        try {
            writer.write(element);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean close() {
        try {
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
