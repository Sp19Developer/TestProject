package com.etech.testproject.ui.activity.Notification;



import com.etech.testproject.data.model.Lecture;
import com.etech.testproject.ui.base.BaseContractPresenter;
import com.etech.testproject.ui.base.BaseContractView;

import java.util.ArrayList;

/**
 * Created by etech3 on 27/6/18.
 */

public interface NotificationContract {
    interface View extends BaseContractView {
        void setUpView(boolean isReset);

        void loadDataToView(ArrayList<Lecture> list);

        void setNotRecordsFoundView(boolean isActive);

    }

    interface Presenter<V extends NotificationContract.View> extends BaseContractPresenter<V> {

        void init();

        void reset();

        void loadMoreRecords();

    }
}
