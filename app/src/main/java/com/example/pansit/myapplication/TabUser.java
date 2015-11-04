package com.example.pansit.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by Pansit on 6/2/2015.
 */
public class TabUser extends Fragment {


    TextView calBurnTxt,waterGetTxt,calGetToday,calMaxToday,calBurnMax,waterMaxTxt,calSlash,calUnit,graphtext, lvltext;
    ImageView btn_arrowback,btn_arrownext;

    //Button waterButton;
    LinearLayout foodButton,activityButton,reportButton,assistanceButton;
    LinearLayout calBox,calburnBox;



    //check show graph
    int graphnow = 1;


    boolean isOpen;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home, container, false);
        final Context context = new ContextThemeWrapper(getActivity(), R.style.Theme_AppCompat_Light);//set theme for fragment
        //isOpen = false;


        final DataKeeper data  = ((NewHome) getActivity()).getData();




        lvltext = (TextView) view.findViewById(R.id.lvltext);
        lvltext.setText(""+data.getlvl());

        ProgressBar progressBarlvl;
        progressBarlvl = (ProgressBar) view.findViewById(R.id.lvlprogess);
        progressBarlvl.setMax(100);
        progressBarlvl.setProgress(data.getLvlexp());





        // setgraph


        foodButton = (LinearLayout)view.findViewById(R.id.foodButton);
        activityButton = (LinearLayout)view.findViewById(R.id.activityButton);
        reportButton = (LinearLayout)view.findViewById(R.id.reportButton);
        assistanceButton = (LinearLayout)view.findViewById(R.id.assistantButton);

