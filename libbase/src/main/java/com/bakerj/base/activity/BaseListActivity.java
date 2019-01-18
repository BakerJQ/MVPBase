package com.bakerj.base.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bakerj.base.R;
import com.bakerj.base.loadmore.delegate.LoadMoreDelegate;
import com.bakerj.base.loadmore.mvp.LoadMoreContract;
import com.bakerj.base.loadmore.mvp.LoadMorePresenter;
import com.bakerj.base.widgets.refresh.CustomRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * @author BakerJ
 * @date 2018/5/9
 * 列表形式Activity基类
 */
public abstract class BaseListActivity<Presenter extends LoadMorePresenter, Adapter
        extends BaseQuickAdapter<Item, ?>, Item> extends BaseActivity implements LoadMoreContract
        .View<Item> {
    protected CustomRefreshLayout mRefreshLayout;
    protected RecyclerView mRecycleView;
    protected Adapter mAdapter;
    protected Presenter mPresenter;
    protected LoadMoreDelegate<Presenter, Adapter, Item> mLoadMoreDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRecycleView = findViewById(R.id.recycle_view);
        mPresenter = getPresenter();
        beforeInitView();
        mAdapter = getAdapter();
        mLoadMoreDelegate = new LoadMoreDelegate<>(mRefreshLayout, mRecycleView, mAdapter,
                mPresenter);
        mLoadMoreDelegate.initView(this);
        afterInitView();
        mLoadMoreDelegate.refreshOnInit();
    }

    protected int getLayoutId() {
        return R.layout.layout_refresh_list;
    }

    protected abstract Presenter getPresenter();

    protected void beforeInitView() {

    }

    protected void afterInitView() {

    }

    protected abstract Adapter getAdapter();

    @Override
    public void refresh() {
        mLoadMoreDelegate.refresh();
    }

    @Override
    public void refreshSucceed(List<Item> resultList, boolean hasMore) {
        mLoadMoreDelegate.refreshSucceed(resultList, hasMore);
    }

    @Override
    public void refreshFailed(Throwable t) {
        mLoadMoreDelegate.refreshFailed(t);
    }

    @Override
    public void loadMoreSucceed(List<Item> resultList, boolean hasMore) {
        mLoadMoreDelegate.loadMoreSucceed(resultList, hasMore);
    }

    @Override
    public void finishRefresh() {
        mLoadMoreDelegate.finishRefresh();
    }

    @Override
    public void finishLoadMore() {
        mLoadMoreDelegate.finishLoadMore();
    }

    @Override
    public void loadMoreFailed(Throwable t) {
        mLoadMoreDelegate.loadMoreFailed(t);
    }

    @Override
    public boolean isRefreshingOrLoading() {
        return mLoadMoreDelegate.isRefreshingOrLoading();
    }
}
