package it.giacomobergami.graph;

import java.util.HashMap;
import java.util.Objects;

public class Properties extends HashMap<String,Object> {
    public void set(String s, Object x) {
        put(s, x);
    }
}
