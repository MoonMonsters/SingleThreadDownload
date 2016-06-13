package chalmers.com.singlethreaddownload.db;

import chalmers.com.singlethreaddownload.bean.ThreadInfo;

/**
 * Created by Chalmers on 2016-06-13 15:32.
 * email:qxinhai@yeah.net
 */
public interface IDbDao {
    /** 插入 */
    void insert(ThreadInfo threadInfo);
    /** 删除 */
    void delete();
    /** 查询 */
    ThreadInfo query(String url);
    /** 更新 */
    void update(int finished,String url);
}
