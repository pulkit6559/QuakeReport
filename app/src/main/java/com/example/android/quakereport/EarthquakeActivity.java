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

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<earthquakes>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    public static final String urlstring = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=15";
    ListView earthquakeListView;
    TextView mEmptyStateTextView;
    earthquakeAdapter Earthquakeadapter;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        earthquakeListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);
        getLoaderManager().initLoader(0, null, this);
        //ArrayList<earthquakes> Earthquake =.extractEarthquakes();


    }

    @Override
    public Loader<ArrayList<earthquakes>> onCreateLoader(int i, Bundle bundle) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        progressBar = (ProgressBar) findViewById(R.id.indeterminateBar);
        if(networkInfo!=null) {

            progressBar.setIndeterminate(true);

            return new EarthquakeLoader(this);
        }   else {
            progressBar.setVisibility(View.INVISIBLE);
            mEmptyStateTextView.setText(R.string.no_internet);
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<earthquakes>> loader, ArrayList<earthquakes> earthquakes) {
        progressBar.setVisibility(View.INVISIBLE);

        if (earthquakes != null && earthquakes.size() > 0) {
            updateUI(earthquakes);
        } else {
            Log.e("EarthQuakeACTIVITY", "********     ON POST EXECUTE ELSE      ********");
            mEmptyStateTextView.setText(R.string.no_earthquakes);

            // Clear the adapter of previous earthquake data
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<earthquakes>> loader) {

    }



    public void updateUI(ArrayList<earthquakes> earthquakes) {


        earthquakeListView = (ListView) findViewById(R.id.list);

        Earthquakeadapter = new earthquakeAdapter(
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

}
