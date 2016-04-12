package com.tt.circleclualcanvas.entity;

/**
 * Created by TuoZhaoBing on 2016/4/8 0008.
 */
public class CircleView {
    public static final String TAG = "CircleView";
    private int mCenterX,mCenterY;
    private int mRadius;
    private String mText;

    public CircleView() {
    }

    public CircleView(int mCenterX, int mCenterY, int mRadius, String text) {
        this.mCenterX = mCenterX;
        this.mCenterY = mCenterY;
        this.mRadius = mRadius;
        this.mText = text;

    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public int getmCenterX() {
        return mCenterX;
    }

    public void setmCenterX(int mCenterX) {
        this.mCenterX = mCenterX;
    }

    public int getmCenterY() {
        return mCenterY;
    }

    public void setmCenterY(int mCenterY) {
        this.mCenterY = mCenterY;
    }

    public int getmRadius() {
        return mRadius;
    }

    public void setmRadius(int mRadius) {
        this.mRadius = mRadius;
    }
}
