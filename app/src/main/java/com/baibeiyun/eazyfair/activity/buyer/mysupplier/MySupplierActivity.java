package com.baibeiyun.eazyfair.activity.buyer.mysupplier;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivityforBuyer;
import com.baibeiyun.eazyfair.activity.buyer.inquiryprice.DetailforInquiryActivity;
import com.baibeiyun.eazyfair.activity.buyer.inquiryprice.EasyInquiryActivity;
import com.baibeiyun.eazyfair.activity.buyer.inquiryprice.InquiryPriceActivity;
import com.baibeiyun.eazyfair.adapter.XunjiaPriceAdapter;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCustomer;
import com.baibeiyun.eazyfair.entities.MyInquiry;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.OtherUtils;
import com.baibeiyun.eazyfair.utils.ToastUtil;
import com.baibeiyun.eazyfair.view.RoundImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

/**
 * 我是采购商 我的供应商 报价记录
 *
 * @author RuanWei
 * @date 2016/12/10
 **/
public class MySupplierActivity extends BaseActivityforBuyer implements View.OnClickListener {
    // titlebar部分
    private RelativeLayout fanhui_rl;//返回

    //第一模块部分
    private TextView supplier_name_tv;//供应商名字
    private Button supplier_card_bt;//供应商名片
    private RoundImageView supplier_company_logo_iv;//供应商logo
    private TextView supplier_company_name_tv;//供应商公司名


    // 第二模块
    //  供应商资料 交易记录 备注 三个选项
    //供应商资料
    private LinearLayout supplier_data_tv_ll;
    private TextView supplier_data_tv;
    //交易记录
    private TextView trade_record_tv;
    //备注
    private LinearLayout remarks_supplier_hobby_ll;
    private TextView remarks_supplier_hobby_tv;

    //2016/12/10 供应商资料栏的数据
    private LinearLayout supplier_data_ll;//供应商资料
    private TextView name_tv;//姓名
    private TextView phonenumber_tv;//电话
    private TextView job;//职位
    private TextView email;//邮箱
    private TextView companyname;//公司名称
    private TextView address;//公司地址
    private TextView industry_type;//行业类型

    //  2016/12/10 交易记录栏的数据
    private ListView trade_record_lv;//交易记录

    //  2016/12/10 备注栏的数据
    private TextView beizhu_tv;//备注


    // 第三模块
    //  2016/12/10 最新询价 留样询价 预留询价 三个选项栏
    //最新询价
    private LinearLayout uptodate_inquiry_price_ll;
    private TextView uptodate_inquiry_price_tv;
    //留样询价
    private TextView liuyangxunjia_tv;//留样报价
    //预留询价
    private LinearLayout yuliuxunjia_ll;
    private TextView yuliuxunjia_tv;

    private ListView xunjia_price_lv;//三个选项共用一个listview


    //2016/12/10 适配器
    //询价的适配器
    private XunjiaPriceAdapter xunjiaPriceAdapter;

    //  2016/12/10  得到上个页面传递过来的数据
    private int id;

    //存放客户对象的集合
    private MyCustomer myCustomer;

    //查询到的公司名和客户名
    private String uniqued_id;

    //最新询价
    private ArrayList<MyInquiry> myInquiriesforUpdata = new ArrayList<>();

    //留样询价
    private ArrayList<MyInquiry> myInquiriesforLiuYang = new ArrayList<>();

    //预留询价
    private ArrayList<MyInquiry> myInquiriesforYuLiu = new ArrayList<>();

    //存放数据源的集合
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();

    private ArrayList<MyInquiry> myInquiriesforYiXunJiaNumbers = new ArrayList<>();
    private ArrayList<MyInquiry> myInquiriesforLiuYangNumbers = new ArrayList<>();
    private ArrayList<MyInquiry> myInquiriesforYuLiuNumbers = new ArrayList<>();

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private TextView last_tv;
    private TextView current_page_tv;
    private TextView total_page_tv;
    private TextView jump_tv;
    private TextView next_tv;
    private int i = 0;
    private double ceil;
    private int tag = 0;
    private int k = 0;
    private RelativeLayout jump_rl;

