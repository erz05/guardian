package com.erz.weathermap;

import android.content.Context;

import com.erz.weathermap.data.Countries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by edgarramirez on 12/21/14.
 */
public class FetchCountriesTask extends BackgroundTask<Object, Countries> {

    Context context;

    public FetchCountriesTask(OnBackgroundTaskListener<Countries> listener, Context context) {
        super(listener);
        this.context = context;
    }

    @Override
    protected Countries doExecute(Object... params) {
        InputStream is = context.getResources().openRawResource(R.raw.countries);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        return (Countries) JacksonHelper.readValue(jsonString, Countries.class);
    }
}
