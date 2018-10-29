package cn.fireface.utils;

/**
 * Created by maoyi on 2018/10/25.
 * don't worry , be happy
 */
public class CallTree{
    private CallNode root;
    private CallNode last;

    public CallNode getRoot() {
        return root;
    }

    public void setRoot(CallNode root) {
        this.root = root;
    }

    public CallNode getLast() {
        return last;
    }

    public void setLast(CallNode last) {
        this.last = last;
    }
}
