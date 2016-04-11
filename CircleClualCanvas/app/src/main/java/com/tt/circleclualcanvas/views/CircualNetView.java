package com.tt.circleclualcanvas.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.tt.circleclualcanvas.R;
import com.tt.circleclualcanvas.entity.CircleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by TuoZhaoBing on 2016/4/8 0008.
 */
public class CircualNetView extends View implements ViewTreeObserver.OnGlobalLayoutListener{

    public static final String TAG = "CircualNetView";
    private Context mContext;
    /*
	* 单指拖动
	* */
    private GestureDetector mGestureDetector = null;

    /*
    * 双指缩放
    * */
    private ScaleGestureDetector mScaleGestureDetector = null;
    float mScaleFactor = 1;
    float mPivotX;
    float mPivotY;
    private float MIN_SCALE = 1.0f;
    private float MAX_SCALE = 25.0f;
    /*
    * CircualNetView的宽高
    * */
    private int mViewWidth,mViewHeight;

    /*
    * 存储界面上所有圆的信息
    * */
    private List<CircleView> mCircleViews;
    /*
    * 存储要绘制的所有字符串
    * */
    private List<String> mDrawingTextStrList;
    /*
    * 画笔
    * */
    private Paint mTextPaint,mCirclePaint,mLinePaint;
    /*
    * 临时存储圆的半径
    * */
    private int mTmpRadius;
    /*
    * 被点击的圆
    * */
    private CircleView mTouchedCircleView = null;
    private int mTouchStartX,mTouchStartY,mTouchEndX,mTouchEndY;

    private boolean isFirstCreated = true;
    public int touchPointIndex = -1;

    public CircualNetView(Context context) {
        super(context);
        init(context);
    }

