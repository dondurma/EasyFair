package com.baibeiyun.eazyfair.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.buyer.inquiryprice.EasyInquiryActivity;
import com.baibeiyun.eazyfair.activity.buyer.inquiryprice.InquiryPriceActivity;
import com.baibeiyun.eazyfair.activity.buyer.mysupplier.SupplierCardActivity;
import com.baibeiyun.eazyfair.view.RoundImageView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/11.
 */
public class SupplierListForInquiryPriceAdapter extends BaseAdapter {
    private LayoutInflater ml;
    private ArrayList<Map<String, Object>> datas;
    private Context c;

    public SupplierListForInquiryPriceAdapter(ArrayList<Map<String, Object>> datas, Context c) {
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
            view = ml.inflate(R.layout.listview_item_supplier_list_for_inquiryprice, null);
            holder = new ViewHolder();
            holder.supplier_logo_iv = (RoundImageView) view.findViewById(R.id.supplier_logo_iv);
            holder.supplier_commpany_name_tv = (TextView) view.findViewById(R.id.supplier_commpany_name_tv);
            holder.begain_xj = (Button) view.findViewById(R.id.begain_xj);
            holder.easy_xj = (Button) view.findViewById(R.id.easy_xj);
            holder.edit_bt = (Button) view.findViewById(R.id.edit_bt);
            holder.begain_xj.setTag(i);
            holder.easy_xj.setTag(i);
            holder.edit_bt.setTag(i);
            //开始询价
            holder.begain_xj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                    int id = (int) datas.get(tag).get("_id");//得到点击当前item对应客户表中的id
                    Intent intent = new Intent(v.getContext(), InquiryPriceActivity.class);
                    intent.putExtra("customer_id", id);
                    v.getContext().startActivity(intent);

                }
            });
            //简易询价
            holder.easy_xj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                    int id = (int) datas.get(tag).get("_id");//得到点击当前item对应客户表中的id
                    Intent intent = new Intent(v.getContext(), EasyInquiryActivity.class);
                    intent.putExtra("id", id);
                    v.getContext().startActivity(intent);

                }
            });
            //编辑客户资料
            holder.edit_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                    int id = (int) datas.get(tag).get("_id");//得到点击当前item对应客户表中的id
                    Intent intent = new Intent(v.getContext(), SupplierCardActivity.class);
                    intent.putExtra("id", id);
                    v.getContext().startActivity(intent);

                }
            });
            view.setTag(holder);
        } else {
            view = convertview;
            holder = (ViewHolder) view.getTag();
        }
        Map<String, Object> map = datas.get(i);
        holder.supplier_logo_iv.setImageBitmap((Bitmap) map.get("supplier_logo_iv"));
        holder.supplier_commpany_name_tv.setText(map.get("supplier_commpany_name_tv").toString());
        return view;
    }

    class ViewHolder {
        RoundImageView supplier_logo_iv;
        TextView supplier_commpany_name_tv;
        Button begain_xj, easy_xj, edit_bt;
    }
}
