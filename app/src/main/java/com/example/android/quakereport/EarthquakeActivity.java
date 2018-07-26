/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    public static final String urlstring = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=15";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        EarthquakeAsynkTask asynkTask = new EarthquakeAsynkTask();
        asynkTask.execute(urlstring);

        //ArrayList<earthquakes> Earthquake =.extractEarthquakes();


    }
    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    public class EarthquakeAsynkTask extends AsyncTask<String,Void,ArrayList<earthquakes>>{
        @Override
        protected ArrayList<earthquakes> doInBackground(String... strings) {
            URL url = createUrl(urlstring);
            String JSONResponse = "";
            try{
                JSONResponse = makeHTTPRequest(url);
            }catch (Exception e){
                Log.e("EarthQuakeACTIVITY", "********     JSONResponse CATCH      ********");

            }
            if(JSONResponse!=null){
                EarthQuakeQuery earthQuakeQuery = new EarthQuakeQuery(JSONResponse);
                ArrayList<earthquakes> earthquakes = earthQuakeQuery.extractEarthquakes();
                return earthquakes;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<earthquakes> earthquakes){
            if(earthquakes!=null&&earthquakes.size()>0){
                updateUI(earthquakes);
            }
            else
                Log.e("EarthQuakeACTIVITY", "********     ON POST EXECUTE ELSE      ********");

        }

    }

    public void updateUI(ArrayList<earthquakes> earthquakes){


        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        earthquakeAdapter Earthquakeadapter = new earthquakeAdapter(
                this, earthquakes);

        earthquakeListView.setAdapter(Earthquakeadapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String url = "https://www.usgs.gov/";
                intent.setData(Uri.parse(url));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });
    }

    public String makeHTTPRequest(URL url){
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);

            urlConnection.connect();
            Log.e("EarthQuakeActivity", "URLRESPONSE CODE");
            if(urlConnection.getResponseCode()==200){
                Log.e("EarthQuakeActivity", "URLRESPONSE CODE = 200");
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else
                Log.e("EarthQuakeActivity", "URLRESPONSE CODE != 200");
            return jsonResponse;
        }   catch (Exception e){
            Log.e("EarthQuakeQuery.class", "********     MAKEHTTPREQUEST      ********",e.getCause());

        }
        finally {
            if(urlConnection!=null)
                urlConnection.disconnect();
            if(inputStream!=null){
                try{
                    inputStream.close();
                }catch (IOException e){

                }
            }
        }
        return null;

    }

    private String readFromStream (InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = bufferedReader.readLine();
            while(line!=null){
                output.append(line);
                line = bufferedReader.readLine();
            }
            Log.e("EarthQuakeActivity", output.toString());

            return output.toString();
        }
        return null;
    }

}
