package com.example.q.helpme;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class Gallery extends Fragment {

    static final int PICK_IMAGE = 1;
    public GridView gridViews;
    public static ArrayList<String> link = GalleryAdapter.getLinks();

    public Gallery() {}

    public List<String> getCameraImages() {
        String[] projection = new String[] { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
        Uri imageURI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cur = getContext().getContentResolver().query(imageURI, projection, null, null, null);
        Log.i("ListingImages", " query count=" + cur.getCount());
        ArrayList<String> imagePaths = new ArrayList<>(cur.getCount());
        int rawCol = cur.getColumnIndex(MediaStore.Images.Media.DATA);
        if (cur.moveToFirst()) {
            do {
                imagePaths.add(cur.getString(rawCol));
            } while (cur.moveToNext());
        }
        return imagePaths;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = null;
        view = inflater.inflate(R.layout.gallery, container, false);

        gridViews = view.findViewById(R.id.listview_gallery);

        dataSetting();

        //gridview item을 image와 link 보유.

        gridViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Log.d("img",link.get(position));
                Intent i = new Intent(getActivity().getApplicationContext(), ItemClick.class);
                i.putExtra("id", link.get(position));
                startActivity(i);
            }
        });

        return view;
    }

    private void dataSetting(){

        List<String> allimage = getCameraImages();
        int len = allimage.size();
        int index = 0;

        GalleryAdapter mAdapter = new GalleryAdapter();

        while(index < len){
            ImageView iv = new ImageView(getActivity().getApplicationContext());
            String imgpath = allimage.get(index++);
            Log.d("img path is ", imgpath);
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bm,300,300);
            mAdapter.addItem(thumbnail,imgpath);
            link.add(imgpath);
        }

        gridViews.setAdapter(mAdapter);
    }
}
