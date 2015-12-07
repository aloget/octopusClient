package com.example.anna.octopuschat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.anna.octopuschat.R;
import com.example.anna.octopuschat.dbTables.User;

import java.util.List;

/**
 * Created by anna on 06/12/15.
 */

public class UserAdapter extends ArrayAdapter<User> {
    private LayoutInflater mInflater;

    public UserAdapter(Context context, List<User> list) {
        super(context, 0, list);
        mInflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
        public TextView textViewUsername;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            rowView = mInflater.inflate(R.layout.users_list_item, parent, false);
            holder = new ViewHolder();

            holder.textViewUsername = (TextView)rowView.findViewById(R.id.textViewUsername);

            rowView.setTag(holder);
        } else {
            holder = (ViewHolder)rowView.getTag();
        }

        User item = getItem(position);
        holder.textViewUsername.setText(item.username);
        return rowView;
    }

}
