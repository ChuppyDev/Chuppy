package com.example.pansit.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pansit.myapplication.LoginActivity;

/**
 * Created by Pansit on 6/2/2015.
 */
public class ForgetActivity extends Activity {


    Button enterButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpass);


        enterButton = (Button)findViewById(R.id.enterButton_forgetpass);
    }

    public void email_submit_forgetpass(View v) {
        //Database//




        /*************************/
        showToast(v);
        Intent intent = new Intent(v.getContext(), LoginActivity.class);
        startActivityForResult(intent, 0);
    }
    public void showToast(View view){
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
    }
}
