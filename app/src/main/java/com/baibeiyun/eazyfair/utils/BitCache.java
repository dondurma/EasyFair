package com.baibeiyun.eazyfair.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Administrator on 2016/12/30.
 */
public class BitCache implements ImageLoader.ImageCache {
    /*  LruCache图片缓存处理类
    *  特点：当缓存的图片达到了预先设定值时，近期使用次数最少的图片就会被回收
    */
    private LruCache<String, Bitmap> myCache;

    public BitCache() {
        //设置缓存大小，应用内存的1/8作为缓存空间
        myCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024 / 8)) {
            @Override
            //缓存图片大小
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    //从缓存中得到图片
    public Bitmap getBitmap(String s) {
        return myCache.get(s);
    }

    @Override
    //将图片放入缓存
    public void putBitmap(String s, Bitmap bitmap) {
        myCache.put(s, bitmap);
    }
}
