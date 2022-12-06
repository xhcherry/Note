package com.example.fragmentnote.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fragmentnote.R;
import com.example.fragmentnote.vo.note;

import java.util.List;

public class noteadapter1 extends BaseAdapter {
    List<note> list1;
    private final LayoutInflater linf;

    public noteadapter1(Context context, List<note> listtm) {
        this.list1 = listtm;
        linf = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list1.size();
    }

    @Override
    public Object getItem(int position) {
        return list1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (view == null) {
            view = linf.inflate(R.layout.station_list_item, null);
            viewHolder.title = view.findViewById(R.id.title);
            viewHolder.content = view.findViewById(R.id.content);
            viewHolder.read_num = view.findViewById(R.id.read_num);
            viewHolder.good_num = view.findViewById(R.id.good_num);
            viewHolder.time = view.findViewById(R.id.time);
            //viewHolder.picture = view.findViewById(R.id.picture);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        note listbean = list1.get(position);
        viewHolder.title.setText(listbean.getTitle());
        viewHolder.content.setText(listbean.getContent());
        viewHolder.read_num.setText(listbean.getReadNum());
        viewHolder.good_num.setText(listbean.getGoodNum());
        viewHolder.time.setText(listbean.getTime());
        //viewHolder.picture.setImageResource(listbean.getPicture());
        return view;
    }

    public static final class ViewHolder {
        public TextView title, content, read_num, good_num, time;
        //public ImageView picture;
    }
}