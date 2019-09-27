
package com.etech.testproject.data;

import com.etech.testproject.data.db.DbHelper;
import com.etech.testproject.data.network.ApiHelper;
import com.etech.testproject.data.prefs.PreferencesHelper;

public interface DataManager extends DbHelper, PreferencesHelper, ApiHelper {

    interface Callback<V> {
        void onSuccess(V baseResponse);

        void onFailed(int code, String message);
    }

}
