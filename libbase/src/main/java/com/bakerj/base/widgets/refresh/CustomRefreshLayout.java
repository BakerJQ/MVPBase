package com.bakerj.base.widgets.refresh;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;

/**
 * Created by BakerJ on 2017/12/20.
 */

public class CustomRefreshLayout extends SmartRefreshLayout {
    public CustomRefreshLayout(Context context) {
        this(context, null);
    }

    public CustomRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setEnableLoadMoreWhenContentNotFull(false);
    }

    public boolean isRefreshing() {
        return mState == RefreshState.Refreshing;
    }

    public boolean isLoading() {
        return mState == RefreshState.Loading;
    }

    public static class AbstractOnMultiPurposeListener implements OnMultiPurposeListener {


        @Override
        public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent,
                                   int offset, int headerHeight, int maxDragHeight) {

        }

        @Override
        public void onHeaderReleased(RefreshHeader header, int headerHeight, int extendHeight) {

        }


        @Override
        public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int
                extendHeight) {

        }

        @Override
        public void onHeaderFinish(RefreshHeader header, boolean success) {

        }

        @Override
        public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent,
                                   int offset, int footerHeight, int maxDragHeight) {

        }

        @Override
        public void onFooterReleased(RefreshFooter footer, int footerHeight, int extendHeight) {

        }

        @Override
        public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int
                extendHeight) {

        }

        @Override
        public void onFooterFinish(RefreshFooter footer, boolean success) {

        }

        @Override
        public void onRefresh(RefreshLayout refreshlayout) {

        }

        @Override
        public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState,
                                   RefreshState newState) {

        }

        @Override
        public void onLoadMore(RefreshLayout refreshLayout) {

        }
    }
}
