package com.example.pansit.myapplication;

/**
 * Created by Pansit on 7/1/2015.
 */
public class Workout {
    private String name;
    private float val;

    public Workout(String name, float val) {
        this.name = name;
        this.val = val;
    }

    public String getName() {
        return name;
    }

    public float getVal() {
        return val;
    }
}
