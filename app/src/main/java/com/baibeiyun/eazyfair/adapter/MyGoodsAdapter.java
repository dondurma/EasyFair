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
import com.baibeiyun.eazyfair.activity.supplier.mygoods.GoodsDetailActivity;
import com.baibeiyun.eazyfair.activity.supplier.mygoods.ModifyGoodsforSupplierActivity;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/29.
 */
public class MyGoodsAdapter extends BaseAdapter {
    private LayoutInflater ml;//用来将一个xml布局转换成一个view对象
    private ArrayList<Map<String, Object>> datas;//数据源
    private Context context;//上下文对象


    public MyGoodsAdapter(Context context, ArrayList<Map<String, Object>> datas) {
        this.context = context;
        this.datas = datas;
        ml = LayoutInflater.from(context);
    }

    //获得数据源总数
    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    //获得指定位置的数据
    @Override
    public Object getItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    //获得制定下标的id
    @Override
    public long getItemId(int position) {
        return position;
    }

    //加载每一个item的布局
    @Override
    public View getView(final int position, View convertview, ViewGroup viewGroup) {
        View view;
        ViewHolder holder;
        if (convertview == null) {
            //将item转换为view对象
            view = ml.inflate(R.layout.listview_item_mygoods, null);
            holder = new ViewHolder();
            holder.goods_picture_iv = (ImageView) view.findViewById(R.id.goods_picture_iv);
            holder.goods_name_tv = (TextView) view.findViewById(R.id.goods_name_tv);
            holder.goods_price_tv = (TextView) view.findViewById(R.id.goods_price_tv);
            holder.edit_bt = (Button) view.findViewById(R.id.edit_bt);
            holder.money_fh = (TextView) view.findViewById(R.id.money_fh);
            holder.inquiry_customer_bt = (Button) view.findViewById(R.id.inquiry_customer_bt);
            view.setTag(holder);
        } else {
            view = convertview;
            holder = (ViewHolder) view.getTag();
        }

        holder.edit_bt.setTag(position);
        holder.inquiry_customer_bt.setTag(position);
        //通过下标获得需要展示的数据
        Map<String, Object> data = datas.get(position);
        //将图片展示到ImageView上
        holder.goods_picture_iv.setImageBitmap((Bitmap) data.get("goods_picture_iv"));
        //将商品名称展示到TextView上
        holder.goods_name_tv.setText((String) data.get("goods_name_tv"));
        //将商品价格展示到TextView上
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
        holder.edit_bt.setTag(position);
        holder.inquiry_customer_bt.setTag(position);
        //将编辑按钮的监听事件
        holder.edit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                int id = (int) datas.get(tag).get("_id");
                Intent intent = new Intent(view.getContext(), ModifyGoodsforSupplierActivity.class);
                intent.putExtra("id", id);
                view.getContext().startActivity(intent);
            }
        });
        //询价客户按钮的监听事件
        holder.inquiry_customer_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                int id = (int) datas.get(tag).get("_id");
                Intent intent = new Intent(view.getContext(), GoodsDetailActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("tag", "2");
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
        TextView money_fh;
        Button inquiry_customer_bt;
    }


}


