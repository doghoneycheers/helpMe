package com.example.q.helpme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Chewy.ttf");

        TextView textView = findViewById(R.id.helper);
        textView.setTypeface(typeFace);

        Handler hd = new Handler();

        hd.postDelayed(new splashHandler() , 2000); // 3초 후에 splashHandler 실행
    }

    private class splashHandler implements Runnable{
        public void run() {
            startActivity(new Intent(getApplication(), MainActivity.class)); // 로딩이 끝난후 이동할 Activity
            SplashActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }
    }




}
