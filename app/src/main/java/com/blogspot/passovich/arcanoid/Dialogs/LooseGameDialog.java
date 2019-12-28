package com.blogspot.passovich.arcanoid.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.DialogFragment;

import com.blogspot.passovich.arcanoid.GameActivitySurfaceView;
import com.blogspot.passovich.arcanoid.MenuActivity;
import com.blogspot.passovich.arcanoid.R;


    public class LooseGameDialog extends DialogFragment implements View.OnClickListener {
    final String TAG = "myLogs";
    public GameActivitySurfaceView gasv;
    private Button btnYes;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Resources res = getResources();
        getDialog().setTitle(res.getString(R.string.dialog_loose_game_header));
        View v = inflater.inflate(R.layout.dialog_loose_game,null);
        btnYes = (Button) v.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(this);
        return v;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnYes:
                Intent intent = new Intent(gasv, MenuActivity.class);
                startActivity(intent);
                gasv.finish();
                break;
        }
        dismiss();
    }
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        Log.d(TAG,"Loose_game_dialog onDismiss");
    }
    public void onCancel (DialogInterface dialog){
        super.onCancel(dialog);
        Log.d(TAG,"Loose_game_dialog onCancel");
    }
}
