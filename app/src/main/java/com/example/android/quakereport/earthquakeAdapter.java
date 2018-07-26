package com.example.android.quakereport;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.SimpleTimeZone;

/**
 * Created by dell on 3/10/2018.
 */

public class earthquakeAdapter extends ArrayAdapter {

    earthquakeAdapter(Activity context, ArrayList<earthquakes> Earthquake){
        super(context,0,Earthquake);
    }

    @Override
    public View getView(int position, @Nullable View convertView,@NonNull ViewGroup parent){
        View listItemView=convertView;
        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        earthquakes currentQuake = (earthquakes) getItem(position);

        TextView loc1 = (TextView) listItemView.findViewById(R.id.location1);
        TextView loc2 = (TextView) listItemView.findViewById(R.id.location2);
        String location = currentQuake.getLocation();
        String[] str = location.split("of ",2);
        String n=str[0].concat("of");
        loc1.setText(n);
        loc2.setText(str[1]);


        Log.e("EarthQuakeadapter", "********           ********");



        TextView mag = (TextView) listItemView.findViewById(R.id.magnitude);
        Float f= currentQuake.getMagnitude();
        DecimalFormat df= new DecimalFormat("0.0");
        String s=df.format(f);
        mag.setText(s);
        TextView date = (TextView) listItemView.findViewById(R.id.date);
        long l=currentQuake.getDate();
        Date d = new Date(l);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
        String dateToDisplay = dateFormatter.format(d);
        date.setText(dateToDisplay);
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        String timeToDisplay = timeFormatter.format(d);
        TextView time = (TextView) listItemView.findViewById(R.id.time);
        time.setText(timeToDisplay);



        return listItemView;

    }


}
