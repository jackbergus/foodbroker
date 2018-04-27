package org.biiig.foodbroker.stores;

import org.biiig.foodbroker.formatter.Formatter;

/**
 * Created by peet on 27.11.14.
 */
public abstract class AbstractStore implements Store {
    protected Formatter formatter;
    @Override
    public void open() {}
    @Override
    public void close() {}
    public Formatter getFormatter() {
        return formatter;
    }
}
