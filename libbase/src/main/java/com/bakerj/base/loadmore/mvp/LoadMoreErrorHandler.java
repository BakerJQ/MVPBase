package com.bakerj.base.loadmore.mvp;

public interface LoadMoreErrorHandler {
    boolean handle(Throwable t);

    interface LoadMoreErrorHandlerCreator{
        LoadMoreErrorHandler create();
    }
}
