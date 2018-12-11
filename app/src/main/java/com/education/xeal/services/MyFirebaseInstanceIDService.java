package com.education.xeal.services;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.education.xeal.prefs.AppPreferencesHelper;
import com.education.xeal.utils.AppConstants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * @author by Mohit Arora on 15/3/18.
 */

@SuppressLint("Registered")
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "sendRegistrationToServer: " + refreshedToken);

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(AppConstants.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void storeRegIdInPref(String token) {
        AppPreferencesHelper appPreferencesHelper = new AppPreferencesHelper(this, AppConstants.PREF_NAME);
        appPreferencesHelper.setToken(token);
    }
}