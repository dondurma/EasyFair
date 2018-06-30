package com.baibeiyun.eazyfair.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baibeiyun.eazyfair.database.MyDataBaseHelper;
import com.baibeiyun.eazyfair.entities.EasyQuote;

import java.util.ArrayList;

/**
 * Created by rw on 2017/4/30.
 */

public class EasyQuoteDao {

    MyDataBaseHelper helper;
    Context context;

    public EasyQuoteDao(Context context) {
        this.context = context;
        helper = new MyDataBaseHelper(context);
    }

    //添加的方法
    public long insertData(EasyQuote easyQuote) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //构建保存数据
        ContentValues values = new ContentValues();
        values.put("goods_img1", easyQuote.getGoods_img1());
        values.put("goods_img2", easyQuote.getGoods_img2());
        values.put("goods_img3", easyQuote.getGoods_img3());
        values.put("goods_img4", easyQuote.getGoods_img4());
        values.put("goods_name", easyQuote.getGoods_name());
        values.put("content", easyQuote.getContent());
        values.put("uniqued_id", easyQuote.getUniqued_id());
        values.put("code", easyQuote.getCode());
        long row = db.insert("easy_quote", "_id", values);
        db.close();
        return row;
    }

    public int deleteById(EasyQuote easyQuote) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int row = db.delete("easy_quote", "_id=?", new String[]{easyQuote.get_id() + ""});
        return row;
    }

    public Cursor SelectAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from easy_quote";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    //修改数据的方法
    public int upData(EasyQuote easyQuote) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id", easyQuote.get_id());
        values.put("goods_img1", easyQuote.getGoods_img1());
        values.put("goods_img2", easyQuote.getGoods_img2());
        values.put("goods_img3", easyQuote.getGoods_img3());
        values.put("goods_img4", easyQuote.getGoods_img4());
        values.put("goods_name", easyQuote.getGoods_name());
        values.put("content", easyQuote.getContent());
        values.put("uniqued_id", easyQuote.getUniqued_id());
        int row = db.update("easy_quote", values, "_id=?", new String[]{easyQuote.get_id() + ""});
        db.close();
        return row;
    }

    //根据id来查询数据
    public Cursor selectById(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from easy_quote where _id=?";
        String[] selectionArgs = {id + ""};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //根据uniqued_id来查询数据
    public Cursor selectByUniqued_id(String i) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from easy_quote where uniqued_id=?";
        String[] selectionArgs = {i};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }


    //分页查询的语句
    public Cursor selectByUnique_id_limit(String uniqued_id, int i) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from easy_quote where uniqued_id=? limit " + i + ",5";
        String[] selectionArgs = {uniqued_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //根据多个code来查询简易报价中的商品
    public Cursor selectIdsByEasyQuote(ArrayList<String> code) {
        SQLiteDatabase db = helper.getWritableDatabase();
        StringBuilder stringBuilder = new StringBuilder();
        String[] selectionArgs = new String[code.size()];
        //创建sql语句
        for (int i = 0; i < code.size(); i++) {
            if (i == 0) {
                stringBuilder.append("code=?");
            } else {
                stringBuilder.append(" or code=?");
            }
            selectionArgs[i] = code.get(i);
        }
        String sql ="select * from easy_quote where " + stringBuilder.toString();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    public int deleteByUniqued_id(String i){
        SQLiteDatabase db = helper.getWritableDatabase();
        int row = db.delete("easy_quote", "uniqued_id=?", new String[]{i});
        return row;
    }


}
