package com.baibeiyun.eazyfair.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/26.
 */
public class YuLiuXunJiaAdapter extends BaseAdapter {
    private LayoutInflater ml;
    private ArrayList<Map<String, Object>> datas3;
    private Context c;

    public YuLiuXunJiaAdapter(ArrayList<Map<String, Object>> datas3, Context c) {
        this.datas3 = datas3;
        this.c = c;
        ml = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return datas3 == null ? 0 : datas3.size();
    }

    @Override
    public Object getItem(int i) {
        return datas3 == null ? null : datas3.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {
        View view;
        ViewHolder holder;
        view = ml.inflate(R.layout.listview_item_yuliuxunjia, null);
        if (convertview == null) {
            holder = new ViewHolder();
            holder.custom_name_tv = (TextView) view.findViewById(R.id.custom_name_tv);
            holder.xunjia_content_tv = (TextView) view.findViewById(R.id.xunjia_content_tv);
            view.setTag(holder);

        } else {
            view = convertview;
            holder = (ViewHolder) view.getTag();
        }
        Map<String, Object> map = datas3.get(i);
        holder.custom_name_tv.setText(map.get("custom_name_tv").toString());
        holder.xunjia_content_tv.setText(map.get("xunjia_content_tv").toString());
        return view;
    }

    class ViewHolder {
        TextView custom_name_tv;
        TextView xunjia_content_tv;
    }
}
