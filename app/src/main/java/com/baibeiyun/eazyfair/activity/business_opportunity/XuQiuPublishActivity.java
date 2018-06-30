package com.baibeiyun.eazyfair.activity.business_opportunity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.fragment.QuanQiuCaiGouShangFragment;
import com.baibeiyun.eazyfair.fragment.WoDeGongYingShangFragment;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.SystemBarTintManager;

import java.util.Locale;

public class XuQiuPublishActivity extends FragmentActivity implements View.OnClickListener {
    private TextView wodegongyinshang_tv, quanqiucaigoushang_tv;//两个Fragment的切换按钮
    private RelativeLayout fanhui_rl;//返回
    private TextView tv1, tv2;//切换Fragment时的下划线
    private WoDeGongYingShangFragment wdgys;//我的供应商
    private QuanQiuCaiGouShangFragment quanqiucaigoushang;//全行供应商
    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xu_qiu_publish);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.orange_color);//通知栏所需颜色
        }
        initYuyan();
        initview();
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(XuQiuPublishActivity.this);
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


    private void initview() {
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        fanhui_rl.setOnClickListener(XuQiuPublishActivity.this);
        wodegongyinshang_tv = (TextView) findViewById(R.id.wodegongyinshang_tv);
        wodegongyinshang_tv.setOnClickListener(this);
        quanqiucaigoushang_tv = (TextView) findViewById(R.id.quanqiucaigoushang_tv);
        quanqiucaigoushang_tv.setOnClickListener(this);
        tv1 = (TextView) findViewById(R.id.xuqiu_tv_line1);
        tv2 = (TextView) findViewById(R.id.xuqiu_tv_line2);
        tv2.setVisibility(View.INVISIBLE);
        wdgys = new WoDeGongYingShangFragment();
        addFragment(wdgys);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
            case R.id.wodegongyinshang_tv:
                wdgys = new WoDeGongYingShangFragment();
                addFragment(wdgys);
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.INVISIBLE);
                wodegongyinshang_tv.setTextColor(getResources().getColor(R.color.orange_color));
                quanqiucaigoushang_tv.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.quanqiucaigoushang_tv:
                quanqiucaigoushang = new QuanQiuCaiGouShangFragment();
                addFragment(quanqiucaigoushang, 2);
                tv1.setVisibility(View.INVISIBLE);
                tv2.setVisibility(View.VISIBLE);
                quanqiucaigoushang_tv.setTextColor(getResources().getColor(R.color.orange_color));
                wodegongyinshang_tv.setTextColor(getResources().getColor(R.color.black));
                break;
        }
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.xuqiu_fragment, fragment);
        ft.commit();
    }

    private void addFragment(Fragment fragment, int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("tag", type);
        fragment.setArguments(bundle);
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.xuqiu_fragment, fragment, "22");
        ft.commit();
    }

}
