package com.education.xeal.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.education.xeal.R;
import com.education.xeal.utils.AppConstants;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author by Mohit Arora on 8/5/18.
 */

public class MainActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    public static final String TAG = MainActivity.class.getSimpleName();

    @NonNull
    public static Intent getStartIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (getIntent().getBooleanExtra("isComingFromNotification", false)) {
            onNotificationButtonClick();
        }
    }

    @OnClick(R.id.btnSend)
    public void onSendButtonClick() {
        hideKeyboard();
        sendSms();
    }

    @OnClick(R.id.btnCall)
    public void onCallButtonClick() {
        hideKeyboard();
        call();
    }

    @OnClick(R.id.btnAdmission)
    public void onAdmissionButtonClick() {
        hideKeyboard();
        Intent intent = MainSubActivity.getStartIntent(MainActivity.this);
        intent.putExtra("FLAG", "Admission");
        startActivity(intent);
    }

    @OnClick(R.id.btnCoaching)
    public void onCoachingButtonClick() {
        hideKeyboard();
        Intent intent = MainSubActivity.getStartIntent(MainActivity.this);
        intent.putExtra("FLAG", "Coaching");
        startActivity(intent);
    }

    @OnClick(R.id.btnTalentHunt)
    public void onTalentHuntButtonClick() {
        hideKeyboard();
        Intent intent = MainSubActivity.getStartIntent(MainActivity.this);
        intent.putExtra("FLAG", "TalentHunt");
        startActivity(intent);
    }

    @OnClick(R.id.btnMakePayment)
    public void onMakePaymentButtonClick() {
        hideKeyboard();
        Intent intent = PaymentActivity.getStartIntent(MainActivity.this);
        startActivity(intent);
    }

    @OnClick(R.id.btnNotification)
    public void onNotificationButtonClick() {
        hideKeyboard();
        Intent intent = NotificationActivity.getStartIntent(MainActivity.this);
        startActivity(intent);
    }

    @OnClick(R.id.btnSocialNetworks)
    public void onSocialNetworksButtonClick() {
        hideKeyboard();
        Intent intent = SocialNetworkActivity.getStartIntent(MainActivity.this);
        startActivity(intent);
    }

    @OnClick(R.id.btnBookMyAdmission)
    public void onBookMyAdmissionButtonClick() {
        hideKeyboard();
        Intent intent = BookMyAdmissionActivity.getStartIntent(MainActivity.this);
        startActivity(intent);
    }

    protected void sendSms() {
        //showMessage(getString(R.string.send_sms));
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", AppConstants.WHATSAPP);
        smsIntent.putExtra("sms_body", "");

        try {
            startActivity(smsIntent);
            //showMessage(getString(R.string.finish_send_sms));
        } catch (android.content.ActivityNotFoundException ex) {
            showMessage(getString(R.string.failed_send_sms));
        }
    }

    @SuppressLint("MissingPermission")
    protected void call() {
        Intent my_callIntent = new Intent(Intent.ACTION_CALL);
        my_callIntent.setData(Uri.parse("tel:" + AppConstants.LANDLINE));
        //here the word 'tel' is important for making a call...
        try {
            Boolean isPermission = isPermissionsGranted(Manifest.permission.CALL_PHONE);
            if (!isPermission) {
                this.requestPermission(new String[]{Manifest.permission.CALL_PHONE}, AppConstants.RESPONSE_CODE);
                return;
            }
            startActivity(my_callIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            showMessage(getString(R.string.no_call_client_installed));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case AppConstants.RESPONSE_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    showMessage(getString(R.string.call_permission));
                }

                break;
            }
        }
    }
}