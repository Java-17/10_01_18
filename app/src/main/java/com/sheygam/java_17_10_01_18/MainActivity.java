package com.sheygam.java_17_10_01_18;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startBtn, stopBtn;
    private MyThread myThread;
    private MyTask myTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startBtn = findViewById(R.id.start_btn);
        stopBtn = findViewById(R.id.stop_btn);
        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.start_btn){
            myThread = new MyThread();
            myThread.start();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myThread.interrupt();
            myTask = new MyTask();
            myTask.execute();
            try {
//                String result = myTask.get();
                String result = myTask.get(2, TimeUnit.SECONDS);
                Log.d("MY_TAG", "onClick: " + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }else if(v.getId() == R.id.stop_btn){
//            myThread.interrupt();

            myTask.cancel(true);
        }
    }

    class MyTask extends AsyncTask<Void,Integer,String>{

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.e("MY_TAG", "onProgressUpdate: " + values[0]);
        }

        @Override
        protected String doInBackground(Void... voids) {
            for (int i = 0; i < 10; i++) {
                publishProgress(i);
                Log.d("MY_TAG", "doInBackground: " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(isCancelled()){
                    break;
                }
            }
            return "Done";
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            Log.d("MY_TAG", "onPostExecute: ");
        }

        @Override
        protected void onCancelled(String aVoid) {
            super.onCancelled(aVoid);
            Log.d("MY_TAG", "onCancelled: ");
        }
    }
}













