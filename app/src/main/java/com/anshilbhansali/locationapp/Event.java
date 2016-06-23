package com.anshilbhansali.locationapp;

import android.util.Log;

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
    private String ppl_going;

    public Event(String name, String description, JSONObject place, String start_time, String end_time, String id, String status)
    {
        this.name = name;
        this.description = description;
        this.place = place;
        this.start_time = start_time;
        this.end_time = end_time;
        this.id = id;
        this.status = status;

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
        Log.d("FACEBOOK ", "EVENT PPL GOING: " + ppl_going);
    }
}
