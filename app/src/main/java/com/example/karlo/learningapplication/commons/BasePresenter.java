package com.example.karlo.learningapplication.commons;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V extends BaseView> {

    public interface ViewAction<V> {
        void run(@NonNull V view);
    }

    private WeakReference<V> viewRef;

    //public BasePresenter(V view) {
    //    attachView(view);
    //}

    public void attachView(V view) {
        viewRef = new WeakReference<>(view);
    }

    public V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    private void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }

    protected void destroy() {
        detachView();
    }

    protected boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    protected final void ifViewAttached(ViewAction<V> action) {
        ifViewAttached(false, action);
    }

    protected final void ifViewAttached(boolean exceptionIfViewNotAttached, ViewAction<V> action) {
        final V view = viewRef == null ? null : viewRef.get();
        if (view != null) {
            action.run(view);
        } else if (exceptionIfViewNotAttached) {
            throw new IllegalStateException("No View attached to Presenter");
        }
    }
}
