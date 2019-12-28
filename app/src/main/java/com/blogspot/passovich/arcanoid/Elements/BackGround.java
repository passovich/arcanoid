package com.blogspot.passovich.arcanoid.Elements;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.R;

import java.util.ArrayList;
import java.util.List;

public class BackGround extends Element {
    public static final int WINTER = 1;
    public static final int SPRING = 2;
    public static final int SUMMER = 3;
    public static final int AUTUMN = 4;
    public static final int RANDOM = 0;

    private int bgType = 1;
    private float counter=0;
    private List<Snowflake> snow = new ArrayList<>(50);

    public BackGround (Context context, Properties p, Images images,int bgType){
        super(context, p);
        this.bgType = bgType;
        x = 0; y = 0;
        switch (bgType) {
            case WINTER:
                image = loadBackGround(R.drawable.background_winter2);
                for (int i = 0; i <= 50; i++) {
                    Snowflake s = new Snowflake(context, p, images);
                    snow.add(s);
                }
                break;
            case SPRING: break;
            case SUMMER: break;
            case AUTUMN:
                image = loadBackGround(R.drawable.background_autumn1);
                break;
        }
    }
    public void draw(Canvas canvas){
        switch (bgType){
            case 1:
                drawStaticImage(canvas);
                drawSnow(canvas);
                break;
            case SPRING: break;
            case SUMMER: break;
            case AUTUMN:
                drawStaticImage(canvas);
                break;
        }
        //drawSlideImage(canvas);   движущаяся картинка

    }
    private void drawStaticImage (Canvas canvas){
        canvas.drawBitmap(image, x,y, null);
    }

    private void drawSlideImage(Canvas canvas){
        canvas.drawBitmap(image, x,y + counter, null);
        canvas.drawBitmap(image, x,y - height + counter, null);
        if (counter >= p.SCREEN_HEIGHT)counter = 0;
        counter++;
    }
    private void drawSnow(Canvas canvas){
        for (Snowflake s : snow){
            s.draw(canvas);
        }
    }
    private Bitmap loadBackGround(int bitmapId){
        Bitmap resultImage;
        Bitmap bitmapTemp = BitmapFactory.decodeResource(context.getResources(),bitmapId);
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postScale(p.SCREEN_WIDTH / bitmapTemp.getWidth(),(p.SCREEN_HEIGHT - p.STATUSBAR_HEIGHT) / bitmapTemp.getHeight());
        resultImage=Bitmap.createBitmap(bitmapTemp,0,0, bitmapTemp.getWidth(), bitmapTemp.getHeight(), matrix,true);
        bitmapTemp.recycle();
        return resultImage;
    }
}
