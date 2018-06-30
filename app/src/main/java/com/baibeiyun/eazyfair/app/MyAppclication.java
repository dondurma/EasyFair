package com.baibeiyun.eazyfair.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baibeiyun.eazyfair.database.MyDataBaseHelper;
import com.baibeiyun.eazyfair.entities.MyCommodity;

import org.xutils.BuildConfig;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/12/27.
 */
public class MyAppclication extends Application {
    public ArrayList<MyCommodity> li_app = new ArrayList<>();
    private List<String> mlist = new ArrayList<>();

    public List<String> getMlist() {
        return mlist;
    }

    public static RequestQueue requestQueue;
    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        context = getApplicationContext();
        MyDataBaseHelper helper = new MyDataBaseHelper(MyAppclication.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.close();
        // 建立Volley的Http请求队列
        try {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 开放Volley的HTTP请求队列接口
    public static RequestQueue getHttpQueues() {
        return requestQueue;
    }

    public static Context getContext() {
        return context;
    }
}