package cn.fireface.call.webgraph.filter.strategy;

import cn.fireface.call.core.utils.CallNode;
import cn.fireface.call.core.utils.CallTree;
import cn.fireface.call.core.utils.LogGraph;
import cn.fireface.call.webgraph.filter.TreeView;
import com.alibaba.fastjson.JSON;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by maoyi on 2018/10/30.
 * don't worry , be happy
 */
public class TreeFilterStrategy implements FilterStrategy{
    @Override
    public String execute(Map<String, String[]> parameters) {
        ConcurrentHashMap<String, CallTree> graph = LogGraph.graph;
        String[] keys = parameters.get("key");
        if(null == keys ){
            return null;
        }
        CallTree callTree = graph.get(keys[0]);
        TreeView parse = null;
        if (callTree !=null) {
            parse = parse(callTree.getRoot());
        }
        return JSON.toJSONString(parse);
    }

    private static TreeView parse(CallNode node){
        if(node == null){return null;}
        TreeView view = new TreeView();
        view.setValue(node.getCallInfo().getGapTime());
        String key1 = node.getKey();
        String[] split = key1.split("\\.");
        String key = "["+split[split.length-2]+"."+split[split.length-1]+":"+view.getValue()+"]";
        view.setName(key);
        List<TreeView> treeViews =null;
        if (node.getChildList()!=null) {
            treeViews =  new ArrayList<>();
            for (CallNode callNode : node.getChildList()) {
                treeViews.add(parse(callNode));
            }
        }
        view.setChildren(treeViews);
        return view;
    }
}
