package com.blogspot.passovich.arcanoid.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.Log;

import com.blogspot.passovich.arcanoid.R;

public class Images {
    private Properties p;
    private Context context;

    private static final int[]BRICKS_IMAGE_IDS = { //Массив ссылок на картинки кирпичей
            R.drawable.brick_stone_1,
            R.drawable.brick_stone_2,
            R.drawable.brick_stone_3,
            R.drawable.brick_stone_4,
            R.drawable.brick_bricks_1,
            R.drawable.brick_bricks_2,
            R.drawable.brick_bricks_3,
            R.drawable.brick_cloth_1,
            R.drawable.brick_carbon_1,
    };
    private static final int[]BONUSES_IMAGE_IDS = { //Массив ссылок на картинки бонусов
            R.drawable.bonus_ten_scores,        //10 очков
            R.drawable.bonus_hundred_scores,    //100 очков
            R.drawable.bonus_life,              //жизнь
            R.drawable.bonus_three_balls_new,   //3 шарика
            R.drawable.bonus_eight_balls_new,   //8 шариков
            R.drawable.bonus_large_bumper,      //большая ракетка
            R.drawable.bonus_small_bumper,      //маленькая ракетка
            R.drawable.bonus_normal_bumper,     //обычная ракетка
            R.drawable.bonus_magnet,            //магнит
            R.drawable.bonus_gun,               //кулемет
            R.drawable.bonus_rocket,            //ракетница
            R.drawable.bonus_fast,              //++скорость
            R.drawable.bonus_slow,              //--скорость
            R.drawable.bonus                    //запас
    };

    //Картинки
    private Bitmap[] bitmapBricks=new Bitmap[BRICKS_IMAGE_IDS.length];
    private Bitmap[] bitmapBonuses = new Bitmap[BONUSES_IMAGE_IDS.length];
    private Bitmap[] bitmapSnowFlakes = new Bitmap [5];
    private Bitmap bitmapBall;
    private Bitmap bitmapBumper;
    private Bitmap bitmapGun;
    private Bitmap bitmapStatusBar;
    private Bitmap bitmapBackground;
    private Bitmap bitmapRocket;
    private Bitmap bitmapPauseButton;
    private Bitmap bitmapPaused;
    private Bitmap bitmapWheel;
    private Bitmap bitmapSnow;

    public Images(Context context,Properties p){
        this.context = context;
        this.p = p;
        loadImages();
    }
    private void loadImages(){
        //Шарики
        bitmapBall = loadBitmap(18,18, R.drawable.ball_basket);
        //Bumper
        bitmapBumper = loadBitmap(4,4, R.drawable.bumper);
        //Кулемет
        bitmapGun = loadBitmap(50,50, R.drawable.bumper_gun);
        //Ракета
        bitmapRocket = loadBitmap(50,25, R.drawable.rocket);
        //StatusBar background
        bitmapStatusBar = loadBitmap(1,(int)p.STATUSBAR_SIZE_HEIGHT, R.drawable.status_bar_backgroung);
        //PauseButtonBitmap
        bitmapPauseButton = loadBitmap((int)p.STATUSBAR_SIZE_HEIGHT,(int)p.STATUSBAR_SIZE_HEIGHT,R.drawable.pause_button1);
        //Paused - шторка паузы
        bitmapPaused = loadBitmap(1,1,R.drawable.paused);
        //Колесо
        bitmapWheel = loadBitmap(15,15, R.drawable.wheel);
        //bitmapSnowFlakes
        for (int i = 0; i<bitmapSnowFlakes.length; i++){
            int sw=(int)(80/(i/2+1));
            int sh=(int)(80/(i/2+1));
            bitmapSnowFlakes[i] = loadBitmap(sw, sh, R.drawable.snow_flake);
        }
        //Кирпичи
        for (int i = 0; i < bitmapBricks.length; i++){
            bitmapBricks[i]=loadBitmap(8,10, BRICKS_IMAGE_IDS[i]);
        }
        //Бонусы
        for (int i = 0; i < bitmapBonuses.length; i++) {
            bitmapBonuses[i]=loadBitmap(13,13, BONUSES_IMAGE_IDS[i]);
        }
    }
    private Bitmap loadBitmap(int sizeWidth, int sizeHeight,int bitmapId){
        //загружаем и масштабируем картинку
        Bitmap resultImage;
        Bitmap tempBitmap = BitmapFactory.decodeResource(context.getResources(),bitmapId);
        Matrix matrix = new Matrix();
        matrix.postScale((p.SCREEN_WIDTH/sizeWidth)/tempBitmap.getWidth(),(p.SCREEN_WIDTH/sizeHeight)/tempBitmap.getWidth());
        resultImage=Bitmap.createBitmap(tempBitmap,0,0,tempBitmap.getWidth(),tempBitmap.getHeight(),matrix,true);
        tempBitmap.recycle();
        return resultImage;
    }

    public Bitmap getBonusImage(int bonusImageId){return bitmapBonuses[bonusImageId];}
    public Bitmap getBallImage(){return bitmapBall;}
    public Bitmap getBumperImage(){return bitmapBumper;}
    public Bitmap[] getBitmapBricks(){return bitmapBricks;}
    public Bitmap getBitmapGun(){return  bitmapGun;}
    public Bitmap getBitmapStatusBar(){return  bitmapStatusBar;}
    public Bitmap getBitmapRocket(){return  bitmapRocket;}
    public Bitmap getBitmapPauseButton(){return  bitmapPauseButton;}
    public Bitmap getBitmapPaused(){return  bitmapPaused;}
    public Bitmap getBitmapSnowFlake(int id){return bitmapSnowFlakes[id];}
    public Bitmap getBitmapWheel(){return bitmapWheel;}
    public static Bitmap rotateImage(Bitmap bitmap,int angle) {
        Camera camera = new Camera();
        camera.save();
        Matrix matrix = new Matrix();
        camera.rotateZ(angle);
        camera.getMatrix(matrix);
        camera.restore();
        Bitmap newBit = null;
        try {
            newBit = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
        if (newBit != null) {
            return newBit;
        }
        Log.d("myLogs","битмап не повернулся");
        return bitmap;
    }
}
