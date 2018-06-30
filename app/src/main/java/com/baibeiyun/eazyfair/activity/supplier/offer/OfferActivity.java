package com.baibeiyun.eazyfair.activity.supplier.offer;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.activity.supplier.mycustomer.NewCustomActivity;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.utils.CursorUtils;

import java.util.Locale;

public class OfferActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;
    private Button custom_bt, new_custom_bt;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        initYuyan();
        initview();
    }


    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(OfferActivity.this);
        if (language != null) {
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


    private void initview() {
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        custom_bt = (Button) findViewById(R.id.custom_bt);
        new_custom_bt = (Button) findViewById(R.id.new_custom_bt);
        fanhui_rl.setOnClickListener(OfferActivity.this);
        custom_bt.setOnClickListener(OfferActivity.this);
        new_custom_bt.setOnClickListener(OfferActivity.this);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
            case R.id.custom_bt:
                intent = new Intent(OfferActivity.this, CustomListActivity.class);
                startActivity(intent);
                break;
            case R.id.new_custom_bt:
                intent = new Intent(OfferActivity.this, NewCustomActivity.class);
                startActivity(intent);
                break;
        }
    }
}
