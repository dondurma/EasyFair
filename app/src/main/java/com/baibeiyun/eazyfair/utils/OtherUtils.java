package com.baibeiyun.eazyfair.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * cretae by RuanWei at 2017/3/13
 */

public class OtherUtils {
    //excel中的标题
    public static String GOODSDETAIL = "商品详情单";
    public static String SUPPLIERNAME = "供应商姓名";
    public static String SUPPLIERCOMPANY = "供应商公司名";
    public static String BUYERNAME = "采购商姓名";
    public static String BUYERCOMPANY = "采购商公司名";
    public static String GOODSPIC = "产品图片";
    public static String GOODS_PARAMETER = "产品参数";
    public static String GOODS_NAME = "商品名称";
    public static String GOODSPRICE = "商品价格";
    public static String ART_NO = "商品货号";
    public static String GOODSMATERIAL = "商品材质";
    public static String MOQ = "最少起订量";
    public static String GOODSCOLOR = "商品颜色";
    public static String PRICECLAUSE = "价格条款";
    public static String CURRENCY = "货币类型";
    public static String INTRODUCTION = "商品简介";
    public static String REMARK = "商品备注";
    public static String OUTPAC = "外箱包装";
    public static String NUMBER = "数量";
    public static String SIZE = "尺寸";
    public static String WEIGHT = "重量";
    public static String INNER_PAC = "中盒包装";
    public static String OFFERLIST = "商品报价单";
    public static String INQUIRY_TYPE = "询价类型";
    public static String INQUIRY_TIME = "询价时间";

    public static String INQUIRYLIST = "商品询价单";
    public static String QUOTETYPE = "报价类型";
    public static String QUOTETIME = "报价时间";

    private static long lastClickTime;

    //将bitmap转化成图片保存在本地
    private static final String SD_PATH = "/sdcard/EasyFair/pic/";
    private static final String IN_PATH = "/EasyFair/pic/";


    //让同一个控件要等0.5s才能点击
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
    //让同一个控件要等30s才能点击
    public static boolean isFastDoubleClick2(){
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 30000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }



    //解决ScorllView嵌套ListView只显示一行的问题
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    //将bitmap转换为.png格式的图片
    public static void savePNG_After(Bitmap bitmap, String name) {
        File file = new File(name);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 30, out)) {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //随机产生文件名
    public static String generateFileName() {
        return UUID.randomUUID().toString();
    }

    //将bitmap转换为图片保存在一个固定的路径中
    public static String saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir().getAbsolutePath() + IN_PATH;
        }
        try {
            filePic = new File(savePath + generateFileName() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }

    //将dp转换为像素的方法
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
