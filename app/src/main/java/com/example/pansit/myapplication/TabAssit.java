package com.example.pansit.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Pansit on 6/2/2015.
 */
public class TabAssit extends Fragment {

    Calendar calendar;

    Context context;

    ListView talkinglistView,dialogwaterlistview;

    Button comfirmButton;
    ImageButton talkagainButton;


    ArrayList<String> talking_list = new ArrayList<String>();
    ArrayList<String> nametalking_list = new ArrayList<String>();
    ArrayList<String> pictalking_list = new ArrayList<String>();  // 0 chuppy 1 me
    ArrayList<String>  checktalking = new ArrayList<String>();; // 0-- chuppy talking 1--member talking

    ArrayList<Food> food_list = new ArrayList<Food>();
    DataKeeper memberdata ;
    CharSequence oldText = "hi";



    boolean checkspeakend = false;


    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final int RESULT_OK = -1;

    public String outputtext="";
    public String conditext="";
    int foodadress = 0;

    public static  Dialog dialog;
    public static customgetfood_assit_Adapter adapter;
    public static ListView  dialogfoodlistview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = new ContextThemeWrapper(getActivity(), R.style.Theme_AppCompat_Light);//set theme for fragment
        LayoutInflater localInflater = inflater.cloneInContext(context);
        View view = localInflater.inflate(R.layout.tab_assit, container, false);

        calendar = Calendar.getInstance();
        memberdata = ((NewHome) getActivity()).getData();
        food_list =  ((NewHome)getActivity()).getFood_list();



        talkagainButton= (ImageButton) view.findViewById(R.id.button_talkagain);
        talkinglistView = (ListView) view.findViewById(R.id.talkinglistView);




        nametalking_list.add("Chuppy :");
        pictalking_list.add("0");
        talking_list.add("Talk to Chuppy by Click Microphone Button ");
        checktalking.add("0");


        customtalking_detail_home_Adapter talkingadapter = new customtalking_detail_home_Adapter(getActivity(),talking_list,nametalking_list,pictalking_list,checktalking);
        talkinglistView.setAdapter(talkingadapter);

        talkagainButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });




        //View view = inflater.inflate(R.layout.tab_diet,null);
        return view;

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((NewHome) activity).onSectionAttached(7);
    }


    // speed fun
    // 1
    public void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "th-TH");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something;");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                    "Sorry! Your device doesn't support speech input",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // 2
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    outputtext = result.get(0);

                    checkspeakend = true;
                    updatetext();
                    break;
                }


            }
        }


    }

    private void updatetext()
    {
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(checkspeakend == true)
        {

            nametalking_list.add(memberdata.getUser()+" : ");
            pictalking_list.add("1");
            talking_list.add(outputtext);
            checktalking.add("1");

            outputtext+=" "+conditext;
            outputtext+="     ";
            //outputtext = "i drink water 100";
            if (outputtext.toLowerCase().contains("help")) {

                CharSequence text;



                text = "Hello";
                if (text.equals(oldText)) {
                    text = "I'm Intelligent";
                }


                if (memberdata.getBreakfastFood().isEmpty() && (hour <= 9) && (hour >= 5)) {
                    text = "Good Morning! Don't forget your breakfast.";
                    if (text.equals(oldText)) {
                        text = "Drink water when you wake up can lose weight.";
                    }
                } else if (memberdata.getLunchFood().isEmpty() && (hour >= 11) && (hour < 14)) {
                    text = "Good Afternoon! Don't forget your lunch.";
                } else if (memberdata.getEveningFood().isEmpty() && (hour >= 17) && (hour <= 20)) {
                    text = "Good Evening! Don't forget your dinner.";
                } else if (!memberdata.getNightFood().isEmpty() && (hour >= 19)) {
                    text = "Stop eating after dinner to lose weight.";
                }
                oldText = text;
                Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();


            } else if (outputtext.toLowerCase().contains("eat") || outputtext.toLowerCase().contains(getResources().getString(R.string.assist_eat))) {

                int foodfound = 0;
                final ArrayList<Food> foodlistfound = new ArrayList<Food>();

                for(int i=0;i<food_list.size();i++)
                {
                    foodadress = i;
                    if(outputtext.toLowerCase().contains(food_list.get(i).getName()))
                    {

                        foodlistfound.add(food_list.get(i));
                        foodfound++;

                        //check num
                        String foodname = food_list.get(i).getName();
                        for(int j=0;j<outputtext.length();j++)
                        {

                            if(outputtext.charAt(j)==foodname.charAt(foodname.length()-1))
                            {
                                if(outputtext.charAt(j-1)==foodname.charAt(foodname.length()-2))
                                {
                                    if(outputtext.charAt(j-2)==foodname.charAt(foodname.length()-3))
                                    {

                                        if(outputtext.charAt(j+2) == '2')
                                        {
                                            foodlistfound.add(food_list.get(i));
                                            foodfound++;
                                        }
                                        else if(outputtext.charAt(j+2) == '3')
                                        {
                                            foodlistfound.add(food_list.get(i));
                                            foodlistfound.add(food_list.get(i));
                                            foodfound+=2;
                                        }
                                    }
                                }
                            }
                        }

                    }

                }

                //after serch food

                if(foodfound > 0)
                {

                    dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.tab_assi_confirm_dialog);

                    TextView textView = (TextView) dialog.findViewById(R.id.assi_dialog_txt);
                    Button confirmbtn = (Button) dialog.findViewById(R.id.assi_dialog_conbtn);
                    Button exitfirmbtn = (Button) dialog.findViewById(R.id.assi_dialog_exitbtn);
                    textView.setText("Chuppy: OK!! i know you are eating ");

                    dialogfoodlistview = (ListView) dialog.findViewById(R.id.fooddialog_assit);

                    adapter = new customgetfood_assit_Adapter(getActivity(), foodlistfound);
                    dialogfoodlistview.setAdapter(adapter);






                    confirmbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            for(int i=0;i<foodlistfound.size();i++)
                            {
                                Food food = foodlistfound.get(i);
                                memberdata.addCaloriesConsumed(foodlistfound.get(i).getCalories());
                                memberdata.addDailyFood(food, hour);

                            }


                            dialog.dismiss();
                        }});

                    exitfirmbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }


               /* if(outputtext.toLowerCase().contains("mama"))
                {

                    talking_list.add("Chuppy: OK!! i know you are eating mama");


                    for(int i=0;i<food_list.size();i++)
                    {

                        if( outputtext.toLowerCase().contains(food_list.get(i).getName()))
                        {
                            Food food = food_list.get(i);
                            memberdata.addCaloriesConsumed(food_list.get(i).getCalories());
                            memberdata.addDailyFood(food, hour);


                            talking_list.add("Insert mama to your profile");

                            break;
                        }

                    }

                }

                else
                {
                    talking_list.add("Chuppy: I don't know what are you eat ?");

                }
                */

            }
            else if (outputtext.toLowerCase().contains("activity") )
            {


            }else if ( outputtext.toLowerCase().contains("water")|| outputtext.toLowerCase().contains(getResources().getString(R.string.assist_drink))) {

                if(outputtext.matches(".*\\d.*"))
                {

                   /*
                        outputtext = outputtext.replaceAll("[^-?0-9]+", " ");

                        talking_list.add("Chuppy: Ok i insert " + outputtext + " mml. of water");
                        Double drinkwater = Double.parseDouble(outputtext);
                        memberdata.addWaterConsumed(drinkwater.intValue());
                        conditext ="";
                    */

                    outputtext = outputtext.replaceAll("[^-?0-9]+", " ");


                    dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.tab_assi_confirm_dialog);

                    TextView textView = (TextView) dialog.findViewById(R.id.assi_dialog_txt);
                    Button confirmbtn = (Button) dialog.findViewById(R.id.assi_dialog_conbtn);
                    Button exitfirmbtn = (Button) dialog.findViewById(R.id.assi_dialog_exitbtn);



                    textView.setText("Chuppy: You want insert " + outputtext + " mml. of water ?");
                    confirmbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Double drinkwater = Double.parseDouble(outputtext);
                            memberdata.addWaterConsumed(drinkwater.intValue());

                            nametalking_list.add("Chuppy : ");
                            pictalking_list.add("0");
                            talking_list.add(" Ok i insert " + outputtext + " mml. of water");
                            checktalking.add("0");

                            dialog.dismiss();
                        }});

                    exitfirmbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            dialog.dismiss();
                        }});
                    dialog.show();
                }
                else
                {
                    nametalking_list.add("Chuppy : ");
                    pictalking_list.add("0");
                    talking_list.add("i don't know how many water are you drink ?");
                    checktalking.add("0");
                }

            }
            else
            {
                nametalking_list.add("Chuppy : ");
                pictalking_list.add("0");
                talking_list.add("I don't know what are you talking about ");
                checktalking.add("0");

            }

            customtalking_detail_home_Adapter talkingadapter = new customtalking_detail_home_Adapter(getActivity(),talking_list,nametalking_list,pictalking_list,checktalking);
            talkinglistView.setAdapter(talkingadapter);


            talking_list = new ArrayList<String>();
            pictalking_list = new ArrayList<String>();
            nametalking_list = new ArrayList<String>();
            nametalking_list = new ArrayList<String>();

            checkspeakend = false;
        }

    }



}

