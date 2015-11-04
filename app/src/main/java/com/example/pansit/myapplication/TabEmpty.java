package com.example.pansit.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Pansit on 6/2/2015.
 */
public class TabEmpty extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View view = inflater.inflate(R.layout.tab_challenge,null);
        View view = inflater.inflate(R.layout.tab_challenge,null);
        return view;
    }
}
