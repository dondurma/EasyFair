package com.baibeiyun.eazyfair.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.buyer.mygoods.GoodsDetailForBuyerActivity;
import com.baibeiyun.eazyfair.activity.buyer.mygoods.ModifyGoodsForBuyerActivity;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/29.
 */
public class MyGoodsListForBuyerAdapter extends BaseAdapter {
    private LayoutInflater ml;
    private ArrayList<Map<String, Object>> datas;
    private Context c;


    public MyGoodsListForBuyerAdapter(ArrayList<Map<String, Object>> datas, Context c) {
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
    public View getView(final int i, View convertview, ViewGroup viewGroup) {
        View view;
        ViewHolder holder;
        if (convertview == null) {
            view = ml.inflate(R.layout.listview_item_mygoods_buyer, null);
            holder = new ViewHolder();
            holder.goods_picture_iv = (ImageView) view.findViewById(R.id.goods_picture_iv);
            holder.goods_name_tv = (TextView) view.findViewById(R.id.goods_name_tv);
            holder.goods_price_tv = (TextView) view.findViewById(R.id.goods_price_tv);
            holder.edit_bt = (Button) view.findViewById(R.id.edit_bt);
            holder.inquiry_customer_bt = (Button) view.findViewById(R.id.inquiry_customer_bt);
            holder.money_fh = (TextView) view.findViewById(R.id.money_fh);
            view.setTag(holder);
        } else {
            view = convertview;
            holder = (ViewHolder) view.getTag();
        }
        Map<String, Object> data = datas.get(i);
        holder.goods_picture_iv.setImageBitmap((Bitmap) data.get("goods_picture_iv"));
        holder.goods_name_tv.setText((String) data.get("goods_name_tv"));
        holder.goods_price_tv.setText(String.valueOf(data.get("goods_price_tv")));

        if (data.get("currency_variety").toString().equals("人民币") || data.get("currency_variety").toString().equals("RMB")) {
            holder.money_fh.setText("￥");
        } else if (data.get("currency_variety").toString().equals("美元") || data.get("currency_variety").toString().equals("dollar")) {
            holder.money_fh.setText("$");
        } else if (data.get("currency_variety").toString().equals("欧元") || data.get("currency_variety").toString().equals("Euro")) {
            holder.money_fh.setText("€");
        } else if (data.get("currency_variety").toString().equals("英镑") || data.get("currency_variety").toString().equals("Pound")) {
            holder.money_fh.setText("￡");
        } else if (data.get("currency_variety").toString().equals("日元") || data.get("currency_variety").toString().equals("Yen")) {
            holder.money_fh.setText("JP￥");
        }
        holder.edit_bt.setTag(i);
        holder.inquiry_customer_bt.setTag(i);
        //编辑按钮的监听
        holder.edit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                int id = (int) datas.get(tag).get("_id");
                Intent intent = new Intent(view.getContext(), ModifyGoodsForBuyerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtra("id", id);
                view.getContext().startActivity(intent);
            }
        });
        //报价供应商的监听事件
        holder.inquiry_customer_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                int id = (int) datas.get(tag).get("_id");
                Intent intent = new Intent(view.getContext(), GoodsDetailForBuyerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtra("id", id);
                intent.putExtra("tag",2);
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }

    class ViewHolder {
        ImageView goods_picture_iv;
        TextView goods_name_tv;
        TextView goods_price_tv;
        Button edit_bt;
        Button inquiry_customer_bt;
        TextView money_fh;

    }

}
