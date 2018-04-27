package it.giacomobergami.graph.factories;

import it.giacomobergami.graph.Properties;
import it.giacomobergami.wrappers.GradoopId;
import it.giacomobergami.wrappers.GradoopIdSet;
import org.gradoop.common.model.impl.pojo.Edge;

import java.util.function.Supplier;

public class EdgeFactory implements Supplier<Edge> {
    private final IdSupplier is = IdSupplier.instance();
    private static final EdgeFactory cf = new EdgeFactory();
    private EdgeFactory() {}
    public static EdgeFactory instance() { return cf; }

    @Override
    public Edge get() {
        throw new RuntimeException("Edge Factory cannot invoke get");
    }

    public Edge createEdge(String label, GradoopId source, GradoopId target, GradoopIdSet graphIds) {
        Edge e = new Edge(source, target, graphIds);
        e.setId(is.get());
        e.setLabel(label);
        return e;
    }

    public Edge createEdge(String label, GradoopId source, GradoopId target, Properties props, GradoopIdSet graphIds) {
        Edge e = new Edge(source, target, graphIds);
        e.setAll(props);
        e.setId(is.get());
        e.setLabel(label);
        return e;
    }
}
