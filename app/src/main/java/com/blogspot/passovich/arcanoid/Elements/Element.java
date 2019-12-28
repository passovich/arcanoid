package com.blogspot.passovich.arcanoid.Elements;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.blogspot.passovich.arcanoid.Data.Properties;

public class Element {
    public float x, y;
    public Properties p;
    public float width, height;             //ширина высота
    public float sizeWidth, sizeHeight;     //относительная ширина и высота
    public Bitmap image;
    Context context;
    public Element(Context context, Properties p){
        this.context = context;
        this.p = p;
        this.image = null;
    }
    public Element(Context context, Properties p, Bitmap image){
        this.context = context;
        this.p = p;
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
    }
}

