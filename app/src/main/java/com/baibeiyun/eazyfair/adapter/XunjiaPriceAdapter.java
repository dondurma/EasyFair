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
 * Created by Administrator on 2016/10/6.
 */
public class XunjiaPriceAdapter extends BaseAdapter {
    private LayoutInflater ml;
    private ArrayList<Map<String, Object>> datas;
    private Context c;

    public XunjiaPriceAdapter(ArrayList<Map<String, Object>> datas, Context c) {
        this.datas = datas;
        this.c = c;
        ml = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas == null ? null : datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {
        View view;
        ViewHolder holder;
        if (convertview == null) {
            view = ml.inflate(R.layout.listview_item_baojia_price, null);
            holder = new ViewHolder();
            holder.goods_name_tv = (TextView) view.findViewById(R.id.goods_name_tv);
            holder.goods_price_tv = (TextView) view.findViewById(R.id.goods_price_tv);
            holder.currency_type_tv = (TextView) view.findViewById(R.id.currency_type_tv);
            holder.goods_material_tv = (TextView) view.findViewById(R.id.goods_material_tv);
            holder.goods_color_tv = (TextView) view.findViewById(R.id.goods_color_tv);
            holder.moq_tv = (TextView) view.findViewById(R.id.moq_tv);
            holder.goods_unit_tv = (TextView) view.findViewById(R.id.goods_unit_tv);
            holder.inquiry_time_tv = (TextView) view.findViewById(R.id.inquiry_time_tv);
            view.setTag(holder);
        } else {
            view = convertview;
            holder = (ViewHolder) view.getTag();
        }
        Map<String, Object> map = datas.get(i);
        holder.goods_name_tv.setText(map.get("goods_name").toString());
        holder.goods_price_tv.setText(map.get("price").toString());
        holder.currency_type_tv.setText(map.get("currency_varitety").toString());
        holder.goods_material_tv.setText(map.get("material").toString());
        holder.goods_color_tv.setText(map.get("color").toString());
        holder.moq_tv.setText(map.get("moq").toString());
        holder.goods_unit_tv.setText(map.get("unit").toString());
        if (map.get("inquiry_time")!=null) {
            holder.inquiry_time_tv.setText(map.get("inquiry_time").toString());
        }else{
            holder.inquiry_time_tv.setText("");
        }

        if (map.get("currency_varitety").toString().equals("人民币") || map.get("currency_varitety").toString().equals("RMB")) {
            holder.currency_type_tv.setText("￥");
        } else if (map.get("currency_varitety").toString().equals("美元") || map.get("currency_varitety").toString().equals("dollar")) {
            holder.currency_type_tv.setText("$");
        } else if (map.get("currency_varitety").toString().equals("欧元") || map.get("currency_varitety").toString().equals("Euro")) {
            holder.currency_type_tv.setText("€");
        } else if (map.get("currency_varitety").toString().equals("英镑") || map.get("currency_varitety").toString().equals("Pound")) {
            holder.currency_type_tv.setText("￡");
        } else if (map.get("currency_varitety").toString().equals("日元") || map.get("currency_varitety").toString().equals("Yen")) {
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
        TextView inquiry_time_tv;
    }


}
