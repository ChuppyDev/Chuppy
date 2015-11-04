package com.example.pansit.myapplication;

import android.animation.Animator;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.echo.holographlibrary.HoloGraphAnimate;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Pansit on 6/2/2015.
 */
public class TabReports extends Fragment {

    final int AmountBar = 30;
    final int BarColor = Color.parseColor("#fffb8209");


    DataKeeper data;
    BarGraph barGraph;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_report, container, false);
        barGraph = (BarGraph)view.findViewById(R.id.barGraph);
        data =  ((NewHome)getActivity()).getData();

        Spinner dropdown = (Spinner) view.findViewById(R.id.spinnerReport);
        String[] items = new String[]{"Calories You Eat", "Calories You Burn", "Water You Drink"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Calendar calendar = Calendar.getInstance();
                    ArrayList<Bar> points = new ArrayList<Bar>();
                    for (int i = 0; i < AmountBar; i++) {
                        Bar bar = new Bar();
                        bar.setColor(BarColor);
                        bar.setName(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH));
                        if (i == 0) {
                            bar.setGoalValue(data.getCaloriesConsumed());
                        } else {
                            bar.setGoalValue(data.caloriesOnDay.get(calendar.get(Calendar.DAY_OF_MONTH)));
                        }
                        points.add(bar);
                        calendar.add(Calendar.DATE, -1);


                        for (Bar b : barGraph.getBars()) {
                            b.setValue(0);
                        }
                        barGraph.setBars(points);
                        barGraph.setDuration(0);//default if unspecified is 300 ms
                        barGraph.setInterpolator(new AccelerateDecelerateInterpolator());//Only use over/undershoot  when not inserting/deleting
                        barGraph.animateToGoalValues();
                        barGraph.setValueStringPrecision(0); //1 decimal place. 0 by default for integers.

                    }
                } else if (position == 1) {
                    Calendar calendar = Calendar.getInstance();
                    ArrayList<Bar> points = new ArrayList<Bar>();
                    for (int i = 0; i < AmountBar; i++) {
                        Bar bar = new Bar();
                        bar.setColor(BarColor);
                        bar.setName(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH));
                        if (i == 0) {
                            bar.setGoalValue(data.getCaloriesBurned());
                        } else {
                            bar.setGoalValue(data.caloriesBurnOnDay.get(calendar.get(Calendar.DAY_OF_MONTH)));
                        }
                        points.add(bar);
                        calendar.add(Calendar.DATE, -1);

                        barGraph.setValueStringPrecision(0);

                        for (Bar b : barGraph.getBars()) {
                            b.setValue(0);
                        }
                        barGraph.setBars(points);
                        barGraph.setDuration(0);//default if unspecified is 300 ms

                        barGraph.setInterpolator(new AccelerateDecelerateInterpolator());//Only use over/undershoot  when not inserting/deleting
                        barGraph.animateToGoalValues();
                        barGraph.setValueStringPrecision(0); //1 decimal place. 0 by default for integers.


                    }

                } else if (position == 2){
                    Calendar calendar = Calendar.getInstance();
                    ArrayList<Bar> points = new ArrayList<Bar>();
                    for (int i = 0; i < AmountBar; i++) {
                        Bar bar = new Bar();
                        bar.setColor(BarColor);
                        bar.setName(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH));
                        if (i == 0) {
                            bar.setGoalValue(data.getWaterConsumed());
                        } else {
                            bar.setGoalValue(data.waterOnDay.get(calendar.get(Calendar.DAY_OF_MONTH)));
                        }
                        points.add(bar);
                        calendar.add(Calendar.DATE, -1);

                        barGraph.setValueStringPrecision(0);


                        for (Bar b : barGraph.getBars()) {
                            b.setValue(0);
                        }
                        barGraph.setBars(points);
                        barGraph.setDuration(0);//default if unspecified is 300 ms

                        barGraph.setInterpolator(new AccelerateDecelerateInterpolator());//Only use over/undershoot  when not inserting/deleting
                        barGraph.animateToGoalValues();
                        barGraph.setValueStringPrecision(0); //1 decimal place. 0 by default for integers.
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




         //1 decimal place. 0 by default for integers.



        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((NewHome) activity).onSectionAttached(6);
    }
}
