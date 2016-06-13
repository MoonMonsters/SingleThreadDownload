package chalmers.com.singlethreaddownload.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import chalmers.com.singlethreaddownload.bean.FileInfo;
import chalmers.com.singlethreaddownload.bean.ThreadInfo;
import chalmers.com.singlethreaddownload.db.DbDaoImpl;
import chalmers.com.singlethreaddownload.utils.Config;
import chalmers.com.singlethreaddownload.utils.DownloadTask;

public class DownloadService extends Service {

    private DownloadTask downloadTask = null;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(downloadTask == null){
            downloadTask = new DownloadTask(this);
        }
        String action = intent.getStringExtra(Config.COMMAND);

        if(action.equals(Config.ACTION_START)){
            //获得MainActivity传递的FileInfo对象
            final FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(Config.FILEINFO);
            //设置可以下载
            downloadTask.setDownloading(true);
            //下载
            new Thread(new Runnable() {
                @Override
                public void run() {
                    downloadTask.download(getThreadInfo(fileInfo));
                }
            }).start();
        } else if (action.equals(Config.ACTION_STOP)) {
            downloadTask.setDownloading(false);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("SERVICE","onDestroy");
    }

    public ThreadInfo getThreadInfo(FileInfo fileInfo){
        DbDaoImpl dao = new DbDaoImpl(this);

        ThreadInfo threadInfo = dao.query(fileInfo.getUrl());
        if(threadInfo == null){
            threadInfo = new ThreadInfo();
            threadInfo.setUrl(fileInfo.getUrl());
            threadInfo.setStart(0);
            threadInfo.setFinished(0);
            threadInfo.setEnd(fileInfo.getLength());

            dao.insert(threadInfo);
        }
        return threadInfo;
    }
}
