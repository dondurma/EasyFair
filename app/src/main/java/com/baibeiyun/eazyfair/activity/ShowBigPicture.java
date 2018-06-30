package com.baibeiyun.eazyfair.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.entities.EasyInquiry;
import com.baibeiyun.eazyfair.entities.EasyQuote;
import com.baibeiyun.eazyfair.entities.MyCommodity;
import com.baibeiyun.eazyfair.entities.MyInquiry;
import com.baibeiyun.eazyfair.entities.MyQuote;
import com.baibeiyun.eazyfair.fragment.PictrueFragment;
import com.baibeiyun.eazyfair.utils.BitmapUtils;
import com.baibeiyun.eazyfair.utils.CursorUtils;
import com.baibeiyun.eazyfair.utils.aboutpic.HackyViewPager;
import com.zdp.aseo.content.AseoZdpAseo;

public class ShowBigPicture extends FragmentActivity {
    private HackyViewPager viewPager;

    private Bitmap[] resId;

    //得到上一个界面点击图片的位置
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_picture);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        int dataType = intent.getIntExtra("DataType", 0);
        if (dataType == 1) {
            int id1 = intent.getIntExtra("id", 0);
            MyCommodity myCommodity = CursorUtils.selectById2(ShowBigPicture.this, id1);
            byte[] product_imgs1 = myCommodity.getProduct_imgs1();
            byte[] product_imgs2 = myCommodity.getProduct_imgs2();
            byte[] product_imgs3 = myCommodity.getProduct_imgs3();
            byte[] product_imgs4 = myCommodity.getProduct_imgs4();
            Bitmap bitmap1 = BitmapUtils.Bytes2Bimap(product_imgs1);
            Bitmap bitmap2 = BitmapUtils.Bytes2Bimap(product_imgs2);
            Bitmap bitmap3 = BitmapUtils.Bytes2Bimap(product_imgs3);
            Bitmap bitmap4 = BitmapUtils.Bytes2Bimap(product_imgs4);
            resId = new Bitmap[]{bitmap1, bitmap2, bitmap3, bitmap4};
        } else if (dataType == 2) {
            String id2 = intent.getStringExtra("id");
            MyQuote myQuote = CursorUtils.selectQuoteById2(ShowBigPicture.this, id2);
            byte[] product_imgs1 = myQuote.getProduct_imgs1();
            byte[] product_imgs2 = myQuote.getProduct_imgs2();
            byte[] product_imgs3 = myQuote.getProduct_imgs3();
            byte[] product_imgs4 = myQuote.getProduct_imgs4();
            Bitmap bitmap1 = BitmapUtils.Bytes2Bimap(product_imgs1);
            Bitmap bitmap2 = BitmapUtils.Bytes2Bimap(product_imgs2);
            Bitmap bitmap3 = BitmapUtils.Bytes2Bimap(product_imgs3);
            Bitmap bitmap4 = BitmapUtils.Bytes2Bimap(product_imgs4);
            resId = new Bitmap[]{bitmap1, bitmap2, bitmap3, bitmap4};
        } else if (dataType == 3) {
            String id3 = intent.getStringExtra("id");
            MyInquiry myInquiry = CursorUtils.selectInquiryById2(ShowBigPicture.this, id3);
            byte[] product_imgs1 = myInquiry.getProduct_imgs1();
            byte[] product_imgs2 = myInquiry.getProduct_imgs2();
            byte[] product_imgs3 = myInquiry.getProduct_imgs3();
            byte[] product_imgs4 = myInquiry.getProduct_imgs4();
            Bitmap bitmap1 = BitmapUtils.Bytes2Bimap(product_imgs1);
            Bitmap bitmap2 = BitmapUtils.Bytes2Bimap(product_imgs2);
            Bitmap bitmap3 = BitmapUtils.Bytes2Bimap(product_imgs3);
            Bitmap bitmap4 = BitmapUtils.Bytes2Bimap(product_imgs4);
            resId = new Bitmap[]{bitmap1, bitmap2, bitmap3, bitmap4};
        }else if(dataType==4){
            int id4 = intent.getIntExtra("id", 0);
            EasyQuote easyQuote = CursorUtils.selectEasyQuotePic(ShowBigPicture.this, id4);
            byte[] product_imgs1 = easyQuote.getGoods_img1();
            byte[] product_imgs2 = easyQuote.getGoods_img2();
            byte[] product_imgs3 = easyQuote.getGoods_img3();
            byte[] product_imgs4= easyQuote.getGoods_img4();
            Bitmap bitmap1 = BitmapUtils.Bytes2Bimap(product_imgs1);
            Bitmap bitmap2 = BitmapUtils.Bytes2Bimap(product_imgs2);
            Bitmap bitmap3 = BitmapUtils.Bytes2Bimap(product_imgs3);
            Bitmap bitmap4 = BitmapUtils.Bytes2Bimap(product_imgs4);
            resId = new Bitmap[]{bitmap1, bitmap2, bitmap3, bitmap4};

        }else if(dataType==5){
            int id5 = intent.getIntExtra("id", 0);
            EasyInquiry easyInquiry = CursorUtils.selectEasyInquiryPic(ShowBigPicture.this, id5);
            byte[] product_imgs1 = easyInquiry.getGoods_img1();
            byte[] product_imgs2 = easyInquiry.getGoods_img2();
            byte[] product_imgs3 = easyInquiry.getGoods_img3();
            byte[] product_imgs4= easyInquiry.getGoods_img4();
            Bitmap bitmap1 = BitmapUtils.Bytes2Bimap(product_imgs1);
            Bitmap bitmap2 = BitmapUtils.Bytes2Bimap(product_imgs2);
            Bitmap bitmap3 = BitmapUtils.Bytes2Bimap(product_imgs3);
            Bitmap bitmap4 = BitmapUtils.Bytes2Bimap(product_imgs4);
            resId = new Bitmap[]{bitmap1, bitmap2, bitmap3, bitmap4};

        }
        AseoZdpAseo.initType(this, AseoZdpAseo.INSERT_TYPE);
        initViewPager();
    }

    private void initViewPager() {
        viewPager = (HackyViewPager) findViewById(R.id.viewPager_show_bigPic);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //跳转到第几个界面
        viewPager.setCurrentItem(position);

    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bitmap bitmap = resId[position];
            return new PictrueFragment(bitmap);
        }

        @Override
        public int getCount() {
            return resId.length;
        }


    }
}
