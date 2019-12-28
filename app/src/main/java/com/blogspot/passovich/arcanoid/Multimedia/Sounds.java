package com.blogspot.passovich.arcanoid.Multimedia;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.R;

public class Sounds {
    private SoundPool soundPool;
    private int impactSound1,impactSound2,impactSound3,impactSound4,looseSound;     //ресурсы звуков
    private int menuClickSound,seekBarClickSound;         //звуки меню
    private Context context;
    private Properties p;
    public Sounds(Context context,Properties p) {
        this.p=p;
        this.context=context;
        //звуки
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        } else {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(5).build();
        }
        //Подключаем звуки через ресурсы
        impactSound1 = soundPool.load(context, R.raw.brick_hit_1, 1);
        impactSound2 = soundPool.load(context, R.raw.brick_hit_2, 1);
        impactSound3 = soundPool.load(context, R.raw.bumper_hit_2, 1);
        looseSound = soundPool.load(context, R.raw.loose_game, 1);

        menuClickSound = soundPool.load(context, R.raw.menu_click, 1);
        seekBarClickSound = soundPool.load(context, R.raw.seek_bar_sound, 1);
        impactSound4 = soundPool.load(context, R.raw.bonus_catch, 1);
    }
    public void playSound(int soundID){

        switch (soundID){
            case-1:soundPool.play(looseSound,p.soundsVolume,p.soundsVolume,1,0,1);break;
            case 1:soundPool.play(impactSound1,p.soundsVolume,p.soundsVolume,1,0,1);break;
            case 2:soundPool.play(impactSound2,p.soundsVolume,p.soundsVolume,1,0,1);break;
            case 3:soundPool.play(impactSound3,p.soundsVolume,p.soundsVolume,1,0,1);break;
            case 4:soundPool.play(menuClickSound,p.soundsVolume,p.soundsVolume,1,0,1);break;
            case 5:soundPool.play(seekBarClickSound,p.soundsVolume,p.soundsVolume,1,0,1);break;
            case 6:soundPool.play(impactSound4,p.soundsVolume,p.soundsVolume,1,0,1);break;
        }
    }
}
