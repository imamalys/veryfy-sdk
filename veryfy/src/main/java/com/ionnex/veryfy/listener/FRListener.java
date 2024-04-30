package com.ionnex.veryfy.listener;

import com.ionnex.veryfy.base.BaseListener;
import com.ionnex.veryfy.model.FRModel;

public interface FRListener extends BaseListener {
    void onFRResult(FRModel frModel);
}
