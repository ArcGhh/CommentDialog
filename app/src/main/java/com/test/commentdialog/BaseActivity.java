package com.test.commentdialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * @author ganhuanhui
 * 时间：2019/12/18 0018
 * 描述：
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initData();
    }

    abstract int getLayoutId();

    abstract void initView();

    abstract void initData();

    protected void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    protected void startActivity(Class c){
        startActivity(new Intent(this,c));
    }

}
