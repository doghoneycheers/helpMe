package com.example.q.helpme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
         // 로딩이 끝난후 이동할 Activity
        int SPLASH_TIME=2000;

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("state", "launch");
        startActivity(intent);
        finish();

    }




}
