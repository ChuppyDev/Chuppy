package com.example.pansit.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Pansit on 5/26/2015.
 */
public class RegisterActivity extends Activity{
    Button submit;
    Spinner diseaseList;
    EditText email,password,name,age,weight,height;
    RadioButton female,male;
    DataKeeper data;


    public void onCreate(Bundle savedInstanceState) {

        // for easy to edit









        super.onCreate(savedInstanceState);
        setContentView(R.layout.registermenu);
        diseaseList = (Spinner)findViewById(R.id.diseaseSpinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.diseaseList,android.R.layout.simple_spinner_item);
        diseaseList.setAdapter(adapter);
        data = new DataKeeper();


        submit = (Button)findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = (EditText) findViewById(R.id.emailTxt);
                password = (EditText) findViewById(R.id.passwordTxt);
                name = (EditText) findViewById(R.id.nameTxt);
                age = (EditText) findViewById(R.id.ageTxt);
                female = (RadioButton) findViewById(R.id.femaleButton);
                male = (RadioButton) findViewById(R.id.maleButton);
                weight = (EditText) findViewById(R.id.weightTxt);
                height = (EditText) findViewById(R.id.heightTxt);

                if ((email.getText().toString().matches("")) || (name.getText().toString().matches("")) ||
                        (password.getText().toString().matches("")) || (age.getText().toString().matches("")) ||
                        (weight.getText().toString().matches("")) || (height.getText().toString().matches(""))){
                    showToast(v, "please fill in the blank.");
                }
                else if (!isEmailValid(email.getText().toString())) {
                    showToast(v, "Email is invalid.");
                }
                else if (!isUserValid(name.getText().toString())){
                    showToast(v, "Username is invalid.");
                }
                else if (diseaseList.getSelectedItem().toString().equals("Select")){
                    showToast(v, "Please select disease");
                }
                else {

                    data.setAge(Integer.parseInt(age.getText().toString()));
                    data.setDisease(diseaseList.getSelectedItem().toString());
                    data.setEmail(email.getText().toString());
                    data.setHeight(Integer.parseInt(height.getText().toString()));
                    data.setWeight(Integer.parseInt(weight.getText().toString()));
                    data.setPassword(password.getText().toString());
                    if (female.isChecked())
                        data.setSex('F');
                    else {
                        data.setSex('M');
                    }
                    data.setUser(name.getText().toString());

                    registerUser(data);
                    //if username is already taken ?


                }
            }
        });

    }
    public void showToast(View view,CharSequence text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void registerUser(DataKeeper user) {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(DataKeeper returnedUser) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    boolean isUserValid(final String username){
        String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
