package com.ionnex.veryfy.presenter;

import android.content.Context;

import com.ionnex.veryfy.base.BasePresenter;
import com.ionnex.veryfy.listener.OCRListener;
import com.ionnex.veryfy.model.DocumentModel;
import com.ionnex.veryfy.repository.OCRRepository;

import java.io.File;

public class OCRPresenter extends BasePresenter<OCRListener> {
    private final OCRRepository<OCRPresenter> repository;
    public OCRPresenter(Context context, OCRListener listener) {
        super(listener);
        repository = new OCRRepository<>(context, this);
    }

    public void getOCRResult(String referenceId,
                             File frontFile,
                             File frontFileFlash,
                             File backFile) {
        repository.getOCRResult(referenceId, frontFile, frontFileFlash, backFile);
    }

    public void onOCRResult(DocumentModel documentModel) {
        mListener.onOCRResult(documentModel);
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
