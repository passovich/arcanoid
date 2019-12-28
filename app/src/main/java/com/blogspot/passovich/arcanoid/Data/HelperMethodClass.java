package com.blogspot.passovich.arcanoid.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import com.blogspot.passovich.arcanoid.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HelperMethodClass {
    private static String TAG = "myLogs";

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private Cursor cursor;
    private ContentValues contentValues;

    public SimpleAdapter getUsers(Context context, String nameDB, String tableName) {
        Log.d(TAG, "helper_method_class getUsers");
        ArrayList<HashMap<String, Object>> sections;
        String sectionName = "Section";
        String myImage = "myImage";
        String selection="name";
        database = getDataBase(context,nameDB);
        cursor = database.query(tableName, null, null, null, null, null, null);

        sections = new ArrayList<HashMap<String, Object>>();    //создаём массив списков
        HashMap<String, Object> hm;                             //элемент массива списков
        boolean flag = true;                                    //признак наличия следующей записи в БД
        int counter = 0;                                        //счётчик записей
        while (flag) {                                          //Заполнение коллекции ИЗ БД
            hm = new HashMap<String, Object>();
            if (cursor.moveToPosition(counter)) {
                hm.put(sectionName, cursor.getString(cursor.getColumnIndex(selection)));
                hm.put(myImage, R.drawable.ball_basket_icon);
                sections.add(hm);
            } else {
                flag = false;
             }
            counter++;
        }
        cursor.close(); dbHelper.close(); database.close();

        SimpleAdapter adapter_image = new SimpleAdapter(
                context,
                sections,
                R.layout.list_users_image,
                new String[]{sectionName, myImage},      //Массив названий
                new int[]{R.id.textView1, R.id.img}      //Массив Форм
        );
        return adapter_image;
    }
    public SimpleAdapter getLeaders (Context context, String nameDB) {
        Log.d(TAG, "helper_method_class getUsers");
        ArrayList<HashMap<String, Object>> sections;
        String [] columns = {"U.name as name","US.scores as scores","US.stage as stage"};
        String fullTable = "users as U inner join users_scores as US on U._id = US.key";

        database = getDataBase(context,nameDB);
        cursor = database.query(fullTable, columns, null, null, null, null, null);
        //Создаём коллекцию с разделами и картинками
        sections = new ArrayList<HashMap<String, Object>>();    //создаём массив списков
        HashMap<String, Object> hm;                             //элемент массива списков
        boolean flag = true;                                    //признак наличия следующей записи в БД
        int counter = 0;
        while (flag) {                                          //Заполнение коллекции ИЗ БД
            hm = new HashMap<String, Object>();
            if (cursor.moveToPosition(counter)) {
                hm.put("section1", cursor.getString(cursor.getColumnIndex("name")));
                hm.put("section2", cursor.getString(cursor.getColumnIndex("scores")));
                hm.put("section3", cursor.getString(cursor.getColumnIndex("stage")));
                hm.put("image", R.drawable.ball_basket_icon);
                sections.add(hm);
            } else {
                flag = false;
            }
            counter++;
        }
        cursor.close(); dbHelper.close(); database.close();

        SimpleAdapter adapter_image = new SimpleAdapter(
                context
                , sections
                , R.layout.list_leaders_image                                       //разметка одного элемента
                , new String[]{"section1","section2","section3","image"}            //Массив названий
                , new int[]{R.id.textView1,R.id.textView2,R.id.textView3, R.id.img} //Массив Форм
        );
        return adapter_image;
    }

    public void getDBOnFirstStart(Context context, String nameDB, int actualDBVersion) {
        Log.d(TAG, "Helper_method_class getDBOnFirstStart");
        String str = File.readFile("firstStart", context);
        Log.d(TAG, "str=" + str);
        if (str.equals("fileNotFound")) {    //Если первый запуск приложения, то создать файл с текущей версией базы и перезаписать базу
            onCreateDBAPK(context, nameDB);
            File.saveFile("firstStart", Integer.toString(actualDBVersion), context);
            Log.d(TAG, "File versionDB not found" + str);
        } else {                             //Иначе проверить версию, если не совпадает с текущей, перезаписать базу
            if (Integer.parseInt(str) != actualDBVersion) {
                onCreateDBAPK(context, nameDB);
                File.saveFile("firstStart", "" + actualDBVersion, context);
            }
        }
    }

    private void onCreateDBAPK(Context context, String nameDB) {
        Log.d(TAG, "Helper_method_class onCreateDBAPK");
        dbHelper = new DBHelper(context, nameDB);
        try {
            dbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create DataBase");
        }
        try {
            dbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        dbHelper.close();
    }

    public void addPlayerToDB(Context context, String nameDB, String nameTableDB, String column, String data) {
        Log.d(TAG, "Helper_method_class addPlayerToDB");
        database = getDataBase(context,nameDB);
        // Заносим данные в таблицу
        cursor = database.query(nameTableDB, null, null, null, null, null, null);
        contentValues = new ContentValues();
        contentValues.put(column, data);
        database.insert(nameTableDB, null, contentValues);
        cursor.close(); dbHelper.close(); database.close();
    }

    public void addLiaderToDB(Context context, String nameDB, String nameTableDB, String [] columns, String[] data) {
        Log.d(TAG, "Helper_method_class addLeaderToDB");
        database = getDataBase(context,nameDB);
        ////////-------ЗАносим данные в таблицу-------/////////
        cursor = database.query(nameTableDB, null, null, null, null, null, null);
        contentValues = new ContentValues();
        contentValues.put(columns[0], data[0]);
        contentValues.put(columns[1], data[1]);
        contentValues.put(columns[2], data[2]);
        database.insert(nameTableDB, null, contentValues);
        cursor.close(); dbHelper.close(); database.close();
    }

    public String searchInColumn(Context context,String nameDB,String nameTableDB,String nameColumn,String selection,String data) {
        Log.d(TAG, "Helper_method_class serachInColumn");
        database = getDataBase(context,nameDB);
        String str = "";
        String [] yo = {data};
        cursor = database.query(nameTableDB, null, selection, yo, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                str = str + " " + cursor.getString(cursor.getColumnIndex(nameColumn));
            } while (cursor.moveToNext());
        } else {
            str = "no_records";
        }
        cursor.close(); dbHelper.close(); database.close();
        return str;
    }
    private SQLiteDatabase getDataBase(Context context,String nameDB){
        SQLiteDatabase database = null;
        //Открываем базу для записи или чтения
        dbHelper = new DBHelper(context, nameDB);
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            database = dbHelper.getReadableDatabase();
        }
        return database;
    }
}
