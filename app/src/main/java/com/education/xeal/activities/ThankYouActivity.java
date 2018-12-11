package com.education.xeal.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.education.xeal.R;
import com.education.xeal.prefs.AppPreferencesHelper;
import com.education.xeal.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author by Mohit Arora on 8/5/18.
 */

public class ThankYouActivity extends BaseActivity {
    public static final String TAG = ThankYouActivity.class.getSimpleName();

    @BindView(R.id.tvThanksMessage)
    TextView tvThanksMessage;

    @NonNull
    public static Intent getStartIntent(Context context) {
        return new Intent(context, ThankYouActivity.class);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);
        ButterKnife.bind(this);
        AppPreferencesHelper appPreferencesHelper = new AppPreferencesHelper(this, AppConstants.PREF_NAME);
        tvThanksMessage.setText(getString(R.string.thanks_text) + " " + appPreferencesHelper.getName() +
                getString(R.string.made_payment) + " "
                + getIntent().getStringExtra("Amount") + "/- " +
                getString(R.string.for_text) + " " +
                getIntent().getStringExtra("Purpose") +
                getString(R.string.thanks_text1) + " " + getIntent().getStringExtra("UTRNumber") +
                getString(R.string.thanks_text2));
    }
}