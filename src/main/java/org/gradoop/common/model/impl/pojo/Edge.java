package org.gradoop.common.model.impl.pojo;

import it.giacomobergami.wrappers.GradoopId;
import it.giacomobergami.wrappers.GradoopIdSet;

public class Edge extends Vertex {
    public final GradoopId src;
    public final GradoopId dst;
    public GradoopIdSet graphId;

    public Edge(GradoopId src, GradoopId dst, GradoopIdSet graphId) {
        this.src = src;
        this.dst = dst;
        this.graphId = graphId;
    }
}
