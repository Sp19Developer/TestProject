package com.etech.testproject.ui.activity.Notification;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.etech.testproject.R;
import com.etech.testproject.data.model.Lecture;
import com.etech.testproject.databinding.ActivityNotificationBinding;
import com.etech.testproject.ui.adapter.NotificationAdapter;
import com.etech.testproject.ui.base.BaseActivity;
import com.etech.testproject.ui.callback.OnRecyclerViewItemClickListener;
import com.etech.testproject.utils.ViewUtils;

import java.util.ArrayList;


public class NotificationActivity extends BaseActivity implements NotificationContract.View {

    private NotificationAdapter dashboardAdapter;
    private ActivityNotificationBinding binding;
    private NotificationContract.Presenter<NotificationContract.View> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);

        presenter = new NotificationPresenter<>();
        presenter.onAttach(this);

        setUpView(binding.header.mainToolbar, binding.extraViews, "menu", true);

        presenter.init();

        binding.swRefDashboard.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.reset();
            }
        });
    }


    @Override
    public void onRetryClicked() {
        presenter.reset();
    }


    @Override
    public void setUpView(boolean isReset) {
        if (isReset) {
            dashboardAdapter = null;
        }
        ViewUtils.setVisibility(binding.tvNotFound, View.GONE);
        if (dashboardAdapter == null) {
            dashboardAdapter = new NotificationAdapter(this, new OnRecyclerViewItemClickListener() {


                @Override
                public void onClicked(Object bean, View view, int position, ViewType viewType) {
                    if (viewType == ViewType.View) {

                    }
                }

                @Override
                public void onLastItemReached() {
                    presenter.loadMoreRecords();
                }
            });
            binding.rvDashboard.setAdapter(dashboardAdapter);
//            skeletonScreen = AppUtils.bindRecyclerViewForSkeleton(binding.rvMyComplain, dashboardAdapter);
        } else {
            binding.rvDashboard.setAdapter(dashboardAdapter);
        }

    }

    @Override
    public void loadDataToView(ArrayList<Lecture> list) {
        dashboardAdapter.addData(list);
        hideSkeletonView();
    }


    private void hideSkeletonView() {
//        if (skeletonScreen != null) {
//            skeletonScreen.hide();
//            skeletonScreen = null;
//        }
    }

    @Override
    public void setNotRecordsFoundView(boolean isActive) {
        if (isActive) {
            ViewUtils.setVisibility(binding.tvNotFound, View.VISIBLE);
        } else {
            ViewUtils.setVisibility(binding.tvNotFound, View.GONE);
        }
    }

    @Override
    public void showProgressBar() {
        dashboardAdapter.updateBottomProgress(0);
    }

    @Override
    public void hideProgressBar() {
        dashboardAdapter.updateBottomProgress(1);
        binding.swRefDashboard.setRefreshing(false);
        hideSkeletonView();
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }


}
