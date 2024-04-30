package com.ionnex.veryfy.listener;

import com.ionnex.veryfy.base.BaseListener;
import com.ionnex.veryfy.model.JourneyIdModel;

public interface JourneyIdListener extends BaseListener {
    void onJourneyIdResult(JourneyIdModel journeyIdModel);
}
