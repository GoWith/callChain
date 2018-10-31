package cn.fireface.call.webgraph.filter.strategy;

import cn.fireface.call.core.utils.CallTree;
import cn.fireface.call.core.utils.LogGraph;
import com.alibaba.fastjson.JSON;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by maoyi on 2018/10/30.
 * don't worry , be happy
 */
public class InitFilterStrategy implements FilterStrategy{

    @Override
    public String execute(Map<String, String[]> parameters) {
        ConcurrentHashMap<String, CallTree> graph = LogGraph.graph;
        Set<String> keySet = graph.keySet();
//        String[] keyA = new
        String[] keyArr = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArr);
        return JSON.toJSONString(keyArr);
    }
}
