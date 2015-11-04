package com.example.pansit.myapplication;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by Pansit on 6/2/2015.
 */
public class LoginActivity extends Activity {

    EditText id_input,password_input;
    Button enterButton;
    ArrayList<Food> food_list;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.pansit.myapplication.R.layout.login);
        id_input = (EditText)findViewById(R.id.id_input);
        password_input = (EditText)findViewById(R.id.password_input);
        enterButton = (Button)findViewById(R.id.enterlogin_btn);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = id_input.getText().toString();
                String password = password_input.getText().toString();
                DataKeeper dataKeeper = new DataKeeper(user,password);
                authenticate(dataKeeper);
                //}
                //else{
                //  showToast(v,"No Internet connection.");
                //}
            }
        });

        password_input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    String user = id_input.getText().toString();
                    String password = password_input.getText().toString();
                    DataKeeper dataKeeper = new DataKeeper(user, password);
                    authenticate(dataKeeper);

                    return true;
                }
                return false;
            }
        });

    }
   @Override
   public void onSaveInstanceState(Bundle outState) {
       super.onSaveInstanceState(outState);
       outState.putString("message", "This is my message to be reloaded");
       showErrorMessage();
   }

    public void forget_pass_clicked(View v) {
        Intent intent = new Intent(v.getContext(), ForgetActivity.class);
        startActivityForResult(intent, 0);
    }


    private void authenticate(DataKeeper user) {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.fetchUserDataAsyncTask(user, new GetUserCallback() {
            @Override
            public void done(DataKeeper returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    food_list = new ArrayList<Food>();
                    Food food = new Food("df", "df", "df", 1, 4, 3, 2, 1, 3);
                    food_list.add(food);
                    logUserIn(returnedUser);
                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        dialogBuilder.setMessage("Incorrect Username or Password details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void logUserIn(DataKeeper returnedUser) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        String foodListJson = getJSONUrl("http://www.mainarak.esy.es/getfoodlist.php");
        String activityListJson = getJSONUrl("http://www.mainarak.esy.es/getactlist.php");
        Intent sendData = new Intent(this , NewHome.class);
        //sendData.putExtra("data", returnedUser);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",returnedUser);
        bundle.putString("foodlistjson", foodListJson);
        bundle.putString("activitylistjson",activityListJson);
        sendData.putExtras(bundle);
        startActivityForResult(sendData, 1);
        finish();
    }
    @SuppressWarnings("deprecation")
    public String getJSONUrl(String url) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Download OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            Log.e("Log", "Failed to download result..");
        } catch (IOException e) {
            Log.e("Log", "Failed to download result..");
        }
        return str.toString();
    }

}
