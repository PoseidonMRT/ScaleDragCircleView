package com.tt.canvasmethoddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.tt.canvasmethoddemo.view.CanvasView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{

    @Bind(R.id.canvas_view)
    CanvasView canvasView;
    @Bind(R.id.line)
    Button line;
    @Bind(R.id.circle)
    Button circle;
    @Bind(R.id.circualrect)
    Button circualrect;
    @Bind(R.id.str)
    Button str;
    @Bind(R.id.path)
    Button path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
