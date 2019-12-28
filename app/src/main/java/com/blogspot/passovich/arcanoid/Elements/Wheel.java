package com.blogspot.passovich.arcanoid.Elements;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;

public class Wheel extends Element {
    private Properties p;
    private Images images;
    private Bumper bumper;
    private int angleCounter;
    private float x2;
    private float oborot;
    public Wheel(Context context, Properties p, Images images, Bumper bumper){
        super(context, p, images.getBitmapWheel());
        this.p = p;
        this.images = images;
        this.bumper = bumper;
        oborot = (float) Math.PI * image.getWidth();
    }
    public void draw(Canvas canvas){
        angleCounter = (int)((360.0f / oborot) * bumper.x);
        image = images.rotateImage(images.getBitmapWheel(), - (angleCounter));
        x = bumper.x - bumper.width / 2;
        x2 = bumper.x + bumper.width / 2 - image.getWidth();
        y = bumper.y + bumper.height;
        canvas.drawBitmap(image, x , y,null);
        canvas.drawBitmap(image, x2, y,null);
        //Log.d ("myLogs","wheels.draw x = "+x+", y = "+y+", angle = "+angleCounter);
    }
}
