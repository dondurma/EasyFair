package com.baibeiyun.eazyfair.activity.business_opportunity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivityforSJ;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.utils.CursorUtils;

import java.util.Locale;

public class BusinessActivity extends BaseActivityforSJ implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    private Button goods_publish_bt;//商品发布
    private Button xuqiu_publish_bt;//需求发布
    private Button exhibition_info_bt;//展会信息

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        initYuyan();
        initView();
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(BusinessActivity.this);
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
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        xuqiu_publish_bt = (Button) findViewById(R.id.xuqiu_publish_bt);
        exhibition_info_bt = (Button) findViewById(R.id.exhibition_info_bt);
        goods_publish_bt = (Button) findViewById(R.id.goods_publish_bt);
        fanhui_rl.setOnClickListener(BusinessActivity.this);
        xuqiu_publish_bt.setOnClickListener(BusinessActivity.this);
        exhibition_info_bt.setOnClickListener(BusinessActivity.this);
        goods_publish_bt.setOnClickListener(BusinessActivity.this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            //返回
            case R.id.fanhui_rl:
                finish();
                break;
            //商品发布
            case R.id.goods_publish_bt:
                intent = new Intent(BusinessActivity.this, GoodsPublishActivity.class);
                startActivity(intent);
                break;
            //需求发布
            case R.id.xuqiu_publish_bt:
                intent = new Intent(BusinessActivity.this, XuQiuPublishActivity.class);
                startActivity(intent);
                break;
            //展会信息
            case R.id.exhibition_info_bt:
                intent = new Intent(BusinessActivity.this, ExhibitionInfoActivity.class);
                startActivity(intent);
                break;
        }

    }
}
