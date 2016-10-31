package com.example.abhishek.chitchat.models;

import com.example.abhishek.chitchat.utils.Constant;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by abhishekdesai on 10/31/16.
 */

@ParseClassName("Message")
public class Message extends ParseObject {

    public String getUserId() {
        return getString(Constant.USER_ID_KEY);
    }

    public String getBody() {
        return getString(Constant.BODY_KEY);
    }

    public void setUserId(String userId) {
        put(Constant.USER_ID_KEY, userId);
    }

    public void setBody(String body) {
        put(Constant.BODY_KEY, body);
    }



}
