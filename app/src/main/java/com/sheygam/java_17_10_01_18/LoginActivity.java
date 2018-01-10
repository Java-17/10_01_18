package com.sheygam.java_17_10_01_18;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputPassword, inputEmail;
    private ProgressBar myProgress;
    private Button registerationBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        myProgress = findViewById(R.id.my_progress);
        registerationBtn = findViewById(R.id.reg_btn);
        registerationBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.reg_btn){
            new RegTask().execute();
        }
    }

    class RegTask extends AsyncTask<Void,Void,String>{
        String email, password;
        boolean isSuccess = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myProgress.setVisibility(View.VISIBLE);
            email = inputEmail.getText().toString();
            password = inputPassword.getText().toString();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "Reg ok";
            Gson gson = new Gson();
            Auth auth = new Auth(email,password);
            String data = gson.toJson(auth);
            String baseUrl = "https://telranstudentsproject.appspot.com/_ah/api/contactsApi/v1";

            try {
                URL url = new URL(baseUrl+"/registration");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.addRequestProperty("Content-Type","application/json");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);

                OutputStream os = connection.getOutputStream();
//                connection.connect(); -- GET
                BufferedWriter bw  = new BufferedWriter(new OutputStreamWriter(os));
                bw.write(data);
                bw.flush();
                bw.close();
                String line;
                String res = "";
                BufferedReader br;
                if(connection.getResponseCode()<400){
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line  = br.readLine())!= null){
                        res+=line;
                    }
                    AuthToken authToken = gson.fromJson(res,AuthToken.class);
                    getSharedPreferences("AUTH",MODE_PRIVATE)
                    .edit()
                    .putString("TOKEN",authToken.getToken())
                    .commit();
                    Log.d("MY_TAG", "doInBackground: " + authToken.getToken());
                    br.close();

                }else if(connection.getResponseCode() == 409){
                    result = "User already exist!";
                    isSuccess = false;
                }else{
                    result = "Server error";
                    br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    while ((line = br.readLine())!=null){
                        res += line;
                    }
                    Log.d("MY_TAG", "doInBackground: error " + res);
                    isSuccess = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = "Connection error! Check your internet!";
                isSuccess = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
            myProgress.setVisibility(View.GONE);
            if (isSuccess){
                //Start next activity!
            }
        }
    }
}
