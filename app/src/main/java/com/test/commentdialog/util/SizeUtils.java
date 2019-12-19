package com.test.commentdialog.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.test.commentdialog.CommentApplication;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class SizeUtils {

    private SizeUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    public static int dp2px(float value) {
        return AutoSizeUtils.dp2px(CommentApplication.getApplication(), value);
    }

    public static int sp2px(float value) {
        return AutoSizeUtils.sp2px(CommentApplication.getApplication(), value);
    }

    public static int pt2px(float value) {
        return AutoSizeUtils.pt2px(CommentApplication.getApplication(), value);
    }

    public static int in2px(float value) {
        return AutoSizeUtils.in2px(CommentApplication.getApplication(), value);
    }

    public static int mm2px(float value) {
        return AutoSizeUtils.mm2px(CommentApplication.getApplication(), value);
    }

    public static float getScreenWidth() {
        WindowManager w = (WindowManager) CommentApplication.getApplication().getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);

        return metrics.widthPixels;
    }

    public static float getScreenHeight() {
        WindowManager w = (WindowManager) CommentApplication.getApplication().getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static float getStatusBarHeight(Context context) {
        float result = 24;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        } else {
            result = AutoSizeUtils.dp2px(context, result);
        }
        return result;
    }
}
