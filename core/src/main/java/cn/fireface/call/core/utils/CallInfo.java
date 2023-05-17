package cn.fireface.call.core.utils;

/**
 * 电话信息
 * Created by maoyi on 2018/10/25.
 * don't worry , be happy
 *
 * @author maoyi
 * @date 2023/05/17
 */
public class CallInfo {
    /**
     * 开始时间
     */
    private long startTime;
    /**
     * 结束时间
     */
    private long endTime;
    /**
     * 持续时间
     */
    private long duration;

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
        this.duration = endTime - this.startTime;
    }

    /**
     * 设置时间
     * 设置时间差距
     *
     * @param duration 时间差距
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * 电话信息
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    public CallInfo(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = endTime - startTime;
    }

    /**
     * 开始时间
     *
     * @return long
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * 得到结束时间
     *
     * @return long
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * 得到时间
     * 有差距时间
     *
     * @return long
     */
    public long getDuration() {
        return duration;
    }
}
