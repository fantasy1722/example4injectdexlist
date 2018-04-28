package com.example.fantasy1722.example4injectdexlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.examplelibrary.test;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test test1 = new test();
        String ss = test1.getString();
        TextView mTextView = (TextView)findViewById(R.id.text);
        mTextView.setText(ss);
    }
}
