package com.blogspot.passovich.arcanoid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.Graphics.BackGroundSurfaceView;
import com.blogspot.passovich.arcanoid.Multimedia.Sounds;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG="myLogs";
    private Button button1;
    private TextView textView1,textView2,textView3;
    private Sounds sounds;

    private FrameLayout frameLayout;
    private LinearLayout linearLayout;
    private BackGroundSurfaceView backGroundSurfaceView;
    private Display display;
    private Images images;
    private Properties p;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //Убрать statusBar
        display = ((WindowManager) this.getSystemService(this.WINDOW_SERVICE)).getDefaultDisplay();
        p = new Properties(this, display.getHeight(), display.getWidth());
        images = new Images(this,p);
        backGroundSurfaceView = new BackGroundSurfaceView(this, p, images);

        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(params);
        LayoutInflater inflater = getLayoutInflater();
        linearLayout = (LinearLayout) inflater.inflate(R.layout.activity_about, null, false);
        frameLayout.addView(backGroundSurfaceView);
        frameLayout.addView(linearLayout);
        setContentView(frameLayout);

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);

        textView1.setText(
                "The game was created by\n"+
                "Postulga Ievgen\n"+
                "\n"+
                "\n");

        textView2.setText(
                "Music you hear in the game getted from\n"+
                "https://filmmusic.io\n"+
                "Airport Lounge\n"+
                "Covert Affair\n"+
                "Deadly Roulette\n"+
                "Folk Round\n"+
                "George Street Shuffle\n"+
                "Glitter Blast\n"+
                "Lobby Time\n"+
                "Long Stroll\n"+
                "Opportunity Walks\n"+
                "Smooth Lovin\n"+
                "by Kevin MacLeod\n"+
                "(https://incompetech.com)\n"+
                "License: CC BY (http://creativecommons.org/licenses/by/4.0/)");
        textView3.setText(
                "\nKharkiv, Ukraine\n"+
                "2019\n"
        );
        sounds = new Sounds(this,new Properties(this));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        switch (v.getId()) {
            case R.id.button1:
                button1.startAnimation(anim);
                sounds.playSound(4);
                intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyKode, KeyEvent event){
        if (keyKode==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(this,MenuActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        backGroundSurfaceView.startThread();
    }
    @Override
    protected void onPause() {
        super.onPause();
        backGroundSurfaceView.stopThread();
    }
}
