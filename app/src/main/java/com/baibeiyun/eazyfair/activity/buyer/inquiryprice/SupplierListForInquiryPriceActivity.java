package com.baibeiyun.eazyfair.activity.buyer.inquiryprice;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivityforBuyer;
import com.baibeiyun.eazyfair.activity.buyer.mysupplier.NewSupplierActivity;
import com.baibeiyun.eazyfair.activity.buyer.mysupplier.SelectQRcodeorOCRForBuyerActivity;
import com.baibeiyun.eazyfair.activity.supplier.offer.CustomListActivity;
import com.baibeiyun.eazyfair.adapter.SupplierListForInquiryPriceAdapter;
import com.baibeiyun.eazyfair.dao.MyCustomerDao;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCustomer;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class SupplierListForInquiryPriceActivity extends BaseActivityforBuyer implements View.OnClickListener {
    private RelativeLayout fanhui_iv;//返回
    private EditText name_or_companyname_et;//输入供应商名称或公司名称
    private ListView supplier_list_lv;
    //声明一个适配器对象
    private SupplierListForInquiryPriceAdapter supplierListForInquiryPriceAdapter;
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();
    private ArrayList<MyCustomer> list = new ArrayList<>();
    private MyCustomer myCustomer;
    private String search;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;
    private RelativeLayout buildgoods;

    private int from = 0;
    private PopupWindow mPopupWindow;

    //构建数据源
    private void getData() {
        if (datas != null) {
            datas.clear();
        }
        for (MyCustomer s : list) {
            Map<String, Object> data = new ArrayMap<>();
            if (s.getCompany_logo() != null) {
                byte[] company_logo = s.getCompany_logo();
                Bitmap bitmap = BitmapUtils.Bytes2Bimap(company_logo);
                data.put("supplier_logo_iv", bitmap);
            } else {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_pic);
                data.put("supplier_logo_iv", bitmap);
            }
            data.put("supplier_tv", s.getName());
            data.put("supplier_commpany_name_tv", s.getCompany_name());
            data.put("unique_id", s.getUnique_id());
            data.put("_id", s.get_id());
            datas.add(data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_list_for_inquiry_price);
        initYuyan();
        initview();
        init();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
        supplierListForInquiryPriceAdapter = new SupplierListForInquiryPriceAdapter(datas, this);
        supplier_list_lv.setAdapter(supplierListForInquiryPriceAdapter);
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(this);
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
        fanhui_iv = (RelativeLayout) findViewById(R.id.fanhui_iv);
        name_or_companyname_et = (EditText) findViewById(R.id.name_or_companyname_et);
        supplier_list_lv = (ListView) findViewById(R.id.supplier_list_lv);
        buildgoods = (RelativeLayout) findViewById(R.id.buildgoods);
        buildgoods.setOnClickListener(this);
        fanhui_iv.setOnClickListener(SupplierListForInquiryPriceActivity.this);
        name_or_companyname_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search = charSequence.toString().trim();
                list.clear();
                findDataByLike();
                if (findDataByLike().isEmpty()) {
                    findDataByLike2();
                }
                datas.clear();
                getData();
                supplierListForInquiryPriceAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        datas.clear();
        SelectforTypebyBuyer();
        getData();
        supplierListForInquiryPriceAdapter.notifyDataSetChanged();
    }

    private void init() {
        getData();
        supplierListForInquiryPriceAdapter = new SupplierListForInquiryPriceAdapter(datas, this);
        supplier_list_lv.setAdapter(supplierListForInquiryPriceAdapter);

        supplier_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = (Map<String, Object>) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(SupplierListForInquiryPriceActivity.this, InquiryPriceActivity.class);
                int id = (int) map.get("_id");
                intent.putExtra("customer_id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui_iv:
                finish();
                break;
            //新建商品
            case R.id.buildgoods:
                from = CustomListActivity.Location.TOP.ordinal();
                //调用此方法，menu不会顶置
                initPopupWindow();
                break;

        }
    }


    //从数据库中查询客户的信息
    private ArrayList<MyCustomer> SelectforTypebyBuyer() {
        list.clear();
        //创建一个dao层方法的对象
        MyCustomerDao myCustomerDao = new MyCustomerDao(SupplierListForInquiryPriceActivity.this);
        //调用对象查询的方法
        Cursor cursor = myCustomerDao.SelectforTypeForBuyer();
        //遍历游标
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String company_name = cursor.getString(cursor.getColumnIndex("company_name"));
            byte[] company_logos = cursor.getBlob(cursor.getColumnIndex("company_logo"));
            String unique_id = cursor.getString(cursor.getColumnIndex("unique_id"));

            myCustomer = new MyCustomer(id, name, company_name, company_logos, unique_id);
            //将myCustomer存入集合
            list.add(myCustomer);
        }
        cursor.close();
        return list;
    }


    //模糊查询的方法
    public ArrayList<MyCustomer> findDataByLike() {
        list.clear();
        MyCustomerDao myCustomerDao = new MyCustomerDao(this);
        Cursor mycustomer = myCustomerDao.QueryByLikeforBuyer(search);
        while (mycustomer.moveToNext()) {
            int id = mycustomer.getInt(mycustomer.getColumnIndex("_id"));
            String name = mycustomer.getString(mycustomer.getColumnIndex("name"));
            String company_name = mycustomer.getString(mycustomer.getColumnIndex("company_name"));
            byte[] company_logos = mycustomer.getBlob(mycustomer.getColumnIndex("company_logo"));
            String unique_id = mycustomer.getString(mycustomer.getColumnIndex("unique_id"));
            myCustomer = new MyCustomer(id, name, company_name, company_logos, unique_id);
            list.add(myCustomer);
        }
        mycustomer.close();
        return list;

    }

    //模糊查询的方法
    public ArrayList<MyCustomer> findDataByLike2() {
        list.clear();
        MyCustomerDao myCustomerDao = new MyCustomerDao(this);
        Cursor mycustomer = myCustomerDao.QueryByLikeforBuyer2(search);
        while (mycustomer.moveToNext()) {
            int id = mycustomer.getInt(mycustomer.getColumnIndex("_id"));
            String name = mycustomer.getString(mycustomer.getColumnIndex("name"));
            String company_name = mycustomer.getString(mycustomer.getColumnIndex("company_name"));
            byte[] company_logos = mycustomer.getBlob(mycustomer.getColumnIndex("company_logo"));
            String unique_id = mycustomer.getString(mycustomer.getColumnIndex("unique_id"));
            myCustomer = new MyCustomer(id, name, company_name, company_logos, unique_id);
            list.add(myCustomer);
        }
        mycustomer.close();
        return list;

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
    class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(SupplierListForInquiryPriceActivity.this, 1f);
        }

    }

    //新建供应商弹出的popupwindow
    private void initPopupWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.popup_customlist, null);
        if (CustomListActivity.Location.TOP.ordinal() == from) {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        } else {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        }
        //动画效果
        if (CustomListActivity.Location.TOP.ordinal() == from) {
            mPopupWindow.setAnimationStyle(R.style.AnimationTopFade);
        }
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        mPopupWindow.setBackgroundDrawable(dw);
        if (CustomListActivity.Location.TOP.ordinal() == from) {
            mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_custom_list, null), Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(SupplierListForInquiryPriceActivity.this, 0.5f);
        //关闭事件
        mPopupWindow.setOnDismissListener(new SupplierListForInquiryPriceActivity.popupDismissListener());
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
                Intent intent = new Intent(SupplierListForInquiryPriceActivity.this, SelectQRcodeorOCRForBuyerActivity.class);
                startActivity(intent);
                mPopupWindow.dismiss();
            }
        });
        //手动添加的监听事件
        shoudong_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SupplierListForInquiryPriceActivity.this, NewSupplierActivity.class);
                startActivity(intent);
                mPopupWindow.dismiss();
            }
        });
    }

}
