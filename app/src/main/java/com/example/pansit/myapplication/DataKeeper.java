package com.example.pansit.myapplication;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Pansit on 6/3/2015.
 */
public class DataKeeper implements Serializable{

    final int achievementAmount = 7;

    private String user;
    private String password;
    private String email;
    private int age;
    private int weight;
    private int height;
    private char sex;
    private String disease;
    private int caloriesConsumed;
    private int waterConsumed;
    private int caloriesBurned;
    private int lvl=1;
    private int lvlexp=0;
    public boolean isCarbCheck = false;

    public ArrayList<Food> breakfastFood = new ArrayList<Food>();
    public ArrayList<Food> lunchFood = new ArrayList<Food>();
    public ArrayList<Food> eveningFood = new ArrayList<Food>();
    public ArrayList<Food> nightFood = new ArrayList<Food>();

    public Queue<Integer> foodRecentIndex = new LinkedList<>();
    public Queue<Integer> activityRecentIndex = new LinkedList<>();
    public ArrayList<Integer> foodFavoriteIndex = new ArrayList<>();
    public ArrayList<Integer> activityFavoriteIndex = new ArrayList<>();

    public ArrayList<ArrayList<Food>> foodOnDay = new ArrayList<ArrayList<Food>>();
    public ArrayList<Integer> caloriesOnDay = new ArrayList<Integer>();
    public ArrayList<Integer> waterOnDay = new ArrayList<>();
    public ArrayList<Integer> caloriesBurnOnDay = new ArrayList<>();

    public ArrayList<Integer> achieveValue = new ArrayList<>();


    //Calories For Men: (10 x weight in kg) + (6.25 x height in cm) - (4.92 x age) + 5
    private int caloriesMaleFormular(){
        return (int)((10 * weight) + (6.25 * height) - (4.92 * age) + 5);
    }

    //Calories For Women: (10 x weight in kg) + (6.25 x height in cm) - (4.92 x age) - 161
    private int caloriesFemaleFormular(){
        return (int)((10 * weight) + (6.25 * height) - (4.92 * age) - 61);
    }

    //Water 40.31 * weight
    private int waterFormular(){
        return (int)(weight * 40.31);
    }

    //Calories Burn
    private int caloriesBurnFormular() {
        return 0;
    }



    //Constructor
    public DataKeeper() {
        caloriesConsumed = 0;
        waterConsumed = 0;
        caloriesBurned = 0;

        this.user = "null";
    }
    public DataKeeper(String user, String password,String email,int age,int weight,int height,char sex,String disease) {
        this.user = user;
        this.password = password;
        this.email = email;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.sex = sex;
        this.disease = disease;
        caloriesOnDay = new ArrayList<>();
        for(int i = 0;i<32;i++) {
            foodOnDay.add(new ArrayList<Food>());
            caloriesOnDay.add(0);
            waterOnDay.add(0);
            caloriesBurnOnDay.add(0);
        }
        for(int i =0;i<achievementAmount+1;i++){
            achieveValue.add(0);
        }


    }
    public DataKeeper(String user, String password,String email,int age,int weight,int height,char sex,String disease,ArrayList<ArrayList<Food>> foodOnDay) {
        this.user = user;
        this.password = password;
        this.email = email;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.sex = sex;
        this.disease = disease;
        this.foodOnDay = foodOnDay;
        for(int i =0;i<achievementAmount;i++){
            achieveValue.add(0);
        }
    }
    public DataKeeper(String user, String password) {
        this(user,password,"Unknow",-1,-1,-1,'0',"Unknow");
        for(int i =0;i<achievementAmount;i++){
            achieveValue.add(0);
        }
    }

    //Method
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public int getCaloriesPerDay() {

        if (sex == 'M')
            return caloriesMaleFormular();
        else
            return caloriesFemaleFormular();
        //Men: (10 x weight in kg) + (6.25 x height in cm) - (4.92 x age) + 5
        //Women: (10 x weight in kg) + (6.25 x height in cm) - (4.92 x age) - 161
    }

