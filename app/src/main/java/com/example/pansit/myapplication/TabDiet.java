package com.example.pansit.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;


public class TabDiet extends Fragment {
    ListView listView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        final Context context = new ContextThemeWrapper(getActivity(), R.style.Theme_AppCompat_Light);//set theme for fragment
        LayoutInflater localInflater = inflater.cloneInContext(context);
        View view = localInflater.inflate(R.layout.tab_diet, container, false);

        listView =(ListView)view.findViewById(R.id.list_view);
        ArrayList<String> counsels = new ArrayList<String>();

        counsels.add("Basic course");
        counsels.add("develope coure1");
        counsels.add("develope coure2");
        counsels.add("develope coure3");

        listView.setAdapter(new ArrayAdapter(getActivity()
                , android.R.layout.simple_list_item_1
                , counsels));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    Fragment fragment;
                    FragmentManager fragmentManager = getFragmentManager();
                    fragment = new TabDiet_sub1();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }else if (position == 1){
                    Fragment fragment;
                    FragmentManager fragmentManager = getFragmentManager();
                    fragment = new TabDiet_sub2();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }else if (position == 2){
                    Fragment fragment;
                    FragmentManager fragmentManager = getFragmentManager();
                    fragment = new TabDiet_sub3();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }else if (position == 3){
                    Fragment fragment;
                    FragmentManager fragmentManager = getFragmentManager();
                    fragment = new TabDiet_sub4();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            }
        });

        //View view = inflater.inflate(R.layout.tab_diet,null);
        return view;


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((NewHome) activity).onSectionAttached(5);
    }



}
