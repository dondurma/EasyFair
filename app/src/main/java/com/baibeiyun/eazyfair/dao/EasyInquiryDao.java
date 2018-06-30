package com.baibeiyun.eazyfair.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baibeiyun.eazyfair.database.MyDataBaseHelper;
import com.baibeiyun.eazyfair.entities.EasyInquiry;

import java.util.ArrayList;

/**
 * Created by bby on 2017/5/4.
 */

public class EasyInquiryDao {

    MyDataBaseHelper helper;
    Context context;

    public EasyInquiryDao(Context context) {
        this.context = context;
        helper = new MyDataBaseHelper(context);
    }

    //添加的方法
    public long insertData(EasyInquiry easyInquiry) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("goods_img1", easyInquiry.getGoods_img1());
        values.put("goods_img2", easyInquiry.getGoods_img2());
        values.put("goods_img3", easyInquiry.getGoods_img3());
        values.put("goods_img4", easyInquiry.getGoods_img4());
        values.put("goods_name", easyInquiry.getGoods_name());
        values.put("content", easyInquiry.getContent());
        values.put("uniqued_id", easyInquiry.getUniqued_id());
        values.put("code", easyInquiry.getCode());
        long row = db.insert("easy_inquiry", "_id", values);
        db.close();
        return row;

    }

    //删除的方法
    public int deleteById(EasyInquiry easyInquiry) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int row = db.delete("easy_inquiry", "_id=?", new String[]{easyInquiry.get_id() + ""});
        return row;
    }

    //根据uniqued_id来删除
    public int deleteByUniqued_id(String i){
        SQLiteDatabase db = helper.getWritableDatabase();
        int row = db.delete("easy_inquiry", "uniqued_id=?", new String[]{i});
        return row;
    }


    //修改的方法
    public int upData(EasyInquiry easyInquiry) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id", easyInquiry.get_id());
        values.put("goods_img1", easyInquiry.getGoods_img1());
        values.put("goods_img2", easyInquiry.getGoods_img2());
        values.put("goods_img3", easyInquiry.getGoods_img3());
        values.put("goods_img4", easyInquiry.getGoods_img4());
        values.put("goods_name", easyInquiry.getGoods_name());
        values.put("content", easyInquiry.getContent());
        values.put("uniqued_id", easyInquiry.getUniqued_id());
        int row = db.update("easy_inquiry", values, "_id=?", new String[]{easyInquiry.get_id() + ""});
        db.close();
        return row;
    }


    //根据id来查询数据
    public Cursor selectById(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from easy_inquiry where _id=?";
        String[] selectionArgs = {id + ""};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //根据uniqued_id来查询数据
    public Cursor selectByUniqued_id(String i) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from easy_inquiry where uniqued_id=?";
        String[] selectionArgs = {i};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //分页查询的语句
    public Cursor selectByUnique_id_limit(String uniqued_id, int i) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from easy_inquiry where uniqued_id=? limit " + i + ",5";
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
        String sql = "select * from easy_inquiry where " + stringBuilder.toString();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }


}
