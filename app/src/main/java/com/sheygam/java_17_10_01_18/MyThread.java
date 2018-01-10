package com.sheygam.java_17_10_01_18;

import android.util.Log;

/**
 * Created by gregorysheygam on 10/01/2018.
 */

public class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            Log.d("MY_TAG", "run: " + i);
            if(isInterrupted()){
                break;
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
