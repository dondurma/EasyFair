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

import cn.forward.androids.utils.LogUtil;

/**
 * cretae by RuanWei at 2017/3/2
 */

public class XunJiaRecordAdapter extends BaseAdapter {
    private LayoutInflater ml;
    private ArrayList<Map<String, Object>> datas1;
    private Context context;

    public XunJiaRecordAdapter(ArrayList<Map<String, Object>> datas, Context context) {
        this.datas1 = datas;
        this.context = context;
        ml = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas1.size();
    }

    @Override
    public Object getItem(int i) {
        return datas1.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        View view;
        if (convertView == null) {
            view = ml.inflate(R.layout.item_xjrecord_layout, null);
            holder = new ViewHolder();
            holder.name_tv = (TextView) view.findViewById(R.id.name_tv);
            holder.money_fh_tv = (TextView) view.findViewById(R.id.money_fh_tv);
            holder.price_tv = (TextView) view.findViewById(R.id.price_tv);
            holder.time_tv = (TextView) view.findViewById(R.id.time_tv);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        Map<String, Object> map = datas1.get(i);
        holder.name_tv.setText(map.get("name_tv").toString());
        holder.price_tv.setText(map.get("price_tv").toString());
        holder.time_tv.setText(map.get("time_tv").toString());
        if (map.get("currency_variety").toString().equals("人民币") || map.get("currency_variety").toString().equals("RMB")) {
            holder.money_fh_tv.setText("￥");
        } else if (map.get("currency_variety").toString().equals("美元") || map.get("currency_variety").toString().equals("dollar")) {
            holder.money_fh_tv.setText("$");
        } else if (map.get("currency_variety").toString().equals("欧元") || map.get("currency_variety").toString().equals("Euro")) {
            holder.money_fh_tv.setText("€");
        } else if (map.get("currency_variety").toString().equals("英镑") || map.get("currency_variety").toString().equals("Pound")) {
            holder.money_fh_tv.setText("￡");
        } else if (map.get("currency_variety").toString().equals("日元") || map.get("currency_variety").toString().equals("Yen")) {
            holder.money_fh_tv.setText("JP￥");
        }
        return view;
    }

    public class ViewHolder {
        TextView name_tv;
        TextView money_fh_tv;
        TextView price_tv;
        TextView time_tv;
    }
}
