package com.example.pansit.myapplication;

import android.provider.ContactsContract;

/**
 * Created by Qling on 15/08/2015.
 */
public class Achievement {
    private String name;
    private int value;
    private int reachValue;
    private String tag;
    private boolean isDone;

    public Achievement(String name, int value, int reachValue, String tag) {
        this.name = name;
        this.value = value;
        this.reachValue = reachValue;
        this.tag = tag;
        this.isDone = false;
    }
    public void done(){
        isDone = true;
    }
    public boolean isDone(){
        return isDone;
    }
    public boolean isActive(){
        return (value < reachValue);
    }

    public boolean addValue(){
        if(!isDone) {
            this.value++;
            if ((value >= reachValue)) {
                done();
                return true;
            }
        }
        return false;
    }

    public boolean setValue(int value) {
        this.value = value;
        if((value >= reachValue) && !isDone) {
            done();
            return true;
        }
        else
            return false;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public int getReachValue() {
        return reachValue;
    }

    public String getTag() {
        return tag;
    }
}
