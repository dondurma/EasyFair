package com.baibeiyun.eazyfair.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.utils.CursorUtils;

import java.util.ArrayList;
import java.util.Locale;

public class AgreementActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;
    private WebView agreement_wb;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        initYuyan();
        initview();
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(AgreementActivity.this);
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
        fanhui_rl.setOnClickListener(AgreementActivity.this);
        agreement_wb = (WebView) findViewById(R.id.agreement_wb);
        agreement_wb.loadUrl("file:///android_asset/agreement.html");
        //支持JavaScript
        agreement_wb.getSettings().setJavaScriptEnabled(true);
        //自适应屏幕
        agreement_wb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        agreement_wb.getSettings().setLoadWithOverviewMode(true);
        //设置可以支持缩放
        agreement_wb.getSettings().setSupportZoom(true);
        //扩大比例的缩放
        agreement_wb.getSettings().setUseWideViewPort(true);
        //设置是否出现缩放工具
        agreement_wb.getSettings().setBuiltInZoomControls(true);
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
