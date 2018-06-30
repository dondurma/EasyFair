package com.baibeiyun.eazyfair.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.entities.BussinessGY;
import com.baibeiyun.eazyfair.utils.TimeUtils;

import java.util.List;


public class BusinessAdapter extends BaseAdapter {
    private Context context;
    private List<BussinessGY.BusinessOpportunityListBean> list;

    public BusinessAdapter(Context context, List<BussinessGY.BusinessOpportunityListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_buissness_gong_ying, null);
            holder.title_tv = (TextView) convertView.findViewById(R.id.productIntroduction);
            holder.date_tv = (TextView) convertView.findViewById(R.id.pubDate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BussinessGY.BusinessOpportunityListBean businessOpportunityListBean = list.get(position);
        holder.title_tv.setText(businessOpportunityListBean.getProductName());
        holder.date_tv.setText(TimeUtils.timeFormat(businessOpportunityListBean.getPubDate()));
        return convertView;

    }

    class ViewHolder {
        TextView title_tv, date_tv;
    }
}
