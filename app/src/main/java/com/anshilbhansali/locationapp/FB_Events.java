package com.anshilbhansali.locationapp;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anshilbhansali on 6/22/16.
 */

public class FB_Events {

    String user_id;
    Events_Model model;
    private GoogleMap mMap;
    Activity activity;

    public FB_Events(GoogleMap mMap, Activity activity)
    {
        this.mMap = mMap;
        this.activity = activity;

        if(AccessToken.getCurrentAccessToken() != null)
        {
            user_id = AccessToken.getCurrentAccessToken().getUserId();
        }

        Log.d("GET USER ID", "USERID: " + user_id);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+user_id+"/events",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        handle_result(response);
                    }
                }
        ).executeAsync();


    }

    void handle_result(GraphResponse response)
    {
        Log.d("Facebook-events Request", "Response: " + response);

        //get JSON object
        JSONObject jsonObject = response.getJSONObject();

        Log.d("Facebook-events Request", "JSONOBJ: " + jsonObject);

        parse_events(jsonObject);

    }

    void parse_events(JSONObject jsonObject)
    {
        model = new Events_Model();

        JSONArray data_array = null;

        try {

            //get list of events
            data_array = jsonObject.getJSONArray("data");

            for(int i = 0; i < data_array.length(); i++)
            {
                //get each event
                JSONObject event = data_array.getJSONObject(i);
                Log.d("Facebook-events ", "EVENT: " + event);

                String id = null, start_time = null, end_time = null, status = null, desc = null, name = null;
                JSONObject place = null;

                //PARSE

                if(event.has("id"))
                    id = event.getString("id");

                if(event.has("start_time"))
                    start_time = event.getString("start_time");

                if(event.has("rsvp_status"))
                    status = event.getString("rsvp_status");

                if(event.has("place"))
                    place = event.getJSONObject("place");

                if(event.has("end_time"))
                    end_time = event.getString("end_time");

                if(event.has("description"))
                    desc = event.getString("description");

                if(event.has("name"))
                    name = event.getString("name");



                //SET null strings to "#" to prevent exceptions
                desc = check_null(desc);
                name = check_null(name);
                id = check_null(id);
                start_time = check_null(start_time);
                end_time = check_null(end_time);
                status = check_null(status);

                Log.d("Facebook-events ", "EVENT NAME: " + name);
                Log.d("Facebook-events ", "EVENT DESC: " + desc);
                Log.d("Facebook-events ", "EVENT PLACE: " + place);
                Log.d("Facebook-events ", "EVENT ID: " + id);
                Log.d("Facebook-events ", "EVENT START: " + start_time);
                Log.d("Facebook-events ", "EVENT STATUS: " + status);
                Log.d("Facebook-events ", "EVENT END: " + end_time);


                Event e = new Event(name, desc, place, start_time, end_time, id, status);

                model.add_event(e);

            }


            model.print_all();
            add_events_to_map();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String check_null(String s)
    {
        if (s == null)
            s = "#";

        return s;
    }

    Events_Model getModel()
    {
        return model;
    }

    void add_events_to_map()
    {
        if(model.getSize() == 0)
            Toast.makeText(activity, "Could not retrieve your events", Toast.LENGTH_SHORT).show();

        //add events on map from model
        for(int i=0; i< model.getSize() ; i++)
        {
            Event e = model.get_event(i);
            if(e.getLat() != 0 && e.getLong() != 0)
            {
                LatLng location = new LatLng(e.getLat(), e.getLong());
                String name = e.getName();
                mMap.addMarker(new MarkerOptions().position(location).title(name));
            }
        }
    }


}
