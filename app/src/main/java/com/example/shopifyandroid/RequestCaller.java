package com.example.shopifyandroid;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RequestCaller {

    public String makeRequest(String requestUrl) {
        OkHttpClient client = new OkHttpClient();

        Request req = new Request.Builder()
                .url(requestUrl)
                .build();

        try (Response res = client.newCall(req).execute()) {
            ResponseBody resBody = res.body();
            if (resBody != null) {
                return resBody.string();
            }

            throw new Error("Could not get result.");
        } catch (Exception e) {
            Log.e("Error", e.getMessage(), e);
        }

        return null;
    }
}