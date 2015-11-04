package com.example.pansit.myapplication;

import java.io.Serializable;

/**
 * Created by Pansit on 6/9/2015.
 */
public class Food implements Serializable{
    private String name;
    private String restaurant;
    private String type;
    private int calories;
    private int carbohydrate;
    private int protein;
    private int fat;
    private int cholesterol;
    private int sodium;

    public Food(String name, String restaurant, String type, int calories, int carbohydrate, int protein, int fat, int cholesterol, int sodium) {
        this.name = name;
        this.restaurant = restaurant;
        this.type = type;
        this.calories = calories;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
    }

    public String getName() {
        return name;
    }
    public int getCalories(){
        return calories;
    }
    public String getRestaurant() {
        return restaurant;
    }

    public String getType() {
        return type;
    }

    public int getCarbohydrate() {
        return carbohydrate;
    }

    public int getProtein() {
        return protein;
    }

    public int getFat() {
        return fat;
    }

    public int getCholesterol() {
        return cholesterol;
    }

    public int getSodium() {
        return sodium;
    }
}
