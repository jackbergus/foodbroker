package it.giacomobergami.graph.factories;

import it.giacomobergami.wrappers.GradoopId;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class IdSupplier implements Supplier<GradoopId> {
    private final AtomicLong ai = new AtomicLong(0);
    private IdSupplier() { }
    private static IdSupplier vf = new IdSupplier();
    public static IdSupplier instance() { return vf; }

    @Override
    public GradoopId get() {
        return new GradoopId(ai.getAndIncrement());
    }
}
