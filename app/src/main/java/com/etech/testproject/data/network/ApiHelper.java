package com.etech.testproject.data.network;


import com.etech.testproject.data.DataManager;
import com.etech.testproject.data.model.Lecture;
import com.etech.testproject.data.network.model.Pagging;


public interface ApiHelper {


    void getNotificationList(Pagging<Lecture> pagging, DataManager.Callback<Pagging> onApiCallback);

}
