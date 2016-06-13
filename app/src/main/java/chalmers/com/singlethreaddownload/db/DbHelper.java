package chalmers.com.singlethreaddownload.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Chalmers on 2016-06-13 15:23.
 * email:qxinhai@yeah.net
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "download.db";
    public static final int VERSION = 1;

    /**
     *  _id 主键
     *  url 下载地址
     *  start 开始位置
     *  end 结束位置
     *  finished 下载量
     */
    public String CREATE_TABLE_THREAD_INFO = "create table thread_info(_id integer primary key autoincrement, " +
            "url text, start integer, end integer, finished integer)";
    /** 如果表存在，则删除 */
    public String DROP_TABLE_THREAD_INFO = "drop table if exist thread_info";

    public DbHelper(Context context){
        this(context,DB_NAME,null,VERSION);
    }

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_THREAD_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_THREAD_INFO);
        db.execSQL(CREATE_TABLE_THREAD_INFO);
    }
}
