package com.example.shopifyandroid;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestCaller extends AsyncTask<String, Void, String> {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();
        String requestUrl = params[0];

        Request req = new Request.Builder()
                .url(requestUrl)
                .build();

        try (Response res = client.newCall(req).execute()) {
            return res.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
