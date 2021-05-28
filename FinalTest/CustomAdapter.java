package com.example.finaltest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    ArrayList<workItem> item = new ArrayList<workItem>();

    public void addItem(workItem item) {
        this.item.add(item);
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        workItem workItem = item.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem, parent, false);
        }

        TextView dayTxt = convertView.findViewById(R.id.textDay);
        TextView workTxt = convertView.findViewById(R.id.textWork);

        dayTxt.setText(workItem.getDay());
        workTxt.setText(workItem.getWorkType());

        return convertView;
    }
}