/*  ArrayList<Food> breakfastFood;
    ArrayList<Food> lunchFood;
    ArrayList<Food> eveningFood;
    ArrayList<Food> nightFood;

        breakfastFood = data.getBreakfastFood();
        lunchFood = data.getLunchFood();
        eveningFood = data.getEveningFood();
        nightFood = data.getNightFood();

        //calBox = (LinearLayout) view.findViewById(R.id.calBox);
        //calburnBox = (LinearLayout) view.findViewById(R.id.calburnbox);
        //calGetToday = (TextView) view.findViewById(R.id.calGetToday);
        //calMaxToday = (TextView) view.findViewById(R.id.calMaxToday);
        //calSlash = (TextView) view.findViewById(R.id.calSlash1);
        //calUnit = (TextView) view.findViewById(R.id.calUnit);

        // waterButton = (Button) view.findViewById(R.id.waterButton);
        //calMaxToday.setText("" + data.getCaloriesPerDay());
        //calGetToday.setText("" + data.getCaloriesConsumed());
        //calBurnTxt.setText("" + data.getCaloriesBurned());
        //calBurnMax.setText("" + data.getCaloriesConsumed());
        //waterGetTxt.setText("" + data.getWaterConsumed());
        //waterMaxTxt.setText("" + data.getWaterPerDay());

*/

        /*
        calBox.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                      arr_list = getFoodHistoryToDialog();
                      final Dialog dialog = new Dialog(context);
                      dialog.setTitle("Food History");
                      dialog.setContentView(R.layout.foodhistory_dialog);

                      ListView listView = (ListView) dialog.findViewById(R.id.foodhistory_listview);
                      ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arr_list);
                      listView.setAdapter(adapter);

                      Button button = (Button) dialog.findViewById(R.id.foodhistoryclose);
                      button.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {

                              dialog.dismiss();

                          }});

                      dialog.show();

                  }

              }

        );
        */





        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewHome) getActivity()).changeFragment(1);

            }
        });

        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewHome) getActivity()).changeFragment(3);
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewHome) getActivity()).changeFragment(5);
            }
        });

        assistanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewHome) getActivity()).changeFragment(6);
            }
        });


        final PieGraph pg = (PieGraph)view.findViewById(R.id.graph);
        pg.removeSlices();

        ListView detail_listview = (ListView)view.findViewById(R.id.listView_detail_home);
        detail_listview.setAdapter(null);

        TextView graph1_percen = (TextView)view.findViewById(R.id.graph1_score);
        TextView graph1_percen_text = (TextView)view.findViewById(R.id.percent_1);

        TextView graph2_percen = (TextView)view.findViewById(R.id.graph2_score);
        TextView graph2_percen_text = (TextView)view.findViewById(R.id.percent_2);

        ArrayList<String> detailname = new ArrayList<String>();
        ArrayList<String> valname = new ArrayList<String>();
        ArrayList<String> colorname = new ArrayList<String>();
        ArrayList<String> percentext = new ArrayList<String>();
        ArrayList<Integer> valpercen = new ArrayList<Integer>();
        customdetail_home_Adapter adapter;


        int calperday = data.getCaloriesPerDay();
        int caleat = data.getCaloriesConsumed();
        int calburn = data.getCaloriesBurned();


        int percentval01 = 0;
        int percentval02 = 0;


        // graph1
        String textcolor1 = "#ff262626";
        String textcolor2 = "#fffd8409";


        pg.setDuration(2000);//default if unspecified is 300 ms



        percentval01 =  caleat*100/(calperday);



        if(calperday >= caleat)
        {
            caleat = (caleat *100)/calperday;
            calperday = 100;
        }
        else
        {
            int calmore = caleat%calperday;
            caleat =  (calmore *100)/calperday;
            calperday = 100;

            textcolor1 = "#fffd8409";
            textcolor2 = "#FFB01F1B";
        }

        PieSlice slice = new PieSlice();

        slice = new PieSlice();
        slice.setColor(Color.parseColor(textcolor1));
        slice.setValue(calperday-caleat);
        pg.addSlice(slice);

        slice = new PieSlice();
        slice.setColor(Color.parseColor(textcolor2));
        slice.setValue(caleat);
        pg.addSlice(slice);

        pg.setInnerCircleRatio(220);
        pg.setPadding(5);







        // graph2

        String textcolor3 = "#ff262626";
        String textcolor4 = "#ff16b405";




        PieGraph pg2 = (PieGraph)view.findViewById(R.id.graph2);;
        // pg2.setDuration(2000);//default if unspecified is 300 ms

        calburn = data.getCaloriesBurned();
        caleat = data.getCaloriesConsumed();

        if(calburn!=0)
        {

            percentval02 =  calburn*100/(caleat);

        }
        else
        {
            percentval02 =  0;

        }



        if(caleat >= calburn)
        {
            if(caleat != 0) {
                calburn = (calburn * 100) / caleat;
            }
            caleat = 100;
        }
        else
        {
            int calmore = calburn - caleat;
            calburn =  (calmore *100)/caleat;
            caleat = 100;

            textcolor3 = "#ff16b405";
            textcolor4 = "#ff438424";
        }




        PieSlice slice2 = new PieSlice();
        slice2.setColor(Color.parseColor(textcolor3));
        slice2.setValue(caleat - calburn);
        slice2.setTitle("CAL ALL");
        pg2.addSlice(slice2);


        slice2 = new PieSlice();
        slice2.setColor(Color.parseColor(textcolor4));
        slice2.setValue(calburn);
        pg2.addSlice(slice2);


        pg2.setInnerCircleRatio(220);
        pg2.setPadding(5);



        // list view
        detailname.add("Calorie Require : ");
        valname.add(data.getCaloriesConsumed() + " / " + data.getCaloriesPerDay() + " cal");
        colorname.add(textcolor2);
        percentext.add(percentval01+"%");
        valpercen.add(percentval01);

        detailname.add("Calorie Burn : ");
        valname.add(data.getCaloriesBurned() +" / "+data.getCaloriesConsumed() + " cal");
        colorname.add(textcolor4);
        percentext.add(percentval02+"%");
        valpercen.add(percentval02);

        graph1_percen.setText(percentval01 + "");
        graph1_percen.setTextColor(Color.parseColor(textcolor2));
        graph1_percen_text.setTextColor(Color.parseColor(textcolor2));

        graph2_percen.setText(percentval02 + "");
        graph2_percen.setTextColor(Color.parseColor(textcolor4));
        graph2_percen_text.setTextColor(Color.parseColor(textcolor4));


        adapter = new customdetail_home_Adapter(getActivity(), detailname,colorname,valname,valpercen);


        detail_listview.setAdapter(adapter);



        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((NewHome) activity).onSectionAttached(1);
    }

    public void showToast(CharSequence text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

/*
    private ArrayList<String> getFoodHistoryToDialog() {
        ArrayList arr_list = new ArrayList<String>();
        arr_list.add("*MORNING*");
        String tmp = " ";
        int c = 1;
        if (!breakfastFood.isEmpty()) {
            for (int i = 0; i < breakfastFood.size(); i++) {
                if (!tmp.equals(breakfastFood.get(i).getName())) {
                    c = 1;
                    arr_list.add("->" + breakfastFood.get(i).getName() + " ( " + breakfastFood.get(i).getCalories() + " Cal. ) ");
                } else {
                    c++;
                    arr_list.set(i - 1, "->" + breakfastFood.get(i).getName() + ". x" + c + " ( " + c * breakfastFood.get(i).getCalories() + " Cal. ) ");
                }


                tmp = breakfastFood.get(i).getName();
            }
        }
        tmp = " ";
        c = 1;
        arr_list.add("*NOON*");
        if (!lunchFood.isEmpty()) {
            for (int i = 0; i < lunchFood.size(); i++) {
                if (!tmp.equals(lunchFood.get(i).getName())) {
                    c = 1;
                    arr_list.add("->" + lunchFood.get(i).getName() + " ( " + lunchFood.get(i).getCalories() + " Cal. ) ");
                } else {
                    c++;
                    arr_list.set(i - 1, "->" + lunchFood.get(i).getName() + ". x" + c + " ( " + c * lunchFood.get(i).getCalories() + " Cal. ) ");
                }


                tmp = lunchFood.get(i).getName();
            }
        }
        tmp = " ";
        c = 1;
        arr_list.add("*EVENING & NIGHT*");
        if (!eveningFood.isEmpty()) {
            for (int i = 0; i < eveningFood.size(); i++) {
                if (!tmp.equals(eveningFood.get(i).getName())) {
                    c = 1;
                    arr_list.add("->" + eveningFood.get(i).getName() + " ( " + eveningFood.get(i).getCalories() + " Cal. ) ");
                } else {
                    c++;
                    arr_list.set(i - 1, "->" + eveningFood.get(i).getName() + ". x" + c + " ( " + c * eveningFood.get(i).getCalories() + " Cal. ) ");
                }


                tmp = eveningFood.get(i).getName();
            }
        }
        tmp = " ";
        c = 1;
        if (!nightFood.isEmpty()) {
            for (int i = 0; i < nightFood.size(); i++) {
                if (!tmp.equals(nightFood.get(i).getName())) {
                    c = 1;
                    arr_list.add("->" + nightFood.get(i).getName() + " ( " + nightFood.get(i).getCalories() + " Cal. ) ");
                } else {
                    c++;
                    arr_list.set(i - 1, "->" + nightFood.get(i).getName() + ". x" + c + " ( " + c * nightFood.get(i).getCalories() + " Cal. ) ");
                }


                tmp = nightFood.get(i).getName();
            }
        }
        return arr_list;
    }
    */

    public void graphswitch(DataKeeper data ,View view)
    {



        /*else if(graphnow == 3 )
        {
            graphtext.setText("Nutrorail today");

            PieGraph pg3 = pg;

            int fatval=1,choresval=1,sodiumval=1,carboval=1,proteinval=1;
            for(int i=0;i < data.getTodayFood().size();i++)
            {

                fatval += data.getTodayFood().get(i).getFat();
                choresval += data.getTodayFood().get(i).getCalories();
                sodiumval += data.getTodayFood().get(i).getSodium();
                carboval += data.getTodayFood().get(i).getCarbohydrate();
                proteinval += data.getTodayFood().get(i).getProtein();


            }

            int percentval01 = 0;
            int percentval02 = 0;
            int percentval03 = 0;
            int percentval04 = 0;
            int percentval05 = 0;

            int sumofval = fatval+choresval+sodiumval+carboval+proteinval-5;
            if(sumofval !=0)
            {

                percentval01 = (fatval-1)*100/sumofval;
                percentval02 = (choresval-1)*100/sumofval;
                percentval03 = (sodiumval-1)*100/sumofval;
                percentval04 = (carboval-1)*100/sumofval;
                percentval05 = (proteinval-1)*100/sumofval;
            }



            // 1 fat
            PieSlice slice = new PieSlice();
            slice.setColor(Color.parseColor("#fffd8409"));
            slice.setValue(fatval);
            pg3.addSlice(slice);

            // 2 Cholesterol
            slice = new PieSlice();
            slice.setColor(Color.parseColor("#FFD10AA6"));
            slice.setValue(choresval);
            pg3.addSlice(slice);


            // 3 Sodium
            slice = new PieSlice();
            slice.setColor(Color.parseColor("#FF34CB14"));
            slice.setValue(sodiumval);
            pg3.addSlice(slice);

            // 4 Carbohydrate
            slice = new PieSlice();
            slice.setColor(Color.parseColor("#ff2f95be"));
            slice.setValue(carboval);
            pg3.addSlice(slice);

            // 5 Protein
            slice = new PieSlice();
            slice.setColor(Color.parseColor("#FFFDEB37"));
            slice.setValue(proteinval);
            pg3.addSlice(slice);

            pg3.setInnerCircleRatio(200);
            pg3.setPadding(5);


            // list view
            detailname.add("Fat Today : ");
            valname.add(fatval - 1 + " mmg");
            colorname.add("#fffd8409");
            percentext.add(percentval01+"%");

            detailname.add("Cholesterol Today : ");
            valname.add(choresval-1 + " mmg");
            colorname.add("#FFD10AA6");
            percentext.add(percentval02+"%");

            detailname.add("Sodium Today : ");
            valname.add(sodiumval-1 + " mmg");
            colorname.add("#FF34CB14");
            percentext.add(percentval03+"%");

            detailname.add("Carbohydrate Today : ");
            valname.add(carboval-1 + " mmg");
            colorname.add("#ff2f95be");
            percentext.add(percentval04+"%");

            detailname.add("Protein Today : ");
            valname.add(proteinval-1 + " mmg");
            colorname.add("#FFFDEB37");
            percentext.add(percentval05+"%");

            adapter = new customdetail_home_Adapter(getActivity(), detailname,colorname,valname,percentext);
            detail_listview.setAdapter(adapter);

        }
        else if(graphnow <= 0)
        {
            graphnow = 3;
            graphswitch(data,view);

        }
        else
        {

            graphnow = 1;
            graphswitch(data,view);
        }

        */
        //-----------


    }




}
