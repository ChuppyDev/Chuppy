package com.example.pansit.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;


@SuppressWarnings("ALL")
public class ServerRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://www.mainarak.esy.es/";

    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    // Register Call
    public void storeUserDataInBackground(DataKeeper user,
                                          GetUserCallback userCallBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallBack).execute();
    }

    // Login Call
    public void fetchUserDataAsyncTask(DataKeeper user, GetUserCallback userCallBack) {
        progressDialog.show();
        new fetchUserDataAsyncTask(user, userCallBack).execute();
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation result of the background computation
     */

    // Register Process
    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        DataKeeper user;
        GetUserCallback userCallBack;

        public StoreUserDataAsyncTask(DataKeeper user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }


        /*
            private String user;
    private String password;
    private String email;
    private int age;
    private int weight;
    private int height;
    private char sex;
    private String disease;
         */

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("user", user.getUser()));
            dataToSend.add(new BasicNameValuePair("password", user.getPassword()));
            dataToSend.add(new BasicNameValuePair("email", user.getEmail()));
            dataToSend.add(new BasicNameValuePair("age", user.getAge()+""));
            dataToSend.add(new BasicNameValuePair("weight", user.getWeight() + ""));
            dataToSend.add(new BasicNameValuePair("height", user.getHeight() + ""));
            dataToSend.add(new BasicNameValuePair("sex", user.getSex()+""));
            dataToSend.add(new BasicNameValuePair("disease", user.getDisease() ));

            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS
                    + "Register.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private HttpParams getHttpRequestParams() {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            return httpRequestParams;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            userCallBack.done(null);
        }

    }

    // Login Process
    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, DataKeeper> {
        DataKeeper user;
        GetUserCallback userCallBack;

        public fetchUserDataAsyncTask(DataKeeper user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected DataKeeper doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user.getUser()));
            dataToSend.add(new BasicNameValuePair("password", user.getPassword()));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS
                    + "FetchUserData.php");

            DataKeeper returnedUser = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0){
                    Log.v("happened", "2");

                    // output from database
                    String email = jObject.getString("email");
                    int age = jObject.getInt("age");
                    int weight = jObject.getInt("weight");
                    int height = jObject.getInt("height");
                    char sex = 'M';
                    String disease = jObject.getString("disease");



                    returnedUser = new DataKeeper(user.getUser(),user.getPassword(),email,age,weight,height,sex,disease);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(DataKeeper returnedUser) {
            super.onPostExecute(returnedUser);
            progressDialog.dismiss();
            userCallBack.done(returnedUser);
        }
    }

}
