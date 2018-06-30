package com.baibeiyun.eazyfair.activity.buyer.inquiryprice;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.ShowBigPicture;
import com.baibeiyun.eazyfair.activity.base.BaseActivityforBuyer;
import com.baibeiyun.eazyfair.adapter.GoodsDetailParameterAdapter;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyInquiry;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.DataType;
import com.baibeiyun.eazyfair.utils.OtherUtils;
import com.baibeiyun.eazyfair.utils.aboutpic.HackyViewPager;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class DetailforInquiryActivity extends BaseActivityforBuyer implements View.OnClickListener {
    private RelativeLayout fanhui_rl;
    private TextView money_fh;
    private TextView goods_name;
    private TextView goods_price;
    private TextView company_goods_number_tv;//商品货号
    private TextView goods_material_tv;//商品材质
    private TextView goods_unit_tv;//商品单位
    private TextView goods_color_tv;//商品颜色

    private TextView goods_clause_tv;//货币类型
    private TextView moq_tv;
    private TextView price_tk_tv;
    private TextView price_diy_tv;
    private TextView goods_weight_tv;
    private TextView goods_weight_unit_tv;
    private TextView goods_remark_tv;

    private TextView goods_indroduce;//商品简介

    private ListView goods_parameter_lv;//商品自定义参数
    private GoodsDetailParameterAdapter goodsDetailParameterAdapter;//商品参数的适配器
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();
    //得到传递过来的id
    private String id;
    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private ArrayList<Language> listforlanguage = new ArrayList<>();
    private Language language;

    private ArrayList<MyInquiry> myInquiries = new ArrayList<>();
    private MyInquiry myInquiry;

    private Bitmap bitmap1, bitmap2, bitmap3, bitmap4;

    //---------------------------图片轮播-------------------------
    private HackyViewPager viewPager;
    private ArrayList<View> allListView;
    private int position = 0;

    private Bitmap[] resId;

    private TextView outbox_amout_tv;
    private TextView outbox_weight_tv;
    private TextView outbox_unit_tv;
    private TextView outbox_amout_size_tv;
    private TextView centerbox_amout_tv;
    private TextView centerbox_weight_tv;
    private TextView centerbox_unit_tv;
    private TextView centerbox_size_tv;

    //商品参数的数据源
    private void getData() {
        datas.clear();
        String self_defined = myInquiry.getSelf_defined();
        String[] split1 = self_defined.split("\\|");
        for (String aSplit1 : split1) {
            Map<String, Object> map1 = new ArrayMap<>();
            int a = aSplit1.indexOf(":");
            map1.put("parameter_title_tv", aSplit1.substring(0, a));
            map1.put("parameter_content_tv", aSplit1.substring(a + 1, aSplit1.length()));
            datas.add(map1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_for_inquiry);
        initYuyan();
        initView();
        initData();

        setData();
        initAboutPic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (datas != null) {
            datas.clear();
            datas = null;
        }
        if (listforlanguage != null) {
            listforlanguage.clear();
            listforlanguage = null;
        }
        if (myInquiries != null) {
            myInquiries.clear();
            myInquiries = null;
        }

    }


    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(DetailforInquiryActivity.this);
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


    private void initView() {
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        money_fh = (TextView) findViewById(R.id.money_fh);
        goods_name = (TextView) findViewById(R.id.goods_name);
        goods_price = (TextView) findViewById(R.id.goods_price);
        company_goods_number_tv = (TextView) findViewById(R.id.company_goods_number_tv);
        goods_material_tv = (TextView) findViewById(R.id.goods_material_tv);
        goods_unit_tv = (TextView) findViewById(R.id.goods_unit_tv);
        goods_color_tv = (TextView) findViewById(R.id.goods_color_tv);
        goods_clause_tv = (TextView) findViewById(R.id.goods_clause_tv);
        moq_tv = (TextView) findViewById(R.id.moq_tv);
        price_tk_tv = (TextView) findViewById(R.id.price_tk_tv);
        price_diy_tv = (TextView) findViewById(R.id.price_diy_tv);
        goods_weight_tv = (TextView) findViewById(R.id.goods_weight_tv);
        goods_weight_unit_tv = (TextView) findViewById(R.id.goods_weight_unit_tv);
        goods_remark_tv = (TextView) findViewById(R.id.goods_remark_tv);

        goods_indroduce = (TextView) findViewById(R.id.goods_indroduce);
        goods_parameter_lv = (ListView) findViewById(R.id.goods_parameter_lv);

        outbox_amout_tv= (TextView) findViewById(R.id.outbox_amout_tv);
        outbox_weight_tv= (TextView) findViewById(R.id.outbox_weight_tv);
        outbox_unit_tv= (TextView) findViewById(R.id.outbox_unit_tv);
        outbox_amout_size_tv= (TextView) findViewById(R.id.outbox_amout_size_tv);
        centerbox_amout_tv= (TextView) findViewById(R.id.centerbox_amout_tv);
        centerbox_weight_tv= (TextView) findViewById(R.id.centerbox_weight_tv);
        centerbox_unit_tv= (TextView) findViewById(R.id.centerbox_unit_tv);
        centerbox_size_tv= (TextView) findViewById(R.id.centerbox_size_tv);
        fanhui_rl.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

    }


    //将数据设置到控件上
    private void setData() {
        myInquiry = CursorUtils.selectInquiryById(DetailforInquiryActivity.this, id);
        goods_name.setText(myInquiry.getGoods_name());
        goods_price.setText(String.valueOf(myInquiry.getPrice()));
        company_goods_number_tv.setText(myInquiry.getSerial_number());
        goods_material_tv.setText(myInquiry.getMaterial());
        goods_unit_tv.setText(myInquiry.getGoods_unit());
        goods_color_tv.setText(myInquiry.getColor());

        goods_indroduce.setText(myInquiry.getIntroduction());
        switch (myInquiry.getCurrency_varitety()) {
            case "人民币":
            case "RMB":
                money_fh.setText("￥");
                break;
            case "美元":
            case "dollar":
                money_fh.setText("$");
                break;
            case "欧元":
            case "Euro":
                money_fh.setText("€");
                break;
            case "英镑":
            case "Pound":
                money_fh.setText("￡");
                break;
            case "日元":
            case "Yen":
                money_fh.setText("JP￥");
                break;
        }
        byte[] product_imgs1 = myInquiry.getProduct_imgs1();
        byte[] product_imgs2 = myInquiry.getProduct_imgs2();
        byte[] product_imgs3 = myInquiry.getProduct_imgs3();
        byte[] product_imgs4 = myInquiry.getProduct_imgs4();
        bitmap1 = BitmapUtils.Bytes2Bimap(product_imgs1);
        bitmap2 = BitmapUtils.Bytes2Bimap(product_imgs2);
        bitmap3 = BitmapUtils.Bytes2Bimap(product_imgs3);
        bitmap4 = BitmapUtils.Bytes2Bimap(product_imgs4);
        resId = new Bitmap[]{bitmap1, bitmap2, bitmap3, bitmap4};
        goodsDetailParameterAdapter = new GoodsDetailParameterAdapter(datas, DetailforInquiryActivity.this);
        goods_parameter_lv.setAdapter(goodsDetailParameterAdapter);//为listview设置适配器
        if (myInquiry.getSelf_defined() != null) {
            getData();//加载商品参数的数据源
        }
        OtherUtils.setListViewHeightBasedOnChildren(goods_parameter_lv);
        goods_parameter_lv.setDividerHeight(0);//去掉该listview的分割线

        goods_clause_tv.setText(myInquiry.getCurrency_varitety());
        moq_tv.setText(myInquiry.getMoq());
        price_tk_tv.setText(myInquiry.getPrice_clause());
        price_diy_tv.setText(myInquiry.getPrice_clause_diy());
        goods_weight_tv.setText(myInquiry.getGoods_weight());
        goods_weight_unit_tv.setText(myInquiry.getGoods_weight_unit());
        goods_remark_tv.setText(myInquiry.getRemark());

        outbox_amout_tv.setText(myInquiry.getOutbox_number());
        outbox_weight_tv.setText(myInquiry.getOutbox_weight());
        outbox_unit_tv.setText(myInquiry.getOutbox_weight_unit());
        outbox_amout_size_tv.setText(myInquiry.getOutbox_size());
        centerbox_amout_tv.setText(myInquiry.getCenterbox_number());
        centerbox_weight_tv.setText(myInquiry.getCenterbox_weight());
        centerbox_unit_tv.setText(myInquiry.getCenterbox_weight_unit());
        centerbox_size_tv.setText(myInquiry.getCenterbox_size());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
        }
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
            backgroundAlpha(DetailforInquiryActivity.this, 1f);
        }

    }

    //--------------------------图片轮播----------------------


    private void initAboutPic() {
        viewPager = (HackyViewPager) findViewById(R.id.iv_baby);
        initViewPager();
        ViewPagerAdapter mPagerAdapter = new ViewPagerAdapter();
        // 绑定适配器
        viewPager.setAdapter(mPagerAdapter);
    }

    private void initViewPager() {

        if (allListView != null) {
            allListView.clear();
            allListView = null;
        }
        allListView = new ArrayList<>();

        for (int i = 0; i < resId.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.pic_item, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
            imageView.setImageBitmap(resId[i]);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // 跳到查看大图界面
                    Intent intent = new Intent(DetailforInquiryActivity.this, ShowBigPicture.class);
                    intent.putExtra("position", position);
                    intent.putExtra("DataType", DataType.inquirytype_id);
                    intent.putExtra("id", id);

                    startActivity(intent);
                }
            });
            allListView.add(view);
        }

        viewPager = (HackyViewPager) findViewById(R.id.iv_baby);
        ViewPagerAdapter adapter = new ViewPagerAdapter();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                position = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        viewPager.setAdapter(adapter);

    }

    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return allListView.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = allListView.get(position);
            container.addView(view);
            return view;
        }

    }


}
