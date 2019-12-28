package com.blogspot.passovich.arcanoid;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.Multimedia.Sounds;

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private static final String TAG="myLogs";
    private Button button1,button5,button6;
    private Sounds sounds;
    private Properties p;
    private SeekBar soundsVolumeSeekBar,musicVolumeSeekBar;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //Убрать statusBar
        setContentView(R.layout.activity_options);
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        soundsVolumeSeekBar = (SeekBar) findViewById(R.id.seekBar1);
        soundsVolumeSeekBar.setOnSeekBarChangeListener(this);
        musicVolumeSeekBar = (SeekBar) findViewById(R.id.seekBar2);
        musicVolumeSeekBar.setOnSeekBarChangeListener(this);
        p = new Properties(this);
        soundsVolumeSeekBar.setProgress((int)(p.soundsVolume*100));
        musicVolumeSeekBar.setProgress((int)(p.getMusicVolume()*100));
        sounds = new Sounds(this,p);
    }
    @Override
    public void onClick(View view) {
        Intent intent = null;
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        switch (view.getId()) {
            case R.id.button1:
                button1.startAnimation(anim);
                sounds.playSound(4);
                p.saveProperties();
                intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    @Override
    public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser){
        if (seekBar == soundsVolumeSeekBar){
            p.soundsVolume = progress / 100.0f;
        }
        if (seekBar == musicVolumeSeekBar) {
            p.setMusicVolume(progress / 100.0f);
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
    public void onStartTrackingTouch(SeekBar seekBar){ }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar){ }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
}
