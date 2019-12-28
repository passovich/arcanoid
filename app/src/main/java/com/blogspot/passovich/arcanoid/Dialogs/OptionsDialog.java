package com.blogspot.passovich.arcanoid.Dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.R;

public class OptionsDialog extends DialogFragment implements View.OnClickListener,SeekBar.OnSeekBarChangeListener{
    final String TAG = "myLogs";
    public Properties p;
    public Context context;
    private Button btnYes;
    SeekBar soundsVolumeSeekBar,musicVolumeSeekBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Resources res = getResources();
        getDialog().setTitle(res.getString(R.string.dialog_options_header));
        View v = inflater.inflate(R.layout.dialog_options,null);
        btnYes = (Button) v.findViewById(R.id.button1);
        btnYes.setOnClickListener(this);
        soundsVolumeSeekBar = (SeekBar) v.findViewById(R.id.seekBar1);
        soundsVolumeSeekBar.setOnSeekBarChangeListener(this);
        musicVolumeSeekBar = (SeekBar) v.findViewById(R.id.seekBar2);
        musicVolumeSeekBar.setOnSeekBarChangeListener(this);
        soundsVolumeSeekBar.setProgress((int)(p.getSoundsVolume()*100));
        musicVolumeSeekBar.setProgress((int)(p.getMusicVolume()*100));
        return v;
        }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:break;
        }
        dismiss();
    }
    @Override
    public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser){
        if (seekBar == soundsVolumeSeekBar){
            p.soundsVolume = progress / 100.0f;
            Log.d(TAG,"Sounds volume = " + p.getSoundsVolume());
         }
        if (seekBar == musicVolumeSeekBar) {
            p.setMusicVolume(progress / 100.0f);
            Log.d(TAG,"Music volume = " + p.getMusicVolume());
        }
        p.saveProperties();
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar){ }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar){ }

    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        Log.d(TAG,"Loose_game_dialog onDismiss");
    }

    public void onCancel (DialogInterface dialog){
        super.onCancel(dialog);
        Log.d(TAG,"Loose_game_dialog onCancel");
    }
}
