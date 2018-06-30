package com.baibeiyun.eazyfair.activity.business_opportunity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivityforSJ;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.TimeUtils;

import java.util.Locale;

public class BusinessTheWordSupplierActivity extends BaseActivityforSJ implements View.OnClickListener {
    private Intent intent;
    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private RelativeLayout fanhui_rl;
    private TextView goods_name_tv;
    private TextView goods_price_tv;
    private TextView publish_time_tv;
    private TextView goods_type_tv;
    private TextView send_type_tv;
    private TextView goods_introduce_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_the_word_gy);
        initYuyan();
        initView();
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(BusinessTheWordSupplierActivity.this);
        if (language != null) {
            String tag = this.language.getTag();
            if (tag.equals("en")) {
                config.locale = Locale.US;
                resources.updateConfiguration(config, dm);

            } else if (tag.equals("ch")) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(config, dm);
            }
        }
    }


    private void initView() {
        goods_name_tv = (TextView) findViewById(R.id.goods_name_tv);
        goods_price_tv = (TextView) findViewById(R.id.goods_price_tv);
        publish_time_tv = (TextView) findViewById(R.id.publish_time_tv);
        goods_type_tv = (TextView) findViewById(R.id.goods_type_tv);
        send_type_tv = (TextView) findViewById(R.id.send_type_tv);
        goods_introduce_tv = (TextView) findViewById(R.id.goods_introduce_tv);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        fanhui_rl.setOnClickListener(this);
        intent = getIntent();
        String introduction = intent.getStringExtra("introduction");
        String productName = intent.getStringExtra("productName");
        String industryType = intent.getStringExtra("industryType");
        String priceRange = intent.getStringExtra("priceRange");
        long pubDate = intent.getLongExtra("pubDate", 0);
        String sendMode = intent.getStringExtra("sendMode");

        goods_name_tv.setText(productName);
        goods_price_tv.setText(priceRange);
        publish_time_tv.setText(TimeUtils.timeFormat(pubDate));
        goods_type_tv.setText(industryType);
        send_type_tv.setText(sendMode);
        goods_introduce_tv.setText(introduction);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
        }
    }
}
