package com.ionnex.veryfy.presenter;

import android.content.Context;

import com.ionnex.veryfy.base.BasePresenter;
import com.ionnex.veryfy.listener.JourneyIdListener;
import com.ionnex.veryfy.model.JourneyIdModel;
import com.ionnex.veryfy.repository.JourneyIdRepository;

public class JourneyIdPresenter extends BasePresenter<JourneyIdListener> {
    private final JourneyIdRepository<JourneyIdPresenter> repository;

    public JourneyIdPresenter(Context context, JourneyIdListener listener) {
        super(listener);
        this.repository = new JourneyIdRepository<>(context, this);
    }

    public void getJourneyId() {
        repository.getJourneyId();
    }

    public void journeyIdResult(JourneyIdModel journeyIdModel) {
        mListener.onJourneyIdResult(journeyIdModel);
    }

    public void showMessageError(String msg) {
        mListener.onShowMessage(msg);
    }
}
