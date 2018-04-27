package it.giacomobergami.graph.factories;

import org.gradoop.common.model.impl.pojo.GraphTransaction;

import java.util.function.Supplier;

public class GraphFactory implements Supplier<GraphTransaction> {
    private final IdSupplier is = IdSupplier.instance();
    private final static GraphFactory gf = new GraphFactory();
    private GraphFactory() {}
    public static GraphFactory instance() { return gf; }

    @Override
    public GraphTransaction get() {
        GraphTransaction gt = new GraphTransaction();
        gt.setId(is.get());
        return gt;
    }

    public GraphTransaction createGraphHead() {
        return get();
    }
}
