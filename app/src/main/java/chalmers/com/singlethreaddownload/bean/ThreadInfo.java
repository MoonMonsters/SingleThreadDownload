package chalmers.com.singlethreaddownload.bean;

import java.io.Serializable;

/**
 * Created by Chalmers on 2016-06-13 15:35.
 * email:qxinhai@yeah.net
 */
public class ThreadInfo implements Serializable{
    /** 下载链接 */
    private String url;
    /** 开始位置 */
    private int start;
    /** 结束位置 */
    private int end;
    /** 已经下载量 */
    private int finished;

    public ThreadInfo(String url, int start, int end, int finished) {
        this.url = url;
        this.start = start;
        this.end = end;
        this.finished = finished;
    }
    public ThreadInfo(){}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }
}
