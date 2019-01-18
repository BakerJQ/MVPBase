package com.bakerj.base.fragment;

import com.bakerj.base.BasePresenter;

/**
 * @author BakerJ
 * @date 2017/12/28
 */

public class BasePresenterDialogFragment<T extends BasePresenter> extends BaseDialogFragment {
    protected T mPresenter;

    public void setPresenter(T presenter) {
        this.mPresenter = presenter;
    }
}
