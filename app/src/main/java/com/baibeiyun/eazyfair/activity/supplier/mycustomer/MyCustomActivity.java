package com.baibeiyun.eazyfair.activity.supplier.mycustomer;

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
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.activity.supplier.offer.DetailGoodsforQuoteActivity;
import com.baibeiyun.eazyfair.activity.supplier.offer.EasyQuoteActivity;
import com.baibeiyun.eazyfair.activity.supplier.offer.OfferListActivity;
import com.baibeiyun.eazyfair.adapter.XunJiaAdapter;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCustomer;
import com.baibeiyun.eazyfair.entities.MyQuote;
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
 * @author RuanWei
 * @date 2016/12/10
 **/
public class MyCustomActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;
    //第一行
    private TextView custom_name_tv;//客户名
    private Button custom_card_bt;//客户名片
    //第二行
    private RoundImageView custom_company_logo_iv;//公司logo
    private TextView commpany_name_tv;//公司名

    //第二模块  客户资料 交易记录 备注 三个选择按钮
    //客户资料
    private LinearLayout custom_data_tv_ll;
    private TextView custom_data_tv;
    //交易记录
    private TextView trade_record_tv;
    //备注
    private LinearLayout remarks_custom_ll;
    private TextView remarks_custom_tv;

    //客户资料部分
    private LinearLayout custom_data_ll;
    private TextView name_tv;
    private TextView phonenumber_tv;
    private TextView job_tv;
    private TextView email_tv;
    private TextView companyname_tv;
    private TextView address_tv;
    private TextView industry_type_tv;

    //交易记录部分
    private LinearLayout trade_record_ll;

    //备注部分
    private LinearLayout memorandum_custom_data;
    private TextView beizhu_custom_tv;


    //第三模块
    //已报价
    private LinearLayout uptodate_baojia_price_ll;
    private TextView uptodate_quote_price_tv;
    //留样报价
    private TextView liuyangbaojia_tv;
    //预留报价
    private LinearLayout yuliubaojia_ll;
    private TextView yuliubaojia_tv;

    private ListView inquiry_price_lv;


    //最新报价 留样报价 预留报价的适配器
    private XunJiaAdapter xunJiaAdapter;


    //最新询价的数据源
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();


    private int id;
    private MyCustomer myCustomer;

    private String uniqued_id;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    //最新报价
    private ArrayList<MyQuote> myQuotesforUpdata = new ArrayList<>();

    //留样报价
    private ArrayList<MyQuote> myQuotesforLiuYang = new ArrayList<>();

    //预留报价
    private ArrayList<MyQuote> myQuotesforYuLiu = new ArrayList<>();

    //已报价的数量
    private ArrayList<MyQuote> myQuotesforYiNumbers = new ArrayList<>();
    //留样报价的数量
    private ArrayList<MyQuote> myQuotesforLiuYangNumbers = new ArrayList<>();
    //预留报价的数量
    private ArrayList<MyQuote> myQuotesforYuLiuNumbers = new ArrayList<>();

    private TextView last_tv;
    private TextView current_page_tv;
    private TextView total_page_tv;
    private TextView jump_tv;
    private TextView next_tv;

    private int t = 0;//当前页数
    private int k = 0;
    private int tag = 0;
    private double page;//总的页数
    private RelativeLayout jump_rl;//跳转到报价页面

    private int from = 0;
    private PopupWindow mPopupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_custom);
        initYuyan();
        initView();
        initData();
        initSetData();
    }


    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(MyCustomActivity.this);
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


    //已报价的数据源
    private void getDataforUpdate() {
        datas.clear();
        for (MyQuote myQuote : myQuotesforUpdata) {
            Map<String, Object> data = new ArrayMap<>();
            data.put("goods_name_tv", myQuote.getGoods_name());
            data.put("goods_price_tv", myQuote.getPrice());
            data.put("currency_type_tv", myQuote.getCurrency_varitety());
            data.put("goods_material_tv", myQuote.getMaterial());
            data.put("goods_color_tv", myQuote.getColor());
            data.put("moq_tv", myQuote.getMoq());
            data.put("goods_unit_tv", myQuote.getGoods_unit());
            data.put("quote_time_tv", myQuote.getQuote_time());
            data.put("_id", myQuote.get_id());
            datas.add(data);
        }
    }

    //留样报价的数据源
    private void getDataforLiuYang() {
        datas.clear();
        for (MyQuote myQuote : myQuotesforLiuYang) {
            Map<String, Object> data = new ArrayMap<>();
            data.put("goods_name_tv", myQuote.getGoods_name());
            data.put("goods_price_tv", myQuote.getPrice());
            data.put("currency_type_tv", myQuote.getCurrency_varitety());
            data.put("goods_material_tv", myQuote.getMaterial());
            data.put("goods_color_tv", myQuote.getColor());
            data.put("moq_tv", myQuote.getMoq());
            data.put("goods_unit_tv", myQuote.getGoods_unit());
            data.put("quote_time_tv", myQuote.getQuote_time());
            data.put("_id", myQuote.get_id());
            datas.add(data);
        }
    }

    //预留报价的数据源
    private void getDataforYuLiu() {
        datas.clear();
        for (MyQuote myQuote : myQuotesforYuLiu) {
            Map<String, Object> data = new ArrayMap<>();
            data.put("goods_name_tv", myQuote.getGoods_name());
            data.put("goods_price_tv", myQuote.getPrice());
            data.put("currency_type_tv", myQuote.getCurrency_varitety());
            data.put("goods_material_tv", myQuote.getMaterial());
            data.put("goods_color_tv", myQuote.getColor());
            data.put("moq_tv", myQuote.getMoq());
            data.put("goods_unit_tv", myQuote.getGoods_unit());
            data.put("quote_time_tv", myQuote.getQuote_time());
            data.put("_id", myQuote.get_id());
            datas.add(data);
        }
    }

    private void initView() {
        jump_rl = (RelativeLayout) findViewById(R.id.jump_rl);
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        custom_name_tv = (TextView) findViewById(R.id.custom_name_tv);
        custom_card_bt = (Button) findViewById(R.id.custom_card_bt);
        custom_company_logo_iv = (RoundImageView) findViewById(R.id.custom_company_logo_iv);
        commpany_name_tv = (TextView) findViewById(R.id.commpany_name_tv);
        custom_data_tv_ll = (LinearLayout) findViewById(R.id.custom_data_tv_ll);
        custom_data_tv = (TextView) findViewById(R.id.custom_data_tv);
        trade_record_tv = (TextView) findViewById(R.id.trade_record_tv);
        remarks_custom_ll = (LinearLayout) findViewById(R.id.remarks_custom_ll);
        remarks_custom_tv = (TextView) findViewById(R.id.remarks_custom_tv);
        custom_data_ll = (LinearLayout) findViewById(R.id.custom_data_ll);
        name_tv = (TextView) findViewById(R.id.name_tv);
        phonenumber_tv = (TextView) findViewById(R.id.phonenumber_tv);
        job_tv = (TextView) findViewById(R.id.job_tv);
        email_tv = (TextView) findViewById(R.id.email_tv);
        companyname_tv = (TextView) findViewById(R.id.companyname_tv);
        address_tv = (TextView) findViewById(R.id.address_tv);
        industry_type_tv = (TextView) findViewById(R.id.industry_type_tv);
        trade_record_ll = (LinearLayout) findViewById(R.id.trade_record_ll);
        memorandum_custom_data = (LinearLayout) findViewById(R.id.memorandum_custom_data);
        beizhu_custom_tv = (TextView) findViewById(R.id.beizhu_custom_tv);
        uptodate_baojia_price_ll = (LinearLayout) findViewById(R.id.uptodate_baojia_price_ll);
        uptodate_quote_price_tv = (TextView) findViewById(R.id.uptodate_quote_price_tv);
        liuyangbaojia_tv = (TextView) findViewById(R.id.liuyangbaojia_tv);
        yuliubaojia_ll = (LinearLayout) findViewById(R.id.yuliubaojia_ll);
        yuliubaojia_tv = (TextView) findViewById(R.id.yuliubaojia_tv);
        inquiry_price_lv = (ListView) findViewById(R.id.inquiry_price_lv);


        last_tv = (TextView) findViewById(R.id.last_tv);
        current_page_tv = (TextView) findViewById(R.id.current_page_tv);
        total_page_tv = (TextView) findViewById(R.id.total_page_tv);
        jump_tv = (TextView) findViewById(R.id.jump_tv);
        next_tv = (TextView) findViewById(R.id.next_tv);

        last_tv.setOnClickListener(this);
        jump_tv.setOnClickListener(this);
        next_tv.setOnClickListener(this);
        fanhui_rl.setOnClickListener(MyCustomActivity.this);
        custom_card_bt.setOnClickListener(MyCustomActivity.this);
        custom_data_tv.setOnClickListener(MyCustomActivity.this);
        trade_record_tv.setOnClickListener(MyCustomActivity.this);
        remarks_custom_tv.setOnClickListener(MyCustomActivity.this);
        uptodate_quote_price_tv.setOnClickListener(MyCustomActivity.this);
        liuyangbaojia_tv.setOnClickListener(MyCustomActivity.this);
        yuliubaojia_tv.setOnClickListener(MyCustomActivity.this);
        jump_rl.setOnClickListener(this);

        inquiry_price_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = (Map<String, Object>) adapterView.getItemAtPosition(i);
                String id = (String) map.get("_id");
                Intent intent = new Intent(MyCustomActivity.this, DetailGoodsforQuoteActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);

            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        myCustomer = CursorUtils.selectCustomerById(MyCustomActivity.this, id);
        uniqued_id = myCustomer.getUnique_id();
        //已报价
        myQuotesforUpdata = CursorUtils.selectYiQuoteDesc(MyCustomActivity.this, uniqued_id, 0);
        getDataforUpdate();
        showList();

        myQuotesforYiNumbers = CursorUtils.selectQuoteNumberforYiBaoJia(MyCustomActivity.this, uniqued_id);
        int size = myQuotesforYiNumbers.size();
        page = Math.ceil((double) size / (double) 10);
        //去掉末尾的0
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        if (page != 0) {
            total_page_tv.setText(decimalFormat.format(page));
        } else {
            total_page_tv.setText("1");
        }
        current_page_tv.setText("1");//当前页
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    //将从数据库查询到的数据显示在控件上
    private void initSetData() {
        custom_name_tv.setText(myCustomer.getName());
        byte[] company_logo = myCustomer.getCompany_logo();
        Bitmap bitmap = BitmapUtils.Bytes2Bimap(company_logo);
        custom_company_logo_iv.setImageBitmap(bitmap);
        commpany_name_tv.setText(myCustomer.getCompany_name());
        name_tv.setText(myCustomer.getName());
        phonenumber_tv.setText(myCustomer.getPhone());
        job_tv.setText(myCustomer.getJob_position());
        email_tv.setText(myCustomer.getEmail());
        address_tv.setText(myCustomer.getCompany_address());
        industry_type_tv.setText(myCustomer.getIndustry_type());
        beizhu_custom_tv.setText(myCustomer.getCustomer_ohter());
        companyname_tv.setText(myCustomer.getCompany_name());
    }


    @Override
    public void onClick(View view) {
        //蓝色
        int color_blue = getResources().getColor(R.color.blue);
        //白色
        int color_white = getResources().getColor(R.color.color_white);
        //左边的蓝色背景样式
        int blue_background_left = R.drawable.left_corner_round;
        //左边的白色背景样式
        int white_background_left = R.drawable.left_corner_round_white;
        //右边的蓝色背景样式
        int blue_background_right = R.drawable.right_corner_round_blue;
        //右边的白色背景样式
        int white_background_right = R.drawable.right_corner_round;
        int size;

        switch (view.getId()) {
            //返回按钮
            case R.id.fanhui_rl:
                finish();
                break;
            // 客户名片
            case R.id.custom_card_bt:
                Intent intent = new Intent(MyCustomActivity.this, CustomCardActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                break;
            // 客户资料
            case R.id.custom_data_tv:
                //将该字体颜色改为白色  背景设置左边倒角背景蓝色的样式
                custom_data_tv.setTextColor(color_white);
                custom_data_tv_ll.setBackgroundResource(blue_background_left);
                //将交易记录字体设置为蓝色 背景设置为白色
                trade_record_tv.setTextColor(color_blue);
                trade_record_tv.setBackgroundColor(color_white);
                //将备忘客户爱好字体设置为蓝色 背景设置为右边白色样式
                remarks_custom_tv.setTextColor(color_blue);
                remarks_custom_ll.setBackgroundResource(white_background_right);
                //将客户资料栏数据显示 将其他隐藏
                custom_data_ll.setVisibility(View.VISIBLE);
                memorandum_custom_data.setVisibility(View.GONE);
                trade_record_ll.setVisibility(View.GONE);
                break;
            // 交易记录
            case R.id.trade_record_tv:
                //将客户资料 字体设置为蓝色 背景设置为左边白色样式
                custom_data_tv.setTextColor(color_blue);
                custom_data_tv_ll.setBackgroundResource(white_background_left);
                //将自己的字体设置为白色 背景设置为蓝色
                trade_record_tv.setTextColor(color_white);
                trade_record_tv.setBackgroundColor(color_blue);
                //将 备忘客户爱好 字体设置为蓝色 背景设置为右边白色样式
                remarks_custom_tv.setTextColor(color_blue);
                remarks_custom_ll.setBackgroundResource(white_background_right);
                //将交易记录的控件显示 隐藏其他控件
                trade_record_ll.setVisibility(View.VISIBLE);
                custom_data_ll.setVisibility(View.GONE);
                memorandum_custom_data.setVisibility(View.GONE);

                break;
            // 2016/12/8 备注
            case R.id.remarks_custom_tv:
                //将自己的字体颜色变为白色 背景变为右边蓝色样式
                remarks_custom_tv.setTextColor(color_white);
                remarks_custom_ll.setBackgroundResource(blue_background_right);
                //将客户资料字体设置为蓝色 背景设置为左边白色
                custom_data_tv.setTextColor(color_blue);
                custom_data_tv_ll.setBackgroundResource(white_background_left);
                //将交易记录字体设置为蓝色 背景设置为白色
                trade_record_tv.setTextColor(color_blue);
                trade_record_tv.setBackgroundColor(color_white);
                //将客户资料整栏资料隐藏 将其他隐藏
                custom_data_ll.setVisibility(View.GONE);
                memorandum_custom_data.setVisibility(View.VISIBLE);
                trade_record_ll.setVisibility(View.GONE);
                break;
            //  2016/12/8 已报价
            case R.id.uptodate_quote_price_tv:
                //将该字体变为白色 背景变为左边蓝色样式
                uptodate_quote_price_tv.setTextColor(color_white);
                uptodate_baojia_price_ll.setBackgroundResource(blue_background_left);
                //将留样询价字体变为蓝色 背景变为白色
                liuyangbaojia_tv.setTextColor(color_blue);
                liuyangbaojia_tv.setBackgroundColor(color_white);
                //将客户预留询价字体变为蓝色 背景变为右边白色样式
                yuliubaojia_tv.setTextColor(color_blue);
                yuliubaojia_ll.setBackgroundResource(white_background_right);
                clear();
                myQuotesforUpdata = CursorUtils.selectYiQuoteDesc(MyCustomActivity.this, uniqued_id, 0);
                getDataforUpdate();
                showList();
                tag = 0;
                current_page_tv.setText("1");//当前页

                clearNumbers();
                myQuotesforYiNumbers = CursorUtils.selectQuoteNumberforYiBaoJia(MyCustomActivity.this, uniqued_id);
                size = myQuotesforYiNumbers.size();
                showNumber(size);
                break;
            //  2016/12/8 留样询价
            case R.id.liuyangbaojia_tv:
                //将该字体颜色变为白色  背景变为蓝色
                liuyangbaojia_tv.setTextColor(color_white);
                liuyangbaojia_tv.setBackgroundColor(color_blue);
                //将最新询价字体变为白色 背景变为左边蓝色样式
                uptodate_quote_price_tv.setTextColor(color_blue);
                uptodate_baojia_price_ll.setBackgroundResource(white_background_left);
                //将客户预留询价字体变为蓝色 背景变为白色
                yuliubaojia_tv.setTextColor(color_blue);
                yuliubaojia_ll.setBackgroundResource(white_background_right);
                clear();
                myQuotesforLiuYang = CursorUtils.selectLiuYangQuoteDesc(MyCustomActivity.this, uniqued_id, 0);
                getDataforLiuYang();
                showList();
                tag = 1;
                current_page_tv.setText("1");//当前页

                clearNumbers();
                myQuotesforLiuYangNumbers = CursorUtils.selectQuoteNumberforLiuYang(MyCustomActivity.this, uniqued_id);
                size = myQuotesforLiuYangNumbers.size();
                showNumber(size);
                break;
            // 2016/12/8 预留询价
            case R.id.yuliubaojia_tv:
                //将改字体颜色变为白色 背景变为右边白色样式
                yuliubaojia_tv.setTextColor(color_white);
                yuliubaojia_ll.setBackgroundResource(blue_background_right);
                //将最新询价字体变为蓝色 背景变为左边白色样式
                uptodate_quote_price_tv.setTextColor(color_blue);
                uptodate_baojia_price_ll.setBackgroundResource(white_background_left);
                //将留样询价字体变为蓝色 背景变为白色
                liuyangbaojia_tv.setTextColor(color_blue);
                liuyangbaojia_tv.setBackgroundColor(color_white);
                clear();
                myQuotesforYuLiu = CursorUtils.selectYuLiuQuoteDesc(MyCustomActivity.this, uniqued_id, 0);
                getDataforYuLiu();
                showList();
                tag = 2;
                current_page_tv.setText("1");//当前页

                clearNumbers();
                myQuotesforYuLiuNumbers = CursorUtils.selectQuoteNumberforYuLiu(MyCustomActivity.this, uniqued_id);
                size = myQuotesforYuLiuNumbers.size();
                showNumber(size);
                break;
            //上一页
            case R.id.last_tv:
                if (OtherUtils.isFastDoubleClick()) {
                    return;
                }
                selectNumbers();
                t--;
                if (t >= 0) {
                    k = 10 * t;
                    selectTypeQuote(k);
                    int p = t + 1;
                    current_page_tv.setText(p + "");
                } else {
                    ToastUtil.showToast2(MyCustomActivity.this, R.string.one_page);
                    t = 0;
                }

                break;
            //下一页
            case R.id.next_tv:
                if (OtherUtils.isFastDoubleClick()) {
                    return;
                }
                selectNumbers();
                t++;
                if (t < page) {
                    k = 10 * t;
                    selectTypeQuote(k);
                    int p = t + 1;
                    current_page_tv.setText(p + "");

                } else {
                    ToastUtil.showToast2(MyCustomActivity.this, R.string.last_page);
                    t = (int) page;
                }

                break;
            //跳转到
            case R.id.jump_tv:
                if (OtherUtils.isFastDoubleClick()) {
                    return;
                }
                selectNumbers();

                jump(MyCustomActivity.this);

                break;
            //跳转到报价页面
            case R.id.jump_rl:
                from = Location.TOP.ordinal();
                //调用此方法，menu不会顶置
                initPopupWindow();
                break;
        }

    }

    //跳转到报价类型的popupwindow
    private void initPopupWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.pop_select_quote, null);
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
            mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_my_custom, null), Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(MyCustomActivity.this, 0.5f);
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
                Intent intent = new Intent(MyCustomActivity.this, EasyQuoteActivity.class);
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
                Intent intent = new Intent(MyCustomActivity.this, OfferListActivity.class);
                intent.putExtra("id", id);
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
            backgroundAlpha(MyCustomActivity.this, 1f);
        }

    }


    private void selectNumbers() {
        int size;
        switch (tag) {
            case 0:
                clearNumbers();
                myQuotesforYiNumbers = CursorUtils.selectQuoteNumberforYiBaoJia(MyCustomActivity.this, uniqued_id);
                size = myQuotesforYiNumbers.size();
                showNumber(size);
                break;
            case 1:
                clearNumbers();
                myQuotesforLiuYangNumbers = CursorUtils.selectQuoteNumberforLiuYang(MyCustomActivity.this, uniqued_id);
                size = myQuotesforLiuYangNumbers.size();
                showNumber(size);
                break;
            case 2:
                clearNumbers();
                myQuotesforYuLiuNumbers = CursorUtils.selectQuoteNumberforYuLiu(MyCustomActivity.this, uniqued_id);
                size = myQuotesforYuLiuNumbers.size();
                showNumber(size);
                break;
        }
    }

    private void showNumber(double size) {
        page = Math.ceil(size / (double) 10);
        //去掉末尾的0
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        if (page != 0) {
            total_page_tv.setText(decimalFormat.format(page));
        } else {
            total_page_tv.setText("1");
        }
    }

    private void selectTypeQuote(int q) {
        switch (tag) {
            case 0:
                clear();
                myQuotesforUpdata = CursorUtils.selectYiQuoteDesc(MyCustomActivity.this, uniqued_id, q);
                getDataforUpdate();
                showList();
                break;
            case 1:
                clear();
                myQuotesforLiuYang = CursorUtils.selectLiuYangQuoteDesc(MyCustomActivity.this, uniqued_id, q);
                getDataforLiuYang();
                showList();

                break;
            case 2:
                clear();
                myQuotesforYuLiu = CursorUtils.selectYuLiuQuoteDesc(MyCustomActivity.this, uniqued_id, q);
                getDataforYuLiu();
                showList();

                break;
        }
    }


    private void showList() {
        xunJiaAdapter = new XunJiaAdapter(datas, MyCustomActivity.this);
        inquiry_price_lv.setAdapter(xunJiaAdapter);
        OtherUtils.setListViewHeightBasedOnChildren(inquiry_price_lv);
        xunJiaAdapter.notifyDataSetChanged();
    }

    private void clear() {
        if (myQuotesforUpdata != null) {
            myQuotesforUpdata.clear();
        }
        if (myQuotesforLiuYang != null) {
            myQuotesforLiuYang.clear();
        }
        if (myQuotesforYuLiu != null) {
            myQuotesforYuLiu.clear();
        }
    }

    private void clearNumbers() {
        if (myQuotesforYiNumbers != null) {
            myQuotesforYiNumbers.clear();
        }
        if (myQuotesforYuLiuNumbers != null) {
            myQuotesforYuLiuNumbers.clear();
        }
        if (myQuotesforLiuYangNumbers != null) {
            myQuotesforLiuYangNumbers.clear();
        }
    }

    private void jump(final Context context) {
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.jump_to_layout_dialog, null);
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
                t = integer - 1;
                if (integer <= page && integer != 0) {
                    int r = (integer - 1) * 10;
                    selectTypeQuote(r);
                    showList();
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


}
