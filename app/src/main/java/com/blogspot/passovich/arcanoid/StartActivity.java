package com.blogspot.passovich.arcanoid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;


import com.blogspot.passovich.arcanoid.Data.HelperMethodClass;
import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.Dialogs.NewPlayerDialog;

public class StartActivity extends Activity implements View.OnClickListener {
    private LinearLayout linearLayout;
    private Properties p;

    private final int ACTUAL_DB_VERSION = 1;
    private final String NAME_DB = "users.db";
    private HelperMethodClass helperMethodClass = new HelperMethodClass();
    private NewPlayerDialog newPlayerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout1);linearLayout.setOnClickListener(this);

        helperMethodClass.getDBOnFirstStart(this,NAME_DB,ACTUAL_DB_VERSION);
        p = new Properties(this);
        if (p.playerName.equals("none")){
            newPlayerDialog = new NewPlayerDialog();
            newPlayerDialog.show(getFragmentManager(), "newPlayerDialog");
            newPlayerDialog.context = this;
            newPlayerDialog.sa = this;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,MenuActivity.class);
        startActivity(intent);
        finish();
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
}
