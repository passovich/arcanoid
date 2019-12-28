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
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.passovich.arcanoid.Data.HelperMethodClass;
import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.Graphics.BackGroundSurfaceView;
import com.blogspot.passovich.arcanoid.Multimedia.Sounds;

public class LidersActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button1;
    private Sounds sounds;
    private ListView listView;
    private final String NAME_DB = "users.db";

    private FrameLayout frameLayout;
    private LinearLayout linearLayout;
    private BackGroundSurfaceView backGroundSurfaceView;
    private Display display;
    private Images images;
    private Properties p;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //Убрать statusBar
        display = ((WindowManager) this.getSystemService(this.WINDOW_SERVICE)).getDefaultDisplay();
        p = new Properties(this, display.getHeight(), display.getWidth());
        images = new Images(this,p);
        backGroundSurfaceView = new BackGroundSurfaceView(this,p,images);

        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(params);
        LayoutInflater inflater = getLayoutInflater();
        linearLayout = (LinearLayout) inflater.inflate(R.layout.activity_liders, null, false);
        frameLayout.addView(backGroundSurfaceView);
        frameLayout.addView(linearLayout);
        setContentView(frameLayout);

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.alphaprozrachn);
        sounds = new Sounds(this,new Properties(this));
        button1 = (Button) findViewById(R.id.button1); button1.setOnClickListener(this);
        button1.startAnimation(anim);

        listView = (ListView) findViewById(R.id.listView);
        HelperMethodClass hmc = new HelperMethodClass();
        listView.setAdapter(hmc.getLeaders(this,NAME_DB));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.translate);
        switch(v.getId()) {
            case R.id.button1:                      //Новая игра
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
