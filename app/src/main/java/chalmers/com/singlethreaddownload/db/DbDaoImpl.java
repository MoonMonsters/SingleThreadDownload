package chalmers.com.singlethreaddownload.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import chalmers.com.singlethreaddownload.bean.ThreadInfo;

/**
 * Created by Chalmers on 2016-06-13 15:38.
 * email:qxinhai@yeah.net
 */
public class DbDaoImpl implements IDbDao{

    private DbHelper dbHelper = null;

    public DbDaoImpl(Context context){
        dbHelper = new DbHelper(context);
    }

    @Override
    public void insert(ThreadInfo threadInfo) {
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("url",threadInfo.getUrl());
        values.put("start",threadInfo.getStart());
        values.put("end",threadInfo.getEnd());
        values.put("finished",threadInfo.getFinished());
        writableDatabase.insert("thread_info",null,values);

        writableDatabase.close();
    }

    @Override
    public void delete() {

    }

    @Override
    public ThreadInfo query(String url) {
        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.query("thread_info", null, "url = ?", new String[]{url}, null, null, null);

        ThreadInfo threadInfo = null;

        if (cursor.moveToNext()) {
            threadInfo = new ThreadInfo();
            threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            threadInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
        }

        cursor.close();
        readableDatabase.close();

        return threadInfo;
    }

    @Override
    public void update(int finished,String url) {
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("finished",finished);
        writableDatabase.update("thread_info",values,"url=?",new String[]{url});

        writableDatabase.close();
    }
}
