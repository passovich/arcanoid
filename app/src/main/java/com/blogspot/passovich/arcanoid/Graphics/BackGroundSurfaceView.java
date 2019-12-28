package com.blogspot.passovich.arcanoid.Graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.Elements.BackGround;
import com.blogspot.passovich.arcanoid.Elements.StatusBar;

public class BackGroundSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    final String TAG = "myLogs";
    private Context context;
    private StatusBar statusBar;            //Status bar
    private BackGround backGround;          //фон
    private Properties p;                   //параметры

    private BackGroundDrawThread backGroundDrawThread;
    private SurfaceHolder surfaceHolder;

    public BackGroundSurfaceView(Context context, Properties p, Images images){
        super(context);
        getHolder().addCallback(this);
        this.p = p;
        this.context=context;
        statusBar   = new StatusBar(context,p,images);
        backGround  = new BackGround(context,p,images,BackGround.WINTER);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.d(TAG, "BackgroundSurfaceView surfaceChanged");
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        Canvas canvas = surfaceHolder.lockCanvas();
        drawBG(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
        startThread();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "BackgroundSurfaceView surfaceDestroyed");
        boolean retry = true;
        while (retry) {
            try {
                backGroundDrawThread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }
    }
    public void startThread(){
        backGroundDrawThread = new BackGroundDrawThread(this, surfaceHolder);
        backGroundDrawThread.setRunning(true);
        backGroundDrawThread.start();
    }
    public void stopThread(){
        backGroundDrawThread.setRunning(false);
    }
    public void drawBG(Canvas canvas){
        backGround.draw(canvas);
    }
}