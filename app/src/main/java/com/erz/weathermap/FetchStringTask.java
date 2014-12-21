package com.erz.weathermap;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by edgarramirez on 12/18/14.
 */
public class FetchStringTask extends BackgroundTask<Object, String> {

    String url;

    public FetchStringTask(OnBackgroundTaskListener<String> listener, String url) {
        super(listener);
        this.url = url;
    }

    @Override
    protected String doExecute(Object... params) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
