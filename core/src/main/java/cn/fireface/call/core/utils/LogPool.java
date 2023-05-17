package cn.fireface.call.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maoyi on 2018/10/25.
 * don't worry , be happy
 */
public class LogPool {
    private static ThreadLocal<CallTree> local = new ThreadLocal<>();

    private static List<String> excludes = new ArrayList<>();

    public static void addExcludes(List<String> args ){
        excludes.addAll(args);
    }

    public static void startLog(String key) {
        if(excludes.contains(key)){
            return;
        }
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
        if(excludes.contains(key)){
            return;
        }
        CallTree callTree = local.get();
        CallNode last = callTree.getLast();
        CallInfo callInfo = last.getCallInfo();
        callInfo.setEndTime(System.currentTimeMillis());
        if (last.getParent() != null) {
            callTree.setLast(last.getParent());
        } else {
            refresh();
        }
    }

    private static void refresh() {
        LogGraph.addGraph(local.get());
        local.remove();
    }
}
