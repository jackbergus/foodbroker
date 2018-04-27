package org.gradoop.common.model.impl.id;

import java.util.Objects;

public class GradoopId implements Comparable<GradoopId> {
    private final long value;
    public GradoopId(long value) {
        this.value = value;
    }

    @Override
    public int compareTo(GradoopId o) {
        return Long.compare(value, o.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradoopId gradoopId = (GradoopId) o;
        return value == gradoopId.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
