package it.giacomobergami.init;

import it.giacomobergami.iterator.StreamableIterator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.gradoop.common.model.impl.pojo.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ExecutionEnvironment {
    private HashMap<String,List<String>> files;
    private HashMap<String,List<Tuple2<String, String>>> filesTuple2;
    private HashMap<String,List<Vertex>> filesVertices;

    private static List<String> emptyList = new ArrayList<>();
    private static List<Tuple2<String, String>> emptyList2 = new ArrayList<>();
    private static List<Vertex> emptyList3 = new ArrayList<>();

    private static ExecutionEnvironment ee = new ExecutionEnvironment();

    private ExecutionEnvironment() {
        files = new HashMap<>();
        filesTuple2 = new HashMap<>();
        filesVertices = new HashMap<>();
    }
    public static ExecutionEnvironment instance() {
        return ee;
    }

    public List<Tuple2<String, String>> getBroadcastVariable2(String adjectivesBc) {
        return filesTuple2.getOrDefault(adjectivesBc, emptyList2);
    }

    public List<Vertex> getBroadcastVariable3(String adjectivesBc) {
        return filesVertices.getOrDefault(adjectivesBc, emptyList3);
    }

    public <T> List<T> fromCollection(List<T> adjectives) {
        return adjectives;
    }

    public <T> StreamableIterator<T> streamAsIterator(List<T> adjectives) {
        return new StreamableIterator<T>(adjectives.iterator());
    }

    public ExecutionEnvironment withBroadcastSet2(List<Tuple2<String, String>> ls, String key) {
        filesTuple2.put(key, ls);
        return this;
    }

    public ExecutionEnvironment withBroadcastSet(Iterator<Vertex> strings, String adjectivesBc) {
        List<Vertex> toadd = new ArrayList<>();
        strings.forEachRemaining(toadd::add);
        filesVertices.put(adjectivesBc, toadd);
        return this;
    }

    public ExecutionEnvironment withBroadcastSet(List<String> strings, String adjectivesBc) {
        files.put(adjectivesBc, strings);
        return this;
    }

    public List<Long> generateSequence(int i, int caseCount) {
        List<Long> elements = new ArrayList<>(caseCount);
        for (Long j = 0L; j<caseCount; j++) {
            elements.add(j);
        }
        return elements;
    }

    public List<String> getBroadcastVariable(String adjectivesBc) {
        return files.getOrDefault(adjectivesBc, emptyList);
    }
}
