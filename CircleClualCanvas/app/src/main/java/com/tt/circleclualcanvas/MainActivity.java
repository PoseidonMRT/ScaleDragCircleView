package com.tt.circleclualcanvas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tt.circleclualcanvas.listener.OnCircleViewClickListener;
import com.tt.circleclualcanvas.views.CircualNetView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnCircleViewClickListener{

    private CircualNetView mCircualNetView = null;
    private List<String> mDrawingTextStrList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        CircualNetView circualNetView = new CircualNetView(this);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    public void initView(){
        mCircualNetView = (CircualNetView) findViewById(R.id.circual);
    }

    public void initData(){
        mCircualNetView.setCircleViewClickListener(this);
    }

    @Override
    public void onClick(String clickStr) {
        Log.e("ONClick",clickStr+"");
        mDrawingTextStrList = new ArrayList<>();
        mDrawingTextStrList.add(clickStr);
        mDrawingTextStrList.add("456");
        mDrawingTextStrList.add("789");
        mDrawingTextStrList.add("987");
        mDrawingTextStrList.add("654");
        mDrawingTextStrList.add("321");
        mDrawingTextStrList.add("000");
        mDrawingTextStrList.add("...");
       mCircualNetView.setDrawingTextStrList(mDrawingTextStrList);
    }
}
