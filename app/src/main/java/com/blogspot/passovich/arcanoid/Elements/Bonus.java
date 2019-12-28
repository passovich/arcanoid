package com.blogspot.passovich.arcanoid.Elements;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;

public class Bonus extends Element{
    private int bonusType;
    private Images images;

    public Bonus(Context context, Properties p, Images images, int bonusType){
        super(context,p, images.getBonusImage(bonusType));
        this.images = images;
        y = 0;
        x = 0;
        this.bonusType=bonusType;
    }
    public void moveOneStep(){
       y += p.BonusSpeed;
    }
    public void draw(Canvas canvas){
        canvas.drawBitmap(this.image,x,y, null);
    }
    public int getType(){return bonusType;}
}
