package org.biiig.foodbroker.formatter;

/**
 * Created by peet on 27.11.14.
 */
public class CSVFactory extends AbstractFormatterFactory {

    @Override
    public Formatter newInstance(String directory) {
        return new CSVFormatter(directory, ",");
    }
}
