package com.blogspot.passovich.arcanoid.Data;

import android.content.Context;
import android.util.Log;

import com.blogspot.passovich.arcanoid.Elements.Bricks;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class File {
    private static String TAG="myLogs";

    public File(){

    }
    public void createFile (String fileName, Context context){
        Log.d(TAG,"Creating file " + fileName);
        try{
            FileOutputStream outputStream = context.openFileOutput(fileName, context.MODE_PRIVATE);
            outputStream.close();
        }catch (Exception e){e.printStackTrace();}
    }
    public static void saveFile (String fileName, String information, Context context){
        Log.d(TAG,"Writing file " + fileName);
        try{
            FileOutputStream outputStream = context.openFileOutput(fileName,context.MODE_PRIVATE);
            outputStream.write(information.getBytes());
            outputStream.close();
        }catch (Exception e){e.printStackTrace();}
    }
    public static String readFile (String fileName, Context context){
        String ret="";
        try{
            InputStream inputStream = context.openFileInput(fileName);
            if (inputStream!=null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String receiveString = "";
                while((receiveString=bufferedReader.readLine())!=null){
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }catch (FileNotFoundException e){
            Log.e(TAG,"File not found" + e.toString());
            ret="fileNotFound";
        }catch (IOException e){
            Log.e(TAG,"Can't read file" + e.toString());
        }
        Log.d(TAG,"Reading file " + fileName);
        Log.e(TAG,"ret = " + ret);
        return ret;
    }
    void deleteFile (String fileName, Context context){
        Log.d(TAG,"deleting file"+fileName);
        context.deleteFile(fileName);
    }
    public static String stageToString(Bricks bricks){
        //подготовка строки состояния кирпичей для сохранения текущего уровня в файл
        String stringMatrix = "";
        for (int i = 0; i < bricks.bricksMatrix.length; i++){
            for (int j = 0; j < bricks.bricksMatrix[i].length; j++){
                stringMatrix = stringMatrix+Integer.toString(bricks.bricksMatrix[i][j].type);
                stringMatrix = stringMatrix+Integer.toString(bricks.bricksMatrix[i][j].lives);
            }
        }
        return stringMatrix;
    }
    public static void stageFromString(String stringMatrix, Bricks bricks){
        int counter = 0;
        char ch;
        for (int i = 0; i < bricks.bricksMatrix.length; i++){
            for (int j = 0; j < bricks.bricksMatrix[i].length; j++){
                ch = stringMatrix.charAt(counter);
                bricks.bricksMatrix[i][j].type = Character.getNumericValue(ch);
                counter++;
                ch = stringMatrix.charAt(counter);
                bricks.bricksMatrix[i][j].lives = Character.getNumericValue(ch);
                counter++;
            }
        }
        Log.d(TAG,"matrix getted from string = " + stringMatrix);
    }
    public static void saveStageToQuickSave(Bricks bricks, Context context, String playerName){
        Log.d(TAG,"SaveStageToQuickSave");
        saveFile("QS_" + playerName, stageToString(bricks), context);
    }
    public void loadStageFromQuickSave(Bricks bricks, Context context, String playerName){
        Log.d(TAG,"LoadStageFromQuickSave");
        stageFromString(readFile("QS_" + playerName, context), bricks);
    }
}
