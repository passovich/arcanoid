package com.blogspot.passovich.arcanoid;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.blogspot.passovich.arcanoid.Data.HelperMethodClass;
import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Dialogs.LooseGameDialog;
import com.blogspot.passovich.arcanoid.Graphics.GameSurfaceView;

import java.util.Random;

public class GameActivitySurfaceView extends Activity implements MediaPlayer.OnCompletionListener{
    private String TAG="myLogs";
    private Display display;
    private Properties p;
    private Images images;
    private GameSurfaceView gameSurfaceView;
    private LooseGameDialog looseGameDialog;

    private final String NAME_DB = "users.db";
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private int music[]={
            R.raw.music00,R.raw.music01,R.raw.music02,
            R.raw.music03,R.raw.music04,R.raw.music05,
            R.raw.music06,R.raw.music07,R.raw.music08
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //Убрать statusBar
        display = ((WindowManager)this.getSystemService(this.WINDOW_SERVICE)).getDefaultDisplay();
        p = new Properties(this, display.getHeight(), display.getWidth());
        images = new Images(this, p);
        Intent intent = getIntent();
        int startType = Integer.parseInt(intent.getStringExtra("startType"));

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mediaPlayer = MediaPlayer.create(this, music[0]);
        mediaPlayer.setOnCompletionListener(this);
        startRandomMusic();

        gameSurfaceView = new GameSurfaceView(this, p, images, startType, mediaPlayer,this);
        setContentView(gameSurfaceView);

        if(p.lives<=0)showLooseDialog();
    }
    public void showLooseDialog(){
        HelperMethodClass hmk = new HelperMethodClass();
        String []columns = {"scores","stage","key"};
        String[]data = {
                Integer.toString(p.getScores()),
                Integer.toString(p.currentStage),
                hmk.searchInColumn(
                        this,
                        NAME_DB,
                        "users",
                        "_id",
                        "name = ?",
                        p.playerName
                )
        };
        hmk.addLiaderToDB(
                this,
                NAME_DB,
                "users_scores",
                columns,
                data
        );
        if(mediaPlayer.isPlaying())mediaPlayer.stop();
        looseGameDialog = new LooseGameDialog();
        looseGameDialog.show(getFragmentManager(), "looseGameDialog");
        looseGameDialog.gasv = this;
    }
    public void startRandomMusic(){
        mediaPlayer.release();
        Random r = new Random();
        mediaPlayer = MediaPlayer.create(this, music[r.nextInt(9)]);
        mediaPlayer.setVolume(p.getMusicVolume(), p.getMusicVolume());
        mediaPlayer.start();
    }
    @Override
    public boolean onKeyDown(int keyKode, KeyEvent event){
        if (keyKode==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
    @Override
    public void onCompletion(MediaPlayer mediaPlayer)
    {
        startRandomMusic();
    }

    @Override
    protected void onStart(){
        Log.d(TAG,"onStart Game_Activity_Surface_View");
        super.onStart();
        mediaPlayer.start();
    }
    @Override
    protected void onRestart(){
        Log.d(TAG,"onRestart Game_Activity_Surface_View");
        super.onRestart();
        mediaPlayer.start();
    }
    @Override
    protected void onResume(){
        Log.d(TAG,"onResume Game_Activity_Surface_View");
        super.onResume();
        mediaPlayer.start();
    }
    @Override
    protected void onPause(){
        Log.d(TAG,"onPause Game_Activity_Surface_View");
        super.onPause();
        gameSurfaceView.pauseGame();
        if(mediaPlayer.isPlaying())mediaPlayer.pause();
    }
    @Override
    protected void onStop(){
        Log.d(TAG,"onStop Game_Activity_Surface_View");
        super.onStop();
    }
    @Override
    protected void onDestroy(){
        Log.d(TAG,"onDestroy Game_Activity_Surface_View");
        super.onDestroy();
        gameSurfaceView.pauseGame();
        if(mediaPlayer.isPlaying())mediaPlayer.stop();
    }
}