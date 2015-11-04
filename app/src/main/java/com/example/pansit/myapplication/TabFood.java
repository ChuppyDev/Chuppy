package com.example.pansit.myapplication;


import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Queue;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


/**
 * Created by Pansit on 6/2/2015.
 */
@SuppressWarnings("ALL")
public class TabFood extends Fragment    {
    EditText searchTxt;
    Calendar calendar;
    ListView listView;
    ArrayList<Food> food_list = new ArrayList<Food>();
    int nowPosition;
    TextView calSum,calText;
    ImageButton menu1,menu2,menu3;
    String noodleName;
    int noodleCal = 0;
    int soupCal = 0;
    int additionCal = 0;
    CustomListAdapter adapter;

    // speech val
    ImageButton btnspeedfood;
    LinearLayout recentButton;
    LinearLayout favButton;
    LinearLayout suggestButton;

    LinearLayout gridMenu;

    Queue<Integer> recentIndex;
    ArrayList<Integer> favIndex;

    boolean isRecentClicked = false;
    boolean isFavClicked = false;
    boolean isFavMarked = false;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final int RESULT_OK = -1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context context = new ContextThemeWrapper(getActivity(), R.style.Theme_AppCompat_Light);//set theme for fragment
        LayoutInflater localInflater = inflater.cloneInContext(context);
        View view = localInflater.inflate(R.layout.tab_food, container, false);
        calendar = Calendar.getInstance();

        recentIndex = ((NewHome)getActivity()).getData().foodRecentIndex;
        favIndex = ((NewHome)getActivity()).getData().foodFavoriteIndex;

        searchTxt = (EditText)view.findViewById(R.id.searchfoodTxt);
        listView = (ListView)view.findViewById(R.id.foodListView);
        food_list = ((NewHome)getActivity()).getFood_list();
        recentButton = (LinearLayout)view.findViewById(R.id.recentButton);
        favButton = (LinearLayout)view.findViewById(R.id.favButton);
        suggestButton = (LinearLayout)view.findViewById(R.id.suggestButton);
        gridMenu = (LinearLayout)view.findViewById(R.id.gridMenu);
        menu1 = (ImageButton)view.findViewById(R.id.menu1);
        menu2 = (ImageButton)view.findViewById(R.id.menu2);
        menu3 = (ImageButton)view.findViewById(R.id.menu3);

