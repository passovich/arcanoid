package com.blogspot.passovich.arcanoid.Graphics;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import com.blogspot.passovich.arcanoid.Data.Properties;

public class BackGroundDrawThread extends Thread {
    private final String TAG = "myLogs";
    private android.view.SurfaceHolder SurfaceHolder;    //нужен, для получения canvas
    private BackGroundSurfaceView backGroundSurfaceView;//ссылка на вызывающий класс
    private boolean running = false;        //разрешение работы потока
    private long    prevRedrawTime;         //предыдущее время перерисовки кадра
    private long    prevSpeedUpdateTime;    //предыдущее время увеличения скорости

    public BackGroundDrawThread(BackGroundSurfaceView bgsv,SurfaceHolder holder){
        this.backGroundSurfaceView = bgsv;
        SurfaceHolder = holder;
        Log.d("myLogs","BackGroundDrawThread(конструктор) hash="+this.hashCode());
    }
    @Override
    public void run() {
        while (running) {
            long curTime = getTime();
            //перерисовка кадра
            if (!((curTime - prevRedrawTime) < Properties.FPS_RATE)){
                reDrawFrame();
                prevRedrawTime = curTime;
            }
        }
    }
    public long getTime() {
        return System.nanoTime() / 1_000_000;
    }
    private void reDrawFrame(){
        Canvas canvas=null;
        try {
            canvas = SurfaceHolder.lockCanvas();   //получаем canvas
            synchronized (SurfaceHolder) {
                backGroundSurfaceView.drawBG(canvas);
            }
        }
        catch (NullPointerException e) {/*если canvas не доступен*/}
        finally {
            if (canvas != null)
                SurfaceHolder.unlockCanvasAndPost(canvas); //освобождаем canvas
        }
    }

    public void setRunning(boolean running) { //запускает и останавливает процесс
        this.running = running;
        prevRedrawTime = getTime();
    }
}
