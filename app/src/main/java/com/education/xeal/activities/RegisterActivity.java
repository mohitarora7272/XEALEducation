package com.education.xeal.activities;

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

public class RegisterActivity extends BaseActivity {
    public static final String TAG = RegisterActivity.class.getSimpleName();

    @BindView(R.id.edtOtp)
    EditText edtOtp;

    @BindView(R.id.edtName)
    EditText edtName;

    @BindView(R.id.btnRegister)
    Button btnRegister;

    private AppPreferencesHelper appPreferencesHelper;

    @NonNull
    public static Intent getStartIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        appPreferencesHelper = new AppPreferencesHelper(this, AppConstants.PREF_NAME);
    }

    @OnClick(R.id.btnRegister)
    public void onRegisterButtonClick() {
        hideKeyboard();
        if (edtOtp.getText().toString().equals("")) {
            showMessage(getString(R.string.enter_otp));
            return;
        }

        if (edtName.getText().toString().equals("")) {
            showMessage(getString(R.string.enter_name));
            return;
        }

        makeJson();
    }


    private void makeJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("otp", edtOtp.getText().toString());
            json.put("name", edtName.getText().toString());
            json.put("notification", 1);
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
        Call<ResponseModal> call = restClientAPI.register(body);

        call.enqueue(new Callback<ResponseModal>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModal> call, @NonNull Response<ResponseModal> response) {
                ResponseModal responseModal = response.body();
                int code = response.code();
                Log.e(TAG, "code>>>>" + code);
                if (code == RESPONSE_CODE) {
                    hideLoading();
                    if (responseModal.getSuccess().equals("true")) {
                        appPreferencesHelper.setName(edtName.getText().toString());
                        appPreferencesHelper.setMobileNumberVerified(true);
                        Intent intent = CongratulationActivity.getStartIntent(RegisterActivity.this);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                        if (GenerateOtpActivity.generateOtpActivity != null) {
                            try {
                                GenerateOtpActivity.generateOtpActivity.finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
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
