package com.bakerj.base;

public abstract class BasePresenter<T extends BaseView> {
    protected T mView;

    public BasePresenter(T view) {
        this.mView = view;
    }
}
