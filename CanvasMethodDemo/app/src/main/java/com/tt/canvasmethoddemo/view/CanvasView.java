package com.tt.canvasmethoddemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/3/30 0030.
 */
public class CanvasView extends View {

    private Paint mPaint;
    private Context mContext;
    private Canvas mCanvas;

    public CanvasView(Context context) {
        super(context);
        mContext = context;
        initPaint();
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPaint();
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        mCanvas.drawCircle(100,100,90,mPaint);
    }

    public void initPaint(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
    }
}
