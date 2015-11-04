package com.example.pansit.myapplication;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * Created by Pansit on 6/2/2015.
 */
@SuppressWarnings("deprecation")
public class TabListener implements ActionBar.TabListener {

    Fragment fragment;

    public TabListener(Fragment fm){
        fragment=fm;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

        ft.replace(R.id.fragmentContainer,fragment);

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        ft.remove(fragment);

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
