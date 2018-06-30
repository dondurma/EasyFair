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
import com.baibeiyun.eazyfair.view.RoundImageView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/5.
 */
public class CustomListAdapter extends BaseAdapter {
    private LayoutInflater ml;//用来将一个xml转化为一个view对象
    private ArrayList<Map<String,Object>> datas;//数据源
    private Context c;//上下文对象


    //有参构造器
    public CustomListAdapter(ArrayList<Map<String, Object>> datas, Context c) {
        this.datas = datas;
        this.c = c;
        ml=LayoutInflater.from(c);
    }
    //获得数据源总数
    @Override
    public int getCount() {
        return datas ==null?0:datas.size();
    }
    //获得指定位置的数据
    @Override
    public Object getItem(int i) {
        return datas == null ? null :datas.get(i);
    }
    //获得指定的下标
    @Override
    public long getItemId(int i) {
        return i;
    }
    //加载每一个item的布局
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if(convertView==null){
            view=ml.inflate(R.layout.listview_item_customelist,null);
            holder=new ViewHolder();
            holder.custom_logo_iv= (RoundImageView) view.findViewById(R.id.custom_logo_iv);
            holder.custom_name_tv= (TextView) view.findViewById(R.id.custom_name_tv);
            holder.custom_company_tv= (TextView) view.findViewById(R.id.custom_company_tv);
            view.setTag(holder);
        }else{
            view=convertView;
            holder= (ViewHolder) view.getTag();
        }
        Map<String,Object> map = datas.get(i);
        holder.custom_logo_iv.setImageBitmap((Bitmap) map.get("custom_logo_iv"));
        holder.custom_name_tv.setText(map.get("custom_name_tv").toString());
        holder.custom_company_tv.setText(map.get("custom_company_tv").toString());
      return view;
    }
    class ViewHolder{
        RoundImageView custom_logo_iv;
        TextView custom_name_tv,custom_company_tv;

    }


}
