package com.example.examplelibrary;

import android.util.Log;

public class test {
    static String TAG = "test";
    static String info = "I'm test";

    public test(){
        Log.e(TAG, "new test");

    }

    public  String getString(){
        return info;
    }
}