    public int getCaloriesConsumed() {
        return caloriesConsumed;
    }
    public void setCaloriesConsumed(int caloriesConsumed){
        this.caloriesConsumed = caloriesConsumed;
    }

    public void addCaloriesConsumed(int caloriesConsumed) {
        this.caloriesConsumed += caloriesConsumed;
    }
    public int caloriesNeedPerDay(){
        return getCaloriesPerDay() - caloriesConsumed;
    }

    public int getWaterPerDay() {

        return waterFormular();

        //(kg body weight  20) x 15 +1500 = mL fluid required daily

    }


    //should be private

    public void setWaterConsumed(int waterConsumed){this.waterConsumed = waterConsumed;}
    public void addWaterConsumed(int waterConsumed) {
        this.waterConsumed += waterConsumed;
    }
    public int waterNeedPerDay(){
        return getWaterPerDay()-waterConsumed;
    }
    public int getWaterConsumed(){
        return waterConsumed;
    }
    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }
    public int getCaloriesBurned(){
        return caloriesBurned;
    }
    public void addCaloriesBurned(int caloriesBurned){
        this.caloriesBurned += caloriesBurned;
    }





    public void addDailyFood(Food food,int hour){
        if(hour > 6 && hour < 10){
            breakfastFood.add(food);
        }
        else if (hour >= 10 && hour < 15){
            lunchFood.add(food);
        }
        else if (hour >= 15 && hour < 20) {
            eveningFood.add(food);
        }
        else{
            nightFood.add(food);
        }
    }
    public ArrayList getBreakfastFood(){
        return breakfastFood;
    }
    public ArrayList getLunchFood(){
        return lunchFood;
    }
    public ArrayList getEveningFood(){
        return eveningFood;
    }
    public ArrayList getNightFood(){
        return nightFood;
    }



    public ArrayList<Food> getTodayFood(){
        ArrayList<Food> todayFood = new ArrayList<>();
        for(int i = 0;i<breakfastFood.size();i++)
            todayFood.add(breakfastFood.get(i));
        for(int i = 0;i<lunchFood.size();i++)
            todayFood.add(lunchFood.get(i));
        for(int i = 0;i<eveningFood.size();i++)
            todayFood.add(eveningFood.get(i));
        for(int i = 0;i<nightFood.size();i++)
            todayFood.add(nightFood.get(i));
        return todayFood;
    }



    public void setNewDay(int day){
        foodOnDay.set(day,getTodayFood());
        caloriesOnDay.set(day,caloriesConsumed);
        waterOnDay.set(day,waterConsumed);
        caloriesBurnOnDay.set(day,caloriesBurned);

        isCarbCheck = false;

        caloriesBurned = 0;
        caloriesConsumed = 0;
        waterConsumed = 0;
        breakfastFood = new ArrayList<>();
        lunchFood = new ArrayList<>();
        eveningFood = new ArrayList<>();
        nightFood = new ArrayList<>();
    }

    public boolean checkCarbTodayIfMoreThan(int needCarb){
        if(!isCarbCheck) {
            ArrayList<Food> todayFood = getTodayFood();
            int todayCarb = 0;
            for (int i = 0; i < todayFood.size(); i++) {
                todayCarb += todayFood.get(i).getCarbohydrate();
            }
            if (todayCarb >= needCarb) {
                isCarbCheck = true;
                return true;
            }
        }

        return false;
    }


    public int getlvl()
    {
        return lvl;
    }

    private   void  lvlup()
    {
        // show toast
        lvl++;
    }
    public  void  addexp(int exp)
    {
        lvlexp+=exp;

        if(lvlexp>=100)
        {
            lvlexp-=100;
            lvlup();
        }
    }
    public  int getLvlexp()
    {
        return  lvlexp;
    }

    public  void setlvl(int val)
    {
        lvl = val;
    }

    public  void setLvlexp(int val)
    {
        lvlexp = val;
    }

}
