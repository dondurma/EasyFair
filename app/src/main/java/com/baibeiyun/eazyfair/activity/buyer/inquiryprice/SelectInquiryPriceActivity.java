package com.baibeiyun.eazyfair.activity.buyer.inquiryprice;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivityforBuyer;
import com.baibeiyun.eazyfair.activity.buyer.mysupplier.NewSupplierActivity;
import com.baibeiyun.eazyfair.dao.LanguageDao;
import com.baibeiyun.eazyfair.entities.Language;

import java.util.ArrayList;
import java.util.Locale;

//询价
public class SelectInquiryPriceActivity extends BaseActivityforBuyer implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    private Button supplier_bt;//供应商
    private Button new_supplier_bt;//新建供应商

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private ArrayList<Language> listforlanguage = new ArrayList<>();
    Language language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_inquiry_price);
        initYuyan();
        initview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listforlanguage != null) {
            listforlanguage.clear();
            listforlanguage = null;
        }
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
        supplier_bt = (Button) findViewById(R.id.supplier_bt);
        new_supplier_bt = (Button) findViewById(R.id.new_supplier_bt);

        fanhui_rl.setOnClickListener(SelectInquiryPriceActivity.this);
        supplier_bt.setOnClickListener(SelectInquiryPriceActivity.this);
        new_supplier_bt.setOnClickListener(SelectInquiryPriceActivity.this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            //返回
            case R.id.fanhui_rl:
                finish();
                break;
            //供应商
            case R.id.supplier_bt:
                intent = new Intent(SelectInquiryPriceActivity.this, SupplierListForInquiryPriceActivity.class);
                startActivity(intent);
                break;
            //新建供应商
            case R.id.new_supplier_bt:
                intent = new Intent(SelectInquiryPriceActivity.this, NewSupplierActivity.class);
                startActivity(intent);
                break;


        }
    }
}
