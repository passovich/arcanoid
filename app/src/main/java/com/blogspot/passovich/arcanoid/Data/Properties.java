package com.blogspot.passovich.arcanoid.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.blogspot.passovich.arcanoid.Data.XMLPreferences;

public class Properties {
    private Context context;
    private final String TAG="myLogs";
//**************для канваса***********************
    public static int       ballDelayTimer=1;               //Частота обновления шарика (мСек)
    public static int       effectsDelayTimer=1;            //Частота обновления Canvas (мСек)
//**************************************************

    //настройки текущие системные
    public static float     SCREEN_WIDTH;
    public static float     SCREEN_HEIGHT;
    public static float     STATUSBAR_HEIGHT;               //Высота статусар в пикселях
    public static int       STATUSBAR_FONT_SIZE;            //Размер текста в статусбаре
    public static float     STATUSBAR_SIZE_HEIGHT = 16;     //Относительная высота статусбар(по ширине экрана)
    //тайминги
    public static int       FPS_RATE = 4;                   //частота перерисовки анимации
    public static int       SPEED_INCREASE_DELAY = 20_000;  //задержка увеличения скорости шарика
    public static float     MULTIPLIER_BALL_SPEED = 1.0f;   //Текущий множитель шаг движения шарика
    public static float     BonusSpeed=5.0f;                //Шаг падения бонусов (скорость)
    public static  int      BULLET_DRAW_DISATNCE = 20;      //Расстояние между пулями

    //настройки Retain системные
    private static float    musicVolume=1;
    public static float     soundsVolume=1;

    //настройки Retain игровые
    private static int      scores=0;                       //Очки
    public static int       lives=3;                        //Жизни
    public static int       currentStage=1;                 //Текущий уровень
    public static String    playerName="none";              //Имя текущего игрока

    private static float    ballSpeed = 6.0f;          //стартовый шаг движения шарика

    public Properties (Context context,float screenHeight,float screenWidth){
        this.context=context;
        this.SCREEN_HEIGHT = screenHeight;
        this.SCREEN_WIDTH = screenWidth;
        STATUSBAR_HEIGHT=screenWidth/STATUSBAR_SIZE_HEIGHT;
        STATUSBAR_FONT_SIZE=(int)SCREEN_HEIGHT/35;
        loadProperties();
        //корректировка скорости перерисовки относительно разрешения экрана
        FPS_RATE = (int)(FPS_RATE*(1280/SCREEN_HEIGHT));
    }
    public Properties (Context context){
        this.context=context;
        loadProperties();
    }

    public void saveProperties(){
        Log.d(TAG,"SaveProperties");
        XMLPreferences xml = new XMLPreferences();
        xml.onSaveXMLData(context,"options","lives",Integer.toString(lives));
        xml.onSaveXMLData(context,"options","scores",Integer.toString(scores));
        xml.onSaveXMLData(context,"options","currentStage",Integer.toString(currentStage));
        xml.onSaveXMLData(context,"options","playerName",playerName);
     //   xml.onSaveXMLData(context,"options","currentBallSpeed",Double.toString(currentBallSpeed));

        xml.onSaveXMLData(context,"options","musicVolume",Double.toString(musicVolume));
        xml.onSaveXMLData(context,"options","soundVolume",Double.toString(soundsVolume));
    }
    public void loadProperties() {
        Log.d(TAG, "LoadProperties");
        try {
            lives = Integer.parseInt(XMLPreferences.onReadXMLData(context, "options", "lives"));
            scores = Integer.parseInt(XMLPreferences.onReadXMLData(context, "options", "scores"));
            currentStage = Integer.parseInt(XMLPreferences.onReadXMLData(context, "options", "currentStage"));
            playerName = XMLPreferences.onReadXMLData(context, "options", "playerName");
            //   currentBallSpeed =  (float)Double.parseDouble(XMLPreferences.onReadXMLData(context,"options","currentBallSpeed"));

            musicVolume = (float) Double.parseDouble(XMLPreferences.onReadXMLData(context, "options", "musicVolume"));
            soundsVolume = (float) Double.parseDouble(XMLPreferences.onReadXMLData(context, "options", "musicVolume"));

        }catch(Exception e){e.printStackTrace();saveProperties();}
    }
    public void resetProperties(){
        lives = 3;
        scores = 0;
        currentStage = 1;
        MULTIPLIER_BALL_SPEED = 1.0f;
    }
    public float getMusicVolume(){return musicVolume;}
    public float getSoundsVolume(){return soundsVolume;}
    public void setMusicVolume(float musicVolume){this.musicVolume = musicVolume;}
    public void scoresAdd(int scores){this.scores += scores;}
    public int getScores(){return scores;}
    public float getBallSpeed(){return ballSpeed;}
    public void resetBallSpeed(){MULTIPLIER_BALL_SPEED = 1.0f;}
}
