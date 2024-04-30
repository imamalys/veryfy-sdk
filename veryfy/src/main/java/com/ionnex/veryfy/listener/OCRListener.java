package com.ionnex.veryfy.listener;

import com.ionnex.veryfy.base.BaseListener;
import com.ionnex.veryfy.model.DocumentModel;

public interface OCRListener extends BaseListener {
    void onOCRResult(DocumentModel documentModel);
}
