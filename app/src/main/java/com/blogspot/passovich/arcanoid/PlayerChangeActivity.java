package com.blogspot.passovich.arcanoid;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.blogspot.passovich.arcanoid.Data.HelperMethodClass;
import com.blogspot.passovich.arcanoid.Data.Images;
import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.Graphics.BackGroundSurfaceView;
import com.blogspot.passovich.arcanoid.Multimedia.Sounds;

public class PlayerChangeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Button button1;
    private EditText editText;
    private ListView listView;

    private final String TAG = "myLogs";
    private final String NAME_DB = "users.db";
    private final String TABLE_NAME="users";
    private HelperMethodClass helperMethodClass = new HelperMethodClass();
    private Sounds sounds;

    private FrameLayout frameLayout;
    private LinearLayout linearLayout;
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
        linearLayout = (LinearLayout) inflater.inflate(R.layout.activity_player_change, null, false);
        frameLayout.addView(backGroundSurfaceView);
        frameLayout.addView(linearLayout);
        setContentView(frameLayout);

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.alphaprozrachn);
        sounds = new Sounds(this,new Properties(this));
        button1 = (Button) findViewById(R.id.button1);button1.setOnClickListener(this);
        button1.startAnimation(anim);
        listView = (ListView) findViewById(R.id.listView);
        editText = (EditText) findViewById(R.id.editText1);
        editText.clearFocus();

        listView.setAdapter(helperMethodClass.getUsers(this, NAME_DB, TABLE_NAME));
        listView.setOnItemClickListener(this);
     }

    @Override
    public void onClick(View view) {
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.translate);
        switch (view.getId()){
            case R.id.button1:sounds.playSound(4);
                button1.startAnimation(anim);
                sounds.playSound(4);
                if (editText.getText().toString().length()>0){
                    Properties p = new Properties(this);
                    p.playerName = editText.getText().toString();
                    p.saveProperties();
                    String tempStr = helperMethodClass.searchInColumn(
                            this,
                            NAME_DB,
                            "users",
                            "name",
                            "name = ?",
                            editText.getText().toString()
                    );
                    if (tempStr.equals("no_records")) {
                        helperMethodClass.addPlayerToDB(this, NAME_DB,"users", "name", editText.getText().toString());
                    }
                    Intent intent;
                    intent = new Intent(this,MenuActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView <?> arg0, View view, int position,long id){
        Properties p = new Properties(this);
        p.playerName = helperMethodClass.searchInColumn(
                this,
                NAME_DB,
                "users",
                "name",
                "_id = ?",
                Integer.toString(position+1)
        );
        p.saveProperties();
        Intent intent;
        intent = new Intent(this,MenuActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onKeyDown(int keyKode, KeyEvent event){
        if (keyKode == KeyEvent.KEYCODE_BACK){
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