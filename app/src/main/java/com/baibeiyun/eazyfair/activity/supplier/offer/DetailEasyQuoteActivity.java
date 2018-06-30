package com.baibeiyun.eazyfair.activity.supplier.offer;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.activity.ShowBigPicture;
import com.baibeiyun.eazyfair.activity.base.BaseActivity;
import com.baibeiyun.eazyfair.entities.EasyQuote;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.DataType;
import com.baibeiyun.eazyfair.utils.aboutpic.HackyViewPager;

import java.util.ArrayList;
import java.util.Locale;

public class DetailEasyQuoteActivity extends BaseActivity implements View.OnClickListener {

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;
    private Language language;
    private int id;
    private RelativeLayout fanhui_rl;
    private HackyViewPager iv_img;
    private TextView goodsname_tv;
    private TextView content_tv;

    //图片轮播
    private HackyViewPager viewPager;
    private ArrayList<View> allListView;
    private int position = 0;
    private Bitmap[] resId;

    private EasyQuote easyQuote;
    private Bitmap bitmap1, bitmap2, bitmap3, bitmap4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_easy_quote);
        initYuyan();
        initView();
        initData();
        initAboutPic();
    }

    private void initYuyan() {
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();
        language = CursorUtils.selectYuYan(DetailEasyQuoteActivity.this);
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

    private void initView() {
        fanhui_rl = (RelativeLayout) findViewById(R.id.fanhui_rl);
        iv_img = (HackyViewPager) findViewById(R.id.iv_img);
        goodsname_tv= (TextView) findViewById(R.id.goodsname_tv);
        content_tv = (TextView) findViewById(R.id.content_tv);
        fanhui_rl.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        easyQuote = CursorUtils.selectEasyQuoteById(this, id);
        goodsname_tv.setText(easyQuote.getGoods_name());
        content_tv.setText(easyQuote.getContent());
        byte[] product_imgs1 = easyQuote.getGoods_img1();
        byte[] product_imgs2 = easyQuote.getGoods_img2();
        byte[] product_imgs3 = easyQuote.getGoods_img3();
        byte[] product_imgs4 = easyQuote.getGoods_img4();
        bitmap1 = BitmapUtils.Bytes2Bimap(product_imgs1);
        bitmap2 = BitmapUtils.Bytes2Bimap(product_imgs2);
        bitmap3 = BitmapUtils.Bytes2Bimap(product_imgs3);
        bitmap4 = BitmapUtils.Bytes2Bimap(product_imgs4);
        resId = new Bitmap[]{bitmap1, bitmap2, bitmap3, bitmap4};

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fanhui_rl:
                finish();
                break;
        }
    }


    //--------------------------图片轮播----------------------

    private void initAboutPic() {
        viewPager = (HackyViewPager) findViewById(R.id.iv_img);
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
                    Intent intent = new Intent(DetailEasyQuoteActivity.this, ShowBigPicture.class);
                    intent.putExtra("position", position);
                    intent.putExtra("DataType", DataType.easyquotetype_id);
                    intent.putExtra("id", id);

                    startActivity(intent);
                }
            });
            allListView.add(view);
        }

        viewPager = (HackyViewPager) findViewById(R.id.iv_img);
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
