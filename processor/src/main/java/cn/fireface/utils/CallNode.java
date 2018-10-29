package cn.fireface.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maoyi on 2018/10/25.
 * don't worry , be happy
 */
public class CallNode {
    private String key;
    private CallInfo callInfo;
    private List<CallNode> childList;
    private CallNode parent;

    public String getNodeMsg() {
        return "CallNode{" +
                "key='" + key + '\'' +
                ", callInfo=" + callInfo +
                ", childList=" + childList +
                ", parent=" + parent +
                '}';
    }

    public CallNode(String key , CallNode parent , CallInfo callInfo) {
        this.key = key;
        this.parent = parent;
        this.callInfo = callInfo;
        this.childList=new ArrayList<>();
    }

    public void addChild(CallNode child){
        childList.add(child);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public CallInfo getCallInfo() {
        return callInfo;
    }

    public void setCallInfo(CallInfo callInfo) {
        this.callInfo = callInfo;
    }

    public List<CallNode> getChildList() {
        return childList;
    }

    public void setChildList(List<CallNode> childList) {
        this.childList = childList;
    }

    public CallNode getParent() {
        return parent;
    }

    public void setParent(CallNode parent) {
        this.parent = parent;
    }
}
