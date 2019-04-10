package com.bakerj.base.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bakerj.rxretrohttp.interfaces.IBaseApiAction;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.support.RxFragment;

import java.util.List;

import icepick.Icepick;

public class BaseFragment extends RxFragment implements IBaseApiAction {
    private String mFragmentTag;
    private boolean isVisible = false, isCreated = false, isLoaded = false;

    public static BaseFragment findFragmentByTag(FragmentManager fragmentManager, String tag) {
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof BaseFragment && tag.equals(((BaseFragment) fragment)
                    .getFragmentTag())) {
                return (BaseFragment) fragment;
            }
        }
        return null;
    }

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
        return bindUntilEvent(FragmentEvent.DESTROY_VIEW);
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
            isLoaded = true;
        }
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

    public String getFragmentTag() {
        return mFragmentTag;
    }

    public void setFragmentTag(String fragmentTag) {
        this.mFragmentTag = fragmentTag;
    }
}
