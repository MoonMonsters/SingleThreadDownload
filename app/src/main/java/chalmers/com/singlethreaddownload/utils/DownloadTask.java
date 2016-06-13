package chalmers.com.singlethreaddownload.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import chalmers.com.singlethreaddownload.MainActivity;
import chalmers.com.singlethreaddownload.bean.ThreadInfo;
import chalmers.com.singlethreaddownload.db.DbDaoImpl;

/**
 * Created by Chalmers on 2016-06-13 15:37.
 * email:qxinhai@yeah.net
 */
public class DownloadTask {
    private boolean isDownloading;
    /** 下载保存路径 */
    private String path = Environment.getExternalStorageDirectory().toString() + File.separatorChar
            + "downloads" + File.separatorChar;
    private int totle = 0;

    private Context context;
    public DownloadTask(Context context){
        this.context = context;
    }

    public void download(ThreadInfo threadInfo){
        try{
            URL url = new URL(threadInfo.getUrl());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            int start = threadInfo.getStart();
            int end = threadInfo.getEnd();
            int finished = threadInfo.getFinished();
            totle = start + finished;

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setRequestProperty("range","bytes="+totle+"-"+end);
            int code = httpURLConnection.getResponseCode();
            if(code == 200){
                return ;
            }

            InputStream inputStream = httpURLConnection.getInputStream();

            File file = new File(path);
            if(!file.exists()){
                file.mkdir();
            }
            File file2 = new File(file,"imooc.apk");
            RandomAccessFile raf = new RandomAccessFile(file2,"rwd");
            raf.setLength(threadInfo.getEnd());
            raf.seek(totle);
            byte[] buf = new byte[1024];
            int size = -1;
            long time = System.currentTimeMillis();
            while((size = inputStream.read(buf)) != -1){
                totle += size;
                raf.write(buf,0,size);

                if(System.currentTimeMillis() - time >= 500){
                    time = System.currentTimeMillis();
                    sendUpdateBroadcast();
                }
                if(!isDownloading){
                    break;
                }
            }
            DbDaoImpl dao = new DbDaoImpl(context);
            dao.update(totle,threadInfo.getUrl());
            sendUpdateBroadcast();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendUpdateBroadcast(){
        Intent intent = new Intent();
        intent.setAction(Config.ACTION_UPDATE);
        intent.putExtra(Config.TOTLE,totle);
        Log.i("TAG","totle = " + totle);
        MainActivity.getInstance().sendBroadcast(intent);
    }

    public void setDownloading(boolean flag){
        isDownloading = flag;
    }

    public boolean getIsDownloading(){
        return isDownloading;
    }
}
