package org.gradoop.common.model.impl.pojo;

import it.giacomobergami.graph.Properties;
import it.giacomobergami.wrappers.GradoopId;
import it.giacomobergami.wrappers.GradoopIdSet;

public class Vertex {
    private Properties map;
    private String label;
    private GradoopId id;
    private GradoopIdSet graphs;

    public Vertex() {
        map = new Properties();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public GradoopId getId() {
        return this.id;
    }

    public void setId(GradoopId id) {
        this.id = id;
    }

    public void setAll(Properties all) {
        this.map.putAll(all);
    }

    public Object getPropertyValue(String key) {
        return this.map.get(key);
    }

    public void setProperty(String branchnumberKey, Object branchNumber) {
        this.map.set(branchnumberKey, branchNumber);
    }

    public void setGraphIds(GradoopIdSet graphIds) {
        this.graphs = graphIds;
    }

    public GradoopIdSet getGraphIds() {
        return graphs;
    }

    public Properties getProperties() {
        return map;
    }
}
