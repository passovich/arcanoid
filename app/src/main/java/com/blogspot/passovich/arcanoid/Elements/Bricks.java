package com.blogspot.passovich.arcanoid.Elements;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.Data.Stages;

public class Bricks {
    private int stage[][];
    public Brick bricksMatrix[][] = new Brick[10][8]; // матрица кирпичей
    public Bitmap[] bitmapBricks;
    private Context context;
    private Properties p;
    private Images images;

    public Bricks(Context context, Properties p, Images images){
        Log.d("myLogs","Конструктор бриксов");
        this.context = context;
        this.p = p;
        this.images = images;
        stage = Stages.getStage(p.currentStage);
        //Загружаем картинки кирпичей
        bitmapBricks = images.getBitmapBricks();
        //создаём массив кирпичей
        bricksMatrixCreate();
    }

    public void draw(Canvas canvas){
        for (int i = 0; i < bricksMatrix.length; i++){
            for (int j = 0; j < bricksMatrix[i].length; j++){
                if(bricksMatrix[i][j].lives > 0) {
                     canvas.drawBitmap(bitmapBricks[bricksMatrix[i][j].type], bricksMatrix[i][j].x, bricksMatrix[i][j].y, null);
                }
            }
        }
    }
    public void resetBricksMatrix(){
        for (int i = 0;i < bricksMatrix.length; i++){
            for (int j = 0;j < bricksMatrix[i].length; j++){
                bricksMatrix[i][j].lives = 0;
            }
        }
    }
    private void bricksMatrixCreate(){
        for (int i = 0; i < bricksMatrix.length; i++){
            for (int j = 0; j < bricksMatrix[i].length; j++){
                bricksMatrix[i][j] = new Brick(
                        context,
                        stage[i][j],
                        p,
                        j*bitmapBricks[1].getWidth(),
                        i*bitmapBricks[1].getHeight()+p.STATUSBAR_HEIGHT
                );
                bricksMatrix[i][j].width=bitmapBricks[1].getWidth();
                bricksMatrix[i][j].height=bitmapBricks[1].getHeight();
            }
        }
    }
}
