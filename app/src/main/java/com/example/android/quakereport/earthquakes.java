package com.example.android.quakereport;


/**
 * Created by dell on 3/10/2018.
 */

public class earthquakes {
    private String location;
    private float magnitude;
    private long date;

    public earthquakes(String loc, float mag, long d){
        this.location=loc;
        this.magnitude=mag;
        this.date=d;
    }

    public String getLocation() {
        return location;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public long getDate() {
        return date;
    }
}
