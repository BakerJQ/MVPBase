package com.bakerj.base.loadmore.mvp;

import com.bakerj.rxretrohttp.subscriber.ApiObserver;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @param <View>      View层范型
 * @param <Quest>     请求body范型
 * @param <SrcModel>  元数据范型（请求得到的原始数据）
 * @param <DestModel> 转换后的数据范型（Adapter需要使用的目标数据）
 * @author BakerJ
 * @date 2018/1/2
 * 刷新加载Presenter基类
 */
public abstract class LoadMorePresenter<View extends LoadMoreContract.View<DestModel>, Quest,
        SrcModel, DestModel>
        extends LoadMoreContract.Presenter<View, Quest> {
    private static LoadMoreErrorHandler.LoadMoreErrorHandlerCreator errorHandlerCreator;
    private LoadMoreErrorHandler errorHandler;

    public LoadMorePresenter(View view) {
        super(view);
        mBody = getQuestBody();
        if (errorHandlerCreator != null) {
            errorHandler = errorHandlerCreator.create();
        }
    }

    /**
     * 初始化请求body
     */
    protected abstract Quest getQuestBody();

    /**
     * 元数据与目标数据转换规则
     */
    protected abstract DestModel castDataToDest(SrcModel srcModel);

    @Override
    public void refresh() {
        setUpRefreshBody(mBody);
        getRequestObservable()
                .subscribe(new ApiObserver<List<DestModel>>() {
                    @Override
                    protected void success(List<DestModel> data) {
                        refreshSuccess(data);
                    }

                    @Override
                    protected void error(Throwable t) {
                        super.error(t);
                        refreshFailed(t);
                    }

                    @Override
                    protected void complete() {
                        super.complete();
                        mView.finishRefresh();
                    }
                });
    }

    public Observable<List<DestModel>> getRefreshObservable() {
        setUpRefreshBody(mBody);
        return getRequestObservable();
    }

    /**
     * 个性化请求body设置
     */
    protected abstract void setUpRefreshBody(Quest body);

    protected void refreshSuccess(List<DestModel> data) {
        mView.refreshSucceed(data, hasMore(data));
    }

    /**
     * 是否需要加载更多判断规则
     */
    protected abstract boolean hasMore(List<DestModel> data);

    protected void refreshFailed(Throwable t) {
        if (errorHandler != null && errorHandler.handle(t)) {
            return;
        }
        mView.refreshFailed(t);
    }

    /**
     * 具体请求
     */
    protected abstract Observable<List<SrcModel>> getRefreshLoadObservable(Quest body);

    protected Observable<List<DestModel>> extraCast(List<DestModel> list) {
        return Observable.just(list);
    }

    @Override
    public void loadMore() {
        setUpLoadMoreBody(mBody);
        getRequestObservable()
                .subscribe(new ApiObserver<List<DestModel>>() {
                    @Override
                    protected void success(List<DestModel> data) {
                        loadMoreSuccess(data);
                    }

                    @Override
                    protected void error(Throwable t) {
                        super.error(t);
                        loadMoreFailed(t);
                    }

                    @Override
                    protected void complete() {
                        super.complete();
                        mView.finishLoadMore();
                    }
                });
    }

    /**
     * 加载更多个性化body
     */
    protected abstract void setUpLoadMoreBody(Quest body);

    protected void loadMoreSuccess(List<DestModel> data) {
        mView.loadMoreSucceed(data, hasMore(data));
    }

    protected void loadMoreFailed(Throwable t) {
        if (errorHandler != null && errorHandler.handle(t)) {
            return;
        }
        mView.loadMoreFailed(t);
    }

    private Observable<List<DestModel>> getRequestObservable() {
        return getRefreshLoadObservable(mBody)
                .doOnNext(this::doOnSourceGet)
                .flatMap(Observable::fromIterable)
                .map(this::castDataToDest)
                .toList().toObservable()
                .flatMap(this::extraCast)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected void doOnSourceGet(List<SrcModel> data) {
    }

    public static void setErrorHandlerCreator(LoadMoreErrorHandler.LoadMoreErrorHandlerCreator errorHandlerCreator) {
        LoadMorePresenter.errorHandlerCreator = errorHandlerCreator;
    }
}
