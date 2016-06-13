package chalmers.com.singlethreaddownload.bean;

import java.io.Serializable;

/**
 * Created by Chalmers on 2016-06-13 15:33.
 * email:qxinhai@yeah.net
 */
public class FileInfo implements Serializable{
    /** 下载地址 */
    private String url;
    /** 文件名 */
    private String name;
    /** 问件大小 */
    private int length;

    public FileInfo(String url, String name, int length) {
        this.url = url;
        this.name = name;
        this.length = length;
    }

    public FileInfo(){}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
