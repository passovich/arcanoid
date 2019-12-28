package com.blogspot.passovich.arcanoid.Elements;

import android.content.Context;
import android.graphics.Canvas;

import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;

import java.util.LinkedList;
import java.util.List;

public class Bullets {
    private  final String TAG = "myLogs";
    private Bullet bullet;
    private boolean accept = false;
    private boolean leftBullet = true;
    private Context context;
    private Properties p;
    private Images images;
    private Bumper bumper;
    private int distanceCounter;

    public List<Bullet> bulletList = new LinkedList<Bullet>();

    public Bullets(Context context, Properties p, Images images, Bumper bumper){
        this.context = context;
        this.p = p;
        this.images = images;
        this.bumper = bumper;
        bullet = new Bullet(context,p,images,bumper.x,bumper.y);
    }
    public void draw(Canvas canvas){
        if (accept) {
            for (Bullet b : bulletList) {
                canvas.drawBitmap(b.image, b.x, b.y, null);
            }
        }
    }
    public void step(){
        if (accept) {
            if (distanceCounter >= p.BULLET_DRAW_DISATNCE) {
                distanceCounter = 0;
                float x;
                if (leftBullet) {
                    x = bumper.x - bumper.width / 4.0f;
                    leftBullet = false;
                } else {
                    x = bumper.x + bumper.width / 4.0f - bullet.width;
                    leftBullet=true;
                }
                bullet = new Bullet(context, p, images, x, bumper.y);
                bulletList.add(bullet);
            }
            for (Bullet b : bulletList) {
                b.y -= 10;
            }
            distanceCounter++;
        }
    }
    public void setFireAcceptTrue(){accept=true;}
}