    private int from = 0;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_supplier2);
        initYuyan();
        initView();
        initData();
        setData();
    }


    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(MySupplierActivity.this);
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


    //初始化控件
    private void initView() {
        jump_rl = (RelativeLayout) findViewById(R.id.jump_rl);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        supplier_name_tv = (TextView) findViewById(R.id.supplier_name_tv);
        supplier_card_bt = (Button) findViewById(R.id.supplier_card_bt);
        supplier_company_logo_iv = (RoundImageView) findViewById(R.id.supplier_company_logo_iv);
        supplier_company_name_tv = (TextView) findViewById(R.id.supplier_company_name_tv);
        supplier_data_tv_ll = (LinearLayout) findViewById(R.id.supplier_data_tv_ll);
        supplier_data_ll = (LinearLayout) findViewById(R.id.supplier_data_ll);
        supplier_data_tv = (TextView) findViewById(R.id.supplier_data_tv);
        trade_record_tv = (TextView) findViewById(R.id.trade_record_tv);
        remarks_supplier_hobby_ll = (LinearLayout) findViewById(R.id.remarks_supplier_hobby_ll);
        remarks_supplier_hobby_tv = (TextView) findViewById(R.id.remarks_supplier_hobby_tv);
        name_tv = (TextView) findViewById(R.id.name_tv);
        phonenumber_tv = (TextView) findViewById(R.id.phonenumber_tv);
        job = (TextView) findViewById(R.id.job);
        email = (TextView) findViewById(R.id.email);
        companyname = (TextView) findViewById(R.id.companyname);
        address = (TextView) findViewById(R.id.address);
        industry_type = (TextView) findViewById(R.id.industry_type);
        trade_record_lv = (ListView) findViewById(R.id.trade_record_lv);
        beizhu_tv = (TextView) findViewById(R.id.beizhu_tv);
        uptodate_inquiry_price_ll = (LinearLayout) findViewById(R.id.uptodate_inquiry_price_ll);
        uptodate_inquiry_price_tv = (TextView) findViewById(R.id.uptodate_inquiry_price_tv);
        liuyangxunjia_tv = (TextView) findViewById(R.id.liuyangxunjia_tv);
        yuliuxunjia_ll = (LinearLayout) findViewById(R.id.yuliuxunjia_ll);
        yuliuxunjia_tv = (TextView) findViewById(R.id.yuliuxunjia_tv);
        xunjia_price_lv = (ListView) findViewById(R.id.xunjia_price_lv);

        last_tv = (TextView) findViewById(R.id.last_tv);
        current_page_tv = (TextView) findViewById(R.id.current_page_tv);
        total_page_tv = (TextView) findViewById(R.id.total_page_tv);
        jump_tv = (TextView) findViewById(R.id.jump_tv);
        next_tv = (TextView) findViewById(R.id.next_tv);

        fanhui_rl.setOnClickListener(MySupplierActivity.this);
        supplier_card_bt.setOnClickListener(MySupplierActivity.this);
        supplier_data_tv_ll.setOnClickListener(MySupplierActivity.this);
        trade_record_tv.setOnClickListener(MySupplierActivity.this);
        remarks_supplier_hobby_ll.setOnClickListener(MySupplierActivity.this);
        uptodate_inquiry_price_ll.setOnClickListener(MySupplierActivity.this);
        liuyangxunjia_tv.setOnClickListener(MySupplierActivity.this);
        yuliuxunjia_ll.setOnClickListener(MySupplierActivity.this);

        last_tv.setOnClickListener(this);
        jump_tv.setOnClickListener(this);
        next_tv.setOnClickListener(this);
        jump_rl.setOnClickListener(this);

    }

    //初始化数据
    private void initData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        myCustomer = CursorUtils.selectCustomerById(MySupplierActivity.this, id);
        uniqued_id = myCustomer.getUnique_id();

        myInquiriesforYiXunJiaNumbers = CursorUtils.selectInquiryforYiNumbers(MySupplierActivity.this, uniqued_id);
        int size = myInquiriesforYiXunJiaNumbers.size();
        showTotalPage(size);
        current_page_tv.setText("1");//当前页


        myInquiriesforUpdata = CursorUtils.selectYiXunJiaDesc(MySupplierActivity.this, uniqued_id, 0);
        getDataByTime();
        xunjiaPriceAdapter = new XunjiaPriceAdapter(datas, MySupplierActivity.this);
        xunjia_price_lv.setAdapter(xunjiaPriceAdapter);
        OtherUtils.setListViewHeightBasedOnChildren(xunjia_price_lv);
        xunjiaPriceAdapter.notifyDataSetChanged();

        xunjia_price_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = (Map<String, Object>) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(MySupplierActivity.this, DetailforInquiryActivity.class);
                String id = (String) map.get("_id");
                intent.putExtra("id", id);
                startActivity(intent);

            }
        });
    }

    //根据条数来显示总的页数
    private void showTotalPage(double size) {
        ceil = Math.ceil(size / (double) 10);//以询价的总页数
        //去掉末尾的0
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        if (ceil != 0) {
            total_page_tv.setText(decimalFormat.format(ceil));
        } else {
            total_page_tv.setText("1");
        }
    }

    //将从数据库查询到的数据设置到相应的控件上
    private void setData() {
        supplier_name_tv.setText(myCustomer.getName());
        byte[] company_logo = myCustomer.getCompany_logo();
        Bitmap bitmap = BitmapUtils.Bytes2Bimap(company_logo);
        supplier_company_logo_iv.setImageBitmap(bitmap);
        supplier_company_name_tv.setText(myCustomer.getCompany_name());
        name_tv.setText(myCustomer.getName());
        phonenumber_tv.setText(myCustomer.getPhone());
        job.setText(myCustomer.getJob_position());
        email.setText(myCustomer.getEmail());
        companyname.setText(myCustomer.getCompany_name());
        address.setText(myCustomer.getCompany_address());
        industry_type.setText(myCustomer.getIndustry_type());
        beizhu_tv.setText(myCustomer.getCustomer_ohter());

    }

    //构建数据源-留样报价
    private void getDataforLiuYang() {
        datas.clear();
        for (MyInquiry myInquiry : myInquiriesforLiuYang) {
            Map<String, Object> data = new ArrayMap<>();
            data.put("_id", myInquiry.get_id());
            data.put("goods_name", myInquiry.getGoods_name());
            data.put("material", myInquiry.getMaterial());
            data.put("color", myInquiry.getColor());
            data.put("price", myInquiry.getPrice());
            data.put("unit", myInquiry.getGoods_unit());
            data.put("currency_varitety", myInquiry.getCurrency_varitety());
            data.put("inquiry_time", myInquiry.getInquiry_time());
            data.put("moq", myInquiry.getMoq());
            datas.add(data);
        }

    }

    //构建数据源-预留报价
    private void getDataAboutYuLiu() {
        datas.clear();
        for (MyInquiry myInquiry : myInquiriesforYuLiu) {
            Map<String, Object> data = new ArrayMap<>();
            data.put("_id", myInquiry.get_id());
            data.put("goods_name", myInquiry.getGoods_name());
            data.put("material", myInquiry.getMaterial());
            data.put("color", myInquiry.getColor());
            data.put("price", myInquiry.getPrice());
            data.put("unit", myInquiry.getGoods_unit());
            data.put("currency_varitety", myInquiry.getCurrency_varitety());
            data.put("inquiry_time", myInquiry.getInquiry_time());
            data.put("moq", myInquiry.getMoq());
            datas.add(data);
        }


    }

    //构建数据源--按时间排序
    private void getDataByTime() {
        datas.clear();
        for (MyInquiry myInquiry : myInquiriesforUpdata) {
            Map<String, Object> data = new ArrayMap<>();
            data.put("_id", myInquiry.get_id());
            data.put("goods_name", myInquiry.getGoods_name());
            data.put("material", myInquiry.getMaterial());
            data.put("color", myInquiry.getColor());
            data.put("price", myInquiry.getPrice());
            data.put("unit", myInquiry.getGoods_unit());
            data.put("currency_varitety", myInquiry.getCurrency_varitety());
            data.put("inquiry_time", myInquiry.getInquiry_time());
            data.put("moq", myInquiry.getMoq());
            datas.add(data);
        }

    }


    @Override
    public void onClick(View view) {
        Intent intent;
        //蓝色
        int color_blue = getResources().getColor(R.color.green_color);
        //白色
        int color_white = getResources().getColor(R.color.color_white);
        //左边的蓝色背景样式
        int blue_background_left = R.drawable.left_corner_round_green;
        //左边的白色背景样式
        int white_background_left = R.drawable.left_corner_round_white;
        //右边的蓝色背景样式
        int blue_background_right = R.drawable.right_corner_round_green;
        //右边的白色背景样式
        int white_background_right = R.drawable.right_corner_round;
        switch (view.getId()) {
            // 2016/12/3 返回
            case R.id.fanhui_rl:
                finish();
                break;
            //  2016/12/3 供应商卡片
            case R.id.supplier_card_bt:
                intent = new Intent(MySupplierActivity.this, SupplierCardActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                break;
            //  2016/12/3 供应商资料
            case R.id.supplier_data_tv_ll:
                //将该字体颜色改为白色  背景设置左边倒角背景蓝色的样式
                supplier_data_tv.setTextColor(color_white);
                supplier_data_tv_ll.setBackgroundResource(blue_background_left);
                //将交易记录字体设置为蓝色 背景设置为白色
                trade_record_tv.setTextColor(color_blue);
                trade_record_tv.setBackgroundColor(color_white);
                //将备忘供应商爱好字体设置为蓝色 背景设置为右边白色样式
                remarks_supplier_hobby_tv.setTextColor(color_blue);
                remarks_supplier_hobby_ll.setBackgroundResource(white_background_right);

                //显示供应商资料 隐藏交易记录 备忘供应商爱好
                supplier_data_ll.setVisibility(View.VISIBLE);
                trade_record_lv.setVisibility(View.GONE);
                beizhu_tv.setVisibility(View.GONE);


                break;
            //  2016/12/3 交易记录
            case R.id.trade_record_tv:
                //将供应商资料 字体设置为蓝色 背景设置为左边白色样式
                supplier_data_tv.setTextColor(color_blue);
                supplier_data_tv_ll.setBackgroundResource(white_background_left);
                //将自己的字体设置为白色 背景设置为蓝色
                trade_record_tv.setTextColor(color_white);
                trade_record_tv.setBackgroundColor(color_blue);
                //将 备忘供应商爱好 字体设置为蓝色 背景设置为右边白色样式
                remarks_supplier_hobby_tv.setTextColor(color_blue);
                remarks_supplier_hobby_ll.setBackgroundResource(white_background_right);

                //显示交易记录 隐藏供应商资料 备忘
                trade_record_lv.setVisibility(View.VISIBLE);
                supplier_data_ll.setVisibility(View.GONE);
                beizhu_tv.setVisibility(View.GONE);


                break;

            // 2016/12/3 备注
            case R.id.remarks_supplier_hobby_ll:
                //将自己的字体颜色变为白色 背景变为右边蓝色样式
                remarks_supplier_hobby_tv.setTextColor(color_white);
                remarks_supplier_hobby_ll.setBackgroundResource(blue_background_right);
                //将供应商资料字体设置为蓝色 背景设置为左边白色
                supplier_data_tv.setTextColor(color_blue);
                supplier_data_tv_ll.setBackgroundResource(white_background_left);
                //将交易记录字体设置为蓝色 背景设置为白色
                trade_record_tv.setTextColor(color_blue);
                trade_record_tv.setBackgroundColor(color_white);

                //显示备注 隐藏供应商资料 交易记录
                beizhu_tv.setVisibility(View.VISIBLE);
                supplier_data_ll.setVisibility(View.GONE);
                trade_record_lv.setVisibility(View.GONE);


                break;

            //  2016/12/3 已询价
            case R.id.uptodate_inquiry_price_ll:
                //将该字体变为白色 背景变为左边蓝色样式
                uptodate_inquiry_price_tv.setTextColor(color_white);
                uptodate_inquiry_price_ll.setBackgroundResource(blue_background_left);
                //将留样询价字体变为蓝色 背景变为白色
                liuyangxunjia_tv.setTextColor(color_blue);
                liuyangxunjia_tv.setBackgroundColor(color_white);
                //将客户预留询价字体变为蓝色 背景变为右边白色样式
                yuliuxunjia_tv.setTextColor(color_blue);
                yuliuxunjia_ll.setBackgroundResource(white_background_right);

                clear();
                tag = 0;
                myInquiriesforUpdata = CursorUtils.selectYiXunJiaDesc(MySupplierActivity.this, uniqued_id, 0);
                getDataByTime();
                xunjiaPriceAdapter = new XunjiaPriceAdapter(datas, MySupplierActivity.this);
                xunjia_price_lv.setAdapter(xunjiaPriceAdapter);
                OtherUtils.setListViewHeightBasedOnChildren(xunjia_price_lv);
                xunjiaPriceAdapter.notifyDataSetChanged();


                myInquiriesforYiXunJiaNumbers = CursorUtils.selectInquiryforYiNumbers(MySupplierActivity.this, uniqued_id);
                int size = myInquiriesforYiXunJiaNumbers.size();
                showTotalPage(size);

                break;
            //  2016/12/3 留样询价
            case R.id.liuyangxunjia_tv:
                //将该字体颜色变为白色  背景变为蓝色
                liuyangxunjia_tv.setTextColor(color_white);
                liuyangxunjia_tv.setBackgroundColor(color_blue);
                //将最新询价字体变为白色 背景变为左边蓝色样式
                uptodate_inquiry_price_tv.setTextColor(color_blue);
                uptodate_inquiry_price_ll.setBackgroundResource(white_background_left);
                //将客户预留询价字体变为蓝色 背景变为白色
                yuliuxunjia_tv.setTextColor(color_blue);
                yuliuxunjia_ll.setBackgroundResource(white_background_right);

                clear();
                tag = 1;
                myInquiriesforLiuYang = CursorUtils.selectLiuYangDesc(MySupplierActivity.this, uniqued_id, 0);
                getDataforLiuYang();
                xunjiaPriceAdapter = new XunjiaPriceAdapter(datas, MySupplierActivity.this);
                xunjia_price_lv.setAdapter(xunjiaPriceAdapter);
                OtherUtils.setListViewHeightBasedOnChildren(xunjia_price_lv);
                xunjiaPriceAdapter.notifyDataSetChanged();

                myInquiriesforLiuYangNumbers = CursorUtils.selectInquiryforLiuYangNumbers(MySupplierActivity.this, uniqued_id);
                int size1 = myInquiriesforLiuYangNumbers.size();
                showTotalPage(size1);
                break;
            //  2016/12/3 预留询价
            case R.id.yuliuxunjia_ll:
                //将改字体颜色变为白色 背景变为右边白色样式
                yuliuxunjia_tv.setTextColor(color_white);
                yuliuxunjia_ll.setBackgroundResource(blue_background_right);
                //将最新询价字体变为蓝色 背景变为左边白色样式
                uptodate_inquiry_price_tv.setTextColor(color_blue);
                uptodate_inquiry_price_ll.setBackgroundResource(white_background_left);
                //将留样询价字体变为蓝色 背景变为白色
                liuyangxunjia_tv.setTextColor(color_blue);
                liuyangxunjia_tv.setBackgroundColor(color_white);

                clear();
                tag = 2;
                myInquiriesforYuLiu = CursorUtils.selectYuLiuXunJiaDesc(MySupplierActivity.this, uniqued_id, 0);
                getDataAboutYuLiu();
                xunjiaPriceAdapter = new XunjiaPriceAdapter(datas, MySupplierActivity.this);
                xunjia_price_lv.setAdapter(xunjiaPriceAdapter);
                OtherUtils.setListViewHeightBasedOnChildren(xunjia_price_lv);
                xunjiaPriceAdapter.notifyDataSetChanged();

                myInquiriesforYuLiuNumbers = CursorUtils.selectInquiryforYuLiuNumbers(MySupplierActivity.this, uniqued_id);
                int size2 = myInquiriesforYuLiuNumbers.size();
                showTotalPage(size2);
                break;
            //上一页
            case R.id.last_tv:
                clear();
                selectTypeofNumbers();
                i--;
                if (i >= 0) {
                    k = 10 * i;
                    selectTypeInquiry(k);
                    int p = i + 1;
                    current_page_tv.setText(p + "");
                } else {
                    ToastUtil.showToast2(MySupplierActivity.this, R.string.one_page);
                    i = 0;
                }

                break;
            //下一页
            case R.id.next_tv:
                clear();
                selectTypeofNumbers();
                i++;
                if (i < ceil) {
                    k = 10 * i;
                    selectTypeInquiry(k);
                    int p = i + 1;
                    current_page_tv.setText(p + "");
                } else {
                    ToastUtil.showToast2(MySupplierActivity.this, R.string.last_page);
                    i = (int) ceil;
                }

                break;
            //跳转至
            case R.id.jump_tv:
                clear();
                selectTypeofNumbers();
                jump(MySupplierActivity.this);
                break;
            //跳转到询价页面
            case R.id.jump_rl:
                from = Location.TOP.ordinal();
                //调用此方法，menu不会顶置
                initPopupWindow();


                break;

        }
    }


    private void clear() {
        if (myInquiriesforYiXunJiaNumbers != null) {
            myInquiriesforYiXunJiaNumbers.clear();
        }
        if (myInquiriesforLiuYangNumbers != null) {
            myInquiriesforLiuYangNumbers.clear();
        }
        if (myInquiriesforYuLiuNumbers != null) {
            myInquiriesforYuLiuNumbers.clear();
        }
        if (myInquiriesforUpdata != null) {
            myInquiriesforUpdata.clear();
        }
        if (myInquiriesforLiuYang != null) {
            myInquiriesforLiuYang.clear();
        }
        if (myInquiriesforYuLiu != null) {
            myInquiriesforYuLiu.clear();
        }
    }

    private void selectTypeofNumbers() {
        if (tag == 0) {
            myInquiriesforLiuYangNumbers = CursorUtils.selectInquiryforLiuYangNumbers(MySupplierActivity.this, uniqued_id);
            int size1 = myInquiriesforLiuYangNumbers.size();
            showTotalPage(size1);
        } else if (tag == 1) {
            myInquiriesforLiuYangNumbers = CursorUtils.selectInquiryforLiuYangNumbers(MySupplierActivity.this, uniqued_id);
            int size1 = myInquiriesforLiuYangNumbers.size();
            showTotalPage(size1);
        } else if (tag == 2) {
            myInquiriesforYuLiuNumbers = CursorUtils.selectInquiryforYuLiuNumbers(MySupplierActivity.this, uniqued_id);
            int size2 = myInquiriesforYuLiuNumbers.size();
            showTotalPage(size2);
        }
    }

    private void selectTypeInquiry(int q) {
        if (tag == 0) {
            myInquiriesforUpdata = CursorUtils.selectYiXunJiaDesc(MySupplierActivity.this, uniqued_id, q);
            getDataByTime();
            xunjiaPriceAdapter = new XunjiaPriceAdapter(datas, MySupplierActivity.this);
            xunjia_price_lv.setAdapter(xunjiaPriceAdapter);
            OtherUtils.setListViewHeightBasedOnChildren(xunjia_price_lv);
            xunjiaPriceAdapter.notifyDataSetChanged();
        } else if (tag == 1) {
            myInquiriesforLiuYang = CursorUtils.selectLiuYangDesc(MySupplierActivity.this, uniqued_id, q);
            getDataforLiuYang();
            xunjiaPriceAdapter = new XunjiaPriceAdapter(datas, MySupplierActivity.this);
            xunjia_price_lv.setAdapter(xunjiaPriceAdapter);
            OtherUtils.setListViewHeightBasedOnChildren(xunjia_price_lv);
            xunjiaPriceAdapter.notifyDataSetChanged();
        } else if (tag == 2) {
            myInquiriesforYuLiu = CursorUtils.selectYuLiuXunJiaDesc(MySupplierActivity.this, uniqued_id, q);
            getDataAboutYuLiu();
            xunjiaPriceAdapter = new XunjiaPriceAdapter(datas, MySupplierActivity.this);
            xunjia_price_lv.setAdapter(xunjiaPriceAdapter);
            OtherUtils.setListViewHeightBasedOnChildren(xunjia_price_lv);
            xunjiaPriceAdapter.notifyDataSetChanged();
        }
    }

    private void jump(final Context context) {
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.jump_to_buyer_layout_dialog, null);
        dialog.setContentView(view);
        final EditText editText = (EditText) view.findViewById(R.id.jump_et);
        Button jump_dialog_sure = (Button) view.findViewById(R.id.jump_dialog_sure);
        Button jump_dialog_cancel = (Button) view.findViewById(R.id.jump_dialog_cancel);
        //解决不能弹出输入法的问题
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        jump_dialog_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trim = editText.getText().toString().trim();
                Integer integer = Integer.valueOf(trim);
                i = integer - 1;
                if (integer <= ceil && integer != 0) {
                    int r = (integer - 1) * 10;
                    selectTypeInquiry(r);
                    current_page_tv.setText(trim);
                    dialog.dismiss();
                } else {
                    ToastUtil.showToast2(context, R.string.input_page_error);
                    editText.setText("");
                }
            }
        });
        jump_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

    }


    //跳转到报价类型的popupwindow
    private void initPopupWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.pop_select_inquiry, null);
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
            mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_my_supplier2, null), Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(MySupplierActivity.this, 0.5f);
        //关闭事件
        mPopupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        LinearLayout simple_quote_ll = (LinearLayout) popupWindowView.findViewById(R.id.simple_quote_ll);
        LinearLayout detail_quote_ll = (LinearLayout) popupWindowView.findViewById(R.id.detail_quote_ll);
        //简易报价监听事件
        simple_quote_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id = myCustomer.get_id();
                Intent intent = new Intent(MySupplierActivity.this, EasyInquiryActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();

                mPopupWindow.dismiss();
            }
        });
        //详细报价的监听事件
        detail_quote_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = myCustomer.get_id();
                Intent intent = new Intent(MySupplierActivity.this, InquiryPriceActivity.class);
                intent.putExtra("customer_id", id);
                startActivity(intent);
                finish();

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
            backgroundAlpha(MySupplierActivity.this, 1f);
        }

    }

}
