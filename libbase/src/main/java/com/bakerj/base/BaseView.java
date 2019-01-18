package com.bakerj.base;

import android.content.Context;

import com.bakerj.rxretrohttp.interfaces.IBaseApiAction;

public interface BaseView extends IBaseApiAction {
    Context getContext();

    void showLoading(String msg);

    void showLoading(int resId);

    void showToast(int resId);

    Object customFunctionCall(Object obj);
}
