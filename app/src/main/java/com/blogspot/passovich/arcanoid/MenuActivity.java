package com.blogspot.passovich.arcanoid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.passovich.arcanoid.Data.File;
import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.Dialogs.NoSavedGamesDialog;
import com.blogspot.passovich.arcanoid.Dialogs.OptionsDialog;
import com.blogspot.passovich.arcanoid.Graphics.BackGroundSurfaceView;
import com.blogspot.passovich.arcanoid.Multimedia.Sounds;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "myLogs";
    private Button button1, button2, button3, button4, button5, button6, button7, button8, button9;
    private FrameLayout frameLayout;
    private LinearLayout linearLayout;

    private Sounds sounds;
    private BackGroundSurfaceView backGroundSurfaceView;
    private Display display;
    private Images images;
    private Properties p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        linearLayout = (LinearLayout) inflater.inflate(R.layout.activity_menu, null, false);
        frameLayout.addView(backGroundSurfaceView);
        frameLayout.addView(linearLayout);
        setContentView(frameLayout);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alphaprozrachn);
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button1.startAnimation(anim);
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button2.startAnimation(anim);
        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);
        button3.startAnimation(anim);
        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(this);
        button4.startAnimation(anim);
        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(this);
        button5.startAnimation(anim);
        button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(this);
        button6.startAnimation(anim);
        button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener(this);
        button7.startAnimation(anim);
        sounds = new Sounds(this, new Properties(this));
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        switch (view.getId()) {
            case R.id.button1:                      //Новая игра
                button1.startAnimation(anim);
                sounds.playSound(4);
                intent = new Intent(this, GameActivitySurfaceView.class);
                intent.putExtra("startType", "1");
                startActivity(intent);
                finish();
                break;
            case R.id.button2:                      //Продолжить игру
                button2.startAnimation(anim);
                sounds.playSound(4);
                if (!File.readFile("QS_" + new Properties(this).playerName, this).equals("fileNotFound")) {
                    intent = new Intent(this, GameActivitySurfaceView.class);
                    intent.putExtra("startType", "2");
                    startActivity(intent);
                    finish();
                } else {
                    NoSavedGamesDialog nsgd = new NoSavedGamesDialog();
                    nsgd.context = this;
                    nsgd.show(getFragmentManager(), "no_saved_game_dialog");
                }
                break;
            case R.id.button3:                      //Настройки
                sounds.playSound(4);
                button3.startAnimation(anim);
                OptionsDialog options = new OptionsDialog();
                options.context = this;
                options.p = p;
                options.show(getFragmentManager(), "options_dialog");
                break;
            case R.id.button4:                      //about
                sounds.playSound(4);
                button4.startAnimation(anim);
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button5:                      //Таблица лидеров
                button5.startAnimation(anim);
                sounds.playSound(4);
                intent = new Intent(this, LidersActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button6:                      //Смена игрока
                button6.startAnimation(anim);
                sounds.playSound(4);
                intent = new Intent(this, PlayerChangeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button7:                      //Выход
                button7.startAnimation(anim);
                sounds.playSound(4);
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume Menu_Activity");
        super.onResume();
        backGroundSurfaceView.startThread();
    }
    @Override
    protected void onPause() {
        Log.d(TAG, "onPause Menu_Activity");
        super.onPause();
        backGroundSurfaceView.stopThread();
    }
}
