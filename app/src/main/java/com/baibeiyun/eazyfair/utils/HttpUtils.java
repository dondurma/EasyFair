package com.baibeiyun.eazyfair.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baibeiyun.eazyfair.R;
import com.baibeiyun.eazyfair.app.MyAppclication;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/12/28.
 */
public class HttpUtils {
    private static HttpUtils httpUtils;
    public static JsonObjectRequest objectRequest;

    public static HttpUtils getInstance() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }

    private HttpUtils() {
    }
    /**
     * Get请求，获得返回数据
     *
     * @param context 上下文
     * @param url     发送请求的URL
     * @param tag     TAG标签
     * @param vif     请求回调的接口（请求成功或者失败）
     */
    public static void doGet(Context context, String url, String tag, VolleyCallBack vif, JSONObject jsonObject) {
        Log.e("发送Get请求的URL:", url);
        //获取全局的请求队列并把基于Tag标签的请求全部取消，防止重复请求
        MyAppclication.getHttpQueues().cancelAll(tag);
        //实例化StringRequest
        objectRequest = new JsonObjectRequest(Request.Method.GET, url, jsonObject,vif.loadingListener(), vif.errorListener());
        // 设置标签
        objectRequest.setTag(tag);
        // 将请求添加至队列里面
        MyAppclication.getHttpQueues().add(objectRequest);
        // 启动
        MyAppclication.getHttpQueues().start();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param context 上下文
     * @param url     发送请求的URL
     * @param tag     TAG标签
     * @param vif     请求回调的接口（请求成功或者失败）
     */
    public static void doPost(Context context, String url, String tag,JSONObject jsonObject,
                              VolleyCallBack vif) {
        Log.e("发送Get请求的URL:", url);
        //获取全局的请求队列并把基于Tag标签的请求全部取消，防止重复请求
        MyAppclication.getHttpQueues().cancelAll(tag);
        objectRequest = new JsonObjectRequest(Request.Method.POST,url,jsonObject, vif.loadingListener(), vif.errorListener());
        // 设置标签
        objectRequest.setTag(tag);
        // 加入队列
        MyAppclication.getHttpQueues().add(objectRequest);
        // 启动
        MyAppclication.getHttpQueues().start();

    }
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * volloy加载数据库图片
     *
     * @param context
     * @param url
     * @param iv
     */
    public static void imageLoader(Context context, String url, ImageView iv) {
        //ImageLoader的第一个参数就是RequestQueue：即Volley的请求队列
        // ImageLoader的第二个参数是ImageCache：图片缓存，下面会将如何自定义缓存
        RequestQueue queue = Volley.newRequestQueue(context);
        com.android.volley.toolbox.ImageLoader il = new com.android.volley.toolbox.ImageLoader(queue, new BitCache());
        //接下来需要获取一个ImageListener对象
        //getImageListener的三个参数分别为：要加载ImageView对象，图片的默认值（网络图片加载完成前所显示的图片），图片请求出错时的值

        com.android.volley.toolbox.ImageLoader.ImageListener imageListener = com.android.volley.toolbox.ImageLoader.getImageListener(iv, R.drawable.no_pic, R.drawable.no_pic);
        //调用ImageLoader的get()方法来加载图片
        //get()方法第一个参数为图片的url地址，第二个参数是上面获取的ImageListener对象
        //后两个参数用来指定图片允许的最大宽度和高度，可不写
        il.get(url, imageListener, 500, 500);
    }
    /**
     * 用Base64对图片编码
     * @param path
     * @return
     */
    public static String encode(String path) {
        //decode to bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        //convert to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
        byte[] bytes = baos.toByteArray();
        //base64 encode
        byte[] encode = Base64.encode(bytes, Base64.DEFAULT);
        return new String(encode);
    }


}
