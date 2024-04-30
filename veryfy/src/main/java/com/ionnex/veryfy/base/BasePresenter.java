package com.ionnex.veryfy.base;

public abstract class BasePresenter<L extends BaseListener> {
    protected L mListener;

    public BasePresenter(L listener) {
        this.mListener = listener;
    }

    public void onLoading(Boolean isLoading) {
        if (isLoading) {
            mListener.onLoadingShow();
        } else {
            mListener.onLoadingDismiss();
        }
    }
}
