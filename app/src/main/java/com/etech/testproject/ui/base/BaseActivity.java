package com.etech.testproject.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.etech.testproject.app.TestApp;
import com.etech.testproject.data.network.apiHelper.RestHelper;
import com.etech.testproject.data.prefs.AppPreferencesHelper;
import com.etech.testproject.databinding.LayoutExtraViewsBinding;
import com.etech.testproject.utils.ConnectionUtils;
import com.etech.testproject.utils.ViewUtils;

import static com.etech.testproject.utils.ViewUtils.setVisibility;


public class BaseActivity extends AppCompatActivity implements BaseContractView {

    protected final String TAG = BaseActivity.class.getName();
    public static String USER_ID;

    public BaseContractPresenter<BaseContractView> baseContractPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getUserId();
        baseContractPresenter = new BasePresenter<>();

    }

    public static String getUserId() {
        if (TextUtils.isEmpty(USER_ID))
            USER_ID = TestApp.getDataManager().get(AppPreferencesHelper.PREF_USER_ID, "");
        return USER_ID;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    Toolbar toolbar;
    LayoutExtraViewsBinding extraViewBinding;


    public void setUpView(Toolbar toolbar, LayoutExtraViewsBinding extraViewBinding) {
        setUpView(toolbar, extraViewBinding, null, false);
    }

    public void setUpView(Toolbar toolbar, LayoutExtraViewsBinding extraViewBinding, String title, boolean isBackEnable) {
        if (toolbar != null)
            this.toolbar = toolbar;
        this.extraViewBinding = extraViewBinding;
        if (toolbar != null)
            setSupportActionBar(toolbar);
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
        if (isBackEnable) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        }

        if (extraViewBinding == null) return;
        this.extraViewBinding.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetryClicked();
            }
        });
    }

    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    public void setTitle(String title) {
        if (getSupportActionBar() == null) return;
        getSupportActionBar().setTitle(title);
    }

    public void showProgressView() {
        if (extraViewBinding == null) return;
        setVisibility(extraViewBinding.llProgressContainer, View.VISIBLE);
    }

    public void hideProgressView() {
        if (extraViewBinding == null) return;
        setVisibility(extraViewBinding.llProgressContainer, View.GONE);
    }


    // Fragment's Method and variables.
    public BaseFragment baseFragment;
    private int count = 0;

    public void addFragment(BaseFragment fragment, boolean isClearStack) {
        this.addFragment(fragment, isClearStack, true);
    }

    public void addFragment(BaseFragment fragment, boolean isClearStack, boolean isAddToStack) {
        Log.d(TAG, fragment.getClass().getName());
        String tag = fragment.getClass().getName() + count;
        baseFragment = fragment;
        if (isClearStack) {
            clearFragmentStack();
        }
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
//        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

        if (isAddToStack)
            transaction.addToBackStack(tag);
//        transaction.replace(R.id.frm_container, fragment, tag);
        transaction.commit();
        count = count + 1;
    }

    public void clearFragmentStack() {
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            String name = getSupportFragmentManager().getBackStackEntryAt(0).getName();
            getSupportFragmentManager().popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            break;
        }
    }


    //Connection Broadcast
    private static boolean isConnected = false;
    ConnectionServices connectionServices;


    public class ConnectionServices extends BroadcastReceiver {
        Boolean isCalled = false;

        @Override
        public void onReceive(Context context, Intent intent) {

            if (ConnectionUtils.isConnectingToInternet()) {
                isConnected = true;
            } else {
                isConnected = false;
            }
            if (isCalled) {
                isCalled = false;
                onNetWorkChanged(isConnected);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isCalled = true;
                    }
                }, 1000);
            } else {
                isCalled = true;
            }
        }
    }

    public void onNetWorkChanged(boolean isNetworkConnected) {

        if (baseFragment != null)
            baseFragment.onNetworkChanged(isNetworkConnected);
        if (!isNetworkConnected) {
//            showConnectionLostView();
        } else {
            onRetryClicked();
            hideConnectionLostView();
        }
    }

    public void onRetryClicked() {
        if (ConnectionUtils.isConnectingToInternet()) {
            hideConnectionLostView();
            if (baseFragment != null)
                baseFragment.onRetryClicked();
        }
    }


    @Override
    protected void onDestroy() {
        baseContractPresenter.onDetach();
        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        ViewUtils.hideKeyboard(this);
        onBackPressed();
        return true;
    }

    public void onReceiverNotification(Context context, String type, Intent intent) {
        if (baseFragment != null)
            baseFragment.onReceiverNotification(context, type, intent);
    }

    @Override
    public void showProgressBar() {
        showProgressView();
    }

    @Override
    public void hideProgressBar() {
        hideProgressView();
    }

    @Override
    public void showConnectionLostView() {
        if (extraViewBinding == null) return;
        setVisibility(extraViewBinding.llConnectionLostContainer, View.VISIBLE);
    }

    @Override
    public void hideConnectionLostView() {
        if (extraViewBinding == null) return;
        setVisibility(extraViewBinding.llConnectionLostContainer, View.GONE);
    }

    @Override
    public void showError(int code, String resId) {

        if (code == RestHelper.FAIL_INTERNET_CODE) {
            resId = "msg_no_network_connection";
        }

//        CustomAlertDialog.showAlert(this, getString(R.string.app_name), resId, getString(R.string.btn_ok), new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

}
