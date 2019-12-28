package com.blogspot.passovich.arcanoid.Graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.blogspot.passovich.arcanoid.Data.File;
import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.Elements.BackGround;
import com.blogspot.passovich.arcanoid.Elements.Balls;
import com.blogspot.passovich.arcanoid.Elements.Bonuses;
import com.blogspot.passovich.arcanoid.Elements.Bricks;
import com.blogspot.passovich.arcanoid.Elements.Bullets;
import com.blogspot.passovich.arcanoid.Elements.Bumper;
import com.blogspot.passovich.arcanoid.Elements.Gun;
import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Elements.PauseButton;
import com.blogspot.passovich.arcanoid.Elements.Rocket;
import com.blogspot.passovich.arcanoid.Elements.StatusBar;

import com.blogspot.passovich.arcanoid.Elements.Wheel;
import com.blogspot.passovich.arcanoid.GameActivitySurfaceView;
import com.blogspot.passovich.arcanoid.Multimedia.Sounds;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    final String TAG = "myLogs";
    private boolean paused = false;
    private Context context;
    private GameActivitySurfaceView gasv;
    private float touchX=0.0f,touchY=0.0f;  //коорданты касания
    private StatusBar statusBar;            //Status bar
    private Balls balls;                    //шарики
    private Bumper bumper;                  //Ракетка
   // private Wheel wheels;                   //Колёса
    private Bricks bricks;                  //Кирпичи
    private Bonuses bonuses;                //Бонусы
    private Sounds sounds;                  //Звуки
    private Gun gun;                        //Кулемети (ukr)
    private Bullets bullets;                //Пульки
    private Rocket rocket;                  //Ракеты
    private BackGround backGround;          //фон
    private PauseButton pauseButton;        //Кнопка пауза
    private Properties p;                   //параметры экрана и др
    private Images images;                  //Подготовленные картинки
    private File file;
    private MediaPlayer mediaPlayer;

    private SurfaceDrawThread surfaceDrawThread; //второй поток по часам
    private SurfaceHolder surfaceHolder;


    public GameSurfaceView(
            Context context,
            Properties p,
            Images images,
            int startType,
            MediaPlayer mp,
            GameActivitySurfaceView gasv
    ){
        super(context);
        getHolder().addCallback(this);
        this.p = p;
        this.context = context;
        this.gasv = gasv;
        this.images = images;
        this.mediaPlayer = mp;

        file        = new File();
        statusBar   = new StatusBar(context, p, images);
        bricks      = new Bricks(context, p, images);
        sounds      = new Sounds(context, p);
        balls       = new Balls (context, p, images);
        bumper      = new Bumper(context, p, images);
    //    wheels      = new Wheel(context,p,images,bumper);
        gun         = new Gun(context, p, images);
        bullets     = new Bullets(context, p, images, bumper);
        rocket      = new Rocket(context, p, images, bumper);
        bonuses     = new Bonuses(context, p, images);
        backGround  = new BackGround(context, p, images, BackGround.WINTER);
        pauseButton = new PauseButton(context, p, images);

        if (startType == 1){p.resetProperties();}                     //новая игра
        else if(startType == 2){loadGame(bricks); p.loadProperties();} //загрузка сохранения
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.d(TAG, "gameSurfaceView surfaceChanged");
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        Canvas canvas = surfaceHolder.lockCanvas();
        drawGame(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
        if (!paused){
            surfaceDrawThread = new SurfaceDrawThread(this, surfaceHolder);
            surfaceDrawThread.setRunning(true);
            surfaceDrawThread.start();
        }
        Log.d(TAG, "gameSurfaceView surfaceCreated");
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "gameSurfaceView surfaceDestroyed");
        boolean retry = true;
        while (retry) {
            try {
                surfaceDrawThread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch(action){
            case MotionEvent.ACTION_UP:
                //нажатие на кнопку пауза
                if ((touchX > (p.SCREEN_WIDTH - p.STATUSBAR_HEIGHT)) && (touchY<p.STATUSBAR_HEIGHT)){
                    if (paused){resumeGame();}
                    else{pauseGame();}
                }break;
        }
        touchY = event.getY();
        touchX = event.getX();
        if((touchX > p.SCREEN_WIDTH / 15 && touchX < (p.SCREEN_WIDTH - p.SCREEN_WIDTH / 15)) &&
                    touchY > (bumper.y * 0.8f)){    //запрет на выход за края экрана
            bumper.x = touchX;                      //первое касание
        }
        if(event.getPointerCount() >= 2){         //два касания, отпускаем шарик
            balls.release(); bumper.ballRelease();
        }
        if ((touchY < bumper.y * 0.8f) && (touchY > p.STATUSBAR_HEIGHT)){  //касание выше ракетки - отпускаем шарик
            balls.release(); bumper.ballRelease();
        }
        return true;
    }

    public void resumeGame() {
        Log.d(TAG,"gameSurfaceView startGame()");
        if (surfaceHolder != null) {
            paused = false;
            if(!mediaPlayer.isPlaying()) mediaPlayer.start();
            surfaceDrawThread = new SurfaceDrawThread(this, surfaceHolder);
            surfaceDrawThread.setRunning(true);
            surfaceDrawThread.start();
        }
    }
    public void setPaused(){paused = true;}
    public void pauseGame() {
        Log.d(TAG,"gameSurfaceView pauseGame()");
        surfaceDrawThread.setRunning(false);
        if(mediaPlayer.isPlaying())mediaPlayer.pause();
        paused = true;
        saveGame();
    }
    public void saveGame(){
        Log.d(TAG,"gameSurfaceView saveGame()");
        file.saveStageToQuickSave(bricks, context, p.playerName);
        p.saveProperties();
    }
    public void loadGame(Bricks bricks){
        Log.d(TAG,"gameSurfaceView loadGame");
        bricks.resetBricksMatrix();
        file.loadStageFromQuickSave(bricks, context, p.playerName);
        p.loadProperties();
    }
    public void bulletStep(){
        sounds.playSound(Calculate.bulletMove(bullets, bricks, bonuses));
    }
    public void rocketStep(){
        sounds.playSound(Calculate.rocketMove(rocket, bricks, bonuses));
    }
    public void ballsStep(){
        int param = Calculate.ballsMove(context, bumper, balls, p, bricks, bonuses, gun, rocket);
        sounds.playSound(param);
        if (param == -1){gasv.showLooseDialog();}

    }
    public void effectsStep(){
        sounds.playSound(Calculate.bonusMove(bonuses, bumper, p, balls, bullets, gun, rocket));
        if (Calculate.winnCheck(bricks)){
            p.currentStage++;
            bricks = new Bricks(context, p, images);
            balls=new Balls(context, p, images);
            bumper = new Bumper(context, p, images);
        //    wheels = new Wheel(context, p, images, bumper);
            gun = new Gun(context, p, images);
            bullets = new Bullets(context, p, images, bumper);
            rocket = new Rocket(context, p, images, bumper);
            p.resetBallSpeed();
            // Toast.makeText(context," Уровень"+p.currentStage, Toast.LENGTH_LONG).show();
        }
    }
    public void drawGame(Canvas canvas){
        backGround.draw(canvas);
        statusBar.draw(canvas);
        pauseButton.draw(canvas);
        bricks.draw(canvas);
        bumper.draw(canvas);
       // wheels.draw(canvas);
        balls.draw(canvas);
        bonuses.draw(canvas);
        gun.drawGuns(canvas, bumper);
        bullets.draw(canvas);
        rocket.draw(canvas);
        if (paused)canvas.drawBitmap(images.getBitmapPaused(), 0,p.SCREEN_WIDTH/4, null);
     }
}