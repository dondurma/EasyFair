package com.baibeiyun.eazyfair.activity.myaccount;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.adapter.AboutInfoAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AboutDetailsActivity extends BaseActivity implements View.OnClickListener {
    private TextView function_tv;//功能介绍
    private TextView contract_phone_tv;//联系客服电话
    private RelativeLayout contract_phone_rl;
    private RelativeLayout back_rl;
    private Intent intent;
    private ListView info_list;
    private List<String> infoList = new ArrayList<>();
    private AboutInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_details);
        initView();
        initEvent();
    }

    private void initEvent() {
        contract_phone_tv.setOnClickListener(this);
        back_rl.setOnClickListener(this);
    }

    private void initView() {
        intent = getIntent();
        int type = intent.getIntExtra("type", 0);
        String contract = intent.getStringExtra("details");
        info_list = (ListView) findViewById(R.id.info_list);
        back_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        function_tv = (TextView) findViewById(R.id.function_introduce);
        contract_phone_tv = (TextView) findViewById(R.id.contract_phone);
        contract_phone_rl = (RelativeLayout) findViewById(R.id.contract_rl);
        if (1 == type) {
            function_tv.setVisibility(View.VISIBLE);
            function_tv.setText("\u3000\u3000" + contract);
            contract_phone_rl.setVisibility(View.GONE);
            info_list.setVisibility(View.GONE);
        } else if (2 == type) {
            contract_phone_rl.setVisibility(View.VISIBLE);
            contract_phone_tv.setText(contract);
            function_tv.setVisibility(View.GONE);
            info_list.setVisibility(View.GONE);
        } else if (3 == type) {
            contract_phone_rl.setVisibility(View.GONE);
            function_tv.setVisibility(View.GONE);
            info_list.setVisibility(View.VISIBLE);
            infoList.clear();
            String[] split = contract.split("\\|");
            Collections.addAll(infoList, split);
            adapter = new AboutInfoAdapter(AboutDetailsActivity.this, infoList);
            info_list.setAdapter(adapter);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contract_phone:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contract_phone_tv.getText().toString()));
                startActivity(intent);
                break;
            case R.id.fanhui_rl:
                finish();
                break;
        }
    }
}
