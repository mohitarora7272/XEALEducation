package com.education.xeal.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.education.xeal.R;
import com.education.xeal.prefs.AppPreferencesHelper;
import com.education.xeal.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author by Mohit Arora on 8/5/18.
 */

public class CongratulationActivity extends BaseActivity {
    public static final String TAG = CongratulationActivity.class.getSimpleName();

    @BindView(R.id.tvCong)
    TextView tvCong;

    @BindView(R.id.btnMoveOn)
    Button btnMoveOn;

    @NonNull
    public static Intent getStartIntent(Context context) {
        return new Intent(context, CongratulationActivity.class);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulation);
        ButterKnife.bind(this);
        AppPreferencesHelper appPreferencesHelper = new AppPreferencesHelper(this, AppConstants.PREF_NAME);
        tvCong.setText(getString(R.string.congratulation_text) + "\n" + appPreferencesHelper.getName() + "\n" +
                getString(R.string.success_register_text));
    }

    @OnClick(R.id.btnMoveOn)
    public void onMoveOnButtonClick() {
        hideKeyboard();

        Intent intent = MainActivity.getStartIntent(CongratulationActivity.this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}