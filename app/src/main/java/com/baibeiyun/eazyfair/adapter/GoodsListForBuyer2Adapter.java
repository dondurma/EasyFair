package com.baibeiyun.eazyfair.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/15.
 */
public class GoodsListForBuyer2Adapter extends BaseAdapter {
    private LayoutInflater ml;
    private ArrayList<Map<String, Object>> datas;
    private Context c;

    public GoodsListForBuyer2Adapter(ArrayList<Map<String, Object>> datas, Context c) {
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
        ViewHolder holder;
        final View view;
        if (convertview == null) {
            view = ml.inflate(R.layout.listview_item_goods_list_for_buyer2, null);
            holder = new ViewHolder();
            holder.goods_picture_iv = (ImageView) view.findViewById(R.id.goods_picture_iv);
            holder.goods_name_tv = (TextView) view.findViewById(R.id.goods_name_tv);
            holder.goods_price_tv = (TextView) view.findViewById(R.id.goods_price_tv);
            holder.money_fh= (TextView) view.findViewById(R.id.money_fh);
            view.setTag(holder);
        } else {
            view = convertview;
            holder = (ViewHolder) view.getTag();
        }
        Map<String, Object> map = datas.get(i);
        holder.goods_picture_iv.setImageBitmap((Bitmap) map.get("goods_picture_iv"));
        holder.goods_name_tv.setText(map.get("goods_name_tv").toString());
        holder.goods_price_tv.setText(map.get("goods_price_tv").toString());

        if (map.get("currency_variety").toString().equals("人民币")||map.get("currency_variety").toString().equals
                ("RMB")) {
            holder.money_fh.setText("￥");
        } else if (map.get("currency_variety").toString().equals("美元")||map.get("currency_variety").toString
                ().equals("dollar")) {
            holder.money_fh.setText("$");
        } else if (map.get("currency_variety").toString().equals("欧元")||map.get("currency_variety").toString
                ().equals("Euro")) {
            holder.money_fh.setText("€");
        } else if (map.get("currency_variety").toString().equals("英镑")||map.get("currency_variety").toString
                ().equals("Pound")) {
            holder.money_fh.setText("￡");
        } else if (map.get("currency_variety").toString().equals("日元")||map.get("currency_variety").toString
                ().equals("Yen")) {
            holder.money_fh.setText("JP￥");
        }
        return view;
    }

    class ViewHolder {
        ImageView goods_picture_iv;
        TextView goods_name_tv;
        TextView goods_price_tv;
        TextView money_fh;
    }


}
