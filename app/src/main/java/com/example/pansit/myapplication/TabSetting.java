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
public class TabSetting extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View view = inflater.inflate(R.layout.tab_diet,null);
        return inflater.inflate(R.layout.tab_diet,null);
    }
}
