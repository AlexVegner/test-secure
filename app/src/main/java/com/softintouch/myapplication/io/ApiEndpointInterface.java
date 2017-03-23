package com.softintouch.myapplication.io;

import com.softintouch.myapplication.model.Message;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by alex on 3/23/17.
 */

public interface ApiEndpointInterface {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("messages/")
    Call<Message> addMessage(@Body Message encryptedMessage);
}
