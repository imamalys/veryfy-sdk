package com.ionnex.veryfy.presenter;

import android.content.Context;

import com.ionnex.veryfy.base.BasePresenter;
import com.ionnex.veryfy.listener.FRListener;
import com.ionnex.veryfy.model.FRModel;
import com.ionnex.veryfy.repository.FRRepository;

import java.io.File;

public class FRPresenter extends BasePresenter<FRListener> {
    private final FRRepository repository;
    public FRPresenter(Context context, FRListener listener) {
        super(listener);
        repository = new FRRepository<>(context, this);
    }

    public void getFRResult(String referenceId,
                            File selfieFile) {
        repository.getFRResult(referenceId, selfieFile);
    }

    public void onFRResult(FRModel frModel) {
        mListener.onFRResult(frModel);
    }

    public void showMessageError(String msg) {
        mListener.onShowMessage(msg);
    }

    public void increaseCount() {
        repository.retryCount += 1;
    }

    public int getRetryCount() {
        return repository.retryCount;
    }
}
