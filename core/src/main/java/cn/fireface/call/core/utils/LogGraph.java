package cn.fireface.call.core.utils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by maoyi on 2018/10/25.
 * don't worry , be happy
 */
public class LogGraph {
    static public ConcurrentHashMap<String,CallTree> graph = new ConcurrentHashMap<>();

    public static void addGragh(CallTree tCallTree){
        String key = tCallTree.getRoot().getKey();
        graph.putIfAbsent(key, tCallTree);
    }
}
