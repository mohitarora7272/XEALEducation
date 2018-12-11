package com.education.xeal.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.education.xeal.R;
import com.education.xeal.utils.AppConstants;
import com.education.xeal.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author by Mohit Arora on 8/5/18.
 */

public class MainSubActivity extends BaseActivity {
    public static final String TAG = MainSubActivity.class.getSimpleName();

    @BindView(R.id.toolbar_title_login)
    TextView toolbar_title_login;

    @BindView(R.id.tvTexts)
    TextView tvTexts;

    @BindView(R.id.edtMessage)
    EditText edtMessage;

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.btnSend)
    Button btnSend;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.llDown)
    LinearLayout llDown;

    @BindView(R.id.webview)
    WebView webview;

    @NonNull
    public static Intent getStartIntent(Context context) {
        return new Intent(context, MainSubActivity.class);
    }

    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sub);
        ButterKnife.bind(this);

        switch (getIntent().getStringExtra("FLAG")) {
            case "Admission":
                toolbar_title_login.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                llDown.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);
                webview.setWebChromeClient(new WebChromeClient());
                webview.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        showLoading();
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        hideLoading();
                    }
                });
                webview.getSettings().setJavaScriptEnabled(true);
                webview.loadUrl(getString(R.string.xeal_admissions_url));
                break;
            case "Coaching":
                toolbar_title_login.setText(getString(R.string.coaching_text));
                tvTexts.setText(getString(R.string.coaching_full_text));
                break;
            case "TalentHunt":
                toolbar_title_login.setText(getString(R.string.talent_hunt_text));
                tvTexts.setText(getString(R.string.talent_hunt_full_text));
                break;
        }
    }

    @OnClick(R.id.btnSend)
    public void onSendButtonClick() {
        hideKeyboard();
        if (edtMessage.getText().toString().equals("")) {
            showMessage(getString(R.string.enter_message));
            return;
        }

        if (edtEmail.getText().toString().equals("")) {
            showMessage(getString(R.string.enter_email));
            return;
        }

        if (!CommonUtils.isEmailValid(edtEmail.getText().toString())) {
            showMessage(getString(R.string.enter_valid_email));
            return;
        }

        sendEmail();
    }

    protected void sendEmail() {
        String[] TO = {AppConstants.EMAIL};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "XEAL Education Services");
        emailIntent.putExtra(Intent.EXTRA_TEXT, edtMessage.getText().toString());
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            showMessage(getString(R.string.no_email_client_installed));
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webview.canGoBack()) {
            this.webview.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}