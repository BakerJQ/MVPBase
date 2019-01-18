package com.bakerj.demo.mvpbase;

import com.bakerj.base.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected String getJumpBundleString() {
        return "Jump";
    }

    @Override
    protected int getLayoutLoading() {
        return R.layout.activity_main;
    }

    @Override
    protected int getLoadingStyle() {
        return 0;
    }
}
