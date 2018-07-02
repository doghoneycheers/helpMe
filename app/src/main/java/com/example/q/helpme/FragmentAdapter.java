package com.example.q.helpme;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private int tabCount;
    public FragmentAdapter(FragmentManager fm, int tabCount){
        super(fm);
        this.tabCount=tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                return new Contacts();
            case 1 :
                return new Gallery();
            case 2 :
                return new Fragment3();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
