package com.baibeiyun.eazyfair.activity.supplier.mygoods;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.utils.CursorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NewGoodsActivitytwo extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    private EditText goods_color_et;//输入颜色
    private EditText goods_price_et;//输入价格
    private TextView huobi_tv;//货币
    private TextView tiaokuan_tv;//条款
    private TextView input_address;//输入地址
    private LinearLayout huobi_ll;//货币
    private LinearLayout tiaokuan_ll;//条款

    private EditText outbox_amout_et;
    private EditText outbox_size_et;
    private EditText outbox_weight_et;
    private TextView outbox_weight_unit_tv;
    private EditText centerbox_amout_et;
    private EditText centerbox_size_et;
    private EditText centerbox_weight_et;
    private TextView centerbox_weight_unit_tv;
    private LinearLayout outbox_weight_unit_ll;
    private LinearLayout centerbox_weight_unit_ll;
    private Button next_bt;//下一步

    //得到上一个页面的数据
    private String commpanynumber;
    private String goodsname;
    private String goodsmaterial;
    private String goodsunit;
    private String moq;
    private String goodsweight;
    private String goodsweightunit;

    //得到当前页面输入的数据
    private String goodscolor;
    private String goodsprice;
    private String huobi;
    private String tiaokuan;
    private String tiaokuan_diy;

    private String outboxamout;
    private String outboxsize;
    private String outboxweight;
    private String outboxweightunit;
    private String centerboxamout;
    private String centerboxsize;
    private String centerboxweight;
    private String centerboxweightunit;


    private LayoutInflater ml;
    private PopupWindow popupWindow;
    private ListView popup_lv;


    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;
    public static NewGoodsActivitytwo activitytwo;
    private List<String> dropList = new ArrayList<>();//popuWindow中list

    private TextView more_tv;
    private ViewStub viewStub;

    private String tag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goods_activitytwo);
        activitytwo = this;
        initYuyan();
        initview();
        getLastData();
    }


    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(this);
        if (language != null) {
            tag = this.language.getTag();
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
        goods_color_et = (EditText) findViewById(R.id.goods_color_et);
        goods_price_et = (EditText) findViewById(R.id.goods_price_et);
        huobi_tv = (TextView) findViewById(R.id.huobi_tv);
        tiaokuan_tv = (TextView) findViewById(R.id.tiaokuan_tv);
        input_address = (TextView) findViewById(R.id.input_address);
        huobi_ll = (LinearLayout) findViewById(R.id.huobi_ll);
        tiaokuan_ll = (LinearLayout) findViewById(R.id.tiaokuan_ll);
        next_bt = (Button) findViewById(R.id.next_bt);

        more_tv = (TextView) findViewById(R.id.more_tv);

        more_tv.setOnClickListener(NewGoodsActivitytwo.this);
        fanhui_rl.setOnClickListener(NewGoodsActivitytwo.this);
        huobi_ll.setOnClickListener(NewGoodsActivitytwo.this);
        tiaokuan_ll.setOnClickListener(NewGoodsActivitytwo.this);
        next_bt.setOnClickListener(NewGoodsActivitytwo.this);

        ml = LayoutInflater.from(NewGoodsActivitytwo.this);
        View view = ml.inflate(R.layout.listview_popupwindow, null);
        popup_lv = (ListView) view.findViewById(R.id.popup_lv);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            //返回
            case R.id.fanhui_rl:
                finish();
                break;
            //选择货币
            case R.id.huobi_ll:
                showPopuWindow(huobi_ll, 1);
                break;
            //选择条款
            case R.id.tiaokuan_ll:
                showPopuWindow(tiaokuan_ll, 2);
                break;
            //外箱重量单位
            case R.id.outbox_weight_unit_ll:
                showPopuWindow(outbox_weight_unit_ll, 3);
                break;
            //中盒重量单位
            case R.id.centerbox_weight_unit_ll:
                showPopuWindow(centerbox_weight_unit_ll, 4);
                break;
            //下一步
            case R.id.next_bt:
                    getData();
                    intent = new Intent(NewGoodsActivitytwo.this, NewGoodsActivitythree.class);
                    intent.putExtra("commpanynumber", commpanynumber);
                    intent.putExtra("goodsname", goodsname);
                    intent.putExtra("goodsmaterial", goodsmaterial);
                    intent.putExtra("goodsunit", goodsunit);
                    intent.putExtra("moq", moq);
                    intent.putExtra("goodsweight", goodsweight);
                    intent.putExtra("goodsweightunit", goodsweightunit);

                    intent.putExtra("goodscolor", goodscolor);
                    intent.putExtra("goodsprice", goodsprice);
                    intent.putExtra("huobi", huobi);
                    intent.putExtra("tiaokuan", tiaokuan);
                    intent.putExtra("tiaokuan_diy", tiaokuan_diy);
                    intent.putExtra("outboxamout", outboxamout);
                    intent.putExtra("outboxsize", outboxsize);
                    intent.putExtra("outboxweight", outboxweight);
                    intent.putExtra("outboxweightunit", outboxweightunit);
                    intent.putExtra("centerboxamout", centerboxamout);
                    intent.putExtra("centerboxsize", centerboxsize);
                    intent.putExtra("centerboxweight", centerboxweight);
                    intent.putExtra("centerboxweightunit", centerboxweightunit);
                    startActivity(intent);
                break;
            case R.id.more_tv:
                viewStub = (ViewStub) findViewById(R.id.view_stub);
                if (viewStub != null) {
                    View inflatedView = viewStub.inflate();
                    outbox_amout_et = (EditText) inflatedView.findViewById(R.id.outbox_amout_et);
                    outbox_size_et = (EditText) inflatedView.findViewById(R.id.outbox_size_et);
                    outbox_weight_et = (EditText) inflatedView.findViewById(R.id.outbox_weight_et);
                    outbox_weight_unit_tv = (TextView) inflatedView.findViewById(R.id.outbox_weight_unit_tv);
                    centerbox_amout_et = (EditText) inflatedView.findViewById(R.id.centerbox_amout_et);
                    centerbox_size_et = (EditText) inflatedView.findViewById(R.id.centerbox_size_et);
                    centerbox_weight_et = (EditText) inflatedView.findViewById(R.id.centerbox_weight_et);
                    centerbox_weight_unit_tv = (TextView) inflatedView.findViewById(R.id.centerbox_weight_unit_tv);
                    outbox_weight_unit_ll = (LinearLayout) inflatedView.findViewById(R.id.outbox_weight_unit_ll);
                    centerbox_weight_unit_ll = (LinearLayout) inflatedView.findViewById(R.id.centerbox_weight_unit_ll);
                    outbox_weight_unit_ll.setOnClickListener(NewGoodsActivitytwo.this);
                    centerbox_weight_unit_ll.setOnClickListener(NewGoodsActivitytwo.this);
                    more_tv.setVisibility(View.GONE);

                }
                break;

        }
    }

    //得到上一个页面传递过来的数据
    private void getLastData() {
        Intent intent = getIntent();
        commpanynumber = intent.getStringExtra("commpanynumber");
        goodsname = intent.getStringExtra("goodsname");
        goodsmaterial = intent.getStringExtra("goodsmaterial");
        goodsunit = intent.getStringExtra("goodsunit");

        moq = intent.getStringExtra("moq");
        goodsweight = intent.getStringExtra("goodsweight");
        goodsweightunit = intent.getStringExtra("goodsweightunit");

    }

    //得到当前页面用户输入的部分数据
    private void getData() {
        goodscolor = goods_color_et.getText().toString().trim();
        goodsprice = goods_price_et.getText().toString().trim();
        huobi = huobi_tv.getText().toString().trim();
        tiaokuan = tiaokuan_tv.getText().toString().trim();
        tiaokuan_diy = input_address.getText().toString().trim();
        if (viewStub != null) {
            outboxamout = outbox_amout_et.getText().toString().trim();
            outboxsize = outbox_size_et.getText().toString().trim();
            outboxweight = outbox_weight_et.getText().toString().trim();
            outboxweightunit = outbox_weight_unit_tv.getText().toString().trim();
            centerboxamout = centerbox_amout_et.getText().toString().trim();
            centerboxsize = centerbox_size_et.getText().toString().trim();
            centerboxweight = centerbox_weight_et.getText().toString().trim();
            centerboxweightunit = centerbox_weight_unit_tv.getText().toString().trim();
        }


    }

    // 设置添加屏幕的背景透明度
    private void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    private void showPopuWindow(final View v, int status) {
        dropList.clear();
        initDate(status);
        View mV = LayoutInflater.from(NewGoodsActivitytwo.this).inflate(R.layout.listview_popupwindow, null);
        mV.setBackgroundColor(Color.WHITE);
        popupWindow = new PopupWindow(mV, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(mV);
        final ListView listview = (ListView) mV.findViewById(R.id.popup_lv);
        ArrayAdapter adapter = new ArrayAdapter(NewGoodsActivitytwo.this, android.R.layout.simple_list_item_1, dropList);
        listview.setAdapter(adapter);
        popupWindow.setFocusable(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = dropList.get(position);
                switch (v.getId()) {
                    case R.id.huobi_ll:  //货币单位
                        huobi_tv.setText(s);
                        break;
                    case R.id.tiaokuan_ll://价格条款
                        tiaokuan_tv.setText(s);
                        if (!s.equals("EXW")) {
                            input_address.setVisibility(View.VISIBLE);
                        } else {
                            input_address.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case R.id.outbox_weight_unit_ll://外箱重量单位
                        outbox_weight_unit_tv.setText(s);
                        break;
                    case R.id.centerbox_weight_unit_ll://中盒重量单位
                        centerbox_weight_unit_tv.setText(s);
                        break;

                }
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(false);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(NewGoodsActivitytwo.this, 0.5f);//0.0-1.0
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(NewGoodsActivitytwo.this, 1f);
            }
        });
    }

    private void initDate(int status) {
        switch (status) {
            case 1:
                if (tag.equals("ch")) {
                    dropList.add("美元");
                    dropList.add("欧元");
                    dropList.add("英镑");
                    dropList.add("日元");
                    dropList.add("人民币");
                }else if(tag.equals("en")){
                    dropList.add("Dollar");
                    dropList.add("EUR");
                    dropList.add("GBP");
                    dropList.add("JPY");
                    dropList.add("RMB");
                }

                break;
            case 2:
                dropList.add("EXW");
                dropList.add("FOB");
                dropList.add("CFR");
                dropList.add("CIF");
                break;
            case 3:
                dropList.add("mg");
                dropList.add("g");
                dropList.add("kg");
                dropList.add("t");
                break;
            case 4:
                dropList.add("mg");
                dropList.add("g");
                dropList.add("kg");
                dropList.add("t");
                break;
        }
    }


}





