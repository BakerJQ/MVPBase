package com.bakerj.base.observer;

import io.reactivex.observers.DisposableObserver;

public abstract class SimpleObserver<T> extends DisposableObserver<T> {
    @Override
    public void onNext(T o) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
