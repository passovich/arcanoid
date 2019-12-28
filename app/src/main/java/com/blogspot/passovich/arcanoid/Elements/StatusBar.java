package com.blogspot.passovich.arcanoid.Elements;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.R;

public class StatusBar extends Element{
    public Paint fontPaint;        //параметры текста на статусбаре

    public StatusBar(Context context, Properties p, Images images){
        super(context, p, images.getBitmapStatusBar());
        y = 0;
        x = 0;
        fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fontPaint.setTextSize(p.STATUSBAR_FONT_SIZE);
        fontPaint.setColor(Color.YELLOW);

    }
    public void draw(Canvas canvas){
        //Рисуем фон statusBar
        canvas.drawBitmap(image, 0.0f, 0.0f, null);
        //рисуем текст статусбара
        Resources res = context.getResources();
        String text =
                res.getString(R.string.statusbar_lives)+
                p.lives+"  "+
                res.getString(R.string.statusbar_scores)+
                p.getScores()+"  "+
                res.getString(R.string.statusbar_stage)+
                p.currentStage+"  "+
                p.playerName;
        canvas.drawText(text, 0, p.SCREEN_HEIGHT / 35,fontPaint);
    }
}
