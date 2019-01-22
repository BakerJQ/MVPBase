package com.bakerj.base.activity;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bakerj.base.utils.StatusBarUtils;
import com.bakerj.rxretrohttp.interfaces.IBaseApiAction;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import icepick.Icepick;

/**
 * Activity基类
 */
public abstract class BaseActivity extends RxAppCompatActivity implements IBaseApiAction {
    private Dialog mLoadingDialog;//loading

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        //检测是否有拦截跳转相关的bundle存在，一般为ARouter拦截器拦截转发的相关bundle
        if (getIntent().hasExtra(getJumpBundleString())) {
            checkJumpBundle(getIntent().getBundleExtra(getJumpBundleString()));
        } else {
            //初始化intent
            getIntentBundleOrRestore(savedInstanceState);
        }
        initLoadingDialog();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void getIntentBundleOrRestore(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //自动注入恢复数据
            Icepick.restoreInstanceState(this, savedInstanceState);
            initIntentBundleOrRestore(savedInstanceState);
        } else if (getIntent().getExtras() != null) {
            initIntentBundleOrRestore(getIntent().getExtras());
        }
    }

    protected void initIntentBundleOrRestore(Bundle bundle) {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    protected void initLoadingDialog() {
        View loadingView = LayoutInflater.from(this).inflate(getLayoutLoading(),
                findViewById(android.R.id.content), false);
        mLoadingDialog = new Dialog(this, getLoadingStyle());
        mLoadingDialog.setContentView(loadingView);
        if (mLoadingDialog.getWindow() != null) {
            mLoadingDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        mLoadingDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing() && !isFinishing()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed()) {
                return;
            }
            runOnUiThread(() -> {
                mLoadingDialog.show();
            });
        }
    }

    @Override
    public void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing() && !isFinishing()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed()) {
                return;
            }
            runOnUiThread(() -> {
                mLoadingDialog.dismiss();
            });
        }
    }

    @Override
    public void showToast(String msg) {
        if (!TextUtils.isEmpty(msg))
            runOnUiThread(() -> {
                ToastUtils.showLong(msg);
            });
    }

    protected void addFragment(int containerViewId, Fragment fragment, String tag) {
        final FragmentTransaction fragmentTransaction = this.getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }

    public <T> LifecycleTransformer<T> getLifecycleTransformer() {
        return bindUntilEvent(ActivityEvent.DESTROY);
    }

    public IBaseApiAction getApiAction() {
        return this;
    }

    protected void checkJumpBundle(Bundle bundle) {
        initIntentBundleOrRestore(bundle);
    }

    protected abstract String getJumpBundleString();

    protected abstract int getLayoutLoading();

    protected abstract int getLoadingStyle();

    protected void setDarkStatusBar() {
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        int type = StatusBarUtils.statusBarDarkMode(this);
        if (type == 0) {
            BarUtils.setStatusBarColor(this, Color.argb(100, 0, 0, 0));
        }
    }
}
