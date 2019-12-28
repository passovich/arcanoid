package com.blogspot.passovich.arcanoid.Data;

import android.content.Context;
import android.content.SharedPreferences;

import com.blogspot.passovich.arcanoid.Data.File;

public class XMLPreferences {


    public static void onCreateXMLFile (Context context, String fileName){
        //MODE_PRIVATE - создать файл или пересоздать, если он есть
        SharedPreferences sPref = context.getSharedPreferences(fileName,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.apply();
    }
    public static void onSaveXMLData (Context context, String fileName,String nameKey, String data){
        //MODE_APPEND - Открыть файл для работы, или создать новый, если такового нету
        SharedPreferences sPref = context.getSharedPreferences(fileName,context.MODE_APPEND);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(nameKey,data);
        editor.apply();
    }
    public static String onReadXMLData (Context context, String fileName,String nameKey){
        SharedPreferences sPref = context.getSharedPreferences(fileName,context.MODE_APPEND);
        return sPref.getString(nameKey,"");
    }
    public static void onDeleteXMLData(Context context,String fileName, String nameKey){
        SharedPreferences sPref = context.getSharedPreferences(fileName,context.MODE_APPEND);
        SharedPreferences.Editor editor = sPref.edit();
        editor.remove(nameKey);
        editor.apply();
    }
    public static void onDeleteXMLFile (Context context,String fileName){
        String filePath = context.getApplicationContext().getFilesDir().getParent()+"/shared_prefs/"+fileName+".xml";
        File deletePrefFile = new File();
        deletePrefFile.deleteFile(filePath,context);
    }
}
