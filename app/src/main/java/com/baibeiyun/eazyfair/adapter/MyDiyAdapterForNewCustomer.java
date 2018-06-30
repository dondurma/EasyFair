package com.baibeiyun.eazyfair.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.baibeiyun.eazyfair.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/21.
 */
public class MyDiyAdapterForNewCustomer extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Map<String, Object>> datas;
    private Context c;
    private MyDiyAdapterForNewCustomer adapter;
    private int Position = -1;
    public MyDiyAdapterForNewCustomer(ArrayList<Map<String, Object>> datas, Context c) {
        super();
        this.datas = datas;
        this.c = c;
        inflater = LayoutInflater.from(c);
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        convertView = inflater.inflate(R.layout.item_emailset, null);
        EditText diy_content_et = (EditText) convertView.findViewById(R.id.diy_content_et);
        Map<String, Object> map = datas.get(position);
        if (map.get("man")==null){

        }else {
            diy_content_et.setText(map.get("man").toString());
        }
        diy_content_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Map<String, Object> map = datas.get(position);
                map.put("man",  editable.toString());
            }
        });


        return convertView;
    }
}
