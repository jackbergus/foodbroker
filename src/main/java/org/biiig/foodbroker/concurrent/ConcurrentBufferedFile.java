package org.biiig.foodbroker.concurrent;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * This concurrent file writing avoids to generating files that have to be merged later on.
 * By using a blocking queue, all the threads put the elements in the queue, while the
 * actual writer will wait for the
 */
public class ConcurrentBufferedFile implements Runnable {
    private final BlockingQueue<Optional<String>> queue;
    private final BufferedWriter writer;
    private final Thread t;

    public ConcurrentBufferedFile(String path) throws FileNotFoundException {
        queue = new LinkedBlockingDeque<>();
        writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(path), Charset.forName("UTF-8")
                )
        );
        t = new Thread(this);
        t.start();
    }

    /**
     * Sending one message to the writer, which contains the to-be written line
     * @param element
     * @return
     */
    public boolean write(String element) {
        try {
            queue.put(Optional.of(element));
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * By sending an empty message, the writing process terminates.
     * @return
     */
    public boolean close() {
        try {
            queue.put(Optional.empty());
            t.join();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void run() {
        while (true) {
            Optional<String> next;
                try {
                    next = queue.take();
                    if (next.isPresent()) {
                        try {
                            writer.write(next.get());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        }
    }
}
