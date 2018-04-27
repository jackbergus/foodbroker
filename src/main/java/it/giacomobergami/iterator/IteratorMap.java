package it.giacomobergami.iterator;

import java.util.Iterator;
import java.util.function.Function;

public abstract class IteratorMap<S,T> extends StreamableIterator<T> implements Function<S, T> {
    private final Iterator<S> iterator;
    public IteratorMap(Iterator<S> iterator) {
        super(null);
        this.iterator = iterator;
    }
    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }
    @Override
    public T next() {
        return apply(this.iterator.next());
    }
}
