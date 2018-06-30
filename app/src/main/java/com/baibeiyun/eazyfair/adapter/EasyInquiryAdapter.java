package com.baibeiyun.eazyfair.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.baibeiyun.eazyfair.activity.buyer.inquiryprice.DetaiEasyInquiryActivity;
import com.baibeiyun.eazyfair.activity.buyer.inquiryprice.EditEasyInquiryActivity;
import com.baibeiyun.eazyfair.activity.supplier.offer.DetailEasyQuoteActivity;
import com.baibeiyun.eazyfair.activity.supplier.offer.ModifyEasyQuoteActivity;
import com.baibeiyun.eazyfair.dao.EasyInquiryDao;
import com.baibeiyun.eazyfair.entities.EasyInquiry;
import com.baibeiyun.eazyfair.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rw on 2017/4/30.
 */

public class EasyInquiryAdapter extends BaseAdapter {
    private LayoutInflater ml;
    private ArrayList<Map<String, Object>> datas;
    private Context c;
    private Map<Integer, String> list;
    private List<CheckBox> mlist;
    private int id;
    private int tag;




    public EasyInquiryAdapter(ArrayList<Map<String, Object>> datas, Context c, Map<Integer, String> list, List<CheckBox> mlist) {
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
    public Object getItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = ml.inflate(R.layout.item_easyquote_layout, null);
            holder = new ViewHolder();
            holder.goods_name_tv = (TextView) view.findViewById(R.id.goods_name_tv);
            holder.selected_cb = (CheckBox) view.findViewById(R.id.selected_cb);
            holder.goods_picture_iv = (ImageView) view.findViewById(R.id.goods_picture_iv);
            holder.edit_bt = (Button) view.findViewById(R.id.edit_bt);
            holder.detail_bt = (Button) view.findViewById(R.id.detail_bt);
            holder.delet_bt = (Button) view.findViewById(R.id.delet_bt);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        mlist.add(holder.selected_cb);
        holder.edit_bt.setTag(position);//设置一个tag标签
        holder.delet_bt.setTag(position);//设置一个tag标签
        holder.detail_bt.setTag(position);//设置一个tag标签

        Map<String, Object> map = datas.get(position);
        holder.selected_cb.setTag(position);
        //选中按钮的监听事件
        holder.selected_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CheckBox cb = (CheckBox) buttonView;
                int tag = (int) cb.getTag();
                if (isChecked) {
                    String code = (String) datas.get(tag).get("code");
                    list.put(tag, code);
                } else {
                    list.remove(tag);
                }
            }
        });

        holder.goods_name_tv.setText((String) map.get("goods_name"));
        holder.goods_picture_iv.setImageBitmap((Bitmap) map.get("goods_picture_iv"));

        //编辑按钮的监听事件
        holder.edit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取当前点击的item的id
                //跳转到编辑页面
                int tag = (int) view.getTag();
                int id = (int) datas.get(tag).get("_id");
                Intent intent = new Intent(view.getContext(), EditEasyInquiryActivity.class);
                intent.putExtra("id", id);
                view.getContext().startActivity(intent);
            }
        });
        //报价商品详情
        holder.detail_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                int id = (int) datas.get(tag).get("_id");
                Intent intent = new Intent(view.getContext(), DetaiEasyInquiryActivity.class);
                intent.putExtra("id", id);
                view.getContext().startActivity(intent);
            }
        });
        //删除的监听事件
        holder.delet_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取当前点击的item的id
                tag = (int) view.getTag();
                id = (int) datas.get(tag).get("_id");
                showAlertDialog(view.getContext(), R.string.determine_sc);
            }
        });
        return view;
    }

    class ViewHolder {
        TextView goods_name_tv;
        CheckBox selected_cb;
        ImageView goods_picture_iv;
        Button edit_bt;
        Button detail_bt;
        Button delet_bt;

    }

    public void showAlertDialog(final Context context, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.determine, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EasyInquiryDao easyInquiryDao=new EasyInquiryDao(c);
                EasyInquiry easyInquiry=new EasyInquiry();
                easyInquiry.set_id(id);
                easyInquiryDao.deleteById(easyInquiry);
                datas.remove(tag);
                notifyDataSetChanged();
                ToastUtil.showToast2(c, R.string.delet_ok);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
