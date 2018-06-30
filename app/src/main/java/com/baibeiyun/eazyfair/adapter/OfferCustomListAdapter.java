package com.baibeiyun.eazyfair.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.supplier.mycustomer.CustomCardActivity;
import com.baibeiyun.eazyfair.activity.supplier.offer.EasyQuoteActivity;
import com.baibeiyun.eazyfair.activity.supplier.offer.OfferListActivity;
import com.baibeiyun.eazyfair.dao.MyUserDao;
import com.baibeiyun.eazyfair.entities.MyUser;
import com.baibeiyun.eazyfair.view.RoundImageView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/7.
 */
public class OfferCustomListAdapter extends BaseAdapter {
    private LayoutInflater ml;
    private ArrayList<Map<String, Object>> datas;
    private Context c;
    private ArrayList<MyUser> myUsers = new ArrayList<>();
    MyUser myUser;

    public OfferCustomListAdapter(ArrayList<Map<String, Object>> datas, Context c) {
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
            view = ml.inflate(R.layout.listview_item_custom_company_list, null);
            holder = new ViewHolder();
            holder.logo_iv = (RoundImageView) view.findViewById(R.id.logo_iv);
            holder.commpany_name_tv = (TextView) view.findViewById(R.id.commpany_name_tv);
            holder.begin_offer_bt = (Button) view.findViewById(R.id.begin_offer_bt);
            holder.easy_offer_bt = (Button) view.findViewById(R.id.easy_offer_bt);
            holder.edit_custome_bt = (Button) view.findViewById(R.id.edit_custome_bt);
            holder.begin_offer_bt.setTag(i);
            holder.easy_offer_bt.setTag(i);
            holder.edit_custome_bt.setTag(i);

            //开始报价
            holder.begin_offer_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tag = (int) view.getTag();
                    int id = (int) datas.get(tag).get("_id");//得到点击当前item对应客户表中的id
                    Intent intent = new Intent(view.getContext(), OfferListActivity.class);
                    intent.putExtra("id", id);
                    view.getContext().startActivity(intent);
                }
            });
            //简易报价
            holder.easy_offer_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tag = (int) view.getTag();
                    int id = (int) datas.get(tag).get("_id");//得到点击当前item对应客户表中的id
                    Intent intent=new Intent(view.getContext(),EasyQuoteActivity.class);
                    intent.putExtra("id",id);
                    view.getContext().startActivity(intent);
                }
            });

            //编辑客户资料
            holder.edit_custome_bt.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int tag = (int) view.getTag();
                            int id = (int) datas.get(tag).get("_id");//得到点击当前item对应客户表中的id
                            Intent intent = new Intent(view.getContext(), CustomCardActivity.class);
                            intent.putExtra("id", id);
                            view.getContext().startActivity(intent);
                        }
                    });
            view.setTag(holder);
        } else {
            view = convertview;
            holder = (ViewHolder) view.getTag();
        }
        Map<String, Object> map = datas.get(i);
        holder.logo_iv.setImageBitmap((Bitmap) map.get("logo_iv"));
        holder.commpany_name_tv.setText(map.get("commpany_name_tv").toString());
        return view;
    }

    class ViewHolder {
        RoundImageView logo_iv;
        TextView commpany_name_tv;
        Button begin_offer_bt, easy_offer_bt, edit_custome_bt;
    }

    //查询个人信息表的方法
    private ArrayList<MyUser> selectInfo() {
        myUsers.clear();
        MyUserDao myUserDao = new MyUserDao(c);
        Cursor cursor = myUserDao.selectAll();
        while (cursor.moveToNext()) {
            String ch_company_name = cursor.getString(cursor.getColumnIndex("ch_company_name"));
            String ch_contact = cursor.getString(cursor.getColumnIndex("ch_contact"));
            myUser = new MyUser(ch_company_name, ch_contact);
            myUsers.add(myUser);
        }
        cursor.close();
        return myUsers;
    }
}
