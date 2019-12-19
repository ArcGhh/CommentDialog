package com.test.commentdialog.widget;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.view.MotionEvent;
import android.widget.TextView;

public class TextMovementMethods extends LinkMovementMethod {

    private TextClickSpans mTextClickSpan;

    private boolean isSpanClick;

    public boolean isSpanClick() {
        return isSpanClick;
    }

    public void setSpanClick(boolean spanClick) {
        isSpanClick = spanClick;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mTextClickSpan = getTextSpan(widget, buffer, event);
            if (mTextClickSpan != null) {
                setSpanClick(true);
                mTextClickSpan.setPressed(true);
                Selection.setSelection(buffer, buffer.getSpanStart(mTextClickSpan), buffer.getSpanEnd(mTextClickSpan));
            } else {
                setSpanClick(false);
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //不需要处理
//            TextClickSpans touchTextClickSpan = getTextSpan(widget, buffer, event);
//            if (mTextClickSpan != null && touchTextClickSpan != mTextClickSpan) {
//                mTextClickSpan.setPressed(false);
//                mTextClickSpan = null;
//                Selection.removeSelection(buffer);
//            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mTextClickSpan != null) {
                mTextClickSpan.onClick(widget);
                mTextClickSpan.setPressed(false);
                mTextClickSpan = null;
            }
            Selection.removeSelection(buffer);
        } else {
            if (mTextClickSpan != null) {
                mTextClickSpan.onClick(widget);
                mTextClickSpan.setPressed(false);
                mTextClickSpan = null;
            }
            Selection.removeSelection(buffer);

        }
        return Touch.onTouchEvent(widget, buffer, event);
    }

    /**
     * 得到匹配的span
     *
     * @param widget
     * @param spannable
     * @param event
     * @return
     */
    private TextClickSpans getTextSpan(TextView widget, Spannable spannable, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= widget.getTotalPaddingLeft();
        y -= widget.getTotalPaddingTop();

        x += widget.getScrollX();
        y += widget.getScrollY();

        Layout layout = widget.getLayout();

        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);

        TextClickSpans[] link = spannable.getSpans(off, off, TextClickSpans.class);
        TextClickSpans touchedSpan = null;
        if (link.length > 0) {
            touchedSpan = link[0];
        }
        return touchedSpan;
    }
}
