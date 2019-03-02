package com.example.a13787.morningcall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by 13787 on 2019/2/28.
 */

public abstract class BaseActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());
        ActivityCollector.addActivity(this);
        //设置布局
        setContentView(initLayout());
        //初始化控件
        initView();
        //设置数据
        //initData();

    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initListener();
    protected abstract int initLayout();

}
