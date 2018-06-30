package com.baibeiyun.eazyfair.activity.myaccount;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.dao.LanguageDao;
import com.baibeiyun.eazyfair.entities.Language;

import java.util.ArrayList;
import java.util.Locale;

public class Goods_templet_oneActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    private EditText goods_number_et;//商品货号
    private EditText goods_name_et;//商品名称
    private EditText goods_price_et;//商品价格
    private EditText material_et;//商品材料
    private EditText goods_color_et;//商品颜色
    private Button next_bt;//下一步

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private ArrayList<Language> listforlanguage = new ArrayList<>();
    private Language language;

    //得到当前用户输入的数据
    private String goodsNumber, goodsName, goodsPrice, goodsMaterial, goodsColor;

    public static Goods_templet_oneActivity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_templet_one);
        activity=this;
        initYuyan();
        initview();

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

    private void initview() {
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        goods_number_et = (EditText) findViewById(R.id.goods_number_et);
        goods_name_et = (EditText) findViewById(R.id.goods_name_et);
        goods_price_et = (EditText) findViewById(R.id.goods_price_et);
        material_et = (EditText) findViewById(R.id.material_et);
        goods_color_et = (EditText) findViewById(R.id.goods_color_et);
        next_bt = (Button) findViewById(R.id.next_bt);
        fanhui_rl.setOnClickListener(Goods_templet_oneActivity.this);
        next_bt.setOnClickListener(Goods_templet_oneActivity.this);
    }

    //获得用户当前输入的数据
    private void getData() {
        goodsNumber = goods_number_et.getText().toString().trim();
        goodsName = goods_name_et.getText().toString().trim();
        goodsPrice = goods_price_et.getText().toString().trim();
        goodsMaterial = material_et.getText().toString().trim();
        goodsColor = goods_color_et.getText().toString().trim();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
            case R.id.next_bt:
                getData();
                Intent  intent = new Intent(Goods_templet_oneActivity.this, Goods_templet_twoActivity.class);
                intent.putExtra("goodsNumber", goodsNumber);
                intent.putExtra("goodsName", goodsName);
                intent.putExtra("goodsPrice", goodsPrice);
                intent.putExtra("goodsMaterial", goodsMaterial);
                intent.putExtra("goodsColor", goodsColor);
                startActivity(intent);
                break;
        }

    }
}
