package com.test.commentdialog;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.test.commentdialog.multi.CommentMultiActivity;
import com.test.commentdialog.single.CommentSingleActivity;

public class MainActivity extends BaseActivity {

    @Override
    int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    void initView() {
        ImageView iv = findViewById(R.id.iv);
        Glide.with(this).load("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg").into(iv);
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
