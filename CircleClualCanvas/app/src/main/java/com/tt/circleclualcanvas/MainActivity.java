package com.tt.circleclualcanvas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tt.circleclualcanvas.views.CircualNetView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        CircualNetView circualNetView = new CircualNetView(this);
        setContentView(R.layout.activity_main);
    }
}
