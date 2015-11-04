package com.example.pansit.myapplication;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pansit on 6/2/2015.
 */

//food per day
public class TabDiet_sub2 extends Fragment {

    ArrayList<ImageButton> imageButtons;
    ArrayList<Achievement> achievements;
    Dialog dialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context context = new ContextThemeWrapper(getActivity(), R.style.Theme_AppCompat_Light);//set theme for fragment
        LayoutInflater localInflater = inflater.cloneInContext(context);
        View view = localInflater.inflate(R.layout.tab_diet_sub2, container, false);

        imageButtons = new ArrayList<>();
        achievements = ((NewHome)getActivity()).achievements;

        ImageButton tmp = (ImageButton)view.findViewById(R.id.EatFastfood);
        imageButtons.add(tmp);
        tmp = (ImageButton)view.findViewById(R.id.Exercise);
        imageButtons.add(tmp);
        tmp = (ImageButton)view.findViewById(R.id.NoNoodle);
        imageButtons.add(tmp);
        tmp = (ImageButton)view.findViewById(R.id.Walk);
        imageButtons.add(tmp);
        tmp = (ImageButton)view.findViewById(R.id.NoSoda);
        imageButtons.add(tmp);
        tmp = (ImageButton)view.findViewById(R.id.Breakfast);
        imageButtons.add(tmp);

        for(int i = 6;i<8;i++){//7 achievements
            if(achievements.get(i).isDone()){//Done code
                imageButtons.get(i-6).setBackgroundColor(Color.rgb(255, 165, 0));
            }

        }


        imageButtons.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcouserdialog("CARBOHYDRATE BEGINNER", "-carbo 125g/day 5times", context);

            }
        });

        imageButtons.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcouserdialog("WATER BEGINNER", "-drink water for 1 day intake", context);

            }
        });
        return view;
    }
    private void showcouserdialog(String texttile,String textbody,Context context)
    {

        dialog = new Dialog(context);

        dialog.setTitle(texttile);
        dialog.setContentView(R.layout.dialog_speech);

        TextView sleeptext = (TextView)dialog.findViewById(R.id.sleeptext);
        sleeptext.setText(textbody);

        Button CloseButton = (Button)dialog.findViewById(R.id.CloseButton);
        CloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}