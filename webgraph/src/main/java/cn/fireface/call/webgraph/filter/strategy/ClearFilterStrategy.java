package cn.fireface.call.webgraph.filter.strategy;

import cn.fireface.call.core.utils.CallNode;
import cn.fireface.call.core.utils.CallTree;
import cn.fireface.call.core.utils.LogGraph;
import cn.fireface.call.webgraph.filter.TreeView;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by maoyi on 2018/10/30.
 * don't worry , be happy
 */
public class ClearFilterStrategy implements FilterStrategy{
    @Override
    public String execute(Map<String, String[]> parameters) {
        LogGraph.clearGraph();
        return "clear success";
    }
}
