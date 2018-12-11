package com.education.xeal.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.education.xeal.R;
import com.education.xeal.utils.AppConstants;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author by Mohit Arora on 8/5/18.
 */

public class SocialNetworkActivity extends BaseActivity {
    public static final String TAG = SocialNetworkActivity.class.getSimpleName();

    @NonNull
    public static Intent getStartIntent(Context context) {
        return new Intent(context, SocialNetworkActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_networks);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnFacebook)
    public void onFacebookClick() {
        hideKeyboard();
        openIntent("Facebook", AppConstants.FACEBOOK_LINK);
    }

    @OnClick(R.id.btnTwitter)
    public void onTwitterClick() {
        hideKeyboard();
        openIntent("Twitter", AppConstants.TWITTER_LINK);
    }

    @OnClick(R.id.btnInstagram)
    public void onInstagramClick() {
        hideKeyboard();
        openIntent("Instagram", AppConstants.INSTAGRAM_LINK);
    }

    @OnClick(R.id.btnWhatsApp)
    public void onWhatsAppClick() {
        hideKeyboard();
        openIntent("WhatsApp", AppConstants.WHATSAPP);
    }

    @OnClick(R.id.btnTelegram)
    public void onTelegramClick() {
        hideKeyboard();
        openIntent("Telegram", AppConstants.TELEGRAM);
    }

    @OnClick(R.id.btnGooglePlus)
    public void onGooglePlusClick() {
        hideKeyboard();
        openIntent("GooglePlus", AppConstants.GOOGLE_PLUS);
    }

    @OnClick(R.id.btnEmail)
    public void onEmailClick() {
        hideKeyboard();
        sendEmail();
    }

    private void openIntent(String tag, String link) {
        switch (tag) {
            case "Facebook":
            case "Instagram":
            case "Twitter":

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(browserIntent);

                break;
            case "WhatsApp":
                try {
                    String text = getString(R.string.app_name);// Replace with your message.
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + link + "&text=" + text));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    showMessage(getString(R.string.no_whats_app_client_installed));
                    e.printStackTrace();
                }
                break;
            case "Telegram":
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=partsilicon"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {
                    showMessage(getString(R.string.no_telegram_client_installed));
                }
                break;

            case "GooglePlus":

                break;
        }
    }

    protected void sendEmail() {
        String[] TO = {AppConstants.EMAIL};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "XEAL Education Services");
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            showMessage(getString(R.string.no_email_client_installed));
        }
    }
}