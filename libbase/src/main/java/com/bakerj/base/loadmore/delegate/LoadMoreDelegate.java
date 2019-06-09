package com.bakerj.base.loadmore.delegate;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bakerj.base.loadmore.mvp.LoadMorePresenter;
import com.bakerj.base.widgets.refresh.CustomRefreshLayout;
import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * @author BakerJ
 * @date 2018/5/9
 * 下拉刷新上拉加载代理
 */
public class LoadMoreDelegate<Presenter extends LoadMorePresenter, Adapter
        extends BaseQuickAdapter<Item, ?>, Item> {
    private CustomRefreshLayout mRefreshLayout;//刷新布局
    private RecyclerView mRecycleView;
    private Adapter mAdapter;
    private Presenter mPresenter;
    private boolean shouldRefreshOnInit = false;
    private View mEmptyView;

    public LoadMoreDelegate(CustomRefreshLayout refreshLayout, RecyclerView recycleView,
                            Adapter adapter, Presenter presenter) {
        this.mRefreshLayout = refreshLayout;
        this.mRecycleView = recycleView;
        this.mAdapter = adapter;
        this.mPresenter = presenter;
    }

    public void initView(Context context) {
        mRefreshLayout.setOnRefreshListener(refreshLayout -> mPresenter.refresh());
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> mPresenter.loadMore());
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableAutoLoadMore(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(context));
//        mRecycleView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecycleView);
    }

    //设置通用空视图
    public void setCommonEmptyView(String text, View.OnClickListener onClickListener, int
            textColor) {
        TextView emptyView = new TextView(mRecycleView.getContext());
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setGravity(Gravity.CENTER);
        if (textColor != 0) {
            emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            emptyView.setTextColor(textColor);
        }
        emptyView.setText(text);
        emptyView.setClickable(true);
        emptyView.setOnClickListener(onClickListener);
        setEmptyView(emptyView);
    }

    //设置通用空视图
    public void setNoticeEmptyView(String text, View.OnClickListener onClickListener, int
            textColor) {
        TextView emptyView = new TextView(mRecycleView.getContext());
        int height = ScreenUtils.getScreenHeight() / 3;
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, height));
        emptyView.setGravity(Gravity.CENTER);

        if (textColor != 0) {
            emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            emptyView.setTextColor(textColor);
        }
        emptyView.setText(text);
        emptyView.setClickable(true);
        emptyView.setOnClickListener(onClickListener);
        setEmptyView(emptyView);
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public void refreshOnInit() {
        if (shouldRefreshOnInit) {
            shouldRefreshOnInit = false;
            refresh();
        }
    }

    public void refresh() {
        if (mRefreshLayout == null) {
            shouldRefreshOnInit = true;
        } else {
            mRecycleView.smoothScrollToPosition(0);
            mRefreshLayout.autoRefresh(0);
        }
    }

    public void refreshSucceed(List<Item> resultList, boolean hasMore) {
        if (mEmptyView != null && resultList.size() == 0) {
            mAdapter.setEmptyView(mEmptyView);
        }
        mAdapter.setNewData(resultList);
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.finishRefresh(0, true, !hasMore);
//        mRefreshLayout.setEnableLoadMore(hasMore);
//        mRefreshLayout.setNoMoreData(!hasMore);
    }

    public void refreshFailed(Throwable t) {
        mRefreshLayout.finishRefresh(0);
        if (mEmptyView != null) {
            mAdapter.setEmptyView(mEmptyView);
        }
    }

    public void loadMoreSucceed(List<Item> resultList, boolean hasMore) {
        mAdapter.addData(resultList);
        mAdapter.notifyItemInserted(mAdapter.getItemCount() - resultList.size());
//        mAdapter.notifyDataSetChanged();
        mRefreshLayout.finishLoadMore(0, true, !hasMore);
//        mRefreshLayout.setEnableLoadMore(hasMore);
        if (resultList.size() == 0 && mEmptyView != null) {
            mAdapter.setEmptyView(mEmptyView);
        }
    }

    public void finishRefresh() {
        mRefreshLayout.finishRefresh();
    }

    public void finishLoadMore() {
        mRefreshLayout.finishLoadMore(0);
    }

    public void loadMoreFailed(Throwable t) {
        mRefreshLayout.finishLoadMore(0, false, true);
    }

    public void setPresenter(Presenter presenter) {
        this.mPresenter = presenter;
    }

    public boolean isRefreshingOrLoading() {
        return mRefreshLayout.isRefreshing() || mRefreshLayout.isLoading();
    }
}
