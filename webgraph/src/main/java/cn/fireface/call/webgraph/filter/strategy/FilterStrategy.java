package cn.fireface.call.webgraph.filter.strategy;

import java.util.Map;

/**
 * 过滤策略
 * Created by maoyi on 2018/10/30.
 * don't worry , be happy
 *
 * @author maoyi
 * @date 2023/05/17
 */
public interface FilterStrategy {
    /**
     * 执行
     *
     * @param parameters 参数
     * @return {@link String}
     */
    String execute(Map<String,String[]> parameters);
}
