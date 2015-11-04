package com.example.pansit.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Pansit on 6/24/2015.
 */
public class SettingActivity extends Activity {

    EditText genderTxt;
    EditText weightTxt;
    EditText heightTxt;
    TextView dateOfBirthTxt;
    Button submitButton;
    DataKeeper data;
    CalendarView calendarView;
    boolean isOpen;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.setting);

            Intent activityThatCalled = getIntent();
            data = (DataKeeper) activityThatCalled.getSerializableExtra("data");

            isOpen = false;
            genderTxt = (EditText) findViewById(R.id.genderTxt);
            weightTxt = (EditText) findViewById(R.id.weightTxt);
            heightTxt = (EditText) findViewById(R.id.heightTxt);
            dateOfBirthTxt = (TextView) findViewById(R.id.dateOfBirthTxt);
            calendarView = (CalendarView) findViewById(R.id.calendarView);
            submitButton = (Button) findViewById(R.id.submitButton);


            if(data.getSex() == 'M')
                genderTxt.setText("Male");
            else
                genderTxt.setText("Female");

            CharSequence weightChar = "" + data.getWeight();
            CharSequence heightChar = "" + data.getHeight();
            weightTxt.setText(weightChar,TextView.BufferType.EDITABLE);
            heightTxt.setText(heightChar, TextView.BufferType.EDITABLE);

            dateOfBirthTxt.setText("27/9/1995");
            String parts[] = dateOfBirthTxt.getText().toString().split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);

            long milliTime = calendar.getTimeInMillis();
            calendarView.setDate(milliTime,false,false);

            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    dateOfBirthTxt.setText(dayOfMonth + "/" + month + "/" + year);
                }
            });

            dateOfBirthTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isOpen = !isOpen;
                    if (isOpen)
                        calendarView.setVisibility(View.VISIBLE);
                    else {
                        calendarView.setVisibility(View.GONE);
                    }
                }
            });

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.setWeight(Integer.parseInt(weightTxt.getText().toString()));
                    data.setHeight(Integer.parseInt(heightTxt.getText().toString()));
                    if(genderTxt.getText().toString().equals("Male"))
                        data.setSex('M');
                    else if(genderTxt.getText().toString().equals("Female"))
                        data.setSex('F');
                    String parts[] = dateOfBirthTxt.getText().toString().split("/");
                    int year = Integer.parseInt(parts[2]);
                    Calendar calendar1 = Calendar.getInstance();
                    int currrentYear = calendar1.get(Calendar.YEAR);
                    data.setAge(currrentYear - year);
                    Intent sendData = new Intent(v.getContext() , NewHome.class);
                    sendData.putExtra("data", data);
                    startActivityForResult(sendData, 1);
                    finish();
                }
            });







        }
    public void showToast(CharSequence text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
