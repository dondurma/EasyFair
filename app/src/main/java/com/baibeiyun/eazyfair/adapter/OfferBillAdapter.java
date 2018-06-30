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
import com.baibeiyun.eazyfair.activity.supplier.offer.DetailGoodsforQuoteActivity;
import com.baibeiyun.eazyfair.activity.supplier.offer.ModifyGoodsActivity;
import com.baibeiyun.eazyfair.dao.MyQuoteDao;
import com.baibeiyun.eazyfair.entities.MyQuote;
import com.baibeiyun.eazyfair.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author RuanWei
 * @date 2017/3/30
 **/
public class OfferBillAdapter extends BaseAdapter {
    private LayoutInflater ml;
    private ArrayList<Map<String, Object>> datas;
    private Context c;
    private Map<Integer, String> list;
    private List<CheckBox> mlist;
    private String id;
    private int tag;


    public OfferBillAdapter(ArrayList<Map<String, Object>> datas, Context c, Map<Integer, String> list, List<CheckBox> mlist) {
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
        return datas == null ? null : datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {
        ViewHolder holder;
        final View view;
        if (convertview == null) {
            view = ml.inflate(R.layout.listview_item_offer_list, null);
            holder = new ViewHolder();
            holder.selected_cb = (CheckBox) view.findViewById(R.id.selected_cb);
            holder.goods_picture_iv = (ImageView) view.findViewById(R.id.goods_picture_iv);
            holder.goods_name_tv = (TextView) view.findViewById(R.id.goods_name_tv);
            holder.goods_price_tv = (TextView) view.findViewById(R.id.goods_price_tv);
            holder.edit_bt = (Button) view.findViewById(R.id.edit_bt);
            holder.delet_bt = (Button) view.findViewById(R.id.delet_bt);
            holder.detail_bt = (Button) view.findViewById(R.id.detail_bt);
            holder.money_fh = (TextView) view.findViewById(R.id.money_fh);
            holder.weather_select_quote_iv = (ImageView) view.findViewById(R.id.weather_select_quote_iv);
            view.setTag(holder);
        } else {
            view = convertview;
            holder = (ViewHolder) view.getTag();
        }

        mlist.add(holder.selected_cb);
        holder.edit_bt.setTag(i);//设置一个tag标签
        holder.delet_bt.setTag(i);//设置一个tag标签
        holder.detail_bt.setTag(i);//设置一个tag标签
        holder.weather_select_quote_iv.setTag(i);
        Map<String, Object> map = datas.get(i);
        holder.selected_cb.setTag(i);
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

        holder.goods_picture_iv.setImageBitmap((Bitmap) map.get("goods_picture_iv"));
        holder.goods_name_tv.setText(map.get("goods_name_tv").toString());
        holder.goods_price_tv.setText(map.get("goods_price_tv").toString());
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
        //是否显示圆点
        if (map.get("quote_type") != null) {
            holder.weather_select_quote_iv.setVisibility(View.GONE);
        } else {
            holder.weather_select_quote_iv.setVisibility(View.VISIBLE);
        }


        //编辑按钮的监听事件
        holder.edit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取当前点击的item的id
                //跳转到编辑页面
                int tag = (int) view.getTag();
                String id = (String) datas.get(tag).get("_id");
                Intent intent = new Intent(view.getContext(), ModifyGoodsActivity.class);
                intent.putExtra("id", id);
                view.getContext().startActivity(intent);
            }
        });
        //报价商品详情
        holder.detail_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                String id = (String) datas.get(tag).get("_id");
                Intent intent = new Intent(view.getContext(), DetailGoodsforQuoteActivity.class);
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
                id = (String) datas.get(tag).get("_id");
                showAlertDialog(view.getContext(), R.string.determine_sc);
            }
        });

        return view;
    }

    class ViewHolder {
        CheckBox selected_cb;
        ImageView goods_picture_iv;
        TextView goods_name_tv;
        TextView goods_price_tv;
        Button edit_bt;
        Button detail_bt;
        Button delet_bt;
        TextView money_fh;
        ImageView weather_select_quote_iv;

    }


    public void showAlertDialog(final Context context, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.determine, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MyQuoteDao myQuoteDao = new MyQuoteDao(c);
                MyQuote myQuote = new MyQuote();
                myQuote.set_id(id);
                myQuoteDao.deletById(myQuote);
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
