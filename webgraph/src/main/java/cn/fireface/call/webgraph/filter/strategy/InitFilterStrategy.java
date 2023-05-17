package cn.fireface.call.webgraph.filter.strategy;

import cn.fireface.call.core.utils.CallTree;
import cn.fireface.call.core.utils.LogGraph;
import com.alibaba.fastjson.JSON;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * init过滤策略
 * Created by maoyi on 2018/10/30.
 * don't worry , be happy
 *
 * @author maoyi
 * @date 2023/05/17
 */
public class InitFilterStrategy implements FilterStrategy{

    /**
     * 执行
     *
     * @param parameters 参数
     * @return {@link String}
     */
    @Override
    public String execute(Map<String, String[]> parameters) {
        ConcurrentHashMap<String, CallTree> graph = LogGraph.graph;
        Set<String> keySet = graph.keySet();
//        String[] keyA = new
        String[] keyArr = keySet.toArray(new String[0]);
        Arrays.sort(keyArr);
        return JSON.toJSONString(keyArr);
    }
}
