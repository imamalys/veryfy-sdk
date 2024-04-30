package com.ionnex.veryfy.repository;

import static com.ionnex.veryfy.constants.APIConstant.DATA;

import android.content.Context;

import com.google.gson.Gson;
import com.ionnex.veryfy.base.BaseRepository;
import com.ionnex.veryfy.constants.APIConstant;
import com.ionnex.veryfy.listener.ResponseListener;
import com.ionnex.veryfy.model.DocumentModel;
import com.ionnex.veryfy.model.FRModel;
import com.ionnex.veryfy.presenter.FRPresenter;
import com.ionnex.veryfy.request.VolleyMultipartRequest;
import com.ionnex.veryfy.utils.FileUtils;
import com.ionnex.veryfy.utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FRRepository<P extends FRPresenter> extends BaseRepository {
    private final Gson gson = new Gson();
    private final HttpUtil httpUtil;
    private final FRPresenter presenter;
    public int retryCount = 0;

    public FRRepository(Context context, P ocrPresenter) {
        httpUtil = new HttpUtil(context,this);
        this.presenter = ocrPresenter;
    }

    public void getFRResult(String referenceId,
                             File selfieFile) {
        Map<String, String> body = new HashMap<>();
        body.put("referenceId", referenceId);
        Map<String, VolleyMultipartRequest.DataPart> data = new HashMap<>();
        data.put("selfieImage", new VolleyMultipartRequest.DataPart("selfie_file.png", FileUtils.fileToByte(selfieFile), "image/png"));
        httpUtil.multipartMethod(
                APIConstant.FR_REQUEST,
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
                                presenter.onFRResult(gson.fromJson(
                                        jsonObject.optJSONObject("data").toString(),
                                        FRModel.class));
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
