package com.test.commentdialog;

import android.view.View;
import com.test.commentdialog.multi.CommentMultiActivity;
import com.test.commentdialog.single.CommentSingleActivity;

public class MainActivity extends BaseActivity {

    @Override
    int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    void initView() {
    }

    @Override
    void initData() {

    }


    public void Single(View view) {
        startActivity(CommentSingleActivity.class);
    }

    public void Multi(View view) {
        startActivity(CommentMultiActivity.class);
    }
}
