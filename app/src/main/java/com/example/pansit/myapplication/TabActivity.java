package com.example.pansit.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by Pansit on 6/2/2015.
 */
public class TabActivity extends Fragment {

    EditText searchTxt;
    ListView listView;
    Button button,confirmTimerButton,closeButton;
    ArrayList<Workout> activity_list = new ArrayList<Workout>();
    TextView calSum;
    TextView text,timeVal,timeValOut,calSumOut;
    int nowPosition;
    ImageButton recentButton;
    ImageButton favButton;
    ImageButton suggestButton;
    LinearLayout timerBox;
    Timer timer;

    Queue<Integer> recentIndex;
    ArrayList<Integer> favIndex;

    boolean isTimerClick = false;
    boolean isTimerOutClick = false;
    boolean isRecentClicked = false;
    boolean isFavClicked = false;
    boolean isFavMarked = false;
    // speed val
    ImageButton btnspeedact;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final int RESULT_OK = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context context = new ContextThemeWrapper(getActivity(), R.style.Theme_AppCompat_Light);//set theme for fragment
        LayoutInflater localInflater = inflater.cloneInContext(context);
        View view = localInflater.inflate(R.layout.tab_activity, container, false);
        searchTxt = (EditText) view.findViewById(R.id.searchactivityTxt);
        listView = (ListView) view.findViewById(R.id.activityListView);

        recentIndex = ((NewHome)getActivity()).getData().activityRecentIndex;
        favIndex = ((NewHome)getActivity()).getData().activityFavoriteIndex;

        activity_list = ((NewHome)getActivity()).getActivity_list();

        recentButton = (ImageButton)view.findViewById(R.id.recentButton);
        favButton = (ImageButton)view.findViewById(R.id.favButton);
        suggestButton = (ImageButton)view.findViewById(R.id.suggestButton);

        timerBox = (LinearLayout)view.findViewById(R.id.timerBox);
        confirmTimerButton = (Button)view.findViewById(R.id.confirm_button);
        closeButton = (Button)view.findViewById(R.id.closeButton);
        timeValOut = (TextView)view.findViewById(R.id.stoptimeText);
        calSumOut = (TextView)view.findViewById(R.id.calsum);

        timer = new Timer();

        recentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecentClicked = true;
                nowPosition = 0;
                ArrayList<Integer> recent_index =  new ArrayList<Integer>(recentIndex);
                ArrayList<String> src_list = new ArrayList<String>();
                for(int i = recent_index.size()-1; i >= 0; i--){
                    src_list.add(activity_list.get(recent_index.get(i)).getName());
                }
                listView.setVisibility(ListView.VISIBLE);
                listView.setAdapter(new ArrayAdapter(getActivity()
                        , android.R.layout.simple_list_item_1
                        , src_list));
            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavClicked = true;
                isRecentClicked = false;
                nowPosition = 0;
                ArrayList<String> src_list = new ArrayList<String>();
                for(int i = 0;i<favIndex.size();i++){
                    src_list.add(activity_list.get(favIndex.get(i)).getName());
                }
                listView.setVisibility(ListView.VISIBLE);
                listView.setAdapter(new ArrayAdapter(getActivity()
                        , android.R.layout.simple_list_item_1
                        , src_list));
            }
        });

        suggestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        btnspeedact = (ImageButton)view.findViewById(R.id.btnspeedactivity);
        btnspeedact.setOnClickListener(new View.OnClickListener() {

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
                listView.setVisibility(ListView.VISIBLE);
                isRecentClicked = false;
                isFavClicked = false;
                ArrayList<String> src_list = new ArrayList<String>();
                int textlength = searchTxt.getText().length();
                boolean isAddFistPosition = false;
                for (int i = 0; i < activity_list.size(); i++) {
                    if (searchTxt.getText().toString().matches(""))
                        break;
                    try {
                        if (searchTxt.getText().toString()
                                .equalsIgnoreCase(activity_list.get(i).getName()
                                        .subSequence(0, textlength)
                                        .toString())) {
                            src_list.add(activity_list.get(i).getName());
                            if (!isAddFistPosition) {
                                nowPosition = i;
                                isAddFistPosition = true;
                            }
                        }
                    } catch (Exception e) {
                    }
                }

                listView.setAdapter(new ArrayAdapter(getActivity()
                        , android.R.layout.simple_list_item_1
                        , src_list));
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                final Workout activity;


                nowPosition = position + nowPosition;
                if(isRecentClicked){
                    ArrayList<Integer> activityIndex = new ArrayList<Integer>(recentIndex);
                    nowPosition = activityIndex.get(activityIndex.size() - position - 1);
                }
                else if(isFavClicked){
                    nowPosition = favIndex.get(position);
                }

                activity = activity_list.get(nowPosition);



                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                final DataKeeper data = ((NewHome) getActivity()).getData();
                //dialog.setContentView(R.layout.dialog_numberpicker);
                dialog.setContentView(R.layout.dialog_activity);

                timeVal = (TextView) dialog.findViewById(R.id.stoptimeText);

                Button buttonConfirm = (Button) dialog.findViewById(R.id.button_confirm);
                TextView titleTxt = (TextView)dialog.findViewById(R.id.titleTxt);
                calSum = (TextView) dialog.findViewById(R.id.dialog_calsum);
                text = (TextView) dialog.findViewById(R.id.dialog_textview);
                final ImageButton favMark = (ImageButton) dialog.findViewById(R.id.favMark);

                final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker);

                timer = new Timer(timeVal,activity,calSum,data.getWeight());

                timeVal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isTimerClick = !isTimerClick;
                        if(isTimerClick){
                            timer.start();
                        }
                        else{
                            timer.pause();
                            }
                    }
                });

                timeVal.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        isTimerClick = false;
                        timer.reset();
                        calSum.setText("" + 0 + " Cal");
                        return false;
                    }
                });

                titleTxt.setText(activity.getName());
                text.setText("How long ? (In Minute)");
                calSum.setText("" + 3 * Math.round(((numberPicker.getValue()* 30 + 30) * data.getWeight()) / activity.getVal() ) + " Cal");

                String[] numbers = new String[1200/30];
                for (int i=0; i<numbers.length; i++)
                    numbers[i] = Integer.toString(i*30+30);

                numberPicker.setDisplayedValues(numbers);
                numberPicker.setMaxValue(numbers.length - 1);
                numberPicker.setMinValue(0);
                numberPicker.setValue(2);
                numberPicker.setWrapSelectorWheel(false);
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        calSum.setText("" + Math.round(((picker.getValue() * 30 + 30) * data.getWeight()) / activity.getVal()) + " Cal");
                    }
                });
                buttonConfirm.setOnClickListener(new View.OnClickListener() {
                    @SuppressWarnings("ResultOfMethodCallIgnored")
                    @Override
                    public void onClick(View v) {
                        if (recentIndex.size() > 10) {
                            recentIndex.remove();
                        }
                        recentIndex.add(nowPosition);
                        isRecentClicked = false;
                        isFavClicked = false;
                        ArrayList<String> src_list = new ArrayList<String>();
                        listView.setVisibility(ListView.VISIBLE);
                        listView.setAdapter(new ArrayAdapter(getActivity()
                                , android.R.layout.simple_list_item_1
                                , src_list));
                        searchTxt.setText("");

                        int minute = numberPicker.getValue() * 30 + 30;
                        float cal = ((minute * data.getWeight()) / activity.getVal());
                        if (timer.isUse()) {
                            cal = timer.getCal();
                            minute = timer.getMinute();
                        }
                        data.addCaloriesBurned(Math.round(cal));//need to add formula
                        data.activityRecentIndex = recentIndex;

                        ((NewHome) getActivity()).setData(CheckAchievements(data));
                        showToast("You " + activity.getName() + " for " + minute + " minute " + Math.round(cal) + " Cal.");
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

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        ShowTimer();
                    }
                });

                dialog.show();
            }
        });
        return view;
    }

    private DataKeeper CheckAchievements(DataKeeper data) {
        DataKeeper tmpData = data;
        if(((NewHome)getActivity()).achievements.get(5).isActive() && ((NewHome)getActivity()).achievements.get(5).addValue()){
            showToast("\"FIRST EXERCISE \" Achievement Unlocked");
        }

        return tmpData;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((NewHome) activity).onSectionAttached(4);
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
    public void ShowTimer()
    {
        if(timer.isUse()){
            timerBox.setVisibility(View.VISIBLE);
            final DataKeeper data = ((NewHome) getActivity()).getData();
            final Workout activity = timer.getActivity();
            timer.setTextView(timeValOut, calSumOut);
            confirmTimerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.addCaloriesBurned(Math.round(timer.getCal()));//need to add formula
                    data.activityRecentIndex = recentIndex;
                    ((NewHome) getActivity()).setData(CheckAchievements(data));
                    showToast("You " + activity.getName() + " for " + timer.getMinute() + " minute " + timer.getCal() + " Cal.");
                    timerBox.setVisibility(View.GONE);
                    timer.reset();
                    timer = new Timer();
                }
            });
            timeValOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isTimerOutClick = !isTimerOutClick;
                    if (isTimerOutClick) {
                        timer.start();
                    } else {
                        timer.pause();
                    }
                }
            });

            timeValOut.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    timer.reset();
                    calSumOut.setText("" + 0 + " Cal");
                    return false;
                }
            });
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timerBox.setVisibility(View.GONE);
                    timer.reset();
                    timer = new Timer();
                }
            });
        }



    }
}
