package com.blogspot.passovich.arcanoid.Data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper{
    private Context context;
    private static String DB_PATH = "/data/data/com.blogspot.passovich.arcanoid/databases/";
    private static String DB_NAME = "users.db";
    private SQLiteDatabase mDataBase;
    private static final String TAG = "myLogs";
    private Thread thread;
    private Handler handler;

    public DBHelper(Context context, String nameDB) {
        super(context, nameDB, null, 1);
        this.context = context;
    }

    public void createDataBase() throws IOException {
        Log.d(TAG,"DBHelper.onCreateDataBase");
        boolean dbExist = checkDataBase();
        this.getReadableDatabase();
        startRewritingDBThread();
    }
    //проверяет существует ли уже база, чтобы не копировать каждый раз при запуске
    public boolean checkDataBase() {
        Log.d(TAG,"DBHelper.checkDataBase");
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e) {
        }//база ещё не существует
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    public void copyDataBase() throws IOException {
        Log.d(TAG, "copy database");
        //открываем локальную БД как входящий поток
        InputStream myInput = context.getAssets().open(DB_NAME);
        //Путь ко вновь созданнй БД
        String outFileName = DB_PATH + DB_NAME;
        //Открываем пустую базу данных как входящий поток
        OutputStream myOutput = new FileOutputStream(outFileName);
        //перемещаем файлы от входящего файла в исходящий
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        //закрываем потоки
        myOutput.flush(); myOutput.close(); myInput.close();
    }

    void startRewritingDBThread(){ //поток с задержкой для Android 8 и выше
        Log.d(TAG,"DBHelper.startRewritingDBThread");
        thread = new Thread(new Runnable(){
            @Override
            public void run(){
                Log.d(TAG,"DBHelper.startRewritingDBThread - run");
                try {thread.sleep(300);}
                catch (InterruptedException e) {e.printStackTrace();}
                handler.sendEmptyMessage(1);
                try {
                    Log.d (TAG,"Перезаписываем базу");
                    copyDataBase();
                } catch (IOException e) {
                    throw new Error("Error copying database");
                }
            }
        });
        thread.start();
        //инициализируем объект сообщений и получаем сообщение в единицу времени
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {

            }
        };
    }

    public void openDataBase() throws SQLException {
        //Открываем БД
        String myPath = DB_PATH + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }
    @Override
    public synchronized void close() {
        if (mDataBase != null) {
            mDataBase.close();
        }
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade");
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onDowngrade");
    }
}