        recentButton.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridMenu.setVisibility(LinearLayout.GONE);
                listView.setVisibility(ListView.VISIBLE);
                isRecentClicked = true;
                isFavClicked = false;
                nowPosition = 0;
                ArrayList<Integer> recent_index =  new ArrayList<Integer>(recentIndex);
                ArrayList<Food> src_list = new ArrayList<Food>();
                for(int i = recent_index.size()-1; i >= 0; i--){
                    src_list.add(food_list.get(recent_index.get(i)));
                }
                adapter = new CustomListAdapter(getActivity(),src_list);
                listView.setAdapter(adapter);
            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridMenu.setVisibility(LinearLayout.GONE);
                listView.setVisibility(ListView.VISIBLE);
                isFavClicked = true;
                isRecentClicked = false;
                nowPosition = 0;
                ArrayList<Food> src_list = new ArrayList<Food>();
                for(int i = 0;i<favIndex.size();i++){
                    src_list.add(food_list.get(i));
                }
                adapter = new CustomListAdapter(getActivity(),src_list);
                listView.setAdapter(adapter);
            }
        });

        suggestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnspeedfood = (ImageButton)view.findViewById(R.id.btnspeedfood);
        btnspeedfood.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });


        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isRecentClicked = false;
                isFavClicked = false;
                ArrayList<Food> src_list = new ArrayList<Food>();
                int textlength = searchTxt.getText().length();
                boolean isAddFistPosition = false;
                for (int i = 0; i < food_list.size(); i++) {
                    if (searchTxt.getText().toString().matches("")) {
                        listView.setVisibility(ListView.GONE);
                        gridMenu.setVisibility(LinearLayout.VISIBLE);
                        break;
                    }
                    if (i == 0)
                        gridMenu.setVisibility(LinearLayout.GONE);
                    listView.setVisibility(ListView.VISIBLE);

                    try {
                        if (searchTxt.getText().toString()
                                .equalsIgnoreCase(food_list.get(i).getName()
                                        .subSequence(0, textlength)
                                        .toString())) {
                            src_list.add(food_list.get(i));
                            if (!isAddFistPosition) {
                                nowPosition = i;
                                isAddFistPosition = true;
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                adapter = new CustomListAdapter(getActivity(), src_list);
                listView.setAdapter(adapter);
            }
        });
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                final Food food;
                nowPosition = position + nowPosition;//get index from search

                if (isRecentClicked) {//get index from recent
                    ArrayList<Integer> foodIndex = new ArrayList<Integer>(recentIndex);
                    nowPosition = foodIndex.get(foodIndex.size() - position - 1);

                } else if (isFavClicked) {//get index from favorite
                    nowPosition = favIndex.get(position);
                }

                food = food_list.get(nowPosition);
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_numberpicker);

                Button buttonConfirm = (Button) dialog.findViewById(R.id.button_confirm);
                TextView titleTxt = (TextView) dialog.findViewById(R.id.titleTxt);
                TextView textView = (TextView) dialog.findViewById(R.id.dialog_textview);
                final ImageButton favMark = (ImageButton) dialog.findViewById(R.id.favMark);
                calSum = (TextView) dialog.findViewById(R.id.dialog_calsum);

                titleTxt.setText(food.getName());
                textView.setText("How Much ?");

                final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker);

                calSum.setText("" + food.getCalories() + " Cal");
                numberPicker.setMaxValue(100);
                numberPicker.setMinValue(1);
                numberPicker.setValue(1);
                numberPicker.setWrapSelectorWheel(false);
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        calSum.setText("" + food.getCalories() * picker.getValue() + " Cal");
                    }
                });


                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
				//eat
                buttonConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (recentIndex.size() > 10) {
                            recentIndex.remove();
                        }
                        recentIndex.add(nowPosition);
                        isRecentClicked = false;
                        isFavClicked = false;
                        ArrayList<String> src_list = new ArrayList<String>();
                        listView.setAdapter(new ArrayAdapter(getActivity()
                                , android.R.layout.simple_list_item_1
                                , src_list));
                        searchTxt.setText("");

                        DataKeeper data = ((NewHome) getActivity()).getData();

                        int amount = numberPicker.getValue();
                        int cal = (food.getCalories());

                        for (int i = 0; i < amount; i++) {
                            data.addCaloriesConsumed(cal);
                            data.addDailyFood(food, hour);

                        }
                        data.foodRecentIndex = recentIndex;
                        data.foodFavoriteIndex = favIndex;


                        ((NewHome) getActivity()).setData(CheckAchievements(data));

                        showToast("You eat " + food.getName() +
                                " for " + cal + " cal. x" + amount);


                        //noti
                        if (data.getCaloriesConsumed() > data.getCaloriesPerDay()) {
                            final Context context = new ContextThemeWrapper(getActivity(), R.style.Theme_AppCompat_Light);//set theme for fragment
                            Intent intent = new Intent(context, TabFood.class);
                            PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

                            // Build notification
                            // Actions are just fake
                            Notification noti = new Notification.Builder(context)
                                    .setContentTitle("Chuupy Alret")
                                    .setContentText("You Shouldn't eat a lot of food ").setSmallIcon(R.drawable.logo)
                                    .setContentIntent(pIntent)
                                            // .addAction(R.drawable.ic_search_black_24dp, "Call", pIntent)
                                            //.addAction(R.drawable.ic_search_black_24dp, "More", pIntent)
                                            // .addAction(R.drawable.ic_search_black_24dp, "And more", pIntent)
                                    .build();
                            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(context.NOTIFICATION_SERVICE);
                            // hide the notification after its selected
                            noti.flags |= Notification.FLAG_AUTO_CANCEL;

                            notificationManager.notify(0, noti);


                        }


                        dialog.dismiss();
                    }
                });

                for (int i = 0; i < favIndex.size(); i++) {
                    if (favIndex.get(i) == nowPosition || isFavClicked) {
                        isFavMarked = true;
                        favMark.setColorFilter(Color.argb(255, 255, 255, 0));
                        break;
                    }
                }


                favMark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isFavMarked = !isFavMarked;
                        if (isFavMarked) {
                            favMark.setColorFilter(Color.argb(255, 255, 255, 0));
                            favIndex.add(nowPosition);
                        } else {
                            favMark.setColorFilter(Color.argb(255, 128, 128, 128));
                            int indexToRemove = nowPosition;
                            for (int i = 0; i < favIndex.size(); i++) {
                                if (favIndex.get(i) == indexToRemove) {
                                    favIndex.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                });
                dialog.show();
            }
        });


        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_timer);
                TextView timeFormat = (TextView) dialog.findViewById(R.id.stoptimeText);
                Button startTimer = (Button) dialog.findViewById(R.id.button_start);
                Button stopTimer = (Button) dialog.findViewById(R.id.button_cancle);

                startTimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                stopTimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                dialog.show();
            }
        });

		//http://www.ezygodiet.com/%E0%B9%81%E0%B8%84%E0%B8%A5%E0%B8%AD%E0%B8%A3%E0%B8%B5%E0%B9%88/
        //noodle
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_noodle);
				//http://www.ezygodiet.com/category/%E0%B9%81%E0%B8%84%E0%B8%A5%E0%B8%AD%E0%B8%A3%E0%B8%B5%E0%B9%88%E0%B9%83%E0%B8%99%E0%B8%AD%E0%B8%B2%E0%B8%AB%E0%B8%B2%E0%B8%A3/%E0%B9%81%E0%B8%9B%E0%B9%89%E0%B8%87/
                noodleName = "";
                calText = (TextView) dialog.findViewById(R.id.calText);
                calText.setText("0 Cal");
                Button confirm = (Button) dialog.findViewById(R.id.confirm_button);
                Button cancle = (Button) dialog.findViewById(R.id.cancle_button);
                final RadioGroup noodleRadio = (RadioGroup) dialog.findViewById(R.id.radioNoodle);
                final RadioGroup soupRadio = (RadioGroup) dialog.findViewById(R.id.radioSoup);

                CheckBox addBeef = (CheckBox) dialog.findViewById(R.id.radio_noodle_add_beef);
                CheckBox addBall = (CheckBox) dialog.findViewById(R.id.radio_noodle_add_ball);
                CheckBox addChick = (CheckBox) dialog.findViewById(R.id.radio_noodle_add_chick);
                CheckBox addEgg = (CheckBox) dialog.findViewById(R.id.radio_noodle_add_egg);
                CheckBox addPork = (CheckBox) dialog.findViewById(R.id.radio_noodle_add_pork);
                noodleRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId == R.id.radio_noodle_eggnoodle){
                            noodleCal = 120;
                        }
                        else if(checkedId == R.id.radio_noodle_small){
                            noodleCal = 60;//not sure
                        }
                        else if(checkedId == R.id.radio_noodle_mee){
                            noodleCal = 40;
                        }
                        else if(checkedId == R.id.radio_noodle_big){
                            noodleCal = 80;
                        }
                        else if(checkedId == R.id.radio_noodle_woon){
                            noodleCal = 40;
                        }
                        calText.setText("" + (noodleCal + soupCal + additionCal) + " Cal");
						//mama = 250;
                    }
                });
                soupRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId == R.id.radio_noodle_soup_clear){
                            Log.e("soupClearisChecked","true");
                        }
                        else if(checkedId == R.id.radio_noodle_soup_thinken){
                            Log.e("soupThinkenisChecked","true");
                        }
                        else if(checkedId == R.id.radio_noodle_soup_hot){
                            Log.e("soupHotisChecked","true");
                        }
                        else if(checkedId == R.id.radio_noodle_soup_no){
                            Log.e("soupnoisChecked","true");
                        }
                        calText.setText("" + (noodleCal + soupCal + additionCal) + " Cal");

                    }
                });

				//https://www.facebook.com/SlimUp/posts/501981333145630
                addBall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            additionCal += 20;
                        }//10pcs=38cal ^~5pcs
                        else{
                            additionCal -= 20;
                        }
                        calText.setText("" + (noodleCal + soupCal + additionCal) + " Cal");
                    }
                });

                addBeef.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            additionCal += 145;
                        }//~50g (101g=290cal)
                        else{
                            additionCal -= 145;
                        }
                        calText.setText("" + (noodleCal + soupCal + additionCal) + " Cal");
                    }
                });

                addChick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            additionCal += 119;
                        }//~50g (101g=239cal)
                        else{
                            additionCal -= 119;
                        }
                        calText.setText("" + (noodleCal + soupCal + additionCal) + " Cal");
                    }
                });

                addEgg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            additionCal += 84;
                        }
                        else{
                            additionCal -= 84;
                        }
                        calText.setText("" + (noodleCal + soupCal + additionCal) + " Cal");
                    }
                });

                addPork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            additionCal += 136;
                        }
                        else{
                            additionCal -= 136;
                        }//~50g (101g=273cal)
                        calText.setText("" + (noodleCal + soupCal + additionCal) + " Cal");
                    }
                });
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (noodleCal != 0) {
                            DataKeeper data = ((NewHome) getActivity()).getData();
                            final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int cal = noodleCal + soupCal + additionCal;
                            data.addCaloriesConsumed(cal);
                            Food food = new Food("Noodle", "no", "meal", cal, 0, 0, 0, 0, 0);
                            data.addDailyFood(food, hour);
                            ((NewHome) getActivity()).setData(CheckAchievements(data));
                            dialog.dismiss();
                            showToast("You eat Noodle for " + cal + " Cal");
                        } else {
                            showToast("Please Select Noodle");
                        }
                    }
                });

                dialog.show();
            }
        });


        return view;
    }

    private DataKeeper CheckAchievements(DataKeeper inputData) {
        DataKeeper data = inputData;
        if (((NewHome) getActivity()).achievements.get(6).isActive() && data.checkCarbTodayIfMoreThan(125)) {
            ((NewHome) getActivity()).achievements.get(6).addValue();
            if (((NewHome) getActivity()).achievements.get(6).isDone())
            {   showToast("\"CARBON PER DAY 5 TIMES \" Achieved");
                data.addexp(30); }
        }
        if(data.getTodayFood().size() >= 3 && data.getCaloriesConsumed() < data.getCaloriesPerDay()){
            ((NewHome) getActivity()).achievements.get(1).addValue();
            if (((NewHome) getActivity()).achievements.get(1).isDone())
            {showToast("\"FIRST HEALTHY \" Achieved");
            data.addexp(30); }
        }
        if(((NewHome) getActivity()).achievements.get(4).isActive()){
            int protein = 0;
            ArrayList<Food> foods =  data.getTodayFood();
            for(int i = 0;i<foods.size();i++){
                protein = protein + foods.get(i).getProtein();
            }
            if(protein > 100){
                ((NewHome) getActivity()).achievements.get(4).addValue();
                if (((NewHome) getActivity()).achievements.get(4).isDone())
                {  showToast("\"FIRST PROTEIN \" Achieved");
                    data.addexp(30);
                }

            }
        }


        return data;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((NewHome) activity).onSectionAttached(2);
    }
    public void showToast(CharSequence text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    // speed fun
    // 1
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-EN");
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

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchTxt.setText(result.get(0));

                }
                break;
            }

        }
    }


}
