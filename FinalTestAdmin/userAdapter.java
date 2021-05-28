package com.example.finaltestAdmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class userAdapter extends BaseAdapter {
    ArrayList<userList> listitem = new ArrayList<userList>();

    public void addItem(userList item) {
        listitem.add(item);
    }

    @Override
    public int getCount() {
        return listitem.size();
    }

    @Override
    public Object getItem(int position) {
        return listitem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.userlist, parent, false);
        }

        TextView name = convertView.findViewById(R.id.userName);
        TextView work = convertView.findViewById(R.id.userId);

        userList item = listitem.get(position);

        name.setText(item.getName());
        work.setText(item.getId());

        return convertView;


    }
}
