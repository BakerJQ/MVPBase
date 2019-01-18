package com.bakerj.base.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bakerj.base.R;
import com.bakerj.base.loadmore.delegate.LoadMoreDelegate;
import com.bakerj.base.loadmore.mvp.LoadMoreContract;
import com.bakerj.base.loadmore.mvp.LoadMorePresenter;
import com.bakerj.base.widgets.refresh.CustomRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * @author BakerJ
 * @date 2018/3/27
 */

public abstract class BasePresenterListFragment<Presenter extends LoadMorePresenter, Adapter
        extends BaseQuickAdapter<Item, ?>, Item> extends BasePresenterFragment<Presenter>
        implements LoadMoreContract.View<Item> {
    protected CustomRefreshLayout mRefreshLayout;
    protected RecyclerView mRecycleView;
    protected Adapter mAdapter;
    protected LoadMoreDelegate<Presenter, Adapter, Item> mDelegate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRecycleView = view.findViewById(R.id.recycle_view);
        beforeInitView();
        mAdapter = getAdapter();
        mDelegate = new LoadMoreDelegate<>(mRefreshLayout, mRecycleView, mAdapter, mPresenter);
        mDelegate.initView(getActivity());
        initView(view);
        afterInitView();
        mDelegate.refreshOnInit();
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    protected int getLayoutId() {
        return R.layout.layout_refresh_list;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        super.setPresenter(presenter);
        if (mDelegate != null) {
            mDelegate.setPresenter(presenter);
        }
    }

    protected void beforeInitView() {

    }

    protected void initView(View view) {

    }

    protected void afterInitView() {

    }

    protected abstract Adapter getAdapter();

    @Override
    public void refresh() {
        if (mDelegate != null) {
            mDelegate.refresh();
        }
    }

    @Override
    public void refreshSucceed(List<Item> resultList, boolean hasMore) {
        if (mDelegate != null) {
            mDelegate.refreshSucceed(resultList, hasMore);
        }
    }

    @Override
    public void refreshFailed(Throwable t) {
        if (mDelegate != null) {
            mDelegate.refreshFailed(t);
        }
    }

    @Override
    public void loadMoreSucceed(List<Item> resultList, boolean hasMore) {
        if (mDelegate != null) {
            mDelegate.loadMoreSucceed(resultList, hasMore);
        }
    }

    @Override
    public void finishRefresh() {
        if (mDelegate != null) {
            mDelegate.finishRefresh();
        }
    }

    @Override
    public void finishLoadMore() {
        if (mDelegate != null) {
            mDelegate.finishLoadMore();
        }
    }

    @Override
    public void loadMoreFailed(Throwable t) {
        if (mDelegate != null) {
            mDelegate.loadMoreFailed(t);
        }
    }

    @Override
    public boolean isRefreshingOrLoading() {
        return mDelegate.isRefreshingOrLoading();
    }

    /**
     * 将recycleview 回滚到第一条，并结束刷新
     */
    public void setSelectionAfterHeaderView(){
        if (mRecycleView!=null && mRefreshLayout!=null) {
            mRecycleView.scrollToPosition(0);
            finishRefresh();
        }
    }

    /**
     * 设置是否可以refresh 和 loadMore
     * @param isCan
     */
    public void setCanRefreshAndLoadMore(boolean isCan){
        mRefreshLayout.setEnableRefresh(isCan);
        mRefreshLayout.setEnableAutoLoadMore(isCan);
    }
}
