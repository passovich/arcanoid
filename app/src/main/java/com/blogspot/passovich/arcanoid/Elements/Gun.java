package com.blogspot.passovich.arcanoid.Elements;

import android.content.Context;
import android.graphics.Canvas;

import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;

public class Gun extends Element {
    private boolean drawable = false;
    public float x1;


    public Gun(Context context, Properties p, Images images){
        super(context, p, images.getBitmapGun());
    }
    public void drawGuns (Canvas canvas,Bumper bumper){
        if (drawable) {
            y = bumper.y - p.SCREEN_WIDTH / 50;
            x = bumper.x - bumper.width / 4.0f;
            x1 = bumper.x + bumper.width / 4.0f - this.width;
            canvas.drawBitmap(image, x, y, null);
            canvas.drawBitmap(image, x1, y, null);
        }
    }
    public void setDrawable(boolean drawable){this.drawable = drawable;}
}
