package com.bakerj.base.fragment;


import com.bakerj.base.BasePresenter;

public abstract class BasePresenterFragment<T extends BasePresenter> extends BaseFragment {
    protected T mPresenter;

    public void setPresenter(T presenter) {
        this.mPresenter = presenter;
    }
}
