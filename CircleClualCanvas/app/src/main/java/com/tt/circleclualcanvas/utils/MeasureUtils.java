package com.tt.circleclualcanvas.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class MeasureUtils {
    public static int getScreenWidth(Context context){
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context){
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
