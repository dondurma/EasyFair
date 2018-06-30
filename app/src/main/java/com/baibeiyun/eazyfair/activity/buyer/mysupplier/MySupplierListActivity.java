package com.baibeiyun.eazyfair.activity.buyer.mysupplier;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivityforBuyer;
import com.baibeiyun.eazyfair.adapter.CustomListAdapter;
import com.baibeiyun.eazyfair.dao.EasyInquiryDao;
import com.baibeiyun.eazyfair.dao.MyCustomerDao;
import com.baibeiyun.eazyfair.dao.MyInquiryDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCustomer;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

/**
 * 供应商列表
 *
 * @author RuanWei
 * @date 2017/1/6
 **/
public class MySupplierListActivity extends BaseActivityforBuyer implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    private RelativeLayout find_supplier_rl;//扫一扫 或手动添加供应商
    private EditText supplier_name_et;//输入供应商或者公司名
    private ListView supplier_list_lv;//供应商列表
    private CustomListAdapter customlistadapter;
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();
    private PopupWindow mPopupWindow;
    private int from = 0;
    private int statusBarHeight;
    private ArrayList<MyCustomer> myCustomers = new ArrayList<>();
    private MyCustomer myCustomer;
    private int flag;

    private String search;
    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;
    private Map<String, Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_supplier_list);
        initYuyan();
        initview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (datas != null) {
            datas.clear();
            datas = null;
        }
        if (myCustomers != null) {
            myCustomers.clear();
            myCustomers = null;
        }

    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(MySupplierListActivity.this);
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


    @Override
    protected void onStart() {
        super.onStart();
        datas.clear();
        myCustomers = CursorUtils.selectTypeBySupplier(MySupplierListActivity.this);
        getData();
        customlistadapter.notifyDataSetChanged();
    }

    private void initview() {
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        find_supplier_rl = (RelativeLayout) findViewById(R.id.find_supplier_rl);
        supplier_name_et = (EditText) findViewById(R.id.supplier_name_et);
        supplier_list_lv = (ListView) findViewById(R.id.supplier_list_lv);
        getData();
        customlistadapter = new CustomListAdapter(datas, MySupplierListActivity.this);
        supplier_list_lv.setAdapter(customlistadapter);
        fanhui_rl.setOnClickListener(MySupplierListActivity.this);
        find_supplier_rl.setOnClickListener(MySupplierListActivity.this);
        //item的点击跳转详情页面
        supplier_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = (Map<String, Object>) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(MySupplierListActivity.this, MySupplierActivity.class);
                int id = (int) map.get("id");//得到当前点击的item的表的id
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        //item的长按删除事件
        supplier_list_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                flag = i;
                map = (Map<String, Object>) adapterView.getItemAtPosition(i);
                showAlertDialog(MySupplierListActivity.this, R.string.determine_sc);
                return true;
            }
        });
        supplier_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search = supplier_name_et.getText().toString().trim();
                myCustomers.clear();
                datas.clear();
                myCustomers = CursorUtils.findDataByLikeforSupplier(MySupplierListActivity.this, search);
                if (CursorUtils.findDataByLikeforSupplier(MySupplierListActivity.this, search).isEmpty()) {
                    myCustomers = CursorUtils.findDataByLike2forSupplier(MySupplierListActivity.this, search);
                }
                getData();
                customlistadapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
            //扫一扫或者手动添加供应商
            case R.id.find_supplier_rl:
                from = Location.TOP.ordinal();
                //调用此方法，menu不会顶置
                initPopupWindow();
                break;

        }
    }

    //构建数据源
    private void getData() {
        for (int i = 0; i < myCustomers.size(); i++) {
            myCustomer = myCustomers.get(i);
            Map<String, Object> data = new ArrayMap<>();
            if (myCustomer.getCompany_logo() != null) {
                byte[] company_logo = myCustomer.getCompany_logo();
                Bitmap bitmap = BitmapUtils.Bytes2Bimap(company_logo);
                data.put("custom_logo_iv", bitmap);
            } else {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_pic);
                data.put("custom_logo_iv", bitmap);
            }
            data.put("custom_name_tv", myCustomer.getName());
            data.put("custom_company_tv", myCustomer.getCompany_name());
            data.put("id", myCustomer.get_id());
            data.put("uniqued_id", myCustomer.getUnique_id());
            datas.add(data);
        }

    }

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
        backgroundAlpha(MySupplierListActivity.this, 0.5f);
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
                Intent intent = new Intent(MySupplierListActivity.this, SelectQRcodeorOCRForBuyerActivity.class);
                startActivity(intent);
                mPopupWindow.dismiss();
            }
        });
        //手动添加的监听事件
        shoudong_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MySupplierListActivity.this, NewSupplierActivity.class);
                startActivity(intent);
                mPopupWindow.dismiss();
            }
        });
    }

    //上传图片的PopupWindow弹出的方向
    private enum Location {
        TOP
    }

    //设置添加屏幕的背景透明度
    private void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    //添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
    private class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(MySupplierListActivity.this, 1f);
        }

    }

    //获取屏幕状态栏的高度的方法
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        // 状态栏高度
        statusBarHeight = frame.top;
    }


    public void showAlertDialog(final Context context, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.determine, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                datas.remove(flag);
                customlistadapter.notifyDataSetChanged();
                //创建一个方法层的对象
                MyCustomerDao myCustomerDao = new MyCustomerDao(MySupplierListActivity.this);
                int id = (int) map.get("id");
                myCustomer.set_id(id);
                myCustomerDao.deletdata(myCustomer);

                MyInquiryDao myInquiryDao = new MyInquiryDao(MySupplierListActivity.this);
                String uniqued_id = (String) map.get("uniqued_id");
                myInquiryDao.deletdata2(uniqued_id);


                EasyInquiryDao easyInquiryDao = new EasyInquiryDao(MySupplierListActivity.this);
                easyInquiryDao.deleteByUniqued_id(uniqued_id);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


}
