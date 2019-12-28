package com.blogspot.passovich.arcanoid.Dialogs;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import android.app.DialogFragment;
import android.widget.EditText;

import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.MenuActivity;
import com.blogspot.passovich.arcanoid.Multimedia.Sounds;
import com.blogspot.passovich.arcanoid.R;
import com.blogspot.passovich.arcanoid.StartActivity;

public class NewPlayerDialog extends DialogFragment implements View.OnClickListener {
    private final String TAG = "myLogs";
    public Context context;     //must be setted before onClick action
    public StartActivity sa;    //must be setted before onClick action
    private EditText editText1;
    private Button btnYes;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Resources res = getResources();

        getDialog().setTitle(res.getString(R.string.dialog_new_player_header));
        View v = inflater.inflate(R.layout.dialog_new_player,null);

        editText1 = (EditText) v.findViewById(R.id.editText1);
        btnYes = (Button) v.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(this);
        return v;
    }
    @Override
    public void onClick(View v) {
        Sounds s = new Sounds(context,new Properties(context));
        Animation anim =AnimationUtils.loadAnimation(context,R.anim.translate);
        switch (v.getId()){
            case R.id.btnYes:
                btnYes.startAnimation(anim);
                s.playSound(4);
                if (editText1.getText().toString().length() != 0) {
                    Properties p = new Properties(context);
                    p.playerName = editText1.getText().toString();
                    p.saveProperties();
                    Intent intent = new Intent(sa, MenuActivity.class);
                    startActivity(intent);
                    sa.finish();
                    dismiss();
                }
                break;
        }
        Log.d(TAG,"Dialog1:"+((Button)v).getText());
    }
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        Log.d(TAG,"Dialog1 onDismiss");

    }
    public void onCancel (DialogInterface dialog){
        super.onCancel(dialog);
        Log.d(TAG,"Dialog1 onCancel");
    }
}

