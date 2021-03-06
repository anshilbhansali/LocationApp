package com.anshilbhansali.locationapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.IntegerRes;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.FacebookSdk;

import com.facebook.login.LoginManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    //for the drawer
    private String[] mDrawerTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    TextView user_name, user_email;
    CircleImageView user_picture;
    JSONObject response, profile_pic_data, profile_pic_url;

    //tab buttons
    Button map_button, feed_button;

    //feed view
    ListView feed_view;

    //seek bar - to control # of days
    private SeekBar seekBar;
    private TextView current_day, future_day;

    //model
    Events_Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Feedview
        feed_view = (ListView) findViewById(R.id.listView);

        //HEADER
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Location App");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //drawer
        mDrawerTitles = getResources().getStringArray(R.array.drawer_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mActivityTitle = getTitle().toString();

        //to put user profile pic into nav header
        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("jsondata");
        setNavigationHeader();    // call setNavigationHeader Method.
        setUserProfile(jsondata);  // call setUserProfile Method.

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mDrawerTitles));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        setupDrawer();

        //buttons
        map_button = (Button) findViewById(R.id.map_button_id);
        feed_button = (Button) findViewById(R.id.feed_button_id);

        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapFragment.getView().setVisibility(View.VISIBLE);
                feed_view.setVisibility(View.GONE);
            }
        });

        feed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapFragment.getView().setVisibility(View.GONE);
                feed_view.setVisibility(View.VISIBLE);
            }
        });

        //seekbar
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        current_day = (TextView) findViewById(R.id.curr_day);
        future_day = (TextView) findViewById(R.id.fut_day);

        seekBar.setProgress(10);

        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) future_day.getLayoutParams();
        params.leftMargin = (int) (seekBar.getProgress()*4.5);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress <= 10)
                    seekBar.setProgress(10);

                Log.v("progress value", Integer.toString(progress));
                params.leftMargin = (int) (progress*4.5);
                Log.v("margin", Integer.toString(params.leftMargin));
                future_day.setLayoutParams(params);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public void setNavigationHeader(){


        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_home, null);
        mDrawerList.addHeaderView(header);

        user_name = (TextView) header.findViewById(R.id.username);
        user_picture = (CircleImageView) header.findViewById(R.id.profile_pic);
        user_email = (TextView) header.findViewById(R.id.email);
    }

    public  void  setUserProfile(String jsondata){

        try {
            response = new JSONObject(jsondata);
            Log.d("RESPONSE#### ", "RESPONSE"+response);
            if(response.has("email"))
                user_email.setText(response.get("email").toString());
            if(response.has("name"))
                user_name.setText(response.get("name").toString());
            if(response.has("picture"))
            {
                profile_pic_data = new JSONObject(response.get("picture").toString());
                profile_pic_url = new JSONObject(profile_pic_data.getString("data"));

                Picasso.with(this).load(profile_pic_url.getString("url"))
                        .into(user_picture);
            }



        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(MapsActivity.this, "We need to access your location", Toast.LENGTH_SHORT).show();
        }

        //will retrieve events from FB, and add to Map
        FB_Events fb_events = new FB_Events(mMap, this);


        LatLng champaign = new LatLng(40.11, -88.24);
        mMap.addMarker(new MarkerOptions().position(champaign).title("UIUC"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(champaign));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(champaign, 15));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buttons, menu);
        return true;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            // Highlight the selected item, update the title, and close the drawer
            TextView v;
            String s = " ";

            if(view instanceof TextView)
            {
                v = (TextView) view;
                s = (String) v.getText();
            }

            Log.d("DRAWER CLICK###", s);

            if(s.equals("Log Out"))
            {
                //Log out
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(MapsActivity.this, FB_Activity.class);
                startActivity(intent);
            }

            if(s.equals("Preferences"))
                Toast.makeText(MapsActivity.this, "Work in progress", Toast.LENGTH_SHORT).show();

            //Toast.makeText(MapsActivity.this, Long.toString(id), Toast.LENGTH_SHORT).show();
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }


    //Refresh button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.refresh_button:
                //refresh map
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }
}
