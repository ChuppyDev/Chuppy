package com.example.pansit.myapplication;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
//prepare lose weight

public class TabDiet_sub1 extends Fragment {


    ImageButton bananaButon,waterButton,sleepbutton,time_sleepButton,apple_greenButton,no_coffeeeButton;
    ArrayList<ImageButton> imageButtons = new ArrayList<>();
    ArrayList<Achievement> achievements;
    Dialog dialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context context = new ContextThemeWrapper(getActivity(), R.style.Theme_AppCompat_Light);//set theme for fragment
        LayoutInflater localInflater = inflater.cloneInContext(context);
        View view = localInflater.inflate(R.layout.tab_diet_sub1, container, false);

        achievements = ((NewHome)getActivity()).achievements;

        sleepbutton = (ImageButton)view.findViewById(R.id.sleepbutton);
        time_sleepButton = (ImageButton)view.findViewById(R.id.time_sleepButton);
        waterButton = (ImageButton)view.findViewById(R.id.drinkWaterbutton);
        apple_greenButton = (ImageButton)view.findViewById(R.id.apple_greenButton);
        no_coffeeeButton = (ImageButton)view.findViewById(R.id.noCoffee);
        bananaButon = (ImageButton)view.findViewById(R.id.bananaButton);
        //sleepbutton,time_sleepButton,situpButton,detoxButton,apple_greenButton,exerciseButton,
        // no_coffeeeButton,no_carboButton,no_algoholButton,brownriceButton
        imageButtons.add(sleepbutton);
        imageButtons.add(time_sleepButton);
        imageButtons.add(apple_greenButton);
        imageButtons.add(waterButton);
        imageButtons.add(no_coffeeeButton);
        imageButtons.add(bananaButon);

        for(int i = 0;i<=5;i++){//7 achievements
            if(achievements.get(i).isDone()){//Done code
                imageButtons.get(i).setBackgroundColor(Color.rgb(255, 165, 0));
            }

        }

        sleepbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcouserdialog("Sleep Time", "sleep more than 6 hours.", context);

            }
        });

        time_sleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcouserdialog("First Healthy", "-3 meal in a day\n-calories not over the limit", context);

                //howcouserdialog("First Unfat", "-don't eat fat more than 30% of calories intake", context);


            }
        });

        apple_greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //showcouserdialog("First Drink", "-first time drink", context);
                showcouserdialog("First Unfat", "-don't eat fat more than 30% of calories intake", context);

            }
        });

        waterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcouserdialog("First Water Reach", "-drink water reach in day", context);

            }
        });

        no_coffeeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcouserdialog("First Protein", "-eat protein more than xxx g in a day", context);

            }
        });

        bananaButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcouserdialog("First Exercise", "-first time exercise", context);

            }
        });
/* showcouserdialog("Carbohydrate Comtrol I", "-control carbohydrate 125g per day 5 times", context);
        imageButtons.get(6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcouserdialog("First Exercise", "-first time exercise", context);

            }
        });

        imageButtons.get(7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcouserdialog("First Drink", "-first time drink", context);

            }
        });

        */
        /*
       //button 1


        sleepbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcouserdialog("Sleep", "Going to bed before 10 a.m.", context);

            }
        });


        //button 2


        time_sleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcouserdialog("Sleep Time", "sleep more than 6 hours.", context);


            }
        });

        //button 3

        situpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcouserdialog("Sit up", "Sit up 20 times a day : 5 terms", context);


            }
        });

        //button 4

        detoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textInput = "Eat before breakfast at 5.30 - 7.00 a.m.c" +
                        "- Natural  flavor yogurt without mixing = half a cup of fruit in yogurt.\n" +
                        "- 100% fresh milk, not skim recipes. Do not refrigerate = 180-250 ml size\n" +
                        "- honey = 1 tablespoon \n" +
                        "- Lemon = 1 fruit";
                showcouserdialog("Detox", textInput, context);

            }
        });

        //button 5

        apple_greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcouserdialog("Fruit", "Green apple = 1 fruit\n" +
                        "5 terms", context);

            }
        });

        //button 6

        exerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcouserdialog("Exercise", "Exercise 30 minutes / days / weeks.\n" +
                        "4 terms", context);

            }
        });

        //button 7

        no_coffeeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcouserdialog("Drink prohibited", "No coffee 7 times in 1 month", context);

            }
        });

        //button 8

        no_carboButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcouserdialog("Carbohydrate", "carbohydrates 125 g / day.\n" +
                        "5 terms", context);


            }
        });

        //button 9

        no_algoholButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcouserdialog("Drink prohibited", "Not drink alcohol two weeks.", context);

            }
        });

        //button 10

        brownriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcouserdialog("Rice", "Eat Brown rice\n" +
                        "5 terms", context);

            }
        });

        */
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