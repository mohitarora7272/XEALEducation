package com.education.xeal.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.education.xeal.R;
import com.education.xeal.prefs.AppPreferencesHelper;
import com.education.xeal.utils.AppConstants;
import com.education.xeal.utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author by Mohit Arora on 24/1/18.
 */
public class SplashActivity extends BaseActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();
    private AppPreferencesHelper appPreferencesHelper;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        appPreferencesHelper = new AppPreferencesHelper(this, AppConstants.PREF_NAME);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(AppConstants.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(AppConstants.TOPIC_GLOBAL);
                } else if (intent.getAction().equals(AppConstants.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    Log.e("Message", "From Push>>" + message);
                }
            }
        };

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splashscreen);
        imageView.setAnimation(animation);
        animation.setAnimationListener(new SplashAnimation());

        if (getIntent().getStringExtra("message") != null) {
            if (!getIntent().getStringExtra("message").equals("")) {
                openActivity(true);
            }
        }
    }

    public void openActivity(boolean isComingFromNotification) {
        if (appPreferencesHelper.isMobileNoVerified()) {
            Intent intent = MainActivity.getStartIntent(SplashActivity.this);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("isComingFromNotification", isComingFromNotification);
            startActivity(intent);
            finish();
        } else {
            Intent intent = GenerateOtpActivity.getStartIntent(SplashActivity.this);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("isComingFromNotification", isComingFromNotification);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(AppConstants.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(AppConstants.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    class SplashAnimation implements Animation.AnimationListener {
        SplashAnimation() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            openActivity(false);
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }
}