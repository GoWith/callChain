package cn.fireface.utils;

/**
 * Created by maoyi on 2018/10/25.
 * don't worry , be happy
 */
public class LogPool {
    private static ThreadLocal<CallTree> local = new ThreadLocal<>();

    public static void startLog(String key) {
        System.out.println("start:" + key);
        CallTree callTree = local.get();
        if (null == callTree) {
            callTree = new CallTree();
            local.set(callTree);
        }

        CallInfo callInfo = new CallInfo(System.currentTimeMillis(), System.currentTimeMillis());
        CallNode callNode = new CallNode(key, callTree.getLast(), callInfo);
        if (callTree.getLast() != null) {
            callTree.getLast().addChild(callNode);
        }
        callTree.setLast(callNode);
        if (callTree.getRoot() == null) {
            callTree.setRoot(callNode);
        }
    }

    public static void endLog(String key) {
        System.out.println("end:" + key);
        CallTree callTree = local.get();
        CallNode last = callTree.getLast();
        CallInfo callInfo = last.getCallInfo();
        callInfo.setEndTime(System.currentTimeMillis());
        if (last.getParent() != null)
            callTree.setLast(last.getParent());
        else refresh();
    }

    private static void refresh() {
        LogGraph.addGragh(local.get());
        local.remove();
    }
}
