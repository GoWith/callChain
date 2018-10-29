package cn.fireface.call.core.utils;

/**
 * Created by maoyi on 2018/10/25.
 * don't worry , be happy
 */
public class CallInfo {
    private long startTime;
    private long endTime;
    private long gapTime;

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
        this.gapTime = endTime - this.startTime;
    }

    public void setGapTime(long gapTime) {
        this.gapTime = gapTime;
    }

    public CallInfo(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.gapTime = endTime - startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getGapTime() {
        return gapTime;
    }
}
