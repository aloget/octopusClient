package com.example.anna.octopuschat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.anna.octopuschat.R;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by anna on 06/12/15.
 */

public class MessageAdapter extends ArrayAdapter<MessageAdapter.Item> {
    private LayoutInflater mInflater;

    public MessageAdapter(Context context, List<Item> list) {
        super(context, 0, list);
        mInflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
        public TextView sender;
        public TextView message;
        public TextView date;
    }

    public static class Item {
        public int mId;
        public String mUsername;
        public String mMessage;
        public Date mDate;

        public Item(int id, String username, String message, long dispatchTimestamp) {
            mId = id;
            mUsername = username;
            mMessage = message;

            Timestamp timestamp = new Timestamp(dispatchTimestamp);
            mDate = new Date(timestamp.getTime());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            rowView = mInflater.inflate(R.layout.messages_list_item, parent, false);
            holder = new ViewHolder();

            holder.sender = (TextView)rowView.findViewById(R.id.textViewSender);
            holder.message = (TextView)rowView.findViewById(R.id.textViewMessage);
            holder.date = (TextView)rowView.findViewById(R.id.textViewDate);

            rowView.setTag(holder);
        } else {
            holder = (ViewHolder)rowView.getTag();
        }

        Item item = getItem(position);

        holder.sender.setText(item.mUsername);
        holder.message.setText(item.mMessage);
        holder.date.setText(item.mDate.toString());
        return rowView;
    }
}
