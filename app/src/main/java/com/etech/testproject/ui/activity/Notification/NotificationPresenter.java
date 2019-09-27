package com.etech.testproject.ui.activity.Notification;

import com.etech.testproject.app.TestApp;
import com.etech.testproject.data.DataManager;
import com.etech.testproject.data.model.Lecture;
import com.etech.testproject.data.network.model.Pagging;
import com.etech.testproject.ui.base.BasePresenter;

public class NotificationPresenter<V extends NotificationContract.View> extends BasePresenter<V> implements NotificationContract.Presenter<V> {

    private Pagging<Lecture> pagging;

    @Override
    public void onAttach(V baseView) {
        super.onAttach(baseView);
        pagging = new Pagging<>();
    }

    @Override
    public void init() {
        getView().setUpView(false);
        loadMoreRecords();
    }

    @Override
    public void reset() {
        getView().setUpView(true);
        clearPagging();
        loadMoreRecords();
    }

    private void clearPagging() {
        pagging = new Pagging<>();
    }

    @Override
    public void loadMoreRecords() {
        if (pagging.isHasMore()) {
            pagging.setHasMore(false);
            startLoading();
            TestApp.getDataManager().getNotificationList(pagging, new DataManager.Callback<Pagging>() {
                @Override
                public void onSuccess(Pagging baseResponse) {
                    if (getView() == null) return;
                    NotificationPresenter.super.stopLoading();
                    pagging = baseResponse;

                    if (pagging.getPageNumber() == 1) {
                        if (pagging.getArrList().size() > 0) {
                            getView().loadDataToView(pagging.getArrList());
                        } else {
                            getView().setNotRecordsFoundView(true);
                        }
                    }

                    if (pagging.getArrList().size() > 0) {
                        getView().loadDataToView(pagging.getArrList());
                    }

                }

                @Override
                public void onFailed(int code, String message) {
                    if (getView() == null) return;
                    if (pagging.getPageNumber() == 1) {
                        NotificationPresenter.super.onFaild(code, message, false, true);
                    } else {
                        NotificationPresenter.super.onFaild(code, message, true, false);
                    }
                }

            });
        }
    }
}
