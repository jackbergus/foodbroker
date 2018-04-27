package it.giacomobergami.graph.factories;

import it.giacomobergami.graph.Properties;
import it.giacomobergami.wrappers.GradoopIdSet;
import org.gradoop.common.model.impl.pojo.Vertex;

import java.util.function.Supplier;

public class VertexFactory implements Supplier<Vertex> {
    private final IdSupplier is = IdSupplier.instance();
    private static final VertexFactory cf = new VertexFactory();
    private VertexFactory() {}
    public static VertexFactory instance() { return cf; }

    @Override
    public Vertex get() {
        Vertex v = new Vertex();
        v.setId(is.get());
        return v;
    }

    public Vertex createVertex(String className, Properties properties) {
        Vertex v = get();
        v.setLabel(className);
        v.setAll(properties);
        return v;
    }

    public Vertex createVertex(String className, Properties properties, GradoopIdSet gis) {
        Vertex v = get();
        v.setLabel(className);
        v.setAll(properties);
        v.setGraphIds(gis);
        return v;
    }
}
