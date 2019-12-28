package com.blogspot.passovich.arcanoid.Elements;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;

public class Bumper extends Element{
    private Images images;
    private boolean magnet=false;
    private boolean ballOnBumper=false;

    public Bumper(Context context, Properties p,Images images){
        super(context, p, images.getBumperImage());
        this.images = images;
        this.sizeWidth = 4;
        this.sizeHeight = 4;
        y = p.SCREEN_HEIGHT - p.SCREEN_HEIGHT / 6;
        x = p.SCREEN_WIDTH / 2;
    }

    private void resizeBitmap(float newWidthK,float newHeightK){
        Bitmap tempBitmap = images.getBumperImage();
        Matrix matrix = new Matrix();
        matrix.postScale(
                (p.SCREEN_WIDTH/sizeWidth) / tempBitmap.getWidth()*newWidthK,
                (p.SCREEN_WIDTH / sizeHeight) / tempBitmap.getWidth() * newHeightK
        );
        image = Bitmap.createBitmap(
                tempBitmap,
                0,
                0,
                tempBitmap.getWidth(),
                tempBitmap.getHeight(),
                matrix,
                true
        );
        width = image.getWidth();
        height = image.getHeight();
    }
    public void draw(Canvas canvas){
    canvas.drawBitmap(image,x - width / 2.0f, y, null);
}
    public void setNormalBumper(){
        resizeBitmap(1.0f,1.0f);
    }
    public void setBigBumper(){
        resizeBitmap(1.5f,1.0f);
    }
    public void setSmallBumper(){
        resizeBitmap(0.8f,1.0f);
    }
    public boolean magnetActivated(){return magnet;}
    public void magnetActivate(){magnet = true;}
    public void magnetDeActivate(){magnet = false;}
    public boolean ballCatched(){return ballOnBumper;}
    public void ballCatch(){ballOnBumper = true;}
    public void ballRelease(){ballOnBumper = false;}
}
