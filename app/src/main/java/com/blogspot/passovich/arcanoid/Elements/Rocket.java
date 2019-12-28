package com.blogspot.passovich.arcanoid.Elements;

import android.content.Context;
import android.graphics.Canvas;

import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;

public class Rocket extends Element {
    private boolean accept = false;
    private Bumper bumper;
    public Rocket(Context context, Properties p, Images images, Bumper bumper){
        super(context, p, images.getBitmapRocket());
        this.bumper = bumper;
        x = bumper.x * 1.01f;

        y = p.SCREEN_HEIGHT - (p.SCREEN_HEIGHT / 10);
    }
    public void moveOneStep(){
        if (accept) {
            if (y > 0) y -= 10;
            else {rocketReset();}
        }
    }
    public void rocketReset(){x = bumper.x; y = bumper.y;}
    public boolean isFireAccepted(){return accept;}
    public void setFireAcceptTrue(){accept = true; rocketReset();}
    public void setFireAcceptFalse(){accept = false;}
    public void draw (Canvas canvas){
        if (accept) {
            canvas.drawBitmap(image, x, y, null);
        }
    }
}
