package com.example.anna.octopuschat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.anna.octopuschat.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by anna on 06/12/15.
 */

public class MessageAdapter extends ArrayAdapter<MessageAdapter.Item> {
    private final LayoutInflater mInflater;

    public MessageAdapter(Context context, List<Item> list) {
        super(context, 0, list);
        mInflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
        public TextView tv_sender;
        public TextView tv_message;
        public TextView tv_date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View rowView = convertView;

        Item item = getItem(position);

        if (rowView == null) {
            rowView = mInflater.inflate(item.mInbox ? R.layout.messages_list_item_l : R.layout.messages_list_item_r, parent, false);
            holder = new ViewHolder();

            holder.tv_sender = (TextView) rowView.findViewById(R.id.tv_sender);
            holder.tv_message = (TextView) rowView.findViewById(R.id.tv_message);
            holder.tv_date = (TextView) rowView.findViewById(R.id.tv_date);

            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.tv_sender.setText(item.mUsername);
        holder.tv_message.setText(item.mMessage);
        holder.tv_date.setText(item.mDate);
        return rowView;
    }

    public static class Item {
        private int mId;
        private String mUsername;
        private String mMessage;
        private String mDate;
        private boolean mInbox;

        public Item(int id, String username, String message, long dispatchTimestamp, boolean inbox) {
            mId = id;
            mUsername = username;
            mMessage = message;
            mInbox = inbox;

            Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
            calendar.setTimeInMillis(dispatchTimestamp * 1000);
            mDate = String.format("%d:%d:%d %d.%d.%d",
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        }
    }
}