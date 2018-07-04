package com.example.q.helpme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import uk.co.senab.photoview.PhotoViewAttacher;


public class ItemClick extends Activity {

    ImageView imageView;
    PhotoViewAttacher mAttacher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagefrag);

        // get intent data

        Intent i = getIntent();

        // Selected image id
        String link = i.getExtras().getString("id");

        Log.d("position is here", link);
        Bitmap bm = BitmapFactory.decodeFile(link);

        imageView = findViewById(R.id.image);
        imageView.setImageBitmap(bm);
        mAttacher = new PhotoViewAttacher(imageView);
    }
}