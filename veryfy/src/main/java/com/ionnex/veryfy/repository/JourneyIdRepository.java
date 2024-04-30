package com.ionnex.veryfy.repository;

import android.content.Context;

import static com.ionnex.veryfy.constants.APIConstant.*;

import com.google.gson.Gson;
import com.ionnex.veryfy.base.BaseRepository;
import com.ionnex.veryfy.listener.ResponseListener;
import com.ionnex.veryfy.model.JourneyIdModel;
import com.ionnex.veryfy.presenter.JourneyIdPresenter;
import com.ionnex.veryfy.utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JourneyIdRepository<P extends JourneyIdPresenter> extends BaseRepository {
    private final Gson gson = new Gson();
    private final HttpUtil httpUtil;
    private final JourneyIdPresenter presenter;

    public JourneyIdRepository(Context context, P journeyIdPresenter) {
        httpUtil = new HttpUtil(context,this);
        this.presenter = journeyIdPresenter;
    }

    public void getJourneyId() {
        httpUtil.postMethod(
                JOURNEY_ID,
                null,
                true,
                new ResponseListener() {
                    @Override
                    public void onSuccessResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has(DATA)) {
                                presenter.journeyIdResult(gson.fromJson(
                                        jsonObject.optJSONObject("data").toString(),
                                        JourneyIdModel.class));
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailedResponse(JSONObject response) throws JSONException {
                        presenter.showMessageError(
                                response.getString("message"));
                    }
                });
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
