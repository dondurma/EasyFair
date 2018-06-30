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


public class ExhibitionInfoAdapter extends BaseAdapter {
    private LayoutInflater ml;
    private ArrayList<Map<String,Object>> datas;
    private Context c;

    public ExhibitionInfoAdapter(ArrayList<Map<String, Object>> datas, Context c) {
        this.datas = datas;
        this.c = c;
        ml=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {

        return datas==null?0:datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas ==null?null:datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {
        View view;
        ViewHolder holder;
        if(convertview==null){
            view=ml.inflate(R.layout.listview_item_exhibition_info,null);
            holder=new ViewHolder();
            holder.time_tv= (TextView) view.findViewById(R.id.time_tv);
            holder.title_tv= (TextView) view.findViewById(R.id.title_tv);
            view.setTag(holder);
        }else{
            view=convertview;
            holder= (ViewHolder) view.getTag();
        }
        Map<String,Object>map=datas.get(i);
        holder.title_tv.setText(map.get("title_tv").toString());
        holder.time_tv.setText(map.get("time_tv").toString());

        return view;
    }
    class ViewHolder{
        TextView title_tv;
        TextView time_tv;

    }
}
