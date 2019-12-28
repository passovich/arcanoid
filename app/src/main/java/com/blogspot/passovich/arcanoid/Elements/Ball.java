package com.blogspot.passovich.arcanoid.Elements;

import android.content.Context;

import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;

public class Ball extends  Element {
    private final String TAG = "myLogs";

    public final int NOT_BUMPER = 0;
    public final int BUMPER = 1;
    public int lastElementTouch = BUMPER;                       //элемент последнего касания
    public float r;                                             //радиус шарика
    public float angle =70.0f;                                  //Угол движения
    public boolean moveAccept = false;                          //разрешение летать

    private float xStart,yStart;                                //Точка отсчёта начала отрезка движения
    private int vectorX = 1,vectorY = -1;                       //множители направления движения шарика,вектор движения
    private float point = 0.0f;                                 //конец отрезка движения
    private float sin90 = (float)Math.sin(Math.toRadians(90));  //sin угла 90 градусов
    private float XBumperTouch = 0;                             //Смещение шарика на ракетке при включенном магните

    public Ball (Context context, Properties p, Images images){
        super(context,p,images.getBallImage());
        r = width/2;
        y = p.SCREEN_HEIGHT/2.0f;
        x = 100.0f;
        xStart=x;yStart=y;
    }
    public void setZeroPoint(){
        xStart = x; yStart = y; point = 0.0f;
    }
    public void moveOneStep(){
        point += p.getBallSpeed() * p.MULTIPLIER_BALL_SPEED;
        x = xStart+(float)((Math.sin(Math.toRadians((90.0f-angle)))*point)/sin90)*vectorX;
        y = yStart+(float)((Math.sin(Math.toRadians(angle))*point)/sin90)*vectorY;
    }

    public void xVectorInvert(){vectorX *= -1; xStart = x; yStart = y; point = 0.0f;}
    public void yVectorInvert(){vectorY *= -1; xStart = x; yStart = y; point = 0.0f;}
    public void setVectorX(int kX){this.vectorX = kX; xStart = x; yStart = y; point = 0.0f;}
    public void setVectorY(int kY){this.vectorY = kY; xStart = x; yStart = y; point = 0.0f;}
    public int getVectorX(){return vectorX;}
    public int getVectorY(){return vectorY;}
    public void setXBumperTouch(float XBumperTouch){this.XBumperTouch = XBumperTouch;}
    public float getXBumperTouch(){return XBumperTouch;}
    public void moveReset(){vectorX = 1;vectorY= -1 ; angle = 70;}
}
