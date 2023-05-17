package cn.fireface.call.core.utils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 测井图
 * Created by maoyi on 2018/10/25.
 * don't worry , be happy
 *
 * @author maoyi
 * @date 2023/05/17
 */
public class LogGraph {
    /**
     * 图
     */
    static public ConcurrentHashMap<String,CallTree> graph = new ConcurrentHashMap<>();

    /**
     * 添加图
     *
     * @param tCallTree t调用树
     */
    public static void addGraph(CallTree tCallTree){
        String key = tCallTree.getRoot().getKey();
        graph.putIfAbsent(key, tCallTree);
        CallTree tree = graph.get(key);
        if(tCallTree.getRoot().getCallInfo().getDuration() > tree.getRoot().getCallInfo().getDuration()){
            graph.put(key,tCallTree);
        }
    }

    /**
     * 清晰图
     */
    public static void clearGraph(){
        graph.clear();
    }

    /**
     * 清晰图,关键
     *
     * @param key 关键
     */
    public static void clearGraphByKey(String key){
        graph.remove(key);
    }
}
