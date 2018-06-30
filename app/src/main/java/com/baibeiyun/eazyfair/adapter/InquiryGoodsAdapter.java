package com.baibeiyun.eazyfair.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.buyer.inquiryprice.DetailGoodsforBuyerActvity;
import com.baibeiyun.eazyfair.activity.supplier.offer.DetailGoodsforSupplierActvity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InquiryGoodsAdapter extends BaseAdapter {
    private LayoutInflater ml;
    private ArrayList<Map<String, Object>> datas;
    private Context c;
    private Map<Integer, Integer> list;
    private List<CheckBox> mlist;
    HashMap<Integer, Boolean> state = new HashMap<>();


    public InquiryGoodsAdapter(ArrayList<Map<String, Object>> datas, Context c, Map<Integer, Integer> list, List<CheckBox> mlist) {
        this.datas = datas;
        this.c = c;
        this.list = list;
        this.mlist = mlist;
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
        final ViewHolder holder;
        final View view;
        if (convertview == null) {
            view = ml.inflate(R.layout.listview_item_offer_list2, null);
            holder = new ViewHolder();
            holder.selected_cb = (CheckBox) view.findViewById(R.id.selected_cb);
            holder.goods_picture_iv = (ImageView) view.findViewById(R.id.goods_picture_iv);
            holder.goods_name_tv = (TextView) view.findViewById(R.id.goods_name_tv);
            holder.goods_price_tv = (TextView) view.findViewById(R.id.goods_price_tv);
            holder.inquiry_customer_bt = (Button) view.findViewById(R.id.inquiry_customer_bt);
            holder.money_fh = (TextView) view.findViewById(R.id.money_fh);
            view.setTag(holder);
        } else {
            view = convertview;
            holder = (ViewHolder) view.getTag();
        }
        mlist.add(holder.selected_cb);
        holder.inquiry_customer_bt.setTag(i);
        final Map<String, Object> map = datas.get(i);
        holder.selected_cb.setTag(i);

        //选中按钮的监听事件
        holder.selected_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                CheckBox cb = (CheckBox) compoundButton;
                int tag = (int) cb.getTag();
                if (isChecked) {
                    state.put(i, true);
                    list.put(tag, (Integer) datas.get(tag).get("_id"));

                } else {
                    state.remove(i);
                    list.remove(tag);
                }
            }
        });
        holder.selected_cb.setChecked(state.get(i) != null);
        holder.goods_picture_iv.setImageBitmap((Bitmap) map.get("goods_picture_iv"));
        holder.goods_name_tv.setText(map.get("goods_name_tv").toString());
        holder.goods_price_tv.setText(map.get("goods_price_tv").toString());

        if (map.get("currency_variety").toString().equals("人民币") || map.get("currency_variety").toString().equals
                ("RMB")) {
            holder.money_fh.setText("￥");
        } else if (map.get("currency_variety").toString().equals("美元") || map.get("currency_variety").toString
                ().equals("dollar")) {
            holder.money_fh.setText("$");
        } else if (map.get("currency_variety").toString().equals("欧元") || map.get("currency_variety").toString
                ().equals("Euro")) {
            holder.money_fh.setText("€");
        } else if (map.get("currency_variety").toString().equals("英镑") || map.get("currency_variety").toString
                ().equals("Pound")) {
            holder.money_fh.setText("￡");
        } else if (map.get("currency_variety").toString().equals("日元") || map.get("currency_variety").toString
                ().equals("Yen")) {
            holder.money_fh.setText("JP￥");
        }

        //详情按钮的监听事件
        holder.inquiry_customer_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                int id = (int) datas.get(tag).get("_id");
                Intent intent = new Intent(view.getContext(), DetailGoodsforBuyerActvity.class);
                intent.putExtra("id", id);
                view.getContext().startActivity(intent);
            }
        });
        return view;
    }

    class ViewHolder {
        CheckBox selected_cb;
        ImageView goods_picture_iv;
        TextView goods_name_tv;
        TextView goods_price_tv;
        Button inquiry_customer_bt;
        TextView money_fh;
    }


}
