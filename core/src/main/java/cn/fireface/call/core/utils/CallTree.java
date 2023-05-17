package cn.fireface.call.core.utils;

/**
 * 调用树
 * Created by maoyi on 2018/10/25.
 * don't worry , be happy
 *
 * @author maoyi
 * @date 2023/05/17
 */
public class CallTree{
    /**
     * 根
     */
    private CallNode root;
    /**
     * 去年
     */
    private CallNode last;

    /**
     * 得到根
     *
     * @return {@link CallNode}
     */
    public CallNode getRoot() {
        return root;
    }

    /**
     * 设置根
     *
     * @param root 根
     */
    public void setRoot(CallNode root) {
        this.root = root;
    }

    /**
     * 得到最后一个
     *
     * @return {@link CallNode}
     */
    public CallNode getLast() {
        return last;
    }

    /**
     * 去年
     *
     * @param last 去年
     */
    public void setLast(CallNode last) {
        this.last = last;
    }
}
