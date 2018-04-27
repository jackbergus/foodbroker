package it.giacomobergami.iterator;

import java.util.Iterator;
import java.util.function.Function;

public class StreamableIterator<T> implements Iterator<T> {

    private final Iterator<T> it;

    public StreamableIterator(Iterator<T> it) {
        this.it = it;
    }

    public <K> StreamableIterator<K> map(Function<T, K> function) {
        return new IteratorMap<T, K>(this) {
            @Override
            public K apply(T t) {
                return function.apply(t);
            }
        };
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public T next() {
        return it.next();
    }
}
