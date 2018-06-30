package com.baibeiyun.eazyfair.activity.supplier.offer;

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
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.adapter.GoodsDetailParameterAdapter;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyQuote;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.DataType;
import com.baibeiyun.eazyfair.utils.OtherUtils;
import com.baibeiyun.eazyfair.utils.aboutpic.HackyViewPager;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class DetailGoodsforQuoteActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout fanhui_rl;
    private TextView goods_name;
    private TextView money_fh;
    private TextView goods_price;

    private TextView commpany_goods_number_tv;
    private TextView goods_material_tv;
    private TextView goods_size_tv;//商品单位
    private TextView goods_color_tv;
    private TextView goods_clause_tv;//货币类型
    private TextView moq_tv;//最少起订量
    private TextView price_tk_tv;
    private TextView price_diy_tv;
    private TextView goods_weight_tv;
    private TextView goods_weight_unit_tv;
    private TextView goods_remark_tv;

    private TextView goods_indroduce;
    private String id;

    private MyQuote myQuote;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private ArrayList<Language> listforlanguage = new ArrayList<>();
    private Language language;

    private String self_defined;
    private Bitmap bitmap1, bitmap2, bitmap3, bitmap4;
    private ListView goods_parameter_lv;
    private ArrayList<Map<String, Object>> datas = new ArrayList<>();
    private GoodsDetailParameterAdapter goodsDetailParameterAdapter;

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
    private TextView centerbox__size_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_goodsfor_quote);
        initYuyan();
        initView();
        initData();
        initAboutPic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (listforlanguage != null) {
            listforlanguage.clear();
            listforlanguage = null;
        }
        if (datas != null) {
            datas.clear();
            datas = null;
        }
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(DetailGoodsforQuoteActivity.this);
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
        goods_name = (TextView) findViewById(R.id.goods_name);
        money_fh = (TextView) findViewById(R.id.money_fh);
        goods_price = (TextView) findViewById(R.id.goods_price);
        commpany_goods_number_tv = (TextView) findViewById(R.id.commpany_goods_number_tv);
        goods_material_tv = (TextView) findViewById(R.id.goods_material_tv);
        goods_size_tv = (TextView) findViewById(R.id.goods_size_tv);
        goods_color_tv = (TextView) findViewById(R.id.goods_color_tv);

        outbox_amout_tv = (TextView) findViewById(R.id.outbox_amout_tv);
        outbox_weight_tv = (TextView) findViewById(R.id.outbox_weight_tv);
        outbox_unit_tv = (TextView) findViewById(R.id.outbox_unit_tv);
        outbox_amout_size_tv = (TextView) findViewById(R.id.outbox_amout_size_tv);
        centerbox_amout_tv = (TextView) findViewById(R.id.centerbox_amout_tv);
        centerbox_weight_tv = (TextView) findViewById(R.id.centerbox_weight_tv);
        centerbox_unit_tv = (TextView) findViewById(R.id.centerbox_unit_tv);
        centerbox__size_tv = (TextView) findViewById(R.id.centerbox__size_tv);

        goods_clause_tv = (TextView) findViewById(R.id.goods_clause_tv);
        moq_tv = (TextView) findViewById(R.id.moq_tv);
        price_tk_tv = (TextView) findViewById(R.id.price_tk_tv);
        price_diy_tv = (TextView) findViewById(R.id.price_diy_tv);
        goods_weight_tv = (TextView) findViewById(R.id.goods_weight_tv);
        goods_weight_unit_tv = (TextView) findViewById(R.id.goods_weight_unit_tv);
        goods_remark_tv = (TextView) findViewById(R.id.goods_remark_tv);

        goods_parameter_lv = (ListView) findViewById(R.id.goods_parameter_lv);
        goods_indroduce = (TextView) findViewById(R.id.goods_indroduce);
        fanhui_rl.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        myQuote = CursorUtils.selectQuoteById(DetailGoodsforQuoteActivity.this, id);
        goods_name.setText(myQuote.getGoods_name());
        goods_price.setText(String.valueOf(myQuote.getPrice()));
        commpany_goods_number_tv.setText(myQuote.getSerial_number());
        goods_material_tv.setText(myQuote.getMaterial());
        goods_size_tv.setText(myQuote.getGoods_unit());
        goods_color_tv.setText(myQuote.getColor());
        goods_clause_tv.setText(myQuote.getCurrency_varitety());
        moq_tv.setText(myQuote.getMoq());
        price_tk_tv.setText(myQuote.getPrice_clause());
        price_diy_tv.setText(myQuote.getPrice_clause_diy());
        goods_weight_tv.setText(myQuote.getGoods_weight());
        goods_weight_unit_tv.setText(myQuote.getGoods_weight_unit());
        goods_remark_tv.setText(myQuote.getRemark());

        goods_indroduce.setText(myQuote.getIntroduction());

        //得到自定义的字符串
        self_defined = myQuote.getSelf_defined();
        byte[] product_imgs1 = myQuote.getProduct_imgs1();
        byte[] product_imgs2 = myQuote.getProduct_imgs2();
        byte[] product_imgs3 = myQuote.getProduct_imgs3();
        byte[] product_imgs4 = myQuote.getProduct_imgs4();
        bitmap1 = BitmapUtils.Bytes2Bimap(product_imgs1);
        bitmap2 = BitmapUtils.Bytes2Bimap(product_imgs2);
        bitmap3 = BitmapUtils.Bytes2Bimap(product_imgs3);
        bitmap4 = BitmapUtils.Bytes2Bimap(product_imgs4);
        resId = new Bitmap[]{bitmap1, bitmap2, bitmap3, bitmap4};
        goods_parameter_lv = (ListView) findViewById(R.id.goods_parameter_lv);//创建商品参数的listview对象
        if (self_defined != null) {
            getData();//加载商品参数的数据源
        }
        goodsDetailParameterAdapter = new GoodsDetailParameterAdapter(datas, DetailGoodsforQuoteActivity.this);
        goods_parameter_lv.setAdapter(goodsDetailParameterAdapter);//为listview设置适配器
        OtherUtils.setListViewHeightBasedOnChildren(goods_parameter_lv);
        goods_parameter_lv.setDividerHeight(0);//去掉该listview的分割线

        switch (myQuote.getCurrency_varitety()) {
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
        outbox_amout_tv.setText(myQuote.getOutbox_number());
        outbox_weight_tv.setText(myQuote.getOutbox_weight());
        outbox_unit_tv.setText(myQuote.getOutbox_weight_unit());
        outbox_amout_size_tv.setText(myQuote.getOutbox_size());
        centerbox_amout_tv.setText(myQuote.getCenterbox_number());
        centerbox_weight_tv.setText(myQuote.getCenterbox_weight());
        centerbox_unit_tv.setText(myQuote.getCenterbox_weight_unit());
        centerbox__size_tv.setText(myQuote.getCenterbox_size());
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
            backgroundAlpha(DetailGoodsforQuoteActivity.this, 1f);
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
                    Intent intent = new Intent(DetailGoodsforQuoteActivity.this, ShowBigPicture.class);
                    intent.putExtra("position", position);
                    intent.putExtra("DataType", DataType.quotetype_id);
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
