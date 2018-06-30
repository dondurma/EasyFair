package com.baibeiyun.eazyfair.adapter;

/**
 * Created by Administrator on 2016/12/8.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;

import java.util.ArrayList;
import java.util.List;

public class HorizontalListViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> viewList = new ArrayList<>();
    private LayoutInflater mInflater;

    public HorizontalListViewAdapter(Context context, List<String> viewList) {
        this.viewList = viewList;
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return viewList.size() == 0 ? 0 : viewList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_children_view, null);
            holder.mTitle = (TextView) convertView.findViewById(R.id.child_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTitle.setText(viewList.get(position));
        return convertView;
    }

    private static class ViewHolder {
        private TextView mTitle;
    }

}