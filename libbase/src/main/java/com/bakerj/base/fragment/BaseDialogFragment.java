package com.bakerj.base.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bakerj.rxretrohttp.interfaces.IBaseApiAction;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatDialogFragment;

import icepick.Icepick;

/**
 * @author BakerJ
 * @date 2017/12/28
 */

public class BaseDialogFragment extends RxAppCompatDialogFragment implements IBaseApiAction {
    private boolean isVisible = false, isCreated = false, isLoaded = false;

    @Override
    public void showLoading() {
        if (isAdded() && getActivity() instanceof IBaseApiAction) {
            ((IBaseApiAction) getActivity()).showLoading();
        }
    }

    public void dismissLoading() {
        if (isAdded() && getActivity() instanceof IBaseApiAction) {
            ((IBaseApiAction) getActivity()).dismissLoading();
        }
    }

    @Override
    public void showToast(String msg) {
        if (isAdded() && getActivity() instanceof IBaseApiAction) {
            ((IBaseApiAction) getActivity()).showToast(msg);
        }
    }

    public <T> LifecycleTransformer<T> getLifecycleTransformer() {
        return bindToLifecycle();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        getArgumentsOrRestore(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        if (isVisible) {
            tryLazyLoad();
        }
        isCreated = true;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisibleToUser && isCreated) {
            tryLazyLoad();
        }
    }

    private void tryLazyLoad() {
        if (!isAdded()) {
            return;
        }
        lazyLoadOnShow();
        if (!isLoaded) {
            lazyLoadOnce();
        }
        isLoaded = true;
    }

    protected void lazyLoadOnce() {

    }

    protected void lazyLoadOnShow() {

    }

    private void getArgumentsOrRestore(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            initArgumentsOrRestore(getArguments());
        } else {
            Icepick.restoreInstanceState(this, savedInstanceState);
            initArgumentsOrRestore(savedInstanceState);
        }
    }

    protected void initArgumentsOrRestore(Bundle bundle) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
}
