package chalmers.com.singlethreaddownload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

import chalmers.com.singlethreaddownload.bean.FileInfo;
import chalmers.com.singlethreaddownload.service.DownloadService;
import chalmers.com.singlethreaddownload.utils.Config;

public class MainActivity extends AppCompatActivity {

    private TextView tv_filename = null;
    private Button btn_start = null;
    private Button btn_stop = null;
    private ProgressBar progressBar = null;

    private FileInfo fileInfo = null;
    private Intent intent = null;

    private static Context context = null;

    private UpdateBroadcast ub = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        initView();
        initListener();
        initData();
    }

    /**
     * 初始化控件信息
     */
    private void initView(){
        tv_filename = (TextView) findViewById(R.id.tv_filename);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    /**
     * 初始化控件监听器
     */
    private void initListener(){
        btn_start.setOnClickListener(onClickListener);
        btn_stop.setOnClickListener(onClickListener);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        fileInfo = new FileInfo();
        intent = new Intent(this, DownloadService.class);
        new GetLengthThread().start();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Config.ACTION_UPDATE);

        ub = new UpdateBroadcast();
        registerReceiver(ub,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(ub);
    }

    /**
     * 得到文件名
     * @return
     */
    private String getFileName(){

        int index = Config.DOWNLOAD_URL.lastIndexOf('/');
        String fileName = Config.DOWNLOAD_URL.substring(index+1);

        return fileName;
    }

    /** 点击事件 */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_start:
                    intent.putExtra(Config.FILEINFO,fileInfo);
                    intent.putExtra(Config.COMMAND,Config.ACTION_START);
                    startService(intent);
//                    Toast.makeText(MainActivity.this,"开始",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_stop:
                    intent.putExtra(Config.COMMAND,Config.ACTION_STOP);
                    startService(intent);
//                    Toast.makeText(MainActivity.this,"暂停",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * 开启子线程，获得文件大小
     */
    class GetLengthThread extends Thread{
        @Override
        public void run() {
            try{
                URL url = new URL(Config.DOWNLOAD_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                int code = httpURLConnection.getResponseCode();
                if(code != 200){
                    Toast.makeText(MainActivity.this,"请求错误"+code,Toast.LENGTH_SHORT).show();

                    return ;
                }
                final int length = httpURLConnection.getContentLength();
                fileInfo.setLength(length);
                fileInfo.setName(getFileName());
                fileInfo.setUrl(Config.DOWNLOAD_URL);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_filename.setText(getFileName());
                        progressBar.setMax(length);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class UpdateBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //如果是更新广播
            if(intent.getAction().equals(Config.ACTION_UPDATE)){
                int totle = intent.getIntExtra(Config.TOTLE,0);
                Log.i("TAG","reciever-->"+totle);
                progressBar.setProgress(totle);
            }
        }
    }

    public static Context getInstance(){

        return context;
    }
}