package com.example.pansit.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Stack;


public class NewHome extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
         * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    DataKeeper data = new DataKeeper();
    String foodListJson;
    String activityListJson;
    private ArrayList<Food> food_list = new ArrayList<Food>();
    private ArrayList<Workout> activity_list = new ArrayList<Workout>();
    public ArrayList<Achievement> achievements = new ArrayList<>();

    private NavigationDrawerFragment mNavigationDrawerFragment;
    Fragment fragment;
    FragmentManager fragmentManager = getFragmentManager();
    Stack<Fragment> fragmentStack = new Stack<Fragment>();
    Stack<Integer> fragmentCount = new Stack<Integer>();

    String name;
    boolean isLogin = true;
    boolean isLoad;

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newhome);
        // Set up the drawer.
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //get Data from Login Activity
        Intent activityThatCalled = getIntent();
        Bundle bundle;
        bundle = activityThatCalled.getExtras();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        name = sharedPreferences.getString("Username", "Error");

        //initialize Achievements
        achievements.add(new Achievement("sleep8hours5times",0,5,"beginner"));//0
        achievements.add(new Achievement("1sthealthy",0,1,"intro"));//1 done
        achievements.add(new Achievement("1stunfat",0,1,"intro"));//2 done
        achievements.add(new Achievement("1stdrink",0,1,"intro"));//3 TabWater done
        achievements.add(new Achievement("1stpro",0,1,"intro"));//4 done
        achievements.add(new Achievement("1stexercise",0,1,"intro"));//5 done
        achievements.add(new Achievement("carb125FiveTime",0,5,"beginner"));//6 TabFood done
        achievements.add(new Achievement("waterInDay",0,1,"beginner"));//7 TabWater done


        //if Load from Login Activity
        if(bundle != null){
            data = (DataKeeper) bundle.getSerializable("data");
            name = data.getUser();
            // get Foodlist and ActivityList from Login
            foodListJson = bundle.getString("foodlistjson","Error");
            activityListJson = bundle.getString("activitylistjson", "Error");
            food_list = convertFoodJsonStringToFoodList(foodListJson);
            activity_list = convertFoodJsonStringToActivityList(activityListJson);
        }
        //already login
        else{
            foodListJson = (getPreferences(Context.MODE_PRIVATE).getString("FOODLIST","Error"));
            activityListJson = (getPreferences(Context.MODE_PRIVATE).getString("ACTIVITYLIST","Error"));
            food_list = convertFoodJsonStringToFoodList(foodListJson);
            activity_list = convertFoodJsonStringToActivityList(activityListJson);
        }


        //Load old data check if( login username = old data username )
        Log.e("Load from create","run..");
        LoadData();
        isLoad = true;


        for (int i = 0; i < data.achieveValue.size(); i++) {
            achievements.get(i).setValue(data.achieveValue.get(i));
        }





    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isLoad){
            Log.e("Load from Resume", "run");
            LoadData();
            isLoad = true;
            if(fragmentCount.lastElement() == 0)
                onNavigationDrawerItemSelected(0);
            else if(fragmentCount.lastElement() == 5)
                onNavigationDrawerItemSelected(5);
        }
    }

    private void LoadData() {
        String dataJasonString = (getPreferences(Context.MODE_PRIVATE).getString("DATAs" + name,"Error"));
        if (dataJasonString != null) {
            if(!dataJasonString.equals("Error")){
                DataKeeper tmpData = receiveDataKeeperFromJsonString(dataJasonString);
                if(tmpData.getUser().equals(name)){

                    data = tmpData;
                    showToast("Welcome back " + data.getUser());
                }
                else{
                    showToast("Hello " + data.getUser());
                }
            }
        }
    }

    private void saveSetting(){
        for(int i = 0;i<achievements.size();i++){
            data.achieveValue.set(i,achievements.get(i).getValue());
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin",isLogin);
        editor.putString("Username", data.getUser());
        editor.commit();

        SharedPreferences.Editor sPEditor = getPreferences(Context.MODE_PRIVATE).edit();
        String dataJasonString = convertingDataKeeperToJSONString(data);
        sPEditor.putString("DATAs"+ data.getUser(),dataJasonString);
        Log.e("JasonStringSaved", dataJasonString);
        sPEditor.putString("FOODLIST", foodListJson);
        sPEditor.putString("ACTIVITYLIST", activityListJson);
        Calendar calendar = Calendar.getInstance();
        sPEditor.putInt("DAY", calendar.get(Calendar.DAY_OF_MONTH));
        sPEditor.putInt("lvl", data.getlvl());
        sPEditor.putInt("lvlexp", data.getLvlexp());
        sPEditor.commit();



        isLoad = false;
    }

    @Override
    protected void onStop(){
        Log.e("OnStopExecute","run");

        saveSetting();

        super.onStop();
    }

    private ArrayList<Workout> convertFoodJsonStringToActivityList(String activityListJson) {
        ArrayList<Workout> activity_list = new ArrayList<Workout>();
        try {
            JSONObject getjobj = new JSONObject(activityListJson);
            JSONArray jArray = getjobj.optJSONArray("actlist");

            // String txt = "";
            // JSONArray jArray = new JSONArray(txt);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);

                //output val
                // ( name , cal, restaurant, fat, cholesterol, sodium, carbohydrate , protein, type)
                //(String name, String restuarant, String type, int calories, int carbohydrate, int protein, int fat, int cholesterol, int sodium)

                Float val = Float.parseFloat(json_data.optString("val"));
                String name = json_data.optString("name");

                Workout outputact = new Workout(name,val);

                activity_list.add(outputact);


            }
        }
        catch (JSONException e) {

            Log.e("log_tag", "Errrrrrorr");
        }


        return activity_list;
    }

    private ArrayList<Food> convertFoodJsonStringToFoodList(String foodListJson) {
        ArrayList<Food> food_list = new ArrayList<Food>();
        try {
            JSONObject getjobj = new JSONObject(foodListJson);
            JSONArray jArray = getjobj.optJSONArray("foodlist");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);

                //output val
                // ( name , cal, restaurant, fat, cholesterol, sodium, carbohydrate , protein, type)
                //(String name, String restuarant, String type, int calories, int carbohydrate, int protein, int fat, int cholesterol, int sodium)
                String name = json_data.optString("name");
                int cal = Integer.parseInt(json_data.optString("cal"));
                String restaurant = json_data.optString("restaurant");
                int fat = Integer.parseInt(json_data.optString("fat"));
                int cholesterol = Integer.parseInt(json_data.optString("cholesterol"));
                int sodium = Integer.parseInt(json_data.optString("sodium"));
                int carbohydrate = Integer.parseInt(json_data.optString("carbohydrate"));
                int protein = Integer.parseInt(json_data.optString("protein"));
                String type = json_data.optString("type");

                Food outputfood = new Food(name, restaurant, type, cal, carbohydrate, protein, fat, cholesterol, sodium);

                food_list.add(outputfood);

            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return food_list;
    }

    private DataKeeper receiveDataKeeperFromJsonString(String dataJasonString) {
        DataKeeper tmpData = new DataKeeper();
        try {
            JSONObject jsonObject = new JSONObject(dataJasonString);

            //DataKeeper(String user, String password,String email,int age,int weight,int height,char sex,String disease)
            tmpData = new DataKeeper(
                    jsonObject.optString("user", "Error"),
                    jsonObject.optString("password", "Error"),
                    jsonObject.optString("email", "Error"),
                    jsonObject.optInt("age", -1),
                    jsonObject.optInt("weight", -1),
                    jsonObject.optInt("height", -1),
                    jsonObject.optString("sex", "Error").charAt(0),
                    jsonObject.optString("disease", "Error")
            );
            tmpData.setCaloriesConsumed(jsonObject.optInt("caloriesConsumed", -1));
            tmpData.setCaloriesBurned(jsonObject.optInt("caloriesBurned", -1));
            tmpData.setWaterConsumed(jsonObject.optInt("waterConsumed", -1));
            tmpData.setlvl(jsonObject.optInt("lvl", -1));
            tmpData.setLvlexp(jsonObject.optInt("lvlexp", -1));

            JSONArray jArray = jsonObject.optJSONArray("nightFood");
            for(int i = 0; i < jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                tmpData.nightFood.add(new Food(
                        //Food(String name, String restuarant, String type, int calories, int carbohydrate, int protein, int fat, int cholesterol, int sodium)
                        json_data.optString("name","Error"),
                        json_data.optString("restuarant","Error"),
                        json_data.optString("type","Error"),
                        json_data.optInt("calories",-1),
                        json_data.optInt("carbohydrate", -1),
                        json_data.optInt("protein",-1),
                        json_data.optInt("fat",-1),
                        json_data.optInt("cholesterol",-1),
                        json_data.optInt("sodium",-1)
                ));
            }
            jArray = jsonObject.optJSONArray("breakfastFood");
            for(int i = 0; i < jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                tmpData.breakfastFood.add(new Food(
                        //Food(String name, String restuarant, String type, int calories, int carbohydrate, int protein, int fat, int cholesterol, int sodium)
                        json_data.optString("name","Error"),
                        json_data.optString("restuarant","Error"),
                        json_data.optString("type","Error"),
                        json_data.optInt("calories",-1),
                        json_data.optInt("carbohydrate", -1),
                        json_data.optInt("protein",-1),
                        json_data.optInt("fat",-1),
                        json_data.optInt("cholesterol",-1),
                        json_data.optInt("sodium",-1)
                ));
            }
            jArray = jsonObject.optJSONArray("eveningFood");
            for(int i = 0; i < jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                tmpData.eveningFood.add(new Food(
                        //Food(String name, String restuarant, String type, int calories, int carbohydrate, int protein, int fat, int cholesterol, int sodium)
                        json_data.optString("name", "Error"),
                        json_data.optString("restuarant", "Error"),
                        json_data.optString("type", "Error"),
                        json_data.optInt("calories", -1),
                        json_data.optInt("carbohydrate", -1),
                        json_data.optInt("protein", -1),
                        json_data.optInt("fat", -1),
                        json_data.optInt("cholesterol", -1),
                        json_data.optInt("sodium", -1)
                ));
            }
            jArray = jsonObject.optJSONArray("lunchFood");
            for(int i = 0; i < jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                tmpData.lunchFood.add(new Food(
                        //Food(String name, String restuarant, String type, int calories, int carbohydrate, int protein, int fat, int cholesterol, int sodium)
                        json_data.optString("name", "Error"),
                        json_data.optString("restuarant", "Error"),
                        json_data.optString("type", "Error"),
                        json_data.optInt("calories", -1),
                        json_data.optInt("carbohydrate", -1),
                        json_data.optInt("protein", -1),
                        json_data.optInt("fat", -1),
                        json_data.optInt("cholesterol", -1),
                        json_data.optInt("sodium", -1)
                ));
            }

            JSONArray jArray4 = jsonObject.optJSONArray("caloriesBurnOnDay");
            JSONArray jArray3 = jsonObject.optJSONArray("waterOnDay");
            JSONArray jArray2 = jsonObject.optJSONArray("caloriesOnDay");
            jArray = jsonObject.optJSONArray("foodOnDay");

            for(int i = 0; i < jArray.length(); i++){
                JSONArray json_subArray = jArray.getJSONArray(i);
                ArrayList<Food> foods = new ArrayList<>();
                if(!json_subArray.toString().equals("[]")) {
                    for (int j = 0; j < json_subArray.length(); j++) {
                        JSONObject json_data = json_subArray.getJSONObject(j);

                        if (!json_data.toString().equals("{}")) {

                            foods.add(new Food(
                                    //Food(String name, String restuarant, String type, int calories, int carbohydrate, int protein, int fat, int cholesterol, int sodium)
                                    json_data.optString("name", "nul"),
                                    json_data.optString("restuarant", "nul"),
                                    json_data.optString("type", "nul"),
                                    json_data.optInt("calories", -1),
                                    json_data.optInt("carbohydrate", -1),
                                    json_data.optInt("protein", -1),
                                    json_data.optInt("fat", -1),
                                    json_data.optInt("cholesterol", -1),
                                    json_data.optInt("sodium", -1)
                            ));

                        }

                    }
                    tmpData.foodOnDay.set(i, foods);
                    tmpData.caloriesOnDay.set(i,jArray2.getInt(i));
                    tmpData.waterOnDay.set(i, jArray3.getInt(i));
                    tmpData.caloriesBurnOnDay.set(i,jArray4.getInt(i));
                }

            }

            tmpData.isCarbCheck = jsonObject.optBoolean("isCarbCheck",false);

            Calendar calendar = Calendar.getInstance();
            int day = getPreferences(Context.MODE_PRIVATE).getInt("DAY", -1);
            if ((day != -1) && (day != calendar.get(Calendar.DAY_OF_MONTH))){
                Log.e("Day and Calendar", day+ " " +calendar.get(Calendar.DAY_OF_MONTH));
                tmpData.setNewDay(day);
                CheckAchievementsIfEndDay(tmpData,day);
                showAlert();
            }
            jArray = jsonObject.optJSONArray("foodRecentIndex");
            for(int i = 0;i<jArray.length();i++){
                tmpData.foodRecentIndex.add(jArray.getInt(i));
            }
            jArray = jsonObject.optJSONArray("foodFavoriteIndex");
            if(jArray != null) {
                for (int i = 0; i < jArray.length(); i++) {
                    tmpData.foodFavoriteIndex.add(jArray.getInt(i));
                }
            }
            jArray = jsonObject.optJSONArray("achieveValue");
            for(int i =0;i<jArray.length();i++){
                tmpData.achieveValue.set(i,jArray.getInt(i));
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return tmpData;
    }

    private void CheckAchievementsIfEndDay(DataKeeper data,int day) {

        if(achievements.get(2).isActive()) {
            int fat = 0;
            for (int i = 0; i < data.foodOnDay.get(day).size(); i++) {
                fat = fat + data.foodOnDay.get(day).get(i).getFat();
                Log.e("Fat Total", "" + fat);
            }
            if (achievements.get(2).addValue() && fat * 4 < 0.3 * data.caloriesNeedPerDay()) {
                showToast("\"FIRST UN-FAT\" Achievement Unlocked");
            }
        }

    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        switch(position) {
            default:
            case 0:
                fragment = new TabUser();
                fragmentCount.push(position);
                break;
            case 1:
                fragment = new TabFood();
                fragmentCount.push(position);
                break;
            case 2:
                fragment = new TabWater();
                fragmentCount.push(position);
                break;
            case 3:
                fragment = new TabActivity();
                fragmentCount.push(position);
                break;
            case 4:
                fragment = new TabDiet();
                fragmentCount.push(position);
                break;
            case 5:
                fragment = new TabReports();
                fragmentCount.push(position);
                break;
            case 6:
                fragment = new TabAssit();
                fragmentCount.push(position);
                break;
        }

        fragmentStack.push(fragment);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings){
            Intent sendData = new Intent(this , SettingActivity.class);
            sendData.putExtra("data", data);
            startActivityForResult(sendData, 1);
            return true;
        }
        if (id == R.id.action_logout) {
            isLogin = false;
            saveSetting();
            Intent loginIntent = new Intent(NewHome.this, MainActivity.class);
            startActivity(loginIntent);
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((NewHome) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
    public DataKeeper getData(){
        return data;
    }
    public void setData(DataKeeper data){
        this.data = data;
    }

    @Override
    public void onBackPressed() {
        if (fragmentStack.size() > 1) {
            fragmentCount.pop();
            fragmentStack.pop().onPause();
            setItemCheck(fragmentCount.lastElement());
            Fragment fragment = fragmentStack.lastElement();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        } else {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_exit);
            dialog.setTitle("Exit app ?");
            Button button_exit = (Button) dialog.findViewById(R.id.button_exit);
            Button button_cancel = (Button) dialog.findViewById(R.id.button_cancle);
            dialog.show();
            button_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });
            button_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }
    private void setItemCheck(int i){
        mNavigationDrawerFragment.itemCheck(i);
    }
    public void changeFragment(int i){
        onNavigationDrawerItemSelected(i);
        setItemCheck(i);
        onSectionAttached(i+1);
        restoreActionBar();
    }

    public void showToast(CharSequence text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    private String convertingDataKeeperToJSONString(DataKeeper data){
        Gson gson = new Gson();
        return gson.toJson(data);
    }
    public ArrayList<Food> getFood_list(){
        return food_list;
    }
    public ArrayList<Workout> getActivity_list(){
        return activity_list;
    }


    //---------------showalert

    public void showAlert(){
        String Trip = "..";
        Random randTrip = new Random();
        int number = randTrip.nextInt(5)+1;
        switch (number){
            case 1: Trip = "Do not eat more than one teaspoon of sodium.";
                break;
            case 2: Trip = "Instant noodles are extremely harmful to the kidneys.";
                break;
            case 3: Trip = "Eating salty risk of heart disease";
                break;
            case 4: Trip ="Oat\n"+"Eating oatmeal regularly can reduce Cholesterol\n"+
                    "Reduce constipation,the absorption of fat and sugar";
                break;
            case 5: Trip = "Easy Trick to lose weight\n"+
                    "1.Drink water about 12/16 glasses/day\n"+"2.Cheat meal less than 2 potion\n"+
                    "3Avoid energy drinks ex.cola";
                break;
        }

        AlertDialog.Builder chuppyAlert = new AlertDialog.Builder(this);
        chuppyAlert.setMessage(Trip)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setTitle("Trip")
                .setIcon(R.drawable.logo)
                .create();
        chuppyAlert.show();

    }
}



