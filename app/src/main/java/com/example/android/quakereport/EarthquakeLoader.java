package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;
import static com.example.android.quakereport.EarthquakeActivity.urlstring;

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<earthquakes>> {


    public EarthquakeLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<earthquakes> loadInBackground() {

        URL url = null;
        try {
            url = new URL(urlstring);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
        }


        String JSONResponse = "";

        try {
            JSONResponse = makeHTTPRequest(url);
        } catch (Exception e) {
            Log.e("EarthQuakeACTIVITY", "********     JSONResponse CATCH      ********");

        }
        if (JSONResponse != null) {
            EarthQuakeQuery earthQuakeQuery = new EarthQuakeQuery(JSONResponse);
            ArrayList<earthquakes> earthquakes = earthQuakeQuery.extractEarthquakes();
            return earthquakes;
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    public static String makeHTTPRequest(URL url) {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);

            urlConnection.connect();
            Log.e("EarthQuakeActivity", "URLRESPONSE CODE");
            if (urlConnection.getResponseCode() == 200) {
                Log.e("EarthQuakeActivity", "URLRESPONSE CODE = 200");
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else
                Log.e("EarthQuakeActivity", "URLRESPONSE CODE != 200");
            return jsonResponse;
        } catch (Exception e) {
            Log.e("EarthQuakeQuery.class", "********     MAKEHTTPREQUEST      ********", e.getCause());

        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {

                }
            }
        }
        return null;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
            Log.e("EarthQuakeActivity", output.toString());

            return output.toString();
        }
        return null;
    }
}
