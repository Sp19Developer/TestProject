package com.etech.testproject.data.network.apiHelper;

import android.text.TextUtils;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import com.etech.testproject.utils.ConnectionUtils;
import com.etech.testproject.utils.Constants;


/**
 * Created by etech10 on 10/8/17.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class RestHelper implements RestClient.RestClientListener {
    //todo set this in constructor of AppApiHelper
    public static String DEFAULT_BASE_URL = null;

    private static final String INTERNAL_SERVER_ERR = "Internal Server Error. Please try again later.";
    private static final String NO_INTERNET_ERR = "No internet connection";
    public static final String RES_MSG_KEY = "description";
    public static final String RES_CODE_KEY = "result";
    public static final String RES_OBJ_KEY = "data";

    public static final int SUCCESS_CODE = 1;
    public static final String SUCCESS_CODE_STRING = "success";
    public static final int FAIL_CODE = 0;
    public static final int CANCEL_CODE = 2;
    public static final int FAIL_INTERNET_CODE = 3;



    private RestHelperCallback callback;
    private RestClient restClient;

    public interface RestHelperCallback {
        void onRequestCallback(int code, String message, RestResponse restResponse);
    }

    private RestHelper(RestRequest restRequest, RestHelperCallback callback) {
        this.callback = callback;
        restClient = new RestClient(restRequest, this);
    }


    public static class Builder {
        String baseUrl = DEFAULT_BASE_URL;
        RestConst.RequestMethod requestMethod = RestConst.RequestMethod.METHOD_GET;
        RestConst.ContentType contentType = RestConst.ContentType.CONTENT_FORMDATA;
        String url = null;
        HashMap<String, String> params = null;
        HashMap<String, String> headers = null;
        HashMap<String, List<String>> attachments = null;
        JSONObject jsonParams = null;
        RestHelperCallback callback = new RestHelperCallback() {
            @Override
            public void onRequestCallback(int code, String message, RestResponse restResponse) {

            }
        };

        public Builder() {

        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setRequestMethod(RestConst.RequestMethod requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public Builder setContentType(RestConst.ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setHeaders(HashMap<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setParams(HashMap<String, String> params) {
            this.params = params;
            return this;
        }

        public Builder setAttachments(HashMap<String, List<String>> attachments) {
            this.attachments = attachments;
            return this;
        }

        public Builder setJsonObject(JSONObject jsonObject) {
            this.jsonParams = jsonObject;
            return this;
        }

        public Builder setCallBack(RestHelperCallback callBack) {
            this.callback = callBack;
            return this;
        }

        public RestHelper build() {
            if (TextUtils.isEmpty(baseUrl) || TextUtils.isEmpty(url)) {
                throw new IllegalArgumentException("baseurl and url should not be empty");
            }
            return new RestHelper(new RestRequest(requestMethod, contentType, baseUrl, url, headers, params, attachments, jsonParams), callback);
        }

    }

    public void sendRequest() {
        restClient.execute();
    }

    public void cancelRequest() {
        restClient.cancelRequest();
    }

    @Override
    public void onRequestComplete(RestConst.ResponseCode resCode, RestResponse restRes) {
        if (resCode.equals(RestConst.ResponseCode.SUCCESS)) {
            JSONObject jsonObject = null;
            String message = "";
            String responseCode = "fail";
            try {
                jsonObject = new JSONObject(restRes.getResString());
                if (jsonObject.has(RestHelper.RES_CODE_KEY)) {
                    responseCode = jsonObject.getString(RestHelper.RES_CODE_KEY);
                }
                if (jsonObject.has(RestHelper.RES_MSG_KEY)) {
                    message = jsonObject.getString(RestHelper.RES_MSG_KEY);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (jsonObject != null && jsonObject.has(RestHelper.RES_CODE_KEY)) {
                if (responseCode.equals( RestHelper.SUCCESS_CODE_STRING)) {
                    callback.onRequestCallback(RestHelper.SUCCESS_CODE, message, restRes);
                } else {
                    callback.onRequestCallback(RestHelper.FAIL_CODE, message, restRes);
                }
            } else {
                callback.onRequestCallback(RestHelper.FAIL_CODE, INTERNAL_SERVER_ERR, restRes);
            }
        } else {
            if (resCode.equals(RestConst.ResponseCode.CANCEL)) {
                callback.onRequestCallback(RestHelper.CANCEL_CODE, INTERNAL_SERVER_ERR, restRes);
            } else if (resCode.equals(RestConst.ResponseCode.ERROR) && !ConnectionUtils.isConnectingToInternet()) {
                callback.onRequestCallback(RestHelper.FAIL_INTERNET_CODE, NO_INTERNET_ERR, restRes);
            } else {
                callback.onRequestCallback(RestHelper.FAIL_CODE, INTERNAL_SERVER_ERR, restRes);
            }
        }

    }

    public static JSONObject checkResponse(int code, String message, RestResponse restResponse) {
        if (code == RestHelper.SUCCESS_CODE) {
            String flag = null;
            if (code == RestHelper.SUCCESS_CODE) {
                try {
                    JSONObject res = null;
                    try {
                        res = new JSONObject(restResponse.getResString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("response ", res.toString());

                    flag = res.getString(RES_CODE_KEY);
                    String msg = res.getString(RES_MSG_KEY);
                    if (flag.equalsIgnoreCase("1")) {
                    } else {
                        res = null;
                    }
                    return res;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (code == RestHelper.FAIL_CODE) {

        } else {

        }
        return null;

    }

}
