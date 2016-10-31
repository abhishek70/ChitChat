package com.example.abhishek.chitchat;

import android.app.Application;

import com.example.abhishek.chitchat.models.Message;
import com.example.abhishek.chitchat.utils.Config;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.interceptors.ParseLogInterceptor;

/**
 * Created by abhishek on 10/25/16.
 */

public class ChitChatApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models here
        ParseObject.registerSubclass(Message.class);

        // set applicationId and server based on the values in the Heroku settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(Config.APPLICATION_ID) // should correspond to APP_ID env variable
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server(Config.APPLICATION_URL).build());

    }
}