    public CircualNetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircualNetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        mContext = context;
        mCircleViews = new ArrayList<>();
        mDrawingTextStrList = new ArrayList<>();
        mDrawingTextStrList.add("HELLO");
        mDrawingTextStrList.add("NICE");
        mDrawingTextStrList.add("DOUBLE");
        mDrawingTextStrList.add("HELLO");
        mDrawingTextStrList.add("NICE");
        mDrawingTextStrList.add("DOUBLE");
        mDrawingTextStrList.add("HELLO");
        mDrawingTextStrList.add("NICE");
        mDrawingTextStrList.add("DOUBLE");
        mScaleGestureDetector = new ScaleGestureDetector(context,new OnScaleLayoutListener());
        mGestureDetector  = new GestureDetector(context, new SimpleGestureListener());
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.RED);
        mCirclePaint.setColor(Color.GREEN);
        mLinePaint.setColor(Color.BLUE);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG,mScaleFactor+"  SCALE");
        setScaleX(mScaleFactor);
        setScaleY(mScaleFactor);
        if (isFirstCreated){
            for (int i=0;i< mDrawingTextStrList.size();i++){
                mTmpRadius = getRadius(mDrawingTextStrList.get(i).toString());
                CircleView circleView = new CircleView(randomX(),randomY(),mTmpRadius);
                mCircleViews.add(circleView);
            }
            isFirstCreated = false;
        }
        /*
        * 画线
        */
        for (int i=1;i<mCircleViews.size();i++){
            canvas.drawLine(mCircleViews.get(0).getmCenterX(),mCircleViews.get(0).getmCenterY(),mCircleViews.get(i).getmCenterX(),mCircleViews.get(i).getmCenterY(),mLinePaint);
        }
        /*
        * 画圆
        * */
        for (int i=0;i<mCircleViews.size();i++){
            canvas.drawCircle(mCircleViews.get(i).getmCenterX(),mCircleViews.get(i).getmCenterY(),mCircleViews.get(i).getmRadius(),mCirclePaint);
        }
        /*
        * 画字
        * */
        for (int i=0;i<mCircleViews.size();i++){
            canvas.drawText(mDrawingTextStrList.get(i),mCircleViews.get(i).getmCenterX(),mCircleViews.get(i).getmCenterY(),mTextPaint);
        }
        super.onDraw(canvas);
    }

    @Override
    public void onGlobalLayout() {
        mViewWidth = getWidth();
        mViewHeight = getHeight();
    }

    /*
    * 随机生成圆心X坐标
    * */
    public int randomX(){
        Random random = new Random();
        return random.nextInt(mViewWidth)+1;
    }

    /*
    * 随机生成圆心Y坐标
    * */
    public int randomY(){
        Random random = new Random();
        return random.nextInt(mViewHeight)+1;
    }

    /*
    * 获取圆的半径
    * */
    private int getRadius(String str){
        return str.length()*20;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:{
                mTouchStartX = (int)event.getX();
                mTouchStartY = (int)event.getY();
                mTouchedCircleView = getTouchedCircle(mTouchStartX,mTouchStartY);
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                if (mTouchedCircleView != null){
                    mTouchedCircleView.setmCenterX(mTouchStartX);
                    mTouchedCircleView.setmCenterY(mTouchStartY);
                    mTouchStartX = (int)event.getX();
                    mTouchStartY = (int)event.getY();
                }
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:{
                break;
            }
        }
        return true;
    }

    private class SimpleGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (mTouchedCircleView == null){
                setX(getX() - distanceX);
                setY(getY() - distanceY);
                postInvalidate();
                invalidate();
            }
            return false;
        }
    }

    /*
    * 获取被点击的CircleView
    * */
    private CircleView getTouchedCircle(final int xTouch, final int yTouch) {
        CircleView touched = null;

        for (CircleView circle : mCircleViews) {
            if ((circle.getmCenterX() - xTouch) * (circle.getmCenterX() - xTouch) + (circle.getmCenterY() - yTouch) * (circle.getmCenterY() - yTouch) <= circle.getmRadius() * circle.getmRadius()) {
                touched = circle;
                break;
            }
        }

        return touched;
    }

    /*
    * 双指缩放监听类
    * */
    private class OnScaleLayoutListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        float currentSpan;
        float startFocusX;
        float startFocusY;
        public OnScaleLayoutListener() {
            super();
        }

        public boolean onScaleBegin(ScaleGestureDetector detector)     {
            currentSpan = detector.getCurrentSpan();
            startFocusX = detector.getFocusX();
            startFocusY = detector.getFocusY();
            return true;
        }
        public boolean onScale(ScaleGestureDetector detector)     {
            CircualNetView zoomableRelativeLayout= (CircualNetView) ((Activity)mContext).findViewById(R.id.circual);
            zoomableRelativeLayout.relativeScale(detector.getCurrentSpan() / currentSpan, startFocusX, startFocusY);
            currentSpan = detector.getCurrentSpan();
            return true;
        }
        public void onScaleEnd(ScaleGestureDetector detector)     {
            CircualNetView zoomableRelativeLayout= (CircualNetView) ((Activity)mContext).findViewById(R.id.circual);
            zoomableRelativeLayout.release();
        }
    }

    public void scale(float scaleFactor, float pivotX, float pivotY) {
        mScaleFactor = scaleFactor;
        mPivotX = pivotX;
        mPivotY = pivotY;
        this.invalidate();
    }

    public void restore() {
        mScaleFactor = 1;
        this.invalidate();
    }

    public void relativeScale(float scaleFactor, float pivotX, float pivotY) {
        mScaleFactor *= scaleFactor;
        if(scaleFactor >= 1)     {
            mPivotX = mPivotX + (pivotX - mPivotX) * (1 - 1 / scaleFactor);
            mPivotY = mPivotY + (pivotY - mPivotY) * (1 - 1 / scaleFactor);
        }
        else     {
            pivotX = getWidth()/2;
            pivotY = getHeight()/2;
            mPivotX = mPivotX + (pivotX - mPivotX) * (1 - scaleFactor);
            mPivotY = mPivotY + (pivotY - mPivotY) * (1 - scaleFactor);
        }
        this.invalidate();
    }

    public void release() {
        if(mScaleFactor < MIN_SCALE)     {
            final float startScaleFactor = mScaleFactor;
            Animation a = new Animation(){
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t)             {
                    scale(startScaleFactor + (MIN_SCALE - startScaleFactor)*interpolatedTime,mPivotX,mPivotY);
                }
            }
                    ;
            a.setDuration(300);
            startAnimation(a);
        }
        else if(mScaleFactor > MAX_SCALE)     {
            final float startScaleFactor = mScaleFactor;
            Animation a = new Animation()         {
                @Override             protected void applyTransformation(float interpolatedTime, Transformation t)             {
                    scale(startScaleFactor + (MAX_SCALE - startScaleFactor)*interpolatedTime,mPivotX,mPivotY);
                }
            }
                    ;
            a.setDuration(300);
            startAnimation(a);
        }
    }

}
