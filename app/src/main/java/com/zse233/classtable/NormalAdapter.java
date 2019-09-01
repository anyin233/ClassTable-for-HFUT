package com.zse233.classtable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NormalAdapter extends BaseAdapter {
    private List<MyClassTable> schedules;
    private Context context;
    private LayoutInflater inflater;


    public NormalAdapter(Context context, List<MyClassTable> schedules) {
        this.context = context;
        this.schedules = schedules;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return schedules.size();
    }

    @Override
    public Object getItem(int i) {
        return schedules.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View mView = null;
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_layout, null);
            holder.className = convertView.findViewById(R.id.id_name);
            holder.classRoom = convertView.findViewById(R.id.id_room);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MyClassTable schedule = (MyClassTable) getItem(i);
        holder.className.setText(schedule.getName());
        holder.classRoom.setText(schedule.getRoom());
        return convertView;
    }


    class ViewHolder {
        TextView className;
        TextView classRoom;
    }
}
