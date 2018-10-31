package cn.fireface.call.webgraph.filter.strategy;

import java.util.Map;

/**
 * Created by maoyi on 2018/10/30.
 * don't worry , be happy
 */
public interface FilterStrategy {
    String execute(Map<String,String[]> parameters);
}
