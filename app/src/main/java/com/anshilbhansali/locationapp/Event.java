package com.anshilbhansali.locationapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anshilbhansali on 6/22/16.
 */
public class Event {
    private String name;
    private String description;
    private JSONObject place;
    private String start_time;
    private String end_time;
    private String id;
    private String status;

    private int attending, interested;

    private double Lat, Long;


    public Event(String name, String description, JSONObject place, String start_time, String end_time, String id, String status, int attending, int interested)
    {

        //init
        this.Lat = 0;
        this.Long = 0;
        this.attending = 0;
        this.interested = 0;


        this.name = name;
        this.description = description;
        this.place = place;
        this.start_time = start_time;
        this.end_time = end_time;
        this.id = id;
        this.status = status;
        this.attending = attending;
        this.interested = interested;



        //extract latitude and longitude from place
        if(place != null)
        {
            if(place.has("location"))
            {
                try {
                    JSONObject location = place.getJSONObject("location");
                    if(location.has("longitude"))
                    {
                        Long = location.getDouble("longitude");
                    }
                    if(location.has("latitude"))
                    {
                        Lat = location.getDouble("latitude");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    void print_event()
    {
        Log.d("NEW", "####################");
        Log.d("FACEBOOK ", "EVENT NAME: " + name);
        Log.d("FACEBOOK ", "EVENT DESC: " + description);
        Log.d("FACEBOOK ", "EVENT PLACE: " + place);
        Log.d("FACEBOOK ", "EVENT ID: " + id);
        Log.d("FACEBOOK ", "EVENT START: " + start_time);
        Log.d("FACEBOOK ", "EVENT STATUS: " + status);
        Log.d("FACEBOOK ", "EVENT END: " + end_time);
        Log.d("Facebook-events ", "EVENT ATTENDING: " + Integer.toString(attending));
        Log.d("Facebook-events ", "EVENT INTERESTED: " + Integer.toString(interested));

    }

    public double getLat() {
        return Lat;
    }

    public double getLong() {
        return Long;
    }

    public String getName()
    {
        return name;
    }
}
