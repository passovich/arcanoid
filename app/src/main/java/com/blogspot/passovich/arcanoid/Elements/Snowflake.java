package com.blogspot.passovich.arcanoid.Elements;

import android.content.Context;
import android.graphics.Canvas;

import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;

import java.util.Random;

public class Snowflake extends  Element{
    private float counter = 0;
    private int maxCounter;
    private int step = 1;
    private Images images;

    public Snowflake (Context context, Properties p, Images images){
        super(context, p);
        this.images = images;
        reset();
        counter = new Random().nextInt((int)(p.SCREEN_HEIGHT - p.STATUSBAR_HEIGHT));

    }  public void draw (Canvas canvas){
        canvas.drawBitmap(image, x, counter, null);
        counter += step / 2.0f;
        if (counter >= maxCounter)reset();
    }
    private void reset(){
        counter = 0;
        y = 0;
        x = new Random().nextInt((int)p.SCREEN_WIDTH);
        step = (new Random().nextInt(5)) + 1;
        maxCounter = (int) (p.SCREEN_HEIGHT *(0.5 + (step - 1) * 0.125f));
        image = images.getBitmapSnowFlake(step - 1);
    }
}
