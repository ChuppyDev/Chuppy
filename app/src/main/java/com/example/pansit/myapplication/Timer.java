package com.example.pansit.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Qling on 04/11/2015.
 */
public class Timer {

    private TextView timerValue;
    private TextView CalTxt;
    private Workout activity;
    private int weight;
    private boolean isUse;
    private float cal;
    private long startTime = 0L;
    private boolean isStart;
    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    public Timer(){
        isUse = false;
    }

    public Timer(TextView timeVal,Workout act, TextView c,int w){
        timerValue = timeVal;
        CalTxt = c;
        activity = act;
        weight = w;
        isUse = false;
        cal = 0f;
        isStart = false;
    }

    public void start(){
        isUse = true;
        isStart = true;
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    public void pause() {
        isStart = false;
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
    }

    public void reset(){
        isStart = false;
        isUse = false;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedTime = 0L;
        timerValue.setText("0:00:00");
        customHandler.removeCallbacks(updateTimerThread);
    }

    public Workout getActivity(){
        return activity;
    }

    public void setTextView(TextView t,TextView c){
        timerValue = t;
        CalTxt = c;
    }

    public boolean isStart(){
        return isStart;
    }

    public boolean isUse(){
        return isUse;
    }

    public float getCal(){
        return cal;
    }

    public int getMinute() {
        return (int)(updatedTime/60000);
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%02d", milliseconds / 10));
            float cof = (((1) * (float)weight) / activity.getVal());
            CalTxt.setText("" + (int)(Math.floor(mins*cof + secs*cof/60)) + " Cal");
            cal = (int)(Math.floor(mins*cof + secs*cof/60));

            customHandler.postDelayed(this, 0);
        }

    };

}
