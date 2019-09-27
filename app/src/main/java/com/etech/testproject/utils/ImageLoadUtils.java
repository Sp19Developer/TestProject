package com.etech.testproject.utils;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.etech.testproject.R;
import com.etech.testproject.app.TestApp;
import com.etech.testproject.data.network.apiHelper.RestResponse;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;


public class ImageLoadUtils {


    public interface Callback {
        void onSuccess();

        void onError();
    }

    public static void setImageUrl(Context context, String path, final ImageView imageView) {
        setImageUrl(context, path, imageView, null, false);
    }

    public static void setImageUrl(Context context, String path, final ImageView imageView, final ProgressBar progressBar) {
        setImageUrl(context, path, imageView, progressBar, false);
    }

    public static void setImageUrl(Context context, String path, final ImageView imageView, final ProgressBar progressBar, boolean isLocal) {
        setImageUrl(context, path, imageView, progressBar, isLocal, null);
    }

    public static void setImageUrl(Context context, String path, final ImageView imageView, final ProgressBar progressBar, final Callback callback) {
        setImageUrl(context, path, imageView, progressBar, false, callback);
    }

    public static void setImageUrl(Context context, String path, final ImageView imageView, final ProgressBar progressBar, boolean isLocal, final Callback callback) {

        if (!TextUtils.isEmpty(path)) {
            if (isLocal) {
                Glide.with(context)
                        .load(new File(path))
                        .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (progressBar != null)
                                    progressBar.setVisibility(View.GONE);
//                    imageView.setImageResource(R.drawable.no_category_available);
                                if (callback != null)
                                    callback.onError();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (progressBar != null)
                                    progressBar.setVisibility(View.GONE);
                                if (callback != null)
                                    callback.onSuccess();
                                return false;
                            }
                        }).into(imageView);
            } else {
                Glide.with(context)
                        .load(path)
                        .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (progressBar != null)
                                    progressBar.setVisibility(View.GONE);
                                if (callback != null)
                                    callback.onError();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (progressBar != null)
                                    progressBar.setVisibility(View.GONE);
                                if (callback != null)
                                    callback.onSuccess();
                                return false;
                            }
                        })
                        .into(imageView);
            }
        } else {
            if (progressBar != null)
                progressBar.setVisibility(View.GONE);
//            imageView.setImageResource(R.mipmap.default_media_image_gallery);
            if (callback != null)
                callback.onError();
        }

    }



}
