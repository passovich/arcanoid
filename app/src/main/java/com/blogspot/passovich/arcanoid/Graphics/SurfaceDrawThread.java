package com.blogspot.passovich.arcanoid.Graphics;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import com.blogspot.passovich.arcanoid.Data.Properties;

public class SurfaceDrawThread extends Thread {

    private SurfaceHolder SurfaceHolder;    //нужен, для получения canvas
    private GameSurfaceView gameSurfaceView;//ссылка на вызывающий класс
    private boolean running = false;        //разрешение работы потока
    private long    prevRedrawTime;         //предыдущее время перерисовки кадра
    private long    prevSpeedUpdateTime;    //предыдущее время увеличения скорости

    public SurfaceDrawThread(GameSurfaceView gameSurfaceView,SurfaceHolder holder){
       this.gameSurfaceView = gameSurfaceView;
        SurfaceHolder = holder;
        Log.d("myLogs","surfaceDrawThread(конструктор) hash="+this.hashCode());
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
            //периодическое увеличение скорости шарика
            if (!((curTime - prevSpeedUpdateTime) < Properties.SPEED_INCREASE_DELAY)){
                Properties.MULTIPLIER_BALL_SPEED *= 1.1f;
                prevSpeedUpdateTime = curTime;
                Log.d("myLogs","MULTIPLIER_BALL_SPEED="+Properties.MULTIPLIER_BALL_SPEED);
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
            gameSurfaceView.drawGame(canvas);
            gameSurfaceView.effectsStep();
            gameSurfaceView.ballsStep();
            gameSurfaceView.bulletStep();
            gameSurfaceView.rocketStep();
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
