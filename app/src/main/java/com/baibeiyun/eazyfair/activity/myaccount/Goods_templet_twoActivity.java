package com.baibeiyun.eazyfair.activity.myaccount;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.dao.LanguageDao;
import com.baibeiyun.eazyfair.entities.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Goods_templet_twoActivity extends BaseActivity implements View.OnClickListener {
    private PopupWindow popupWindow;
    private RelativeLayout fanhui_rl;//返回


    private EditText goods_number_et;
    private EditText goods_weight_et;

    private LinearLayout goods_weight_unit_ll;//商品重量单位
    private TextView goods_weight_unit_tv;//商品重量单位

    //外箱包装
    private EditText outbox_amout_et;//数量
    private EditText outbox_size_et;//尺寸
    private EditText outbox_weight_et;//重量
    private LinearLayout outbox_weight__unit_ll;
    private TextView outbox_weight__unit_tv;//重量单位

    //中盒包装
    private EditText centerbox_amout_et;//数量
    private EditText centerbox_size_et;//尺寸
    private EditText centerbox_weight_et;//重量
    private LinearLayout centerbox_weight_unit_ll;
    private TextView centerbox_weight_unit_tv;//重量单位

    private Button next_bt;


    private List<String> dropList = new ArrayList<>();//popuWindow中list


    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private ArrayList<Language> listforlanguage = new ArrayList<>();
    Language language;

    //得到上个页面传递过来的数据
    private String goodsNumber, goodsName, goodsPrice, goodsMaterial, goodsColor;
    //得到当前页面用户输入的数据
    private String goodsShuLiang, goodsWeight, goodsWeightUnit, outboxAmout, outboxSize, outboxWeight, outboxWeightUnit, centerboxAmout, centerboxSize, centerboxWeight, centerWeightUnit;

    public static Goods_templet_twoActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_template_two);
        activity=this;
        initYuyan();
        initview();
        getIntentData();

    }

    private void initview() {
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        goods_number_et = (EditText) findViewById(R.id.goods_number_et);
        goods_weight_et = (EditText) findViewById(R.id.goods_weight_et);
        goods_weight_unit_ll = (LinearLayout) findViewById(R.id.goods_weight_unit_ll);
        goods_weight_unit_tv = (TextView) findViewById(R.id.goods_weight_unit_tv);
        outbox_amout_et = (EditText) findViewById(R.id.outbox_amout_et);
        outbox_size_et = (EditText) findViewById(R.id.outbox_size_et);
        outbox_weight_et = (EditText) findViewById(R.id.outbox_weight_et);
        outbox_weight__unit_ll = (LinearLayout) findViewById(R.id.outbox_weight__unit_ll);
        outbox_weight__unit_tv = (TextView) findViewById(R.id.outbox_weight__unit_tv);
        centerbox_amout_et = (EditText) findViewById(R.id.centerbox_amout_et);
        centerbox_size_et = (EditText) findViewById(R.id.centerbox_size_et);
        centerbox_weight_et = (EditText) findViewById(R.id.centerbox_weight_et);
        centerbox_weight_unit_ll = (LinearLayout) findViewById(R.id.centerbox_weight_unit_ll);
        centerbox_weight_unit_tv = (TextView) findViewById(R.id.centerbox_weight_unit_tv);
        next_bt = (Button) findViewById(R.id.next_bt);
        next_bt.setOnClickListener(this);
        fanhui_rl.setOnClickListener(this);
        goods_weight_unit_ll.setOnClickListener(this);
        outbox_weight__unit_ll.setOnClickListener(this);
        centerbox_weight_unit_ll.setOnClickListener(this);

    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        select();
        if (listforlanguage != null) {
            String tag = language.getTag();
            if (tag.equals("en")) {
                config.locale = Locale.US;
                resources.updateConfiguration(config, dm);

            } else if (tag.equals("ch")) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(config, dm);
            }
        }
    }

    // 查询数据库的方法
    private ArrayList<Language> select() {
        listforlanguage.clear();
        LanguageDao languageDao = new LanguageDao(this);
        Cursor cursor = languageDao.selectById("easyfair");
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String tag = cursor.getString(cursor.getColumnIndex("tag"));
            language = new Language(id, tag);
            listforlanguage.add(language);
        }
        cursor.close();
        return listforlanguage;

    }

    //得到上一个页面传递过来的数据
    private void getIntentData() {
        Intent intent = getIntent();
        goodsNumber = intent.getStringExtra("goodsNumber");
        goodsName = intent.getStringExtra("goodsName");
        goodsPrice = intent.getStringExtra("goodsPrice");
        goodsMaterial = intent.getStringExtra("goodsMaterial");
        goodsColor = intent.getStringExtra("goodsColor");

    }

    //得到当前用户输入的数据
    private void getCurrentData() {
        goodsShuLiang = goods_number_et.getText().toString().trim();
        goodsWeight = goods_weight_et.getText().toString().trim();
        goodsWeightUnit = goods_weight_unit_tv.getText().toString().trim();
        outboxAmout = outbox_amout_et.getText().toString().trim();
        outboxSize = outbox_size_et.getText().toString().trim();
        outboxWeight = outbox_weight_et.getText().toString().trim();
        outboxWeightUnit = outbox_weight__unit_tv.getText().toString().trim();
        centerboxAmout = centerbox_amout_et.getText().toString().trim();
        centerboxSize = centerbox_size_et.getText().toString().trim();
        centerboxWeight = centerbox_weight_et.getText().toString().trim();
        centerWeightUnit = centerbox_weight_unit_tv.getText().toString().trim();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回的监听
            case R.id.fanhui_rl:
                finish();
                break;
            //商品重量单位
            case R.id.goods_weight_unit_ll:
                showPopuWindow(goods_weight_unit_ll);
                break;
            //外箱重量单位
            case R.id.outbox_weight__unit_ll:
                showPopuWindow(outbox_weight__unit_ll);
                break;
            //中盒重量单位
            case R.id.centerbox_weight_unit_ll:
                showPopuWindow(centerbox_weight_unit_ll);
                break;
            //下一步
            case R.id.next_bt:
                getCurrentData();
                Intent intent = new Intent(Goods_templet_twoActivity.this, Goods_templet_threeActivity.class);
                intent.putExtra("goodsNumber", goodsNumber);
                intent.putExtra("goodsName", goodsName);
                intent.putExtra("goodsPrice", goodsPrice);
                intent.putExtra("goodsMaterial", goodsMaterial);
                intent.putExtra("goodsColor", goodsColor);

                intent.putExtra("goodsShuLiang", goodsShuLiang);//最少起订量
                intent.putExtra("goodsWeight", goodsWeight);//商品重量
                intent.putExtra("goodsWeightUnit", goodsWeightUnit);//商品重量单位

                //外箱包装
                intent.putExtra("outboxAmout", outboxAmout);//外箱包装数量
                intent.putExtra("outboxSize", outboxSize);//外箱包装尺寸
                intent.putExtra("outboxWeight", outboxWeight);//外箱包装重量
                intent.putExtra("outboxWeightUnit", outboxWeightUnit);//外箱包装重量单位

                //中盒包装
                intent.putExtra("centerboxAmout", centerboxAmout);//中盒包装数量
                intent.putExtra("centerboxSize", centerboxSize);//中盒包装尺寸
                intent.putExtra("centerboxWeight", centerboxWeight);//中盒包装重量
                intent.putExtra("centerWeightUnit", centerWeightUnit);//中盒包装重量单位

                startActivity(intent);
                break;
        }

    }

    private void showPopuWindow(final View v) {
        dropList.clear();
        dropList.add("mg");
        dropList.add("g");
        dropList.add("kg");
        dropList.add("t");
        View mV = LayoutInflater.from(Goods_templet_twoActivity.this).inflate(R.layout.listview_popupwindow, null);
        mV.setBackgroundColor(Color.WHITE);
        popupWindow = new PopupWindow(mV, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(mV);
        final ListView listview = (ListView) mV.findViewById(R.id.popup_lv);
        ArrayAdapter adapter = new ArrayAdapter(Goods_templet_twoActivity.this, android.R.layout.simple_list_item_1, dropList);
        listview.setAdapter(adapter);
        popupWindow.setFocusable(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = dropList.get(position);
                switch (v.getId()) {
                    case R.id.goods_weight_unit_ll:
                        goods_weight_unit_tv.setText(s);
                        break;
                    case R.id.outbox_weight__unit_ll:
                        outbox_weight__unit_tv.setText(s);
                        break;
                    case R.id.centerbox_weight_unit_ll:
                        centerbox_weight_unit_tv.setText(s);
                        break;
                }
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(false);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(Goods_templet_twoActivity.this, 0.5f);//0.0-1.0
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(Goods_templet_twoActivity.this, 1f);
            }
        });
    }

    // 设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }


}
