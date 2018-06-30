package com.baibeiyun.eazyfair.activity.supplier.mygoods;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.ShowBigPicture;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.adapter.GoodsDetailParameterAdapter;
import com.baibeiyun.eazyfair.adapter.XunJiaRecordAdapter;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCommodity;
import com.baibeiyun.eazyfair.entities.MyQuote;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.DataType;
import com.baibeiyun.eazyfair.utils.OtherUtils;
import com.baibeiyun.eazyfair.utils.aboutpic.HackyViewPager;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;//返回
    private RelativeLayout more_rv;//更多
    private LinearLayout inquiry_record_ll;//询价记录模块


    private TextView goods_name;//商品名称
    private TextView goods_price;//商品价格
    private TextView commpany_goods_number_tv;//公司货号
    private TextView goods_material_tv;//材质
    private TextView goods_size_tv;//商品尺寸
    private TextView goods_color_tv;//商品颜色
    private TextView goods_clause_tv;//条款
    private TextView goods_remark_tv;//备注
    private TextView goods_indroduce;//商品介绍
    private TextView price_tk_tv;//价格条款
    private TextView price_diy_tv;//价格条款自定义部分
    private Context context = null;
    private int from = 0;
    private PopupWindow mPopupWindow;

    private GoodsDetailParameterAdapter goodsDetailParameterAdapter;//商品参数的适配器
    private ListView goods_parameter_lv;//商品参数
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();
    private ArrayList<Map<String, Object>> datas1 = new ArrayList<>();
    private String self_defined;//自定义的字符串
    private Bitmap bitmap1, bitmap2, bitmap3, bitmap4;
    private int id;


    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;

    private TextView money_fh;
    private String tag;

    //询价记录
    private ListView inquiry_customer_lv;
    //询价客户的适配器
    private XunJiaRecordAdapter xunJiaRecordAdapter;


    private TextView moq_tv;
    private TextView goods_weight_tv;
    private TextView goods_weight_unit_tv;
    private TextView outbox_amout_tv;
    private TextView outbox_amout_size_tv;
    private TextView outbox_weight_tv;
    private TextView outbox_unit_tv;

    private TextView centerbox_amout_tv;
    private TextView centerbox_weight_tv;
    private TextView centerbox_unit_tv;
    private TextView centerbox__size_tv;


    private MyCommodity myCommodity;
    private ArrayList<MyQuote> myQuotes = new ArrayList<>();

    //---------------------------图片轮播-------------------------
    private HackyViewPager viewPager;
    private ArrayList<View> allListView;
    private int position = 0;
    private Bitmap[] resId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_goods_detail);
        initYuyan();
        initview();
        initData();
        initAboutPic();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (datas != null) {
            datas.clear();
            datas = null;
        }
        if (datas1 != null) {
            datas1.clear();
            datas1 = null;
        }

    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(GoodsDetailActivity.this);
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


    private void getDataforxj() {
        datas1.clear();
        int size = myQuotes.size();
        for (int i = 0; i < size; i++) {
            MyQuote myQuote = myQuotes.get(i);
            Map<String, Object> data = new ArrayMap<>();
            data.put("name_tv", myQuote.getBuyer_name());
            data.put("price_tv", myQuote.getPrice());
            data.put("time_tv", myQuote.getQuote_time());
            data.put("_id", myQuote.get_id());
            data.put("currency_variety", myQuote.getCurrency_varitety());
            datas1.add(data);
        }
    }


    //商品参数的数据源
    private void getData() {
        datas.clear();
        String[] split1 = self_defined.split("\\|");
        for (String aSplit1 : split1) {
            Map<String, Object> map1 = new ArrayMap<>();
            int a = aSplit1.indexOf(":");
            map1.put("parameter_title_tv", aSplit1.substring(0, a));
            map1.put("parameter_content_tv", aSplit1.substring(a + 1, aSplit1.length()));
            datas.add(map1);
        }
    }

    private void initview() {
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        more_rv = (RelativeLayout) findViewById(R.id.more_rv);
        goods_name = (TextView) findViewById(R.id.goods_name);
        goods_price = (TextView) findViewById(R.id.goods_price);
        commpany_goods_number_tv = (TextView) findViewById(R.id.commpany_goods_number_tv);
        goods_material_tv = (TextView) findViewById(R.id.goods_material_tv);
        goods_size_tv = (TextView) findViewById(R.id.goods_size_tv);
        goods_color_tv = (TextView) findViewById(R.id.goods_color_tv);
        goods_clause_tv = (TextView) findViewById(R.id.goods_clause_tv);
        goods_remark_tv = (TextView) findViewById(R.id.goods_remark_tv);
        goods_indroduce = (TextView) findViewById(R.id.goods_indroduce);
        price_tk_tv = (TextView) findViewById(R.id.price_tk_tv);
        price_diy_tv = (TextView) findViewById(R.id.price_diy_tv);
        money_fh = (TextView) findViewById(R.id.money_fh);

        moq_tv = (TextView) findViewById(R.id.moq_tv);
        goods_weight_tv = (TextView) findViewById(R.id.goods_weight_tv);
        goods_weight_unit_tv = (TextView) findViewById(R.id.goods_weight_unit_tv);
        outbox_amout_tv = (TextView) findViewById(R.id.outbox_amout_tv);
        outbox_amout_size_tv = (TextView) findViewById(R.id.outbox_amout_size_tv);
        outbox_weight_tv = (TextView) findViewById(R.id.outbox_weight_tv);
        outbox_unit_tv = (TextView) findViewById(R.id.outbox_unit_tv);

        centerbox_amout_tv = (TextView) findViewById(R.id.centerbox_amout_tv);
        centerbox_weight_tv = (TextView) findViewById(R.id.centerbox_weight_tv);
        centerbox_unit_tv = (TextView) findViewById(R.id.centerbox_unit_tv);
        centerbox__size_tv = (TextView) findViewById(R.id.centerbox__size_tv);


        inquiry_record_ll = (LinearLayout) findViewById(R.id.inquiry_record_ll);
        inquiry_customer_lv = (ListView) findViewById(R.id.inquiry_customer_lv);
        fanhui_rl.setOnClickListener(GoodsDetailActivity.this);
        more_rv.setOnClickListener(GoodsDetailActivity.this);
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        tag = intent.getStringExtra("tag");
        if (tag.equals("1")) {
            inquiry_record_ll.setVisibility(View.GONE);
        } else if (tag.equals("2")) {
            inquiry_record_ll.setVisibility(View.VISIBLE);
        }
        myCommodity = CursorUtils.selectById(GoodsDetailActivity.this, id);
        myQuotes = CursorUtils.selectByAllReadlyQuote(GoodsDetailActivity.this, id);
        //将实体类里面的数据显示到控件上
        goods_name.setText(myCommodity.getName());//商品名字
        goods_price.setText(String.valueOf(myCommodity.getPrice()));//商品价格
        commpany_goods_number_tv.setText(myCommodity.getSerial_number());//公司货号
        goods_material_tv.setText(myCommodity.getMaterial());//材质
        goods_size_tv.setText(myCommodity.getUnit());//尺寸
        goods_color_tv.setText(myCommodity.getColor());//颜色
        goods_clause_tv.setText(myCommodity.getCurrency_variety());//货币单位
        goods_indroduce.setText(myCommodity.getIntroduction());//简介
        price_tk_tv.setText(myCommodity.getPrice_clause());//价格条款
        price_diy_tv.setText(myCommodity.getPrice_clause_diy());
        goods_remark_tv.setText(myCommodity.getRemark());//备注
        switch (myCommodity.getCurrency_variety()) {
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
        //得到自定义的字符串
        self_defined = myCommodity.getDiy();
        byte[] product_imgs1 = myCommodity.getProduct_imgs1();
        byte[] product_imgs2 = myCommodity.getProduct_imgs2();
        byte[] product_imgs3 = myCommodity.getProduct_imgs3();
        byte[] product_imgs4 = myCommodity.getProduct_imgs4();
        bitmap1 = BitmapUtils.Bytes2Bimap(product_imgs1);
        bitmap2 = BitmapUtils.Bytes2Bimap(product_imgs2);
        bitmap3 = BitmapUtils.Bytes2Bimap(product_imgs3);
        bitmap4 = BitmapUtils.Bytes2Bimap(product_imgs4);
        resId = new Bitmap[]{bitmap1, bitmap2, bitmap3, bitmap4};
        goods_parameter_lv = (ListView) findViewById(R.id.goods_parameter_lv);//创建商品参数的listview对象
        if (self_defined != null) {
            getData();//加载商品参数的数据源
        }
        goodsDetailParameterAdapter = new GoodsDetailParameterAdapter(datas, GoodsDetailActivity.this);
        goods_parameter_lv.setAdapter(goodsDetailParameterAdapter);//为listview设置适配器
        OtherUtils.setListViewHeightBasedOnChildren(goods_parameter_lv);
        goods_parameter_lv.setDividerHeight(0);//去掉该listview的分割线

        moq_tv.setText(myCommodity.getMoq());

        goods_weight_tv.setText(myCommodity.getGoods_weight());
        goods_weight_unit_tv.setText(myCommodity.getGoods_weight_unit());
        outbox_amout_tv.setText(myCommodity.getOutbox_number());
        outbox_weight_tv.setText(myCommodity.getOutbox_weight());
        outbox_amout_size_tv.setText(myCommodity.getOutbox_size());
        outbox_unit_tv.setText(myCommodity.getOutbox_weight_unit());

        centerbox_amout_tv.setText(myCommodity.getCenterbox_number());
        centerbox_weight_tv.setText(myCommodity.getCenterbox_weight());
        centerbox_unit_tv.setText(myCommodity.getCenterbox_weight_unit());
        centerbox__size_tv.setText(myCommodity.getCenterbox_size());

        getDataforxj();
        xunJiaRecordAdapter = new XunJiaRecordAdapter(datas1, GoodsDetailActivity.this);
        inquiry_customer_lv.setAdapter(xunJiaRecordAdapter);
        OtherUtils.setListViewHeightBasedOnChildren(inquiry_customer_lv);
        xunJiaRecordAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
            case R.id.more_rv:
                from = Location.BOTTOM.ordinal();
                //调用此方法，menu不会顶置
                initPopupWindow();
                break;
        }
    }


    //点击更多的PopupWindow弹出的方向
    private enum Location {
        BOTTOM
    }

    private void initPopupWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.popup_share, null);
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        } else {
            mPopupWindow = new PopupWindow(popupWindowView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        }

        //动画效果
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow.setAnimationStyle(R.style.AnimationBottomToTop);
        }
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        mPopupWindow.setBackgroundDrawable(dw);
        if (Location.BOTTOM.ordinal() == from) {
            mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_goods_detail, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(GoodsDetailActivity.this, 0.5f);
        //关闭事件
        mPopupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        LinearLayout msg_ch_ll = (LinearLayout) popupWindowView.findViewById(R.id.msg_ch_ll);
        LinearLayout msg_en_ll = (LinearLayout) popupWindowView.findViewById(R.id.msg_en_ll);
        LinearLayout cancel_ll = (LinearLayout) popupWindowView.findViewById(R.id.cancel_ll);
        //发送
        msg_ch_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoodsDetailActivity.this, SendGoodsDataActivity.class);
                intent.putExtra("id", id);//id号
                intent.putExtra("tag",1);//供应商为1
                intent.putExtra("lang","ch");
                startActivity(intent);
                mPopupWindow.dismiss();
            }
        });
        msg_en_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsDetailActivity.this, SendGoodsDataActivity.class);
                intent.putExtra("id", id);//id号
                intent.putExtra("tag",1);//供应商为1
                intent.putExtra("lang","en");
                startActivity(intent);
                mPopupWindow.dismiss();

            }
        });

        //取消
        cancel_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
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
            backgroundAlpha(GoodsDetailActivity.this, 1f);
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
                    Intent intent = new Intent(GoodsDetailActivity.this, ShowBigPicture.class);
                    intent.putExtra("position", position);
                    intent.putExtra("DataType", DataType.goodstype_id);
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
