package com.example.q.helpme;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter {

    private ArrayList<GalleryItem> mItems = new ArrayList<>();
    public static ArrayList<String> links = new ArrayList<>();

    @Override
    public int getCount(){ return mItems.size(); }

    public static ArrayList<String> getLinks(){ return links; }

    @Override
    public GalleryItem getItem(int position) { return mItems.get(position); }

    @Override
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Context context = parent.getContext();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_sub, parent, false);
        }

        ImageView iv_img = (ImageView) convertView.findViewById(R.id.image) ;
        GalleryItem myItem = getItem(position);
        Log.d("links link is ", myItem.getLink());
        iv_img.setImageBitmap(myItem.getBm());
        return convertView;
    }

    public void addItem(Bitmap bm, String link){
        GalleryItem mItem = new GalleryItem();

        mItem.setBm(bm);
        mItem.setLink(link);

        mItems.add(mItem);

    }

}
