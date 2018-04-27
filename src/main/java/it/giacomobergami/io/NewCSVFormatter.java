package it.giacomobergami.io;

import org.apache.flink.core.fs.Path;
import it.giacomobergami.wrappers.GradoopId;
import it.giacomobergami.wrappers.GradoopIdSet;
import org.gradoop.common.model.impl.pojo.Edge;
import org.gradoop.common.model.impl.pojo.Vertex;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Giacomo Bergami
 */
public class NewCSVFormatter  {
    private final DateTimeFormatter dateFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final String separator;
    private final String directory;

    public NewCSVFormatter(String directory, String separator) {
        this.separator = separator;
        this.directory = directory;
        if (!directory.endsWith(Path.SEPARATOR)) {
            directory += Path.SEPARATOR;
        }
    }

    public String getFileExtension() {
        return ".csv";
    }

    private static Iterator<GradoopId> emptyIterator = new Iterator<GradoopId>() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public GradoopId next() {
            return null;
        }
    };

    private static String toVector(GradoopIdSet set) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Iterator<GradoopId> it = set == null ? emptyIterator : set.iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
            if (it.hasNext()) sb.append(',');
        }
        return sb.append(']').toString();
    }


    public String format(Vertex vertex) {
        StringBuilder sb = new StringBuilder();
        return endFormat(vertex, startFormat(vertex, sb));
    }

    public String format(Edge dataObject) {
        StringBuilder sb = new StringBuilder();
        sb = startFormat(dataObject, sb)
                .append(separator)
                .append(dataObject.src)
                .append(separator)
                .append(dataObject.dst);
        return endFormat(dataObject, sb);
    }

    private StringBuilder startFormat(Vertex header, StringBuilder sb) {
        return sb.append(header.getId())
                .append(separator)
                .append(header.getLabel());
    }

    private String endFormat(Vertex header, StringBuilder sb) {
        sb
                .append(separator)
                .append(toVector(header.getGraphIds()));
        Iterator<Map.Entry<String, Object>> prop = header.getProperties().entrySet().iterator();
        while (prop.hasNext()) {
            Map.Entry<String, Object> x = prop.next();
            String key = x.getKey();
            String value;
            if (x.getValue() instanceof LocalDate) {
                value = ((LocalDate) x.getValue()).format(dateFormatter2);
            } else {
                value = x.getValue().toString();
            }
            sb.append(separator).append(key + ":" + value);
        }
        return sb.toString();
    }

    public String getDirectory() {
        return directory;
    }
}
