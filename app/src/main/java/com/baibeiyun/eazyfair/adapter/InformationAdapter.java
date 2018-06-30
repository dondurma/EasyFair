package com.baibeiyun.eazyfair.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.InformationActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/9.
 */
public class InformationAdapter extends BaseAdapter {
    private LayoutInflater ml;
    private ArrayList<Map<String, Object>> datas;
    private Context c;
    private List<CheckBox> mlist;
    private Map<Integer, Integer> list;
    private static HashMap<Integer, Integer> isvisibleMap;

    public InformationAdapter(ArrayList<Map<String, Object>> datas, Context c, Map<Integer, Integer> list, List<CheckBox> mlist) {
        this.datas = datas;
        this.c = c;
        this.list = list;
        this.mlist = mlist;
        isvisibleMap = new HashMap<Integer, Integer>();
        ml = LayoutInflater.from(c);
        initDate();
    }

    private void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsvisibleMap().put(i, CheckBox.INVISIBLE);
        }
    }

    public static HashMap<Integer, Integer> getIsvisibleMap() {
        return isvisibleMap;
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
            view = ml.inflate(R.layout.listview_item_zixunfragment, null);
            holder = new ViewHolder();
            holder.redpoint_iv = (ImageView) view.findViewById(R.id.redpoint_iv);
            holder.title_tv = (TextView) view.findViewById(R.id.title_tv);
            holder.selected_cb = (CheckBox) view.findViewById(R.id.selected_cb);
            view.setTag(holder);
        } else {
            view = convertview;
            holder = (ViewHolder) view.getTag();
        }
        mlist.add(holder.selected_cb);
        holder.selected_cb.setTag(i);
        Map<String, Object> map = datas.get(i);
        holder.selected_cb.setTag(i);
        if ("0".equals(map.get("hasReadState").toString())) {
            holder.redpoint_iv.setVisibility(View.VISIBLE);
        } else {
            holder.redpoint_iv.setVisibility(View.GONE);
        }
        holder.title_tv.setText(map.get("title_tv").toString());



        holder.selected_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CheckBox cb = (CheckBox) buttonView;
                int tag = (int) cb.getTag();
                if (isChecked) {
                    int id = (int) datas.get(tag).get("messageId");
                    list.put(tag, id);
                } else {
                    list.remove(tag);
                }
            }
        });
        Integer integer = getIsvisibleMap().get(i);
        if (integer == null) {
            holder.selected_cb.setVisibility(View.INVISIBLE);
        } else {
            holder.selected_cb.setVisibility(View.VISIBLE);
        }


        return view;
    }

    class ViewHolder {
        ImageView redpoint_iv;
        TextView title_tv;
        CheckBox selected_cb;
    }


}
