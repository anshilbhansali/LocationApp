package com.anshilbhansali.locationapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by anshilbhansali on 6/21/16.
 */
public class FB_Activity extends Activity {
    private TextView info;
    private LoginButton loginButton;

    private CallbackManager callbackManager;

    private LoginManager loginManager; //use to log out

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.fb_login);
        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_events");

        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        if(loggedIn)
        {
            Intent intent = new Intent(FB_Activity.this, MapsActivity.class);
            startActivity(intent);
        }

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                /*info.setText(
                        "User ID: " + loginResult.getAccessToken().getUserId() + "\n" +
                                "Auth Token: " + loginResult.getAccessToken().getToken()
                );*/
                Intent intent = new Intent(FB_Activity.this, MapsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                info.setText("Login attempt failed.");

            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}