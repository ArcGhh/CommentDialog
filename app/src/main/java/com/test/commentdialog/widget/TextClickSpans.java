package com.test.commentdialog.widget;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;


/**
 * @author KCrason
 * @date 2018/4/28
 */
public abstract class TextClickSpans extends ClickableSpan implements View.OnClickListener {

    private boolean mPressed;

    public void setPressed(boolean isPressed) {
        this.mPressed = isPressed;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        if (mPressed){
            ds.bgColor = Color.parseColor("#B5B5B5");
        }else {
            ds.bgColor = Color.TRANSPARENT;
        }
        ds.setColor(Color.parseColor("#697A9F"));
        ds.setUnderlineText(false);
    }

}
