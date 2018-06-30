package com.baibeiyun.eazyfair.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baibeiyun.eazyfair.database.MyDataBaseHelper;
import com.baibeiyun.eazyfair.entities.MyCustomer;

/**
 * Created by Administrator on 2016/10/25.
 * 我是供应商 模块  我的客户 表的增删查改的方法
 */
public class MyCustomerDao {
    MyDataBaseHelper helper;
    Context context;

    //构造器
    public MyCustomerDao(Context context) {
        this.context = context;
        helper = new MyDataBaseHelper(context);
    }

    //添加数据的方法
    public long insertdata(MyCustomer myCustomer) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //构建保存数据库
        ContentValues values = new ContentValues();
        values.put("name", myCustomer.getName());
        values.put("company_name", myCustomer.getCompany_name());
        values.put("phone", myCustomer.getPhone());
        values.put("email", myCustomer.getEmail());
        values.put("company_address", myCustomer.getCompany_address());
        values.put("industry_type", myCustomer.getIndustry_type());
        values.put("job_position", myCustomer.getJob_position());
        values.put("company_logo", myCustomer.getCompany_logo());
        values.put("customer_ohter", myCustomer.getCustomer_ohter());
        values.put("create_time", myCustomer.getCreate_time());
        values.put("customer_type", myCustomer.getCustomer_type());
        values.put("department", myCustomer.getDepartment());
        values.put("fax", myCustomer.getFax());
        values.put("pager", myCustomer.getPager());
        values.put("web", myCustomer.getWeb());
        values.put("postcode", myCustomer.getPostcode());
        values.put("icq", myCustomer.getIcq());
        values.put("unique_id", myCustomer.getUnique_id());
        values.put("background_pic", myCustomer.getBackground_pic());
        long rowid = db.insert("customer", "_id", values);
        db.close();
        return rowid;
    }

    //删除数据 根据id来删除
    public int deletdata(MyCustomer myCustomer) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int row = db.delete("customer", "_id=?", new String[]{myCustomer.get_id() + ""});
        return row;
    }



    //查询全部
    public Cursor selectAll() {
        String sql = "select * from customer";
        //获得SQLiteDatabase对象
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        //执行sql语句
        return cursor;
    }

    //0-供应商的客户-采购商
    public Cursor selectForBuyer(){
        String sql="select * from customer where customer_type=0";
        //获得SQLiteDatabase对象
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        //执行sql语句
        return cursor;
    }
    //供应商
    public Cursor selectForSupplier(){
        String sql="select * from customer where customer_type=1";
        //获得SQLiteDatabase对象
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        //执行sql语句
        return cursor;
    }


    //修改数据
    public int updatedata2(MyCustomer myCustomer) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //构造需要修改的数据
        ContentValues values = new ContentValues();
        values.put("_id", myCustomer.get_id());
        values.put("name", myCustomer.getName());
        values.put("company_name", myCustomer.getCompany_name());
        values.put("phone", myCustomer.getPhone());
        values.put("email", myCustomer.getEmail());
        values.put("company_address", myCustomer.getCompany_address());
        values.put("industry_type", myCustomer.getIndustry_type());
        values.put("job_position", myCustomer.getJob_position());
        values.put("company_logo", myCustomer.getCompany_logo());
        values.put("customer_ohter", myCustomer.getCustomer_ohter());
        values.put("fax", myCustomer.getFax());
        values.put("web", myCustomer.getWeb());
        values.put("icq", myCustomer.getIcq());
        values.put("create_time", myCustomer.getCreate_time());
        values.put("customer_type", myCustomer.getCustomer_type());
        values.put("postcode", myCustomer.getPostcode());
        values.put("unique_id", myCustomer.getUnique_id());
        values.put("background_pic", myCustomer.getBackground_pic());
        //修改数据 返回为受影响的行数 如果为0 则表示修改失败
        int row = db.update("customer", values, "_id=?", new String[]{myCustomer.get_id() + ""});
        return row;
    }

    //修改数据
    public int updatedata3(MyCustomer myCustomer) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //构造需要修改的数据
        ContentValues values = new ContentValues();
        values.put("_id", myCustomer.get_id());
        values.put("background_pic", myCustomer.getBackground_pic());
        //修改数据 返回为受影响的行数 如果为0 则表示修改失败
        int row = db.update("customer", values, "_id=?", new String[]{myCustomer.get_id() + ""});
        return row;
    }

    //根据customer_type字段查询的方法
    public Cursor SelectforTypeForSupplier() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from customer where customer_type=?";
        Cursor cursor = db.rawQuery(sql, new String[]{"0"});
        return cursor;
    }

    //根据customer_type字段查询的方法
    public Cursor SelectforTypeForBuyer() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from customer where customer_type=?";
        Cursor cursor = db.rawQuery(sql, new String[]{"1"});
        return cursor;
    }

    //根据id来查询数据
    public Cursor selectforId(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //创建sql语句
        String sql = "select * from customer where _id=?";
        String[] selectionArgs = {id + ""};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //根据id来查询数据且客户类型为采购商
    public Cursor selectforIdforBuyer(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //创建sql语句
        String sql = "select * from customer where _id=? and customer_type=1";
        String[] selectionArgs = {id + ""};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //模糊查询的方法--供应商--以客户名来查询
    public Cursor QueryByLikeforSupplier(String searcherFilter) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("customer", null, "name like ? and customer_type=0", new String[]{"%" + searcherFilter + "%"}, null, null, null);
        return cursor;
    }

    //模糊查询的方法--供应商--以公司名来查询
    public Cursor QueryByLikeforSupplier2(String searcherFilter) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("customer", null, "company_name like ? and customer_type=0", new String[]{"%" + searcherFilter + "%"}, null, null, null);
        return cursor;
    }

    //模糊查询的方法--采购商--以姓名来查询
    public Cursor QueryByLikeforBuyer(String searcherFilter) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("customer", null, "name like ? and customer_type=1", new String[]{"%" + searcherFilter + "%"}, null, null, null);
        return cursor;
    }

    //模糊查询的方法--采购商--以公司名来查询
    public Cursor QueryByLikeforBuyer2(String searcherFilter) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("customer", null, "company_name like ? and customer_type=1", new String[]{"%" + searcherFilter + "%"}, null, null, null);
        return cursor;
    }


}
