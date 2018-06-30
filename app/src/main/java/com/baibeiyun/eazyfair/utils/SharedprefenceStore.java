package com.baibeiyun.eazyfair.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.baibeiyun.eazyfair.app.MyAppclication;

/**
 * Created by Administrator on 2016/12/28.
 */
public class SharedprefenceStore {
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";
    public static final String REGISTERTYPE = "registertype";
    public static final String USERTYPE = "usertype";
    public static final String INDUSTRYTYPE = "industryType";

    public static SharedPreferences getSp(){
        return MyAppclication.getContext().getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE);
    }
}
