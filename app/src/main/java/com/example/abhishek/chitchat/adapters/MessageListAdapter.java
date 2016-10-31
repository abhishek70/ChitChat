package com.example.abhishek.chitchat.adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishek.chitchat.R;
import com.example.abhishek.chitchat.models.Message;
import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abhishekdesai on 10/31/16.
 */

public class MessageListAdapter extends ArrayAdapter<Message> {

    private String mUserId;

    public MessageListAdapter(Context context, String userId, List<Message> messages) {
        super(context, 0, messages);
        this.mUserId = userId;
    }

    @Nullable
    @Override
    public Message getItem(int position) {
        return super.getItem(super.getCount() - position - 1);
    }

    static class ViewHolder {

        @BindView(R.id.ivProfileOther)
        ImageView ivProfileOther;

        @BindView(R.id.ivProfileMe)
        ImageView ivProfileMe;

        @BindView(R.id.tvBody)
        TextView tvBody;

        public ViewHolder(View view) {

            ButterKnife.bind(this, view);
        }
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Message message = getItem(position);


        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder = null; // view lookup cache stored in tag

        if(convertView == null) {

            convertView = LayoutInflater.from(getContext())
                                        .inflate(R.layout.item_message, parent, false);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        final boolean isMe = message.getUserId() != null && message.getUserId().equals(mUserId);

        if(isMe) {

            viewHolder.ivProfileMe.setVisibility(View.VISIBLE);
            viewHolder.ivProfileOther.setVisibility(View.GONE);
            viewHolder.tvBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

        } else {

            viewHolder.ivProfileMe.setVisibility(View.GONE);
            viewHolder.ivProfileOther.setVisibility(View.VISIBLE);
            viewHolder.tvBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        }

        final ImageView profileView = isMe ? viewHolder.ivProfileMe : viewHolder.ivProfileOther;
        Picasso.with(getContext())
                .load(getProfileUrl(message.getUserId()))
                .into(profileView);

        viewHolder.tvBody.setText(message.getBody());

        return convertView;

    }


    // Create a gravatar image based on the hash value obtained from userId
    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }

}
