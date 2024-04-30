package com.ionnex.veryfy.repository;

import static com.ionnex.veryfy.constants.APIConstant.DATA;

import android.content.Context;

import com.google.gson.Gson;
import com.ionnex.veryfy.base.BaseRepository;
import com.ionnex.veryfy.constants.APIConstant;
import com.ionnex.veryfy.listener.ResponseListener;
import com.ionnex.veryfy.model.DocumentModel;
import com.ionnex.veryfy.presenter.OCRPresenter;
import com.ionnex.veryfy.request.VolleyMultipartRequest.DataPart;
import com.ionnex.veryfy.utils.FileUtils;
import com.ionnex.veryfy.utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class OCRRepository <P extends OCRPresenter> extends BaseRepository {
    private final Gson gson = new Gson();
    private final HttpUtil httpUtil;
    private final OCRPresenter presenter;
    public int retryCount = 0;
    public OCRRepository(Context context, P ocrPresenter) {
        httpUtil = new HttpUtil(context,this);
        this.presenter = ocrPresenter;
    }

    public void getOCRResult(String referenceId,
                             File frontFile,
                             File frontFileFlash,
                             File backFile) {
        Map<String, String> body = new HashMap<>();
        body.put("referenceId", referenceId);
        body.put("imageType", frontFileFlash == null ? "0" : "1");
        Map<String, DataPart> data = new HashMap<>();
        if (frontFile != null && frontFileFlash == null) {
            data.put("frontImage", new DataPart("front_file.png", FileUtils.fileToByte(frontFile), "image/png"));
        }
        if (frontFileFlash != null) {
            data.put("frontImageFlash", new DataPart("front_file_flash.png", FileUtils.fileToByte(frontFileFlash), "image/png"));
        }
        if (backFile != null) {
            data.put("backImage", new DataPart("back_file.png", FileUtils.fileToByte(backFile), "image/png"));
        }
        httpUtil.multipartMethod(
                APIConstant.OCR_REQUEST,
                null,
                body,
                data,
                true,
                new ResponseListener() {
                    @Override
                    public void onSuccessResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has(DATA)) {
                                presenter.onOCRResult(gson.fromJson(
                                        jsonObject.optJSONObject("data").toString(),
                                        DocumentModel.class));
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailedResponse(JSONObject response) throws JSONException {
                        if (response.has("message")) {
                            presenter.showMessageError(
                                    response.getString("message"));
                        } else {
                            presenter.showMessageError("Please try again");
                        }
                    }
                }
        );
    }


    @Override
    protected void onLoadingShow() {
        presenter.onLoading(true);
    }

    @Override
    protected void onLoadingDismiss() {
        presenter.onLoading(false);
    }
}
