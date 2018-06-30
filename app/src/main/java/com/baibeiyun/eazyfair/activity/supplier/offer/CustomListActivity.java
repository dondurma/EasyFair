package com.baibeiyun.eazyfair.activity.supplier.offer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.activity.supplier.mycustomer.NewCustomActivity;
import com.baibeiyun.eazyfair.activity.supplier.mycustomer.SelectQRcodeorOCRActivity;
import com.baibeiyun.eazyfair.adapter.OfferCustomListAdapter;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCustomer;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class CustomListActivity extends BaseActivity implements View.OnClickListener {
    private ListView custom_list_lv;
    private RelativeLayout fanhui_rl;//返回
    private EditText input_commpany_name_et;//输入内容
    private OfferCustomListAdapter offerCustomListAdapter;
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();
    private ArrayList<MyCustomer> list = new ArrayList<>();

    private String search;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private RelativeLayout new_custom_rl;

    private int from = 0;
    private PopupWindow mPopupWindow;

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
        offerCustomListAdapter = new OfferCustomListAdapter(datas, this);
        custom_list_lv.setAdapter(offerCustomListAdapter);
    }


    private void getData() {
        datas.clear();
        for (MyCustomer s : list) {
            Map<String, Object> data = new ArrayMap<>();
            if (s.getCompany_logo() != null) {
                byte[] company_logo = s.getCompany_logo();
                Bitmap bitmap = BitmapUtils.Bytes2Bimap(company_logo);
                data.put("logo_iv", bitmap);
            } else {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_pic);
                data.put("logo_iv", bitmap);
            }
            data.put("commpany_name_tv", s.getCompany_name());
            data.put("_id", s.get_id());
            datas.add(data);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list2);
        initYuyan();
        initview();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (datas != null) {
            datas.clear();
            datas = null;
        }
        if (list != null) {
            list.clear();
            list = null;
        }

    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(CustomListActivity.this);
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
        new_custom_rl = (RelativeLayout) findViewById(R.id.new_custom_rl);
        custom_list_lv = (ListView) findViewById(R.id.custom_list_lv);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        input_commpany_name_et = (EditText) findViewById(R.id.input_commpany_name_et);
        fanhui_rl.setOnClickListener(CustomListActivity.this);
        new_custom_rl.setOnClickListener(this);
        //根据公司名来模糊查询
        input_commpany_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search = charSequence.toString().trim();
                list.clear();
                list = CursorUtils.selectMoHuBuyer(CustomListActivity.this, search);
                getData();
                offerCustomListAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initData() {
        getData();
        offerCustomListAdapter = new OfferCustomListAdapter(datas, this);
        custom_list_lv.setAdapter(offerCustomListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        list = CursorUtils.selectAllBuyer(CustomListActivity.this);
        getData();
        offerCustomListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.fanhui_rl:
                finish();
                break;
            //新建客户
            case R.id.new_custom_rl:
                from = Location.TOP.ordinal();
                //调用此方法，menu不会顶置
                initPopupWindow();
                break;
        }
    }


    //上传图片的PopupWindow弹出的方向
    public enum Location {
        TOP
    }

    //设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    //添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
    private class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(CustomListActivity.this, 1f);
        }

    }

    //新建客户弹出的popupwindow
    private void initPopupWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.popup_customlist, null);
        if (Location.TOP.ordinal() == from) {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        } else {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        }
        //动画效果
        if (Location.TOP.ordinal() == from) {
            mPopupWindow.setAnimationStyle(R.style.AnimationTopFade);
        }
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        mPopupWindow.setBackgroundDrawable(dw);
        if (Location.TOP.ordinal() == from) {
            mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_custom_list, null), Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(CustomListActivity.this, 0.5f);
        //关闭事件
        mPopupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        LinearLayout saoyisao_ll = (LinearLayout) popupWindowView.findViewById(R.id.saoyisao_ll);
        LinearLayout shoudong_ll = (LinearLayout) popupWindowView.findViewById(R.id.shoudong_ll);
        //扫一扫的监听事件
        saoyisao_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomListActivity.this, SelectQRcodeorOCRActivity.class);
                startActivity(intent);
                mPopupWindow.dismiss();
            }
        });
        //手动添加的监听事件
        shoudong_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomListActivity.this, NewCustomActivity.class);
                startActivity(intent);
                mPopupWindow.dismiss();
            }
        });
    }

}
