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

import java.util.Locale;

public class ExhibitionDetailActivity extends BaseActivityforSJ implements View.OnClickListener {
    private RelativeLayout fanhui_rl;
    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private String title, address,chargeStandard, city, contact, introduction,organiser, pavilionName, range, state, telephone, type,endTime,startTime;

    private TextView title_tv;
    private TextView start_time_tv;
    private TextView end_time_tv;
    private TextView introduction_tv;
    private TextView address_tv;
    private TextView organiser_tv;
    private TextView type_tv;
    private TextView city_tv;
    private TextView pavilion_name_tv;
    private TextView range_tv;
    private TextView charge_standard_tv;
    private TextView contact_tv;
    private TextView telephone_tv;
    private TextView state_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_detail);
        initYuyan();
        initView();
        initData();
    }

    private void initData() {
        title_tv.setText(title);
        start_time_tv.setText(startTime+"");
        end_time_tv.setText(endTime+"");
        introduction_tv.setText(introduction);
        address_tv.setText(address);
        organiser_tv.setText(organiser);
        type_tv.setText(type);
        city_tv.setText(city);
        pavilion_name_tv.setText(pavilionName);
        range_tv.setText(range);
        charge_standard_tv.setText(chargeStandard);
        contact_tv.setText(contact);
        telephone_tv.setText(telephone);
        if ("0".equals(state)) {
            state_tv.setText("未举办");
        } else if ("1".equals(state)) {
            state_tv.setText("正在举办中");
        } else if ("2".equals(state)) {
            state_tv.setText("已举办");
        }

    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(ExhibitionDetailActivity.this);
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
        title_tv = (TextView) findViewById(R.id.title_tv);
        start_time_tv = (TextView) findViewById(R.id.start_time_tv);
        end_time_tv = (TextView) findViewById(R.id.end_time_tv);
        introduction_tv = (TextView) findViewById(R.id.introduction_tv);
        address_tv = (TextView) findViewById(R.id.address_tv);
        organiser_tv = (TextView) findViewById(R.id.organiser_tv);
        type_tv = (TextView) findViewById(R.id.type_tv);
        city_tv = (TextView) findViewById(R.id.city_tv);
        pavilion_name_tv = (TextView) findViewById(R.id.pavilion_name_tv);
        range_tv = (TextView) findViewById(R.id.range_tv);
        charge_standard_tv = (TextView) findViewById(R.id.charge_standard_tv);
        contact_tv = (TextView) findViewById(R.id.contact_tv);
        telephone_tv = (TextView) findViewById(R.id.telephone_tv);
        state_tv = (TextView) findViewById(R.id.state_tv);

        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        fanhui_rl.setOnClickListener(ExhibitionDetailActivity.this);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        endTime = intent.getStringExtra("endTime");
        startTime = intent.getStringExtra("startTime");
        address = intent.getStringExtra("address");
        chargeStandard = intent.getStringExtra("chargeStandard");
        city = intent.getStringExtra("city");
        contact = intent.getStringExtra("contact");
        introduction = intent.getStringExtra("introduction");
        organiser = intent.getStringExtra("organiser");
        pavilionName = intent.getStringExtra("pavilionName");
        range = intent.getStringExtra("range");
        state = intent.getStringExtra("state");
        telephone = intent.getStringExtra("telephone");
        type = intent.getStringExtra("type");

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
