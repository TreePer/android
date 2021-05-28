package com.example.finaltestAdmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class workAdapter extends BaseAdapter {
    ArrayList <workList>listitem = new ArrayList<workList>();
    public void addItem(workList item) {
        this.listitem.add(item);
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
            convertView = inflater.inflate(R.layout.worklist, parent, false);
        }

        TextView name = convertView.findViewById(R.id.txtUserName);
        TextView work = convertView.findViewById(R.id.txtUserWork);

        workList item = listitem.get(position);

        name.setText(item.getName());
        work.setText(item.getResult());

        return convertView;
    }

}
