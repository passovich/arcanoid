package com.blogspot.passovich.arcanoid.Elements;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.LinkedList;
import java.util.List;

import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;

public class Balls {
    private Ball ball;
    private Context context;
    private Properties p;
    private Images images;
    private Bitmap bitmapForAnimation;
    private int angleCounter=0;

    public List<Ball>ballList=new LinkedList<Ball>();

    public Balls (Context context, Properties p, Images images){
        this.context = context;
        this.p = p;
        this. images = images;
        bitmapForAnimation = images.getBallImage();
        ball = new Ball(context,p,images);
        ballList.add(ball);
    }
    public void draw (Canvas canvas){
        if (angleCounter > 360) angleCounter = 0;
        bitmapForAnimation = images.rotateImage(images.getBallImage(),angleCounter);
        for(Ball b : ballList){
            canvas.drawBitmap(
                    bitmapForAnimation,
                    b.x-bitmapForAnimation.getWidth()/2.0f,
                    b.y-bitmapForAnimation.getHeight()/2,
                    null);
        }
        angleCounter++;
    }
    //balls+2
    public void getThreeBalls(){
        ballList.get(0).angle = 90;ballList.get(0).setVectorX(1);ballList.get(0).setVectorY(-1);
        addBall(30,1,1);
        addBall(30,-1,1);
    }
    //balls+7
    public void getEightBalls(){
        ballList.get(0).angle = 90;ballList.get(0).setVectorX(1);ballList.get(0).setVectorY(-1);
        addBall(45, 1,-1);
        addBall(5 , 1,-1);
        addBall(45, 1, 1);
        addBall(90, 1, 1);
        addBall(45,-1, 1);
        addBall(5 ,-1, 1);
        addBall(45,-1,-1);
    }
    public void release(){
        for(Ball ball: ballList) {
            ball.setZeroPoint();
            ball.moveAccept = true;
        }
    }
    private void addBall(float angle, int vectorX, int vectorY){
        ball = new Ball(context, p, images);
        ball.x = ballList.get(0).x;
        ball.y = ballList.get(0).y;
        ball.angle = angle; ball.setVectorX(vectorX); ball.setVectorY(vectorY);
        ball.setZeroPoint(); ball.moveAccept = true;
        ball.lastElementTouch = ball.NOT_BUMPER;
        ballList.add(ball);
    }
}

