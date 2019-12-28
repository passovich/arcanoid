package com.blogspot.passovich.arcanoid.Elements;

import android.content.Context;
import android.graphics.Canvas;

import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;

public class PauseButton extends Element{

    public PauseButton (Context context, Properties p, Images images){
        super(context, p, images.getBitmapPauseButton());
        x = p.SCREEN_WIDTH-images.getBitmapPauseButton().getWidth();
        y = 0;
    }
    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
    }
}
