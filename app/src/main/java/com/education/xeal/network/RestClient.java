package com.education.xeal.network;

import com.education.xeal.modal.GetNotificationModal;
import com.education.xeal.modal.ResponseModal;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestClient {

    @POST("./GenerateOTP")
    Call<ResponseModal> generateOtp(@Body RequestBody body);

    @POST("./Register")
    Call<ResponseModal> register(@Body RequestBody body);

    @POST("./enableNotification")
    Call<ResponseModal> notificationOnOff(@Body RequestBody body);

    @POST("./getNotifications")
    Call<GetNotificationModal> getNotification(@Body RequestBody body);

}