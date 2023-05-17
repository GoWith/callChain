package cn.fireface.call.webgraph.filter;

import java.util.List;

/**
 * 树视图
 * Created by maoyi on 2018/10/26.
 * don't worry , be happy
 *
 * @author maoyi
 * @date 2023/05/17
 */
public class TreeView {
    /**
     * 名字
     */
    private String name;
    /**
     * 价值
     */
    private Long value;
    /**
     * 孩子们
     */
    private List<TreeView> children;

    /**
     * 得到名字
     *
     * @return {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * 集名称
     *
     * @param name 名字
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获得价值
     *
     * @return {@link Long}
     */
    public Long getValue() {
        return value;
    }

    /**
     * 设置值
     *
     * @param value 价值
     */
    public void setValue(Long value) {
        this.value = value;
    }

    /**
     * 让孩子
     *
     * @return {@link List}<{@link TreeView}>
     */
    public List<TreeView> getChildren() {
        return children;
    }

    /**
     * 组孩子
     *
     * @param children 孩子们
     */
    public void setChildren(List<TreeView> children) {
        this.children = children;
    }
}
