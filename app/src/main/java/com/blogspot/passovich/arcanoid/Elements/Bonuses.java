package com.blogspot.passovich.arcanoid.Elements;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.LinkedList;
import java.util.List;

import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;

public class Bonuses {
    private Bonus bonus;
    private Context context;
    private Properties p;
    private Images images;
    public List <Bonus> bonusList = new LinkedList<Bonus>();

    public Bonuses(Context context, Properties p,Images images){
        this.context = context;
        this.p = p;
        this.images = images;
    }
    public void draw(Canvas canvas){
        if (!bonusList.isEmpty())
        for(Bonus b : bonusList){
            b.draw(canvas);
        }
    }
    public void add(float x, float y, int type){
        bonus = new Bonus(context, p, images, type);
        bonus.x = x;
        bonus.y = y;
        bonusList.add(bonus);
    }
}
