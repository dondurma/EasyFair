package com.baibeiyun.eazyfair.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baibeiyun.eazyfair.database.MyDataBaseHelper;
import com.baibeiyun.eazyfair.entities.MyUser;

/**
 * Created by Administrator on 2016/11/21.
 * 账户-个人信息表
 */
public class MyUserDao {
    MyDataBaseHelper helper;//创建一个数据库对象
    Context context;//上下文对象

    public MyUserDao(Context context) {
        this.context = context;
        helper = new MyDataBaseHelper(context);
    }

    //添加数据的方法
    public long insertData(MyUser myUser) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //构建保存数据
        ContentValues values = new ContentValues();
        values.put("user_type", myUser.getUser_type());
        values.put("system_language", myUser.getSystem_language());
        values.put("ch_portrait", myUser.getCh_portrait());
        values.put("ch_company_name", myUser.getCh_company_name());
        values.put("ch_industry_type", myUser.getCh_industry_type());
        values.put("ch_company_size", myUser.getCh_company_size());
        values.put("ch_main_business", myUser.getCh_main_business());
        values.put("ch_main_product", myUser.getCh_main_product());
        values.put("ch_official_website", myUser.getCh_official_website());
        values.put("ch_address", myUser.getCh_address());
        values.put("ch_contact", myUser.getCh_contact());
        values.put("ch_job", myUser.getCh_job());
        values.put("ch_job", myUser.getCh_job());
        values.put("ch_telephone", myUser.getCh_telephone());
        values.put("ch_email", myUser.getCh_email());
        values.put("unique_id", myUser.getUnique_id());

        values.put("en_company_name", myUser.getEn_company_name());
        values.put("en_industry_type", myUser.getEn_industry_type());
        values.put("en_company_size", myUser.getEn_company_size());
        values.put("en_main_bussiness", myUser.getEn_main_bussiness());
        values.put("en_main_product", myUser.getEn_main_product());
        values.put("en_address", myUser.getEn_address());
        values.put("en_contact", myUser.getEn_contact());
        values.put("en_job", myUser.getEn_job());

        long rowid = db.insert("user", "_id", values);
        db.close();
        return rowid;
    }

    //添加数据的方法
    public long insertData2(MyUser myUser) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //构建保存数据
        ContentValues values = new ContentValues();
        values.put("unique_id", myUser.getUnique_id());
        long rowid = db.insert("user", "_id", values);
        db.close();
        return rowid;
    }

    //查询数据的方法
    public Cursor selectAll() {
        String sql = "select * from user";
        //获得SQLiteDatabase对象
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    //修改数据的方法
    public int updataData(MyUser myUser) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //构造需要修改的数据
        ContentValues values = new ContentValues();
        values.put("_id", myUser.get_id());
        values.put("user_type", myUser.getUser_type());
        values.put("system_language", myUser.getSystem_language());

        values.put("ch_portrait", myUser.getCh_portrait());
        values.put("ch_company_name", myUser.getCh_company_name());
        values.put("ch_industry_type", myUser.getCh_industry_type());
        values.put("ch_company_size", myUser.getCh_company_size());
        values.put("ch_main_business", myUser.getCh_main_business());
        values.put("ch_main_product", myUser.getCh_main_product());
        values.put("ch_official_website", myUser.getCh_official_website());
        values.put("ch_address", myUser.getCh_address());
        values.put("ch_contact", myUser.getCh_contact());
        values.put("ch_job", myUser.getCh_job());
        values.put("ch_telephone", myUser.getCh_telephone());
        values.put("ch_email", myUser.getCh_email());

        values.put("unique_id", myUser.getUnique_id());

        values.put("en_company_name", myUser.getEn_company_name());
        values.put("en_industry_type", myUser.getEn_industry_type());
        values.put("en_company_size", myUser.getEn_company_size());
        values.put("en_main_bussiness", myUser.getEn_main_bussiness());
        values.put("en_main_product", myUser.getEn_main_product());
        values.put("en_address", myUser.getEn_address());
        values.put("en_contact", myUser.getEn_contact());
        values.put("en_job", myUser.getEn_job());

        int row = db.update("user", values, "_id=?", new String[]{myUser.get_id() + ""});
        db.close();
        return row;
    }

    //删除数据的方法
    public int deletdata(MyUser myUser) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int row = db.delete("user", "_id=?", new String[]{myUser.get_id() + ""});
        return row;
    }


}
