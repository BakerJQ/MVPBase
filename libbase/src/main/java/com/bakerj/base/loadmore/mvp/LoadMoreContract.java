package com.bakerj.base.loadmore.mvp;

import com.bakerj.base.BasePresenter;
import com.bakerj.base.BaseView;

import java.util.List;

/**
 * @author BakerJ
 * @date 2018/1/2
 */

public interface LoadMoreContract {
    interface View<Model> extends BaseView {
        void refresh();

        void refreshSucceed(List<Model> resultList, boolean hasMore);

        void refreshFailed(Throwable t);

        void loadMoreSucceed(List<Model> resultList, boolean hasMore);

        void loadMoreFailed(Throwable t);

        void finishRefresh();

        void finishLoadMore();

        boolean isRefreshingOrLoading();
    }

    abstract class Presenter<View extends LoadMoreContract.View, Quest> extends
            BasePresenter<View> {
        protected Quest mBody;

        public Presenter(View view) {
            super(view);
        }

        public abstract void refresh();

        public abstract void loadMore();
    }
}
