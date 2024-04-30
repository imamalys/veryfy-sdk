package com.ionnex.veryfy.base;

public abstract class BaseRepository {
    public void onLoading(Boolean isLoading) {
        if (isLoading) {
            onLoadingShow();
        } else {
            onLoadingDismiss();
        }
    }

    protected abstract void onLoadingShow();

    protected abstract void onLoadingDismiss();
}
