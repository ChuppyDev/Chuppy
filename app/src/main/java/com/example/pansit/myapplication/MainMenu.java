package com.example.pansit.myapplication;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Window;


/**
 * Created by Pansit on 6/2/2015.
 */
@SuppressWarnings("deprecation")
public class MainMenu extends Activity {

    /* Swipe


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_menu);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setAdapter(new TabPageAdapter(getSupportFragmentManager()));

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        slidingTabLayout.setViewPager(viewPager);


    }
    *//////////////////for swipe////////

    private DataKeeper data;

    ActionBar.Tab emptyTab
            ,newfeedTab,settingTab,userTab;





    Fragment fragement_newfeed = new TabNewfeed();
    Fragment fragement_user = new TabUser();
    Fragment fragement_empty = new TabEmpty();
    Fragment fragement_setting = new TabSetting();




    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.tab_menu);

        ActionBar actionBar = getActionBar();
        Intent activityThatCalled = getIntent();


        data = (DataKeeper) activityThatCalled.getSerializableExtra("data");





        //create Tab
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            newfeedTab = actionBar.newTab().setIcon(R.drawable.ic_chat);
            userTab = actionBar.newTab().setIcon(R.drawable.ic_accessibility_black_24dp);
            emptyTab = actionBar.newTab().setIcon(R.drawable.icn3);
            settingTab = actionBar.newTab().setIcon(R.drawable.ic_settings_black_24dp);

            newfeedTab.setTabListener(new TabListener(fragement_newfeed));
            userTab.setTabListener(new TabListener(fragement_user));
            emptyTab.setTabListener(new TabListener(fragement_empty));
            settingTab.setTabListener(new TabListener(fragement_setting));


            actionBar.addTab(userTab);
            actionBar.addTab(newfeedTab);
            actionBar.addTab(emptyTab);
            actionBar.addTab(settingTab);


        }

    }
    public DataKeeper getData(){
        return data;
    }
    public void setData(DataKeeper data){
        this.data = data;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
