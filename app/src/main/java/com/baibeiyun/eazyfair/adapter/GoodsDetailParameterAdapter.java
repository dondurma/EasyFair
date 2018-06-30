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
 * Created by Administrator on 2016/10/28.
 */
public class GoodsDetailParameterAdapter extends BaseAdapter {
    private LayoutInflater ml;
    private ArrayList<Map<String,Object>>datas;
    private Context c;

    public GoodsDetailParameterAdapter(ArrayList<Map<String, Object>> datas, Context c) {
        this.datas = datas;
        this.c = c;
        ml=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return datas ==null ? 0:datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas ==null ?null : datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {
        final View view;
        ViewHolder holder;
        if (convertview==null){
            view=ml.inflate(R.layout.listview_layout_goods_detail,null);
            holder=new ViewHolder();
            holder.parameter_title_tv= (TextView) view.findViewById(R.id.parameter_title_tv);
            holder.parameter_content_tv= (TextView) view.findViewById(R.id.parameter_content_tv);
            view.setTag(holder);
        }else{
            view=convertview;
            holder= (ViewHolder) view.getTag();
        }
        Map<String,Object>map=datas.get(i);
        holder.parameter_title_tv.setText(map.get("parameter_title_tv").toString());
        holder.parameter_content_tv.setText(map.get("parameter_content_tv").toString());
        return view;
    }
    class ViewHolder{
        TextView parameter_title_tv;
        TextView parameter_content_tv;
    }
}
