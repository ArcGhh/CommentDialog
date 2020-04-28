package com.test.commentdialog.util;

import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @author ganhuanhui
 * @date 2020/4/28 0028
 * @desc
 */
public class RecyclerViewUtil {

    private RecyclerView recyclerView;

    public RecyclerViewUtil() {
    }

    public void initScrollListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        if (mScrollListener != null) {
            this.recyclerView.addOnScrollListener(mScrollListener);
        }
    }

    private int currentState = -1;
    private Handler cancelScrollHandler = new Handler();
    private Runnable cancelScrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (recyclerView != null) {
                currentState = -1;
                // 当用户停止滑动的时候 主动触摸Recyclerview，以达到能够立即消费到停止滚动事件，防止出现点击item2次才会触发点击事件
                recyclerView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0f, 0f, 0));
            }
        }
    };

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            currentState = newState;
            Log.e("bottomSheetAdapter", "newState:: " + newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //如果在持续滚动中，则取消回调
            if (cancelScrollHandler == null || cancelScrollRunnable == null) return;
            cancelScrollHandler.removeCallbacks(cancelScrollRunnable);
            //当前RecyclerView在滚动设置到某个位置的动画状态，代码调用时或者惯性滚动时就是这个状态
            if (currentState == RecyclerView.SCROLL_STATE_SETTLING) {
                cancelScrollHandler.postDelayed(cancelScrollRunnable, 20);
            }
        }
    };

    public void destroy() {
        if (cancelScrollHandler != null && cancelScrollRunnable != null) {
            cancelScrollHandler.removeCallbacks(cancelScrollRunnable);
            cancelScrollHandler = null;
        }
        if (recyclerView != null && mScrollListener != null) {
            recyclerView.removeOnScrollListener(mScrollListener);
            recyclerView = null;
            mScrollListener = null;
        }
    }

}
