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
import com.tt.circleclualcanvas.listener.OnCircleViewClickListener;
import com.tt.circleclualcanvas.utils.MeasureUtils;

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
        * clickListener
        * */
    private OnCircleViewClickListener mCircleViewClickListener = null;
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
    private int mViewWidth,mViewHeight,mViewLeft,mViewTop;

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
    private int mFirstX,mFirstY,mTouchStartX,mTouchStartY,mTouchEndX,mTouchEndY;

    private boolean isFirstCreated = true;
    public int touchPointIndex = -1;
    /*
    * flag to indicate event type
    * false---click
    * true ---Drag
    * */
    public boolean eventType = true;

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

    public float getMIN_SCALE() {
        return MIN_SCALE;
    }

    public void setMIN_SCALE(float MIN_SCALE) {
        this.MIN_SCALE = MIN_SCALE;
    }

    public float getMAX_SCALE() {
        return MAX_SCALE;
    }

    public void setMAX_SCALE(float MAX_SCALE) {
        this.MAX_SCALE = MAX_SCALE;
    }

    public List<String> getDrawingTextStrList() {
        return mDrawingTextStrList;
    }

    public void setDrawingTextStrList(List<String> mDrawingTextStrList) {
        if (this.mDrawingTextStrList != null){
            this.mDrawingTextStrList.clear();
        }
        if (this.mCircleViews != null){
            this.mCircleViews.clear();
        }
        this.mDrawingTextStrList = mDrawingTextStrList;
        isFirstCreated = true;
        invalidate();
    }

    public void setCircleViewClickListener(OnCircleViewClickListener mCircleViewClickListener) {
        this.mCircleViewClickListener = mCircleViewClickListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mScaleFactor <= MIN_SCALE){
            mScaleFactor = MIN_SCALE;
        }else if (mScaleFactor >= MAX_SCALE){
            mScaleFactor = MAX_SCALE;
        }
        setScaleX(mScaleFactor);
        setScaleY(mScaleFactor);
        if (isFirstCreated){
            for (int i=0;i< mDrawingTextStrList.size();i++){
                mTmpRadius = getRadius(mDrawingTextStrList.get(i).toString());
                CircleView circleView = null;
                circleView = new CircleView(randomX(mTmpRadius),randomY(mTmpRadius),mTmpRadius,mDrawingTextStrList.get(i));
                while(!judge2CircleLocationLegal(circleView)){
                    circleView = new CircleView(randomX(mTmpRadius),randomY(mTmpRadius),mTmpRadius,mDrawingTextStrList.get(i));
                }
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
            canvas.drawText(mCircleViews.get(i).getmText(),mCircleViews.get(i).getmCenterX(),mCircleViews.get(i).getmCenterY(),mTextPaint);
        }
        super.onDraw(canvas);
    }

    @Override
    public void onGlobalLayout() {
        mViewWidth = getWidth();
        mViewHeight = getHeight();
        mViewLeft = getLeft();
        mViewTop = getTop();
    }

    /*
    * 判断生成的圆有没有和其他圆相交或重叠
    * */
    public boolean judge2CircleLocationLegal(CircleView circleView){
        for (CircleView circleView1:mCircleViews){
            if ((get2CircleViewCenterDistance(circleView,circleView1) <= get2CircleViewRediusMild(circleView,circleView1)*0.3)
                    || (get2CircleViewCenterDistance(circleView,circleView1) <= get2CircleViewRadiusSub(circleView,circleView1))) {
                return false;
            }
        }
        return true;
    }

    /*
    * 得到两圆圆心之间的距离
    * */
    public int get2CircleViewCenterDistance(CircleView circleView,CircleView cirr){
        return (circleView.getmCenterY()-cirr.getmCenterY())*(circleView.getmCenterY()-cirr.getmCenterY())+(circleView.getmCenterX()-cirr.getmCenterX())*(circleView.getmCenterX()-cirr.getmCenterX());
    }
    /*
    * 得到两圆半径和
    * */
    public int get2CircleViewRediusMild(CircleView circleView,CircleView crr){
        return (circleView.getmRadius()+crr.getmRadius())*(circleView.getmRadius()+crr.getmRadius());
    }

    /*
    * 得到两圆半径差
    * */
    public int get2CircleViewRadiusSub(CircleView circleView,CircleView crr){
        return (circleView.getmRadius()-crr.getmRadius())*(circleView.getmRadius()-crr.getmRadius());
    }
    /*
    * 随机生成圆心X坐标
    * */
    public int randomX(int radius){
        Random random = new Random();
        int centerx = random.nextInt(mViewWidth)+1;
        while(!judgeCircleCenterX(centerx,radius)){
            centerx = random.nextInt(mViewWidth)+1;
        }
        return centerx;
    }

    /*
    * 随机生成圆心Y坐标
    * */
    public int randomY(int radius){
        Random random = new Random();
        int centery = random.nextInt(mViewHeight)+1;
        while(!judgeCircleCenterY(centery,radius)){
            centery = random.nextInt(mViewHeight)+1;
        }
        return centery;
    }

    /*
    * 获取圆的半径
    * */
    private int getRadius(String str){
        return str.length()*20;
    }

    /*
    * 判断圆心坐标是否合法
    * */
    public boolean judgeCircleCenterX(int centerX,int radius){
        if (centerX+radius > mViewWidth+mViewLeft || (centerX-radius)<mViewLeft){
            return false;
        }
        return true;
    }

    public boolean judgeCircleCenterY(int centerY,int radius){
        if (centerY+radius>mViewHeight+mViewTop || (centerY-radius)<mViewTop){
            return false;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:{
                mTouchStartX = (int)event.getX();
                mTouchStartY = (int)event.getY();
                mFirstX = (int)event.getRawX();
                mFirstY = (int)event.getRawY();
                mTouchedCircleView = getTouchedCircle(mTouchStartX,mTouchStartY);
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                if (mTouchedCircleView != null){
                    eventType = true;
                    mTouchedCircleView.setmCenterX(mTouchStartX);
                    mTouchedCircleView.setmCenterY(mTouchStartY);
                    mTouchStartX = (int)event.getX();
                    mTouchStartY = (int)event.getY();
                }
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:{
                mTouchEndX = (int)event.getRawX();
                mTouchEndY = (int)event.getRawY();
                if ((mTouchedCircleView != null) && (Math.abs(mTouchEndX - mFirstX) == 0 && Math.abs(mTouchEndY - mFirstY) == 0)){
                    mCircleViewClickListener.onClick(mTouchedCircleView.getmText());
                }else{
                    eventType = true;
                }
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
