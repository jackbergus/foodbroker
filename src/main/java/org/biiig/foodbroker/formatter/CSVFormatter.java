package org.biiig.foodbroker.formatter;

import org.biiig.foodbroker.model.DataObject;
import org.biiig.foodbroker.model.PropertyContainer;
import org.biiig.foodbroker.model.Relationship;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by peet on 18.11.14.
 */
public class CSVFormatter extends AbstractFormatter {

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyy-MM-dd");
    private final String separator;
    public CSVFormatter(String directory, String separator) {
        super(directory);
        this.separator = separator;
    }

    @Override
    public String getFileExtension() {
        return ".csv";
    }

    public String format (DataObject dataObject){
        StringBuilder sb = new StringBuilder();
        sb.append(dataObject.getID());
        getProperties(dataObject, sb);
        getMetaData(dataObject, sb);
        return sb.toString();
    }

    @Override
    public String format(Relationship relationship) {
        StringBuilder sb = new StringBuilder();
        sb.append(relationship.getID())
                .append(separator)
                .append(relationship.getStartDataObject().getID())
                .append(separator)
                .append(relationship.getEndDataObject().getID());
        getProperties(relationship, sb);
        getMetaData(relationship, sb);
        return sb.toString();
    }

    private void getProperties(PropertyContainer propertyContainer, StringBuilder sb){
        //JSONObject json = new JSONObject();
        sb.append(separator);
        for (Iterator<String> iterator = propertyContainer.getPropertyKeys().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            Object value = propertyContainer.getProperty(key);

            if (value instanceof Date) {
                value = dateFormatter.format((Date) value);
            }
            sb.append(value);
            if (iterator.hasNext()) sb.append(separator);
        }
    }

    private void getMetaData(PropertyContainer propertyContainer, StringBuilder sb){
        sb.append(separator);
        for (Iterator<String> iterator = propertyContainer.getMetaDataKeys().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            String value = propertyContainer.getMetaData(key);
            sb.append(value);
            if (iterator.hasNext()) sb.append(separator);
        }
    }

    protected String getExtension(){
        return ".csv";
    }

    @Override
    public boolean hasSeparateRelationshipHandling() {
        return true;
    }

    @Override
    public String getNodeOpeningFilePath() {
        return null;
    }

    @Override
    public String getNodeFinishFilePath() {
        return null;
    }

    @Override
    public String getEdgeOpeningFilePath() {
        return null;
    }

    @Override
    public String getEdgeFinishFilePath() {
        return null;
    }

    @Override
    public boolean requiresNodeOpening() {
        return false;
    }

    @Override
    public boolean requiresNodeFinish() {
        return false;
    }

    @Override
    public boolean requiresEdgeOpening() {
        return false;
    }

    @Override
    public boolean requiresEdgeFinish() {
        return false;
    }
}
