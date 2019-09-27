package com.etech.testproject.data.network;

import com.etech.testproject.app.TestApp;
import com.etech.testproject.data.DataManager;
import com.etech.testproject.data.model.Lecture;
import com.etech.testproject.data.network.apiHelper.RestHelper;
import com.etech.testproject.data.network.apiHelper.RestResponse;
import com.etech.testproject.data.network.model.Pagging;
import com.etech.testproject.data.prefs.AppPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AppApiHelper implements ApiHelper {


    public static final String BASE_URL = "https://api.demo.com";/*live*/
    private static final String BASE_URL_ADDITITON = "/api";/*live*/

    private static final String API_NOTIFICATION_LIST = BASE_URL_ADDITITON + "/push-list";

    private Gson gson = new Gson();


    public AppApiHelper() {
        RestHelper.DEFAULT_BASE_URL = BASE_URL;
    }


    @Override
    public void getNotificationList(Pagging<Lecture> pagging, DataManager.Callback<Pagging> onApiCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", TestApp.getDataManager().get(AppPreferencesHelper.PREF_USER_ID, null));
        new RestHelper.Builder()
                .setUrl(API_NOTIFICATION_LIST)
                .setParams(params)
                .setCallBack(new RestHelper.RestHelperCallback() {
                    @Override
                    public void onRequestCallback(int code, String message, RestResponse restResponse) {

                        final JSONObject jsonObject = RestHelper.checkResponse(code, message, restResponse);
                        if (jsonObject != null) {
                            try {

                                Pagging<Lecture> newPagging = new Pagging<>();
                                newPagging.setHasMore(false);
                                JSONArray jsonObj = jsonObject.getJSONArray("notification");
                                ArrayList<Lecture> categories = gson.fromJson(jsonObj.toString(), new TypeToken<ArrayList<Lecture>>() {
                                }.getType());
                                newPagging.addItemToList(categories);
                                onApiCallback.onSuccess(newPagging);

                            } catch (Exception e) {
                                onApiCallback.onFailed(code, message);
                                e.printStackTrace();
                            }
                        } else {
                            onApiCallback.onFailed(code, message);
                        }

                    }
                })
                .build()
                .sendRequest();
    }
}

