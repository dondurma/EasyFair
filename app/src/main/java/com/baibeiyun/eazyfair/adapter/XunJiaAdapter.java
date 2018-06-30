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
public class XunJiaAdapter extends BaseAdapter {
    private LayoutInflater ml;
    private ArrayList<Map<String, Object>> datas2;
    private Context c;

    public XunJiaAdapter(ArrayList<Map<String, Object>> datas, Context c) {
        this.datas2 = datas;
        this.c = c;
        ml = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return datas2 == null ? 0 : datas2.size();
    }

    @Override
    public Object getItem(int i) {
        return datas2 == null ? null : datas2.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {
        ViewHolder holder;
        View view;
        if (convertview == null) {
            view = ml.inflate(R.layout.listview_item_liuyangxunjia, null);
            holder = new ViewHolder();
            holder.goods_name_tv = (TextView) view.findViewById(R.id.goods_name_tv);
            holder.goods_price_tv = (TextView) view.findViewById(R.id.goods_price_tv);
            holder.currency_type_tv = (TextView) view.findViewById(R.id.currency_type_tv);
            holder.goods_material_tv = (TextView) view.findViewById(R.id.goods_material_tv);
            holder.goods_color_tv = (TextView) view.findViewById(R.id.goods_color_tv);
            holder.moq_tv = (TextView) view.findViewById(R.id.moq_tv);
            holder.goods_unit_tv = (TextView) view.findViewById(R.id.goods_unit_tv);
            holder.quote_time_tv = (TextView) view.findViewById(R.id.quote_time_tv);

            view.setTag(holder);
        } else {
            view = convertview;
            holder = (ViewHolder) view.getTag();
        }


        Map<String, Object> map = datas2.get(i);
        holder.goods_name_tv.setText(map.get("goods_name_tv").toString());
        holder.goods_price_tv.setText(map.get("goods_price_tv").toString());
        holder.currency_type_tv.setText(map.get("currency_type_tv").toString());
        holder.goods_material_tv.setText(map.get("goods_material_tv").toString());
        holder.goods_color_tv.setText(map.get("goods_color_tv").toString());
        holder.moq_tv.setText(map.get("moq_tv").toString());
        holder.goods_unit_tv.setText(map.get("goods_unit_tv").toString());
        if (map.get("quote_time_tv")!=null) {
            holder.quote_time_tv.setText(map.get("quote_time_tv").toString());
        }else{
            holder.quote_time_tv.setText("");
        }
        if (map.get("currency_type_tv").toString().equals("人民币") || map.get("currency_type_tv").toString().equals("RMB")) {
            holder.currency_type_tv.setText("￥");
        } else if (map.get("currency_type_tv").toString().equals("美元") || map.get("currency_type_tv").toString().equals("dollar")) {
            holder.currency_type_tv.setText("$");
        } else if (map.get("currency_type_tv").toString().equals("欧元") || map.get("currency_type_tv").toString().equals("Euro")) {
            holder.currency_type_tv.setText("€");
        } else if (map.get("currency_type_tv").toString().equals("英镑") || map.get("currency_type_tv").toString().equals("Pound")) {
            holder.currency_type_tv.setText("￡");
        } else if (map.get("currency_type_tv").toString().equals("日元") || map.get("currency_type_tv").toString().equals("Yen")) {
            holder.currency_type_tv.setText("JP￥");
        }

        return view;
    }


    class ViewHolder {
        TextView goods_name_tv;
        TextView goods_price_tv;
        TextView currency_type_tv;
        TextView goods_material_tv;
        TextView goods_color_tv;
        TextView moq_tv;
        TextView goods_unit_tv;
        TextView quote_time_tv;
    }


}
