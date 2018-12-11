/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.education.xeal.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_TOKEN = "PREF_KEY_TOKEN";

    private static final String PREF_KEY_MOBILE_NO_VERIFIED = "PREF_KEY_MOBILE_NO_VERIFIED";

    private static final String PREF_KEY_MOBILE_NO = "PREF_KEY_MOBILE_NO";

    private static final String PREF_KEY_USER_NAME = "PREF_KEY_USER_NAME";

    private static final String PREF_KEY_NOTIFICATION_ALLOW = "PREF_KEY_NOTIFICATION_ALLOW";

    private final SharedPreferences mPrefs;

    public AppPreferencesHelper(Context context, String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public String getToken() {
        return mPrefs.getString(PREF_KEY_TOKEN, null);
    }

    @Override
    public void setToken(String token) {
        mPrefs.edit().putString(PREF_KEY_TOKEN, token).apply();
    }

    @Override
    public Boolean isMobileNoVerified() {
        return mPrefs.getBoolean(PREF_KEY_MOBILE_NO_VERIFIED, false);
    }

    @Override
    public void setMobileNumberVerified(Boolean isVerified) {
        mPrefs.edit().putBoolean(PREF_KEY_MOBILE_NO_VERIFIED, isVerified).apply();
    }

    @Override
    public String getMobileNumber() {
        return mPrefs.getString(PREF_KEY_MOBILE_NO, null);
    }

    @Override
    public void setMobileNumber(String mobileNumber) {
        mPrefs.edit().putString(PREF_KEY_MOBILE_NO, mobileNumber).apply();
    }

    @Override
    public String getName() {
        return mPrefs.getString(PREF_KEY_USER_NAME, null);
    }

    @Override
    public void setName(String name) {
        mPrefs.edit().putString(PREF_KEY_USER_NAME, name).apply();
    }

    @Override
    public Boolean isNotificationAllow() {
        return mPrefs.getBoolean(PREF_KEY_NOTIFICATION_ALLOW, true);
    }

    @Override
    public void setNotificationAllow(Boolean isAllow) {
        mPrefs.edit().putBoolean(PREF_KEY_NOTIFICATION_ALLOW, isAllow).apply();
    }
}