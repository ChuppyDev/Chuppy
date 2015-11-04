package com.example.pansit.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pansit on 6/2/2015.
 */

//breakfast

public class TabDiet_sub4 extends Fragment {

    ListView listView;
    ArrayList<Food> foodList = new ArrayList<>();
    CustomListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View view = inflater.inflate(R.layout.tab_challenge,null);
        View view = inflater.inflate(R.layout.tab_diet_sub4,null);

        listView = (ListView) view.findViewById(R.id.list);
        adapter = new CustomListAdapter(getActivity(),foodList);
        foodList.add(new Food("Mama", "don't know", "Fried", 10,5,4,3,3,2));

        foodList.add(new Food("Madma", "don't know", "Fried", 10,5,4,3,3,2));
        listView.setAdapter(adapter);


        return view;
    }
}