package com.ionnex.veryfy.listener;

import org.json.JSONException;
import org.json.JSONObject;

public interface ResponseListener{
    void onSuccessResponse(String response);
    void onFailedResponse(JSONObject response) throws JSONException;
}
