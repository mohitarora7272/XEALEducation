package com.education.xeal.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.education.xeal.R;
import com.education.xeal.adapters.NotificationAdapter;
import com.education.xeal.modal.GetNotificationModal;
import com.education.xeal.modal.ResponseModal;
import com.education.xeal.network.ApiClient;
import com.education.xeal.network.RestClient;
import com.education.xeal.prefs.AppPreferencesHelper;
import com.education.xeal.services.MyFirebaseMessagingService;
import com.education.xeal.utils.AppConstants;
import com.education.xeal.utils.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leolin.shortcutbadger.ShortcutBadger;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.education.xeal.utils.AppConstants.RESPONSE_CODE;

/**
 * @author by Mohit Arora on 8/5/18.
 */

public class NotificationActivity extends BaseActivity {
    public static final String TAG = NotificationActivity.class.getSimpleName();

    @BindView(R.id.switchNotification)
    Switch switchNotification;

    @BindView(R.id.recycleView)
    RecyclerView recycleView;

    @BindView(R.id.tvNotFound)
    TextView tvNotFound;

    private AppPreferencesHelper appPreferencesHelper;
    private List<String> listGetNotification;

    @NonNull
    public static Intent getStartIntent(Context context) {
        return new Intent(context, NotificationActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        ShortcutBadger.removeCount(getApplicationContext());
        MyFirebaseMessagingService.badgeCount = 0;
        appPreferencesHelper = new AppPreferencesHelper(this, AppConstants.PREF_NAME);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(mLayoutManager);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        listGetNotification = new ArrayList<>();
        setAdapter();
        setNotificationPref();
        makeJson();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    private void setNotificationPref() {
        if (appPreferencesHelper.isNotificationAllow()) {
            switchNotification.setChecked(appPreferencesHelper.isNotificationAllow());
        } else {
            switchNotification.setChecked(appPreferencesHelper.isNotificationAllow());
        }
    }

    private void setAdapter() {
        if (listGetNotification != null && listGetNotification.size() > 0) {
            tvNotFound.setVisibility(View.GONE);
            recycleView.setVisibility(View.VISIBLE);
            NotificationAdapter adapter = new NotificationAdapter(this, listGetNotification);
            recycleView.setAdapter(adapter);
        } else {
            tvNotFound.setVisibility(View.VISIBLE);
            recycleView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.switchNotification)
    public void onSwitchClick() {
        if (switchNotification.isChecked()) {
            makeJson(1);
            appPreferencesHelper.setNotificationAllow(true);
        } else {
            makeJson(0);
            appPreferencesHelper.setNotificationAllow(false);
        }
    }

    private void makeJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("mobile_number", appPreferencesHelper.getMobileNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getNotifications(json.toString());
    }

    private void getNotifications(String json) {
        showLoading();
        hideKeyboard();

        ApiClient apiClient = new ApiClient();
        RestClient restClientAPI = apiClient.getClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Call<GetNotificationModal> call = restClientAPI.getNotification(body);

        call.enqueue(new Callback<GetNotificationModal>() {
            @Override
            public void onResponse(@NonNull Call<GetNotificationModal> call, @NonNull Response<GetNotificationModal> response) {
                GetNotificationModal responseModal = response.body();
                int code = response.code();
                Log.e(TAG, "code>>>>" + code);
                if (code == RESPONSE_CODE) {
                    hideLoading();
                    if (listGetNotification != null && listGetNotification.size() > 0) {
                        listGetNotification.clear();
                    }
                    if (responseModal.getSuccess().equals("true")) {
                        listGetNotification.addAll(responseModal.getMsg());
                        //Collections.reverse(listGetNotification);
                        setAdapter();
                    } else {
                        setAdapter();
                    }
                } else {
                    hideLoading();
                    setAdapter();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetNotificationModal> call, @NonNull Throwable t) {
                Log.e(TAG, "Throwable>>>>" + t.getMessage());
                hideLoading();
                setAdapter();
                showMessage(getString(R.string.internet_not_available));
            }
        });
    }

    private void makeJson(Integer isOnOff) {
        JSONObject json = new JSONObject();
        try {
            json.put("mobile_number", appPreferencesHelper.getMobileNumber());
            json.put("notification", isOnOff);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setNotificationSwitchOnOff(json.toString());
    }

    public void setNotificationSwitchOnOff(String json) {
        hideKeyboard();

        ApiClient apiClient = new ApiClient();
        RestClient restClientAPI = apiClient.getClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Call<ResponseModal> call = restClientAPI.notificationOnOff(body);

        call.enqueue(new Callback<ResponseModal>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModal> call, @NonNull Response<ResponseModal> response) {
                ResponseModal responseModal = response.body();
                int code = response.code();
                Log.e(TAG, "code>>>>" + code);
                if (code == RESPONSE_CODE) {
                    Log.e("Message", "Message>>" + responseModal.getMsg());
                    showMessage(responseModal.getMsg());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModal> call, @NonNull Throwable t) {
                Log.e(TAG, "Throwable>>>>" + t.getMessage());
                showMessage(getString(R.string.internet_not_available));
            }
        });
    }
}