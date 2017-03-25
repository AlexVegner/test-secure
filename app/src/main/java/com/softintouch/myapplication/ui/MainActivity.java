package com.softintouch.myapplication.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.softintouch.myapplication.R;
import com.softintouch.myapplication.io.ApiEndpointInterface;
import com.softintouch.myapplication.model.Message;
import com.softintouch.myapplication.util.CipherManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "https://serene-shelf-28400.herokuapp.com/";

    private TextView mMessage;
    private TextView mSubmit;
    private ApiEndpointInterface mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMessage = (TextView) findViewById(R.id.message);
        mSubmit = (TextView) findViewById(R.id.submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String encryptedMessage = CipherManager.encrypt(CipherManager.getKey(), mMessage.getText().toString());
                if (!TextUtils.isEmpty(encryptedMessage)) {
                    Call<Message> call = mApiService.addMessage(new Message(encryptedMessage));
                    call.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            Toast.makeText(MainActivity.this, "Message was sent", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApiService = retrofit.create(ApiEndpointInterface.class);
    }
}
