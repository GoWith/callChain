package cn.fireface.call.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 调用节点
 * Created by maoyi on 2018/10/25.
 * don't worry , be happy
 *
 * @author maoyi
 * @date 2023/05/17
 */
public class CallNode {
    /**
     * 关键
     */
    private String key;
    /**
     * 电话信息
     */
    private CallInfo callInfo;
    /**
     * 孩子列表
     */
    private List<CallNode> childList;
    /**
     * 父
     */
    private CallNode parent;

    /**
     * 得到节点味精
     *
     * @return {@link String}
     */
    public String getNodeMsg() {
        return "CallNode{" +
                "key='" + key + '\'' +
                ", callInfo=" + callInfo +
                ", childList=" + childList +
                ", parent=" + parent +
                '}';
    }

    /**
     * 调用节点
     *
     * @param key      关键
     * @param parent   父
     * @param callInfo 电话信息
     */
    public CallNode(String key , CallNode parent , CallInfo callInfo) {
        this.key = key;
        this.parent = parent;
        this.callInfo = callInfo;
        this.childList=new ArrayList<>();
    }

    /**
     * 添加孩子
     *
     * @param child 孩子
     */
    public void addChild(CallNode child){
        childList.add(child);
    }

    /**
     * 得到关键
     *
     * @return {@link String}
     */
    public String getKey() {
        return key;
    }

    /**
     * 设置键
     *
     * @param key 关键
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 得到电话信息
     *
     * @return {@link CallInfo}
     */
    public CallInfo getCallInfo() {
        return callInfo;
    }

    /**
     * 设置电话信息
     *
     * @param callInfo 电话信息
     */
    public void setCallInfo(CallInfo callInfo) {
        this.callInfo = callInfo;
    }

    /**
     * 让孩子列表
     *
     * @return {@link List}<{@link CallNode}>
     */
    public List<CallNode> getChildList() {
        return childList;
    }

    /**
     * 设置子列表
     *
     * @param childList 孩子列表
     */
    public void setChildList(List<CallNode> childList) {
        this.childList = childList;
    }

    /**
     * 得到父母
     *
     * @return {@link CallNode}
     */
    public CallNode getParent() {
        return parent;
    }

    /**
     * 设置父
     *
     * @param parent 父
     */
    public void setParent(CallNode parent) {
        this.parent = parent;
    }
}
