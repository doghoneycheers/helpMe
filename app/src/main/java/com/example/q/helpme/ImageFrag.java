package com.example.q.helpme;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ImageFrag extends Fragment {
    public ImageFrag() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedlnstanceState) {
        View view = null;
            view = inflater.inflate(R.layout.imagefrag, container, false);
        return view;
    }
}
