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
 * 清晰过滤策略
 * Created by maoyi on 2018/10/30.
 * don't worry , be happy
 *
 * @author maoyi
 * @date 2023/05/17
 */
public class ClearFilterStrategy implements FilterStrategy{
    /**
     * 执行
     *
     * @param parameters 参数
     * @return {@link String}
     */
    @Override
    public String execute(Map<String, String[]> parameters) {
        LogGraph.clearGraph();
        return "clear success";
    }
}
