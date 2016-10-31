package com.example.abhishek.chitchat.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhishek.chitchat.R;
import com.example.abhishek.chitchat.adapters.MessageListAdapter;
import com.example.abhishek.chitchat.models.Message;
import com.example.abhishek.chitchat.utils.Constant;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import android.os.Handler;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.abhishek.chitchat.utils.Constant.POLL_INTERVAL;

public class ChitChatActivity extends AppCompatActivity {


    private static final String LOG_TAG = ChitChatActivity.class.getSimpleName();

    @BindView(R.id.etMessage)
    EditText etMessage;

    @BindView(R.id.btSend)
    Button btSend;

    @BindView(R.id.lvChat)
    ListView lvChat;

    private ArrayList<Message> mMessageList;
    private MessageListAdapter messageListAdapter;

    // Keep track of initial load to scroll to the bottom of the ListView
    private boolean mFirstLoad;


    Handler mHandler = new Handler();

    private Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {

            refreshMessageList();
            mHandler.postDelayed(this, POLL_INTERVAL);

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitchat);

        ButterKnife.bind(this);


        // Check session for the current user
        // else start with the new user
        if(ParseUser.getCurrentUser() != null) {

            startWithCurrentUser();

        } else {

            login();

        }

        mHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);

    }


    /**
     * This method is called if the user session is still available
     */
    private void startWithCurrentUser() {

        mMessageList = new ArrayList<>();
        lvChat.setTranscriptMode(1);
        mFirstLoad = true;
        final String userId = ParseUser.getCurrentUser().getObjectId();
        messageListAdapter = new MessageListAdapter(ChitChatActivity.this, userId, mMessageList);
        lvChat.setAdapter(messageListAdapter);
    }


    /**
     * TODO : Add login functionality instead of anonymous login
     * Anonymous Login
     */
    private void login() {

        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if( e != null) {

                    Log.d(LOG_TAG + "Login Failed", e.getMessage() );

                } else {

                    startWithCurrentUser();

                }
            }
        });

    }


    /**
     * This method is called when the user click on the Send Button
     */
    @OnClick(R.id.btSend)
    public void sendMessage() {

        // Hiding keyboard after message the user click on the send button
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        // Fetching posted message
        String data = etMessage.getText().toString();

        // Parse-backed model class
        Message message = new Message();
        message.setUserId(ParseUser.getCurrentUser().getObjectId());
        message.setBody(data);

        // Sending data to Parse
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {

                    Toast.makeText(ChitChatActivity.this, "Message Send", Toast.LENGTH_SHORT).show();

                    refreshMessageList();

                } else {

                    Log.d(LOG_TAG + " Failed to send message", e.getMessage());

                }

            }
        });


        etMessage.setText(null);

    }

    /**
     * This method will pull the latest messages from the Parse.
     */
    private void refreshMessageList() {

        // Create Parse Query
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);

        // Limit
        query.setLimit(Constant.MAX_CHAT_MESSAGES_TO_SHOW);

        // Sort Order
        query.orderByDescending("createdAt");


        // Executing in background to fetch the messages from parse asynchronously
        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {

                if( e == null) {

                    mMessageList.clear();
                    mMessageList.addAll(messages);
                    messageListAdapter.notifyDataSetChanged();

                    if(mFirstLoad) {
                        lvChat.setSelection(messageListAdapter.getCount() - 1);
                        mFirstLoad = false;
                    }

                } else {

                    Log.d(LOG_TAG + " Data Fetch Problem", e.getMessage());

                }

            }
        });
    }

}
