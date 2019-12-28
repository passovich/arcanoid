package com.blogspot.passovich.arcanoid.Dialogs;


import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.Multimedia.Sounds;
import com.blogspot.passovich.arcanoid.R;

public class NoSavedGamesDialog  extends DialogFragment implements View.OnClickListener {
    private Button btnYes;
    public Context context; //must be setted before onClick action

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Resources res = getResources();
        getDialog().setTitle(res.getString(R.string.warning_header));
        View v = inflater.inflate(R.layout.dialog_no_saved_game,null);
        btnYes = (Button) v.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(this);
        return v;
    }
    @Override
    public void onClick(View v) {
        Animation anim = AnimationUtils.loadAnimation(context,R.anim.translate);
        Sounds s = new Sounds(context,new Properties(context));
        switch (v.getId()){
            case R.id.btnYes:
                s.playSound(4);
                btnYes.startAnimation(anim);
                break;
        }
        dismiss();
    }
}
