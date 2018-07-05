package com.example.q.helpme;

import com.example.q.helpme.GridViewImageAdapter;
import com.example.q.helpme.AppConstant;
import com.example.q.helpme.Utils;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;


import java.util.ArrayList;
import java.util.Comparator;

import android.Manifest;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.TypedValue;
import android.widget.GridView;

public class GridViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Utils utils;
    private ArrayList<String> imagePaths = new ArrayList<String>();
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private int columnWidth;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public GridViewFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_grid_view, container, false);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return view;
        }

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout_gridview);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        gridView = (GridView) view.findViewById(R.id.grid_view);

        utils = new Utils(getContext());

        // Initilizing Grid View
        InitilizeGridLayout();

        // loading all image paths from SD card
        imagePaths = utils.getFilePaths();

        // Gridview adapter
        adapter = new GridViewImageAdapter(getActivity(), imagePaths,
                columnWidth);

        // setting grid view adapter
        gridView.setAdapter(adapter);

        return view;
    }

    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConstant.GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

        gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){

                utils = new Utils(getContext());

                // Initilizing Grid View
                InitilizeGridLayout();

                // loading all image paths from SD card
                imagePaths = utils.getFilePaths();

                // Gridview adapter
                adapter = new GridViewImageAdapter(getActivity(), imagePaths,
                        columnWidth);

                // setting grid view adapter
                gridView.setAdapter(adapter);

                mSwipeRefreshLayout.setRefreshing(false);
            }
        },500);
    }
}