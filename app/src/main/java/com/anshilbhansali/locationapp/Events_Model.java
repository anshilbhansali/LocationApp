package com.anshilbhansali.locationapp;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by anshilbhansali on 6/22/16.
 */
public class Events_Model {
     private ArrayList<Event> User_events;

    public Events_Model()
    {
        //INIT
        User_events = new ArrayList<>();
    }

    void add_event(Event event)
    {
        User_events.add(event);
    }

    void print_all()
    {
        for(int i=0;i<User_events.size();i++)
        {
            Log.d("EVENT " , Integer.toString(i));

            User_events.get(i).print_event();
        }
    }

    int getSize()
    {
        return User_events.size();
    }

    Event get_event(int i)
    {
        return User_events.get(i);
    }


}
