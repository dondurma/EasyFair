package com.baibeiyun.eazyfair.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/12/28.
 */
public abstract class VolleyCallBack {
    /**
     * 上下文
     */
    public Context mContext;
    /**
     * 请求成功监听
     */
    public static Response.Listener<JSONObject> mListener;
    /**
     * 请求失败监听
     */
    public static Response.ErrorListener mErrorListtener;

    public VolleyCallBack(Context context, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        this.mContext = context;
        mListener = listener;
        mErrorListtener = errorListener;
    }

    /**
     * 请求成功的抽象类
     *
     * @param result
     */
    public abstract void onSuccess(JSONObject result);

    /**
     * 请求失败的抽象类
     *
     * @param error
     */
    public abstract void onError(VolleyError error);

    /**
     * 请求成功监听
     *
     * @return
     */
    public Response.Listener<JSONObject> loadingListener() {
        mListener = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject result) {
//                Log.e("请求成功返回的数据：", result.toString());
                onSuccess(result);
            }
        };
        return mListener;
    }

    /**
     * 请求失败监听
     *
     * @return
     */
    public Response.ErrorListener errorListener() {
        mErrorListtener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("请求失败返回的数据：", error.toString());
                onError(error);
            }
        };
        return mErrorListtener;
    }
}
