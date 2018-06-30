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
import com.baibeiyun.eazyfair.fragment.WoDeKeHuFragment;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.SystemBarTintManager;

import java.util.Locale;

public class GoodsPublishActivity extends FragmentActivity implements View.OnClickListener {
    private TextView wodekehu_tv, quanqiucaigoushang_tv;
    private RelativeLayout fanhui_rl;
    private WoDeKeHuFragment woDeKeHuFragment;//我的客户Fragment
    private QuanQiuCaiGouShangFragment quanqiucaigoushang;//全行业采购商Fragment
    //下划线
    private TextView lineTv1;
    private TextView lineTv2;
    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_publish);
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
        language = CursorUtils.selectYuYan(GoodsPublishActivity.this);
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
        lineTv1 = (TextView) findViewById(R.id.tv_line1);
        lineTv2 = (TextView) findViewById(R.id.tv_line2);
        lineTv2.setVisibility(View.INVISIBLE);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        fanhui_rl.setOnClickListener(GoodsPublishActivity.this);
        wodekehu_tv = (TextView) findViewById(R.id.wodekehu_tv);
        wodekehu_tv.setOnClickListener(this);
        quanqiucaigoushang_tv = (TextView) findViewById(R.id.quanqiucaigoushang_tv);
        quanqiucaigoushang_tv.setOnClickListener(this);
        woDeKeHuFragment = new WoDeKeHuFragment();
        addFragment(woDeKeHuFragment);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
            case R.id.wodekehu_tv:
                woDeKeHuFragment = new WoDeKeHuFragment();
                addFragment(woDeKeHuFragment);
                lineTv1.setVisibility(View.VISIBLE);
                lineTv2.setVisibility(View.INVISIBLE);
                wodekehu_tv.setTextColor(getResources().getColor(R.color.orange_color));
                quanqiucaigoushang_tv.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.quanqiucaigoushang_tv:
                quanqiucaigoushang = new QuanQiuCaiGouShangFragment();
                addFragment(quanqiucaigoushang, 1);
                lineTv1.setVisibility(View.INVISIBLE);
                lineTv2.setVisibility(View.VISIBLE);
                quanqiucaigoushang_tv.setTextColor(getResources().getColor(R.color.orange_color));
                wodekehu_tv.setTextColor(getResources().getColor(R.color.black));
                break;

        }
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, fragment);
        ft.commit();
    }

    private void addFragment(Fragment fragment, int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("tag", type);
        fragment.setArguments(bundle);
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, fragment, "22");
        ft.commit();
    }
}
