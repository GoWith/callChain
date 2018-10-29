package cn.fireface.call.webgraph.filter;

import java.util.List;

/**
 * Created by maoyi on 2018/10/26.
 * don't worry , be happy
 */
public class TreeView {
    private String name;
    private Long value;
    private List<TreeView> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public List<TreeView> getChildren() {
        return children;
    }

    public void setChildren(List<TreeView> children) {
        this.children = children;
    }
}
