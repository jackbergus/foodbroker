package it.giacomobergami.wrappers;

import java.util.HashSet;

public class GradoopIdSet extends HashSet<GradoopId> {
    public static GradoopIdSet fromExisting(GradoopId f1) {
        GradoopIdSet gis = new GradoopIdSet();
        gis.add(f1);
        return gis;
    }
}
