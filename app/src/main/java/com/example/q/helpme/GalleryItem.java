package com.example.q.helpme;

import android.graphics.Bitmap;

public class GalleryItem {

    private Bitmap bm;
    private String link;

    public Bitmap getBm(){
        return bm;
    }
    public String getLink() {return link;}


    public void setBm(Bitmap bm){
        this.bm = bm;
    }
    public void setLink(String link){
        this.link = link;
    }
}
