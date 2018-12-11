package com.education.xeal.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.education.xeal.R;
import com.education.xeal.modal.ResponseModal;
import com.education.xeal.network.ApiClient;
import com.education.xeal.network.RestClient;
import com.education.xeal.prefs.AppPreferencesHelper;
import com.education.xeal.utils.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.education.xeal.utils.AppConstants.RESPONSE_CODE;

/**
 * @author by Mohit Arora on 8/5/18.
 */

public class GenerateOtpActivity extends BaseActivity {
    public static final String TAG = GenerateOtpActivity.class.getSimpleName();

    @BindView(R.id.edtMobileNumber)
    EditText edtMobileNumber;

    @BindView(R.id.btnGenerateOtp)
    Button btnGenerateOtp;

    private AppPreferencesHelper appPreferencesHelper;
    @SuppressLint("StaticFieldLeak")
    public static Activity generateOtpActivity;

    @NonNull
    public static Intent getStartIntent(Context context) {
        return new Intent(context, GenerateOtpActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_otp);
        ButterKnife.bind(this);
        generateOtpActivity = this;
        appPreferencesHelper = new AppPreferencesHelper(this, AppConstants.PREF_NAME);
    }

    @OnClick(R.id.btnGenerateOtp)
    public void onGenerateOtpClick() {
        hideKeyboard();
        if (edtMobileNumber.getText().toString().equals("")) {
            showMessage(getString(R.string.enter_mobile_number));
            return;
        }

        if (edtMobileNumber.getText().toString().length() < 10) {
            showMessage(getString(R.string.enter_ten_digit_number));
            return;
        }

        makeJson();
    }

    private void makeJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("mobile_number", edtMobileNumber.getText().toString());
            json.put("gcm_id", appPreferencesHelper.getToken());
            json.put("mode", "Android");
            json.put("notification", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        generateOtp(json.toString());
    }

    public void generateOtp(String json) {
        showLoading();
        hideKeyboard();

        ApiClient apiClient = new ApiClient();
        RestClient restClientAPI = apiClient.getClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Call<ResponseModal> call = restClientAPI.generateOtp(body);

        call.enqueue(new Callback<ResponseModal>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModal> call, @NonNull Response<ResponseModal> response) {
                ResponseModal responseModal = response.body();
                int code = response.code();
                Log.e(TAG, "code>>>>" + code);
                if (code == RESPONSE_CODE) {
                    hideLoading();
                    if (responseModal.getSuccess().equals("true")) {
                        appPreferencesHelper.setMobileNumber(edtMobileNumber.getText().toString());
                        Intent intent = RegisterActivity.getStartIntent(GenerateOtpActivity.this);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        showMessage(responseModal.getMsg());
                    }
                } else {
                    hideLoading();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModal> call, @NonNull Throwable t) {
                Log.e(TAG, "Throwable>>>>" + t.getMessage());
                hideLoading();
                showMessage(getString(R.string.internet_not_available));
            }
        });
    }
}