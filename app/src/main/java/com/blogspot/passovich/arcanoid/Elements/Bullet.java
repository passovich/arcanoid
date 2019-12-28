package com.blogspot.passovich.arcanoid.Elements;

import android.content.Context;

import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;

public class Bullet extends Element {

    public Bullet(Context context, Properties p, Images images, float x, float y){
        super(context,p,images.getBitmapGun());
        this.x = x;
        this.y = y;
    }
}
