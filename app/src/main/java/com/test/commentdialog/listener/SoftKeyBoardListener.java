package com.test.commentdialog.listener;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * @author ganhuanhui
 * @date 2020/4/23 0023
 * @desc 获取软键盘的高度
 */
public class SoftKeyBoardListener {

    private View rootView;//activity的根视图
    int rootViewVisibleHeight;//纪录根视图的显示高度
    private OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener;

    public SoftKeyBoardListener(Activity activity, OnSoftKeyBoardChangeListener listener) {
        if (activity == null) return;
        this.onSoftKeyBoardChangeListener = listener;
        //获取activity的根视图
        rootView = activity.getWindow().getDecorView();
        if (rootView == null) return;
        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
    }

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            //获取当前根视图在屏幕上显示的大小
            if (rootView == null){
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                return;
            }
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int visibleHeight = r.height();
            if (rootViewVisibleHeight == 0) {
                rootViewVisibleHeight = visibleHeight;
                return;
            }

            //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
            if (rootViewVisibleHeight == visibleHeight) {
                return;
            }

            //根视图显示高度变小超过200，可以看作软键盘显示了
            if (rootViewVisibleHeight - visibleHeight > 200) {
                if (onSoftKeyBoardChangeListener != null) {
                    onSoftKeyBoardChangeListener.keyBoardShow(rootViewVisibleHeight - visibleHeight);
                }
                rootViewVisibleHeight = visibleHeight;
                return;
            }

            //根视图显示高度变大超过200，可以看作软键盘隐藏了
            if (visibleHeight - rootViewVisibleHeight > 200) {
                if (onSoftKeyBoardChangeListener != null) {
                    onSoftKeyBoardChangeListener.keyBoardHide(visibleHeight - rootViewVisibleHeight);
                }
                rootViewVisibleHeight = visibleHeight;
                return;
            }
        }
    };

    public void setOnSoftKeyBoardChangeListener(OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        this.onSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener;
        if (onSoftKeyBoardChangeListener == null){
            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalLayoutListener);
            rootView = null;
        }
    }

    public interface OnSoftKeyBoardChangeListener {
        void keyBoardShow(int height);

        void keyBoardHide(int height);
    }

}
