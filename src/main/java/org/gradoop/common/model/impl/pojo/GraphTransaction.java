package org.gradoop.common.model.impl.pojo;

import it.giacomobergami.wrappers.GradoopId;

import java.util.Set;

public class GraphTransaction extends GraphHead {
    private Set<Vertex> vertices;
    private Set<Edge> edges;

    public void setVertices(Set<Vertex> vertices) {
        this.vertices = vertices;
    }

    public void setEdges(Set<Edge> edges) {
        this.edges = edges;
    }

    public GraphTransaction getGraphHead() {
        return this;
    }

    public Set<Vertex> getVertices() {
        return vertices;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public Vertex getVertexById(GradoopId customerId) {
        for (Vertex v : vertices) {
            if (v.getId().equals(customerId)) return v;
        }
        return null;
    }
}
