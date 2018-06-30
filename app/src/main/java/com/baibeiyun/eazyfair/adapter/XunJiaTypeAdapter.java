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
import com.baibeiyun.eazyfair.activity.buyer.inquiryprice.DetailforInquiryActivity;
import com.baibeiyun.eazyfair.activity.buyer.inquiryprice.EditActivityforInquiry;
import com.baibeiyun.eazyfair.dao.MyInquiryDao;
import com.baibeiyun.eazyfair.entities.MyInquiry;
import com.baibeiyun.eazyfair.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author RuanWei
 * @date edit 2017/3/3
 **/
public class XunJiaTypeAdapter extends BaseAdapter {
    private LayoutInflater ml;
    private ArrayList<Map<String, Object>> datas;
    private Context c;
    private Map<Integer, String> list;
    private List<CheckBox> mlist;
    int tag;
    String id;

    public XunJiaTypeAdapter(ArrayList<Map<String, Object>> datas, Context c, Map<Integer, String> list, List<CheckBox> mlist) {
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
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertview, ViewGroup viewGroup) {
        ViewHolder holder;
        View view;
        if (convertview == null) {
            view = ml.inflate(R.layout.listview_layout_item_xunjia, null);
            holder = new ViewHolder();
            holder.selected_cb = (CheckBox) view.findViewById(R.id.selected_cb);
            holder.img_iv = (ImageView) view.findViewById(R.id.img_iv);
            holder.goods_name_tv = (TextView) view.findViewById(R.id.goods_name_tv);
            holder.goods_price_tv = (TextView) view.findViewById(R.id.goods_price_tv);
            holder.delet_bt = (Button) view.findViewById(R.id.delet_bt);
            holder.bt_edit = (Button) view.findViewById(R.id.bt_edit);
            holder.bt_detail = (Button) view.findViewById(R.id.bt_detail);
            holder.money_fh = (TextView) view.findViewById(R.id.money_fh);
            holder.weather_select_inquiry_iv = (ImageView) view.findViewById(R.id.weather_select_inquiry_iv);
            view.setTag(holder);
        } else {
            view = convertview;
            holder = (ViewHolder) view.getTag();
        }
        mlist.add(holder.selected_cb);
        holder.delet_bt.setTag(i);
        holder.bt_detail.setTag(i);
        holder.bt_edit.setTag(i);
        holder.selected_cb.setTag(i);
        Map<String, Object> map = datas.get(i);
        holder.selected_cb.setTag(i);
        holder.img_iv.setImageBitmap((Bitmap) map.get("goods_img_iv"));
        holder.goods_name_tv.setText((String) map.get("goods_name_tv"));
        holder.goods_price_tv.setText(String.valueOf(map.get("goods_price_tv")));

        if (map.get("currency_variety").toString().equals("人民币") || map.get("currency_variety").toString().equals("RMB")) {
            holder.money_fh.setText("￥");
        } else if (map.get("currency_variety").toString().equals("美元") || map.get("currency_variety").toString().equals("dollar")) {
            holder.money_fh.setText("$");
        } else if (map.get("currency_variety").toString().equals("欧元") || map.get("currency_variety").toString().equals("Euro")) {
            holder.money_fh.setText("€");
        } else if (map.get("currency_variety").toString().equals("英镑") || map.get("currency_variety").toString().equals("Pound")) {
            holder.money_fh.setText("￡");
        } else if (map.get("currency_variety").toString().equals("日元") || map.get("currency_variety").toString().equals("Yen")) {
            holder.money_fh.setText("JP￥");
        }
        if (map.get("inquiry_type") != null) {
            holder.weather_select_inquiry_iv.setVisibility(View.GONE);
        } else {
            holder.weather_select_inquiry_iv.setVisibility(View.VISIBLE);
        }

        //选中按钮的监听事件
        holder.selected_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                CheckBox cb = (CheckBox) compoundButton;
                int tag = (int) cb.getTag();
                if (isChecked) {
                    String id = (String) datas.get(tag).get("_id");
                    list.put(tag, id);
                } else {
                    list.remove(tag);
                }
            }
        });
        //删除的监听事件
        holder.delet_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取当前点击的item的id
                tag = (int) view.getTag();
                id = (String) datas.get(tag).get("_id");

                showAlertDialog(view.getContext(), R.string.determine_sc);
            }
        });
        //详情按钮的监听
        holder.bt_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                String id = (String) datas.get(tag).get("_id");
                Intent intent = new Intent(view.getContext(), DetailforInquiryActivity.class);
                intent.putExtra("id", id);
                view.getContext().startActivity(intent);
            }
        });
        //编辑按钮
        holder.bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                String id = (String) datas.get(tag).get("_id");
                Intent intent = new Intent(view.getContext(), EditActivityforInquiry.class);
                intent.putExtra("id", id);
                view.getContext().startActivity(intent);
            }
        });
        return view;
    }

    class ViewHolder {
        CheckBox selected_cb;
        ImageView img_iv;
        TextView goods_name_tv;
        TextView goods_price_tv;
        Button delet_bt, bt_edit, bt_detail;
        TextView money_fh;
        ImageView weather_select_inquiry_iv;


    }

    public void showAlertDialog(final Context context, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.determine, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                MyInquiryDao myInquiryDao = new MyInquiryDao(c);
                MyInquiry myInquiry = new MyInquiry();
                myInquiry.set_id(id);
                myInquiryDao.deleteById(myInquiry);
                datas.remove(tag);
                notifyDataSetChanged();

                ToastUtil.showToast2(c, R.string.delet_ok);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


}
