package org.biiig.foodbroker.stores;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.biiig.foodbroker.concurrent.BufferedFile;
import org.biiig.foodbroker.formatter.Formatter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by peet on 28.11.14.
 */
public class FileStoreCombiner extends AbstractStoreCombiner {

  private final List<FileStore> fileStores = new ArrayList<>();
  private final Formatter formatter;
  private final String lineSeparator;

  public FileStoreCombiner(Formatter formatter) {
    this.formatter = formatter;
    this.lineSeparator = System.getProperty("line.separator");
  }

  @Override
  public void add(Store store) {
    fileStores.add((FileStore) store);
  }

  @Override
  public void combine() {
    try {
      BufferedWriter nodeFileWriter = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(formatter.getNodeFilePath()),
        Charset.forName("UTF-8")));

      BufferedWriter edgeFileWriter;

      if (formatter.hasSeparateRelationshipHandling()) {
        edgeFileWriter = new BufferedWriter(new OutputStreamWriter(
          new FileOutputStream(formatter.getEdgeFilePath()),
          Charset.forName("UTF-8")));

      } else {
        edgeFileWriter = nodeFileWriter;
      }

      if (formatter.requiresNodeOpening()) {
        File nodeFile = new File(formatter.getNodeOpeningFilePath());
        LineIterator nodeIterator = FileUtils.lineIterator(nodeFile);
        while (nodeIterator.hasNext()) {
          nodeFileWriter.write(nodeIterator.nextLine() + lineSeparator);
        }
      }

      if (formatter.requiresEdgeOpening()) {
        File edgeFile = new File(formatter.getEdgeOpeningFilePath());
        LineIterator edgeIterator = FileUtils.lineIterator(edgeFile);
        while (edgeIterator.hasNext()) {
          nodeFileWriter.write(edgeIterator.nextLine() + lineSeparator);
        }
      }

      Set<String> visitedPaths = new HashSet<>();
      for (FileStore fileStore : this.fileStores) {
        String dir = fileStore.getDirectory();
        if (visitedPaths.add(dir)) {
          File d = new File(dir);
          if (d.isDirectory()) {
            Map<String, List<String>> map = Arrays
                    .stream(Objects.requireNonNull(d.listFiles()))
                    .map(File::getName)
                    .filter(x -> {
                      int indexOf = x.indexOf('_');
                      if (indexOf <= 0) return false;
                      return x.substring(0, indexOf).matches("\\d+") && !x.substring(indexOf + 1).isEmpty();
                    })
                    .collect(Collectors.groupingBy(x -> x.substring(x.indexOf('_') + 1)));
            Iterator<Map.Entry<String, List<String>>> it = map.entrySet().iterator();
            while (it.hasNext()) {
              Map.Entry<String, List<String>> curr = it.next();
              BufferedFile bf = new BufferedFile(new File(d, curr.getKey()).getAbsolutePath());
              for (String x : curr.getValue()) {
                File toDelete = new File(d, x);
                LineIterator lit = FileUtils.lineIterator(toDelete);
                while (lit.hasNext()) {
                  bf.write(lit.next());
                }
                toDelete.delete();
              }
              bf.close();
            }
          }
        }

        /*File nodeFile = new File(fileStore.getNodeFilePath());
        LineIterator nodeIterator = FileUtils.lineIterator(nodeFile);

        while (nodeIterator.hasNext()) {
          nodeFileWriter.write(nodeIterator.nextLine() + lineSeparator);
        }

        nodeIterator.close();
        if (!nodeFile.delete()) {
          System.err.println("Error while deleting " + nodeFile);
        }

        File edgeFile = new File(fileStore.getEdgeFilePath());
        LineIterator edgeIterator = FileUtils.lineIterator(edgeFile);

        while (edgeIterator.hasNext()) {
          edgeFileWriter.write(edgeIterator.nextLine() + lineSeparator);
        }

        edgeIterator.close();
        if (!edgeFile.delete()) {
          System.err.println("Error while deleting " + edgeFile);
        }*/
      }

      if (formatter.requiresNodeFinish()) {
        File nodeFile = new File(formatter.getNodeFinishFilePath());
        LineIterator nodeIterator = FileUtils.lineIterator(nodeFile);
        while (nodeIterator.hasNext()) {
          nodeFileWriter.write(nodeIterator.nextLine() + lineSeparator);
        }
      }

      if (formatter.requiresEdgeFinish()) {
        File edgeFile = new File(formatter.getEdgeFinishFilePath());
        LineIterator edgeIterator = FileUtils.lineIterator(edgeFile);
        while (edgeIterator.hasNext()) {
          nodeFileWriter.write(edgeIterator.nextLine() + lineSeparator);
        }
      }

      nodeFileWriter.close();
      edgeFileWriter.close();


    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
