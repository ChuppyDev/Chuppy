package com.example.pansit.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*
//Update version alpha 9995
    -update assistance tab
    -new Achievement class
    (1.drink water in one day(1 time),
     2.reach carbohydrate 125g/day 5 times)
*/

public class MainActivity extends Activity {

    Button loginButton,registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if user not log out

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isLogin = sharedPreferences.getBoolean("isLogin",false);

        if(isLogin){
            Intent goMainMenu = new Intent(this, NewHome.class);
            startActivityForResult(goMainMenu, 0);
            finish();
        }


        if(savedInstanceState != null){
            showToast(savedInstanceState.getString("message"));
        }

        registerButton = (Button)findViewById(R.id.registerButton);
        loginButton = (Button)findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivityForResult(intent, 0);
                finish();

            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivityForResult(intent, 0);
                finish();
            }
        });

    }
    public void showToast(CharSequence text){
        Toast.makeText(this , text, Toast.LENGTH_SHORT).show();
    }





}

