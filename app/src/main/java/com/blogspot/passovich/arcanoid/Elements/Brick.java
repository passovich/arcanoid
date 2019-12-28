package com.blogspot.passovich.arcanoid.Elements;

import android.content.Context;

import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.R;

public class Brick extends Element{
    private final String TAG = "myLogs";
    private boolean bonus=false;
    private int scores=1;
    public int lives;
    public int type;

    public Brick (Context context, int type, Properties p,float x,float y){
        super(context, p);
        this.context = context;
        this.type = type;
        this.x = x;
        this.y = y;
        if (type == 8) bonus = true;

        switch (type){
            case 0:lives=0;break;
            case 1:lives=1;break;
            case 2:lives=2;break;
            case 3:lives=3;break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:lives=1;break;
        }
    }
    public boolean isBonus(){
        return bonus;
    }
}
