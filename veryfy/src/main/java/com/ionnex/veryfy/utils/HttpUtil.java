package com.ionnex.veryfy.utils;

import static com.ionnex.veryfy.constants.APIConstant.STATUS_ERROR;
import static com.ionnex.veryfy.constants.Constant.MY_SOCKET_TIMEOUT_MS;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ionnex.veryfy.BuildConfig;
import com.ionnex.veryfy.base.BaseRepository;
import com.ionnex.veryfy.constants.APIConstant;
import com.ionnex.veryfy.listener.ResponseListener;
import com.ionnex.veryfy.request.VolleyMultipartRequest;
import com.ionnex.veryfy.request.VolleyMultipartRequest.DataPart;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
    private boolean isLoading;
    private Map<String, String> mHeader;
    private Map<String, String> mBody;
    private Map<String, DataPart> mData;
    private ResponseListener responseListener;
    private final BaseRepository baseRepository;
    private final Context context;

    public HttpUtil(Context context, BaseRepository baseRepository) {
        this.context = context;
        this.baseRepository = baseRepository;
    }
    public void postMethod(String endpoint,
                          Map<String, String> mHeader,
                          boolean isLoading,
                          ResponseListener responseListener) {

        this.isLoading = isLoading;
        this.responseListener = responseListener;
        this.mHeader = mHeader;
        if (this.mHeader == null) {
            this.mHeader = new HashMap<>();
        }
        this.mHeader.put("x-api-key", APIConstant.API_KEY);
        String url = BuildConfig.API_URL + endpoint;
        postData(url);
    }

    public void multipartMethod(String endpoint,
                                Map<String, String> mHeader,
                                Map<String, String> mBody,
                                Map<String, DataPart> mData,
                                boolean isLoading,
                                ResponseListener responseListener) {
        this.isLoading = isLoading;
        this.responseListener = responseListener;
        this.mHeader = mHeader;
        this.mBody = mBody;
        this.mData = mData;
        if (this.mHeader == null) {
            this.mHeader = new HashMap<>();
        }
        this.mHeader.put("x-api-key", APIConstant.API_KEY);
        this.mHeader.put("Accept", "application/json");
        this.mHeader.put("Connection", "close");
        String url = BuildConfig.API_URL + endpoint;
        multipartData(url);
    }

    private void postData(String url) {
        if (isLoading) {
            baseRepository.onLoading(true);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                getObjectListener(),
                getErrorListener()) {

            @Override
            public Map<String, String> getHeaders() {
                return mHeader;
            }
        };

        logRequest(Request.Method.POST, url);
        HttpSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void multipartData(String url) {
        if (isLoading) {
            baseRepository.onLoading(true);
        }
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(
                Request.Method.POST,
                url,
                getNetworkListener(),
                getErrorListener()) {
            @Override
            public Map<String, String> getHeaders() {
                return mHeader;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() {
                return mBody;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                return mData;
            }
        };

        logRequest(Request.Method.POST, url);
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        HttpSingleton.getInstance(context).addToRequestQueue(multipartRequest);
    }

    private Response.Listener<JSONObject> getObjectListener() {
        return response -> {
            try {
                logForResponse(response.toString());
                String status = response.getString("status");
                baseRepository.onLoading(false);
                if (status.equals(STATUS_ERROR)) {
                    responseListener.onFailedResponse(response);
                } else {
                    responseListener.onSuccessResponse(response.toString());
                }
            } catch (JSONException e) {
                baseRepository.onLoading(false);
                e.printStackTrace();
            }
        };
    }

    private Response.Listener<NetworkResponse> getNetworkListener() {
        return response -> {
            String resultResponse = new String(response.data);
            try {
                JSONObject result = new JSONObject(resultResponse);
                logForResponse(result.toString());
                String status = result.getString("status");
                baseRepository.onLoading(false);
                if (status.equals(STATUS_ERROR)) {
                    responseListener.onFailedResponse(result);
                } else {
                    responseListener.onSuccessResponse(result.toString());
                }
            } catch (JSONException e) {
                baseRepository.onLoading(false);
                try {
                    responseListener.onFailedResponse(new JSONObject());
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
                e.printStackTrace();
            }
        };
    }

    private Response.ErrorListener getErrorListener(){
        return error -> {
            Log.d("ERROR","error => " + error.toString());
            try {
                baseRepository.onLoading(false);
                String s = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                JSONObject obj = new JSONObject(s);
                responseListener.onFailedResponse(obj);
            } catch (Exception e) {
                baseRepository.onLoading(false);
                e.printStackTrace();
            }
        };
    }

    private String getMethodType(int type) {
        switch (type) {
            case Request.Method.POST:
                return "POST";
            case Request.Method.PUT:
                return "PUT";
            case Request.Method.PATCH:
                return "PATCH";
            default:
                return "GET";
        }
    }

    private void logForResponse(String response) {
        try {
            Logger.d("<<<<< response start=======================");
            Logger.d(response);
            Logger.d("<<<<< response end=========================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logRequest(int method, String url) {
        try {
            Logger.d(">>>>> request start_________________");
            Logger.d(getMethodType(method) + ' ' + url);
            if (mHeader != null && mHeader.size() > 0) {
                Logger.d("headers : " + mHeader);
            }
            if (mBody != null && mBody.size() > 0) {
                Logger.d("body : " + mBody);
            }
            if (mData != null && mData.size() > 0) {
                Logger.d("data : " + mData);
            }
            Logger.d(">>>>> end request_________________");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
