package com.education.xeal.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.education.xeal.BaseApplication;
import com.education.xeal.R;
import com.education.xeal.prefs.AppPreferencesHelper;
import com.education.xeal.utils.AppConstants;
import com.education.xeal.utils.AppEnvironment;
import com.education.xeal.utils.CommonUtils;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author by Mohit Arora on 8/5/18.
 */

public class PaymentActivity extends BaseActivity {
    public static final String TAG = PaymentActivity.class.getSimpleName();

    @BindView(R.id.edtAmount)
    EditText edtAmount;

    @BindView(R.id.edtPurposeOfPayment)
    EditText edtPurposeOfPayment;

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.btnPay)
    Button btnPay;

    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    private AppPreferencesHelper appPreferencesHelper;

    @NonNull
    public static Intent getStartIntent(Context context) {
        return new Intent(context, PaymentActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        appPreferencesHelper = new AppPreferencesHelper(this, AppConstants.PREF_NAME);
    }

    @OnClick(R.id.btnPay)
    public void onPayButtonClick() {
        hideKeyboard();
        if (edtAmount.getText().toString().equals("")) {
            showMessage(getString(R.string.enter_amount));
            return;
        }

        if (edtPurposeOfPayment.getText().toString().equals("")) {
            showMessage(getString(R.string.enter_purpose));
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

        launchPayUMoneyFlow();
    }

    /**
     * This function prepares the data for payment and launches payumoney plug n play sdk
     */
    private void launchPayUMoneyFlow() {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();
        payUmoneyConfig.setAccentColor("#2074ec");
        payUmoneyConfig.setColorPrimary("#3192f4");
        payUmoneyConfig.setColorPrimaryDark("#2579f7");
        payUmoneyConfig.setTextColorPrimary("#FFFFFF");

        //Use this to set your custom text on result screen button
        //payUmoneyConfig.setDoneButtonText(((EditText) findViewById(R.id.status_page_et)).getText().toString());

        //Use this to set your custom title for the activity
        //payUmoneyConfig.setPayUmoneyActivityTitle(((EditText) findViewById(R.id.activity_title_et)).getText().toString());

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {
            amount = Double.parseDouble(edtAmount.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        String txnId = System.currentTimeMillis() + "";
        String phone = appPreferencesHelper.getMobileNumber().trim();
        String productName = edtPurposeOfPayment.getText().toString();
        String firstName = appPreferencesHelper.getName();
        String email = edtEmail.getText().toString().trim();
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
        builder.setAmount(amount)
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            mPaymentParams = builder.build();

            /*
            * Hash should always be generated from your server side.
            * */
            generateHashFromServer(mPaymentParams);

/*            *//*
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * */
           /* mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

           if (AppPreference.selectedTheme != -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,MainActivity.this, AppPreference.selectedTheme,mAppPreference.isOverrideResultScreen());
            } else {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,MainActivity.this, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
            }*/

        } catch (Exception e) {
            // some exception occurred
            showMessage(e.getMessage());
        }
    }

    /**
     * This method generates hash from server.
     *
     * @param paymentParam payments params used for hash generation
     */
    public void generateHashFromServer(PayUmoneySdkInitializer.PaymentParam paymentParam) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        HashMap<String, String> params = paymentParam.getParams();

        // lets create the post params
        StringBuilder postParamsBuffer = new StringBuilder();
        postParamsBuffer.append(concatParams(PayUmoneyConstants.KEY, params.get(PayUmoneyConstants.KEY)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.AMOUNT, params.get(PayUmoneyConstants.AMOUNT)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.TXNID, params.get(PayUmoneyConstants.TXNID)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.EMAIL, params.get(PayUmoneyConstants.EMAIL)));
        postParamsBuffer.append(concatParams("productInfo", params.get(PayUmoneyConstants.PRODUCT_INFO)));
        postParamsBuffer.append(concatParams("firstName", params.get(PayUmoneyConstants.FIRSTNAME)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF1, params.get(PayUmoneyConstants.UDF1)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF2, params.get(PayUmoneyConstants.UDF2)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF3, params.get(PayUmoneyConstants.UDF3)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF4, params.get(PayUmoneyConstants.UDF4)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF5, params.get(PayUmoneyConstants.UDF5)));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1) : postParamsBuffer.toString();
        //String postParams = json.toString();
        Log.e("postParams", "postParams>>" + postParams);
        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }

    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    /**
     * This AsyncTask generates hash from server.
     */
    @SuppressLint("StaticFieldLeak")
    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PaymentActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {

            String merchantHash = "";
            try {
                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                URL url = new URL("http://www.xealadmissions.com/otp/moneyhash.php");
                //URL url = new URL("https://payu.herokuapp.com/get_hash");

                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuilder responseStringBuffer = new StringBuilder();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        /*
                         * This hash is mandatory and needs to be generated from merchant's server side
                         *
                         */
                        case "payment_hash":
                            merchantHash = response.getString(key);
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return merchantHash;
        }

        @Override
        protected void onPostExecute(String merchantHash) {
            super.onPostExecute(merchantHash);

            progressDialog.dismiss();

            if (merchantHash.isEmpty() || merchantHash.equals("")) {
                showMessage(getString(R.string.not_generate_key_hash));
            } else {
                //Log.e("merchantHash", "merchantHash>>" + merchantHash);
                mPaymentParams.setMerchantHash(merchantHash);
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PaymentActivity.this,
                        R.style.AppTheme, true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from PayU money activity
        //Log.e(TAG, "request code " + requestCode + " resultcode " + resultCode);

        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            //Log.e(TAG, "request code22 " + requestCode + " resultcode22 " + resultCode);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);
            String payuResponse;
            String merchantResponse;
            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //showMessage(transactionResponse.getMessage());
                    //Success Transaction
                    // Response from PayU money
                    payuResponse = transactionResponse.getPayuResponse();
                    //Log.e("Success", "payuResponse>>" + payuResponse);

                    // Response from SURl and FURL
                    merchantResponse = transactionResponse.getTransactionDetails();
                    //Log.e("Success", "merchantResponse>>" + merchantResponse);
                    try {
                        JSONObject jsonObject = new JSONObject(payuResponse);
                        String txnId = jsonObject.getString("txnid");
                        if (txnId != null && !txnId.equals("")) {
                            openThankYouActivity(txnId);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showMessage(transactionResponse.getMessage());
                    //Failure Transaction
                    // Response from PayU money
                    payuResponse = transactionResponse.getPayuResponse();
                    //Log.e("Failure", "payuResponse>>" + payuResponse);

                    // Response from SURl and FURL
                    merchantResponse = transactionResponse.getTransactionDetails();
                    //Log.e("Failure", "merchantResponse>>" + merchantResponse);
                }

            } else if (resultModel != null && resultModel.getError() != null) {
                Log.e(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.e(TAG, "Both objects are null!");
            }
        } else {
            Log.e(TAG, "Both objects are null2!");
        }
    }

    private void openThankYouActivity(String txnId) {
        Intent intent = ThankYouActivity.getStartIntent(PaymentActivity.this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Amount", edtAmount.getText().toString());
        intent.putExtra("Purpose", edtPurposeOfPayment.getText().toString());
        intent.putExtra("UTRNumber", txnId);
        startActivity(intent);
        finish();
    }
}