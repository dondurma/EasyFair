package com.baibeiyun.eazyfair.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baibeiyun.eazyfair.database.MyDataBaseHelper;
import com.baibeiyun.eazyfair.entities.MyInquiry;

import java.util.ArrayList;

/**
 * 询价记录表
 *
 * @author RuanWei
 * @date 2016/12/10
 **/
public class MyInquiryDao {
    MyDataBaseHelper helper;
    Context context;

    public MyInquiryDao(Context context) {
        this.context = context;
        helper = new MyDataBaseHelper(context);
    }

    //添加的方法
    public long insertData(MyInquiry myInquiry) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //构建保存数据
        ContentValues values = new ContentValues();
        values.put("_id", myInquiry.get_id());
        values.put("commodity_id", myInquiry.getCommodity_id());
        values.put("serial_number", myInquiry.getSerial_number());
        values.put("goods_name", myInquiry.getGoods_name());
        values.put("goods_unit", myInquiry.getGoods_unit());
        values.put("material", myInquiry.getMaterial());
        values.put("color", myInquiry.getColor());
        values.put("price", myInquiry.getPrice());
        values.put("currency_varitety", myInquiry.getCurrency_varitety());
        values.put("price_clause", myInquiry.getPrice_clause());
        values.put("price_clause_diy", myInquiry.getPrice_clause_diy());
        values.put("remark", myInquiry.getRemark());
        values.put("introduction", myInquiry.getIntroduction());
        values.put("self_defined", myInquiry.getSelf_defined());
        values.put("product_imgs1", myInquiry.getProduct_imgs1());
        values.put("product_imgs2", myInquiry.getProduct_imgs2());
        values.put("product_imgs3", myInquiry.getProduct_imgs3());
        values.put("product_imgs4", myInquiry.getProduct_imgs4());
        values.put("supplier_name", myInquiry.getSupplier_name());
        values.put("supplier_companyname", myInquiry.getSupplier_companyname());
        values.put("supplier_phone", myInquiry.getSupplier_phone());
        values.put("inquiry_type", myInquiry.getInquiry_type());
        values.put("inquiry_time", myInquiry.getInquiry_time());
        values.put("uniqued_id", myInquiry.getUniqued_id());

        values.put("moq", myInquiry.getMoq());
        values.put("goods_weight", myInquiry.getGoods_weight());
        values.put("goods_weight_unit", myInquiry.getGoods_weight_unit());

        values.put("outbox_number", myInquiry.getOutbox_number());
        values.put("outbox_size", myInquiry.getOutbox_size());
        values.put("outbox_weight", myInquiry.getOutbox_weight());
        values.put("outbox_weight_unit", myInquiry.getOutbox_weight_unit());
        values.put("centerbox_number", myInquiry.getCenterbox_number());
        values.put("centerbox_size", myInquiry.getCenterbox_size());
        values.put("centerbox_weight", myInquiry.getCenterbox_weight());
        values.put("centerbox_weight_unit", myInquiry.getCenterbox_weight_unit());

        long row = db.insert("inquiry", "_id", values);
        db.close();
        return row;
    }

    //添加的方法
    public long addData(MyInquiry myInquiry) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //构建保存数据
        ContentValues values = new ContentValues();
        values.put("_id", myInquiry.get_id());
        values.put("commodity_id", myInquiry.getCommodity_id());
        values.put("serial_number", myInquiry.getSerial_number());
        values.put("goods_name", myInquiry.getGoods_name());
        values.put("goods_unit", myInquiry.getGoods_unit());
        values.put("material", myInquiry.getMaterial());
        values.put("color", myInquiry.getColor());
        values.put("price", myInquiry.getPrice());
        values.put("currency_varitety", myInquiry.getCurrency_varitety());
        values.put("price_clause", myInquiry.getPrice_clause());
        values.put("price_clause_diy", myInquiry.getPrice_clause_diy());
        values.put("remark", myInquiry.getRemark());
        values.put("introduction", myInquiry.getIntroduction());
        values.put("self_defined", myInquiry.getSelf_defined());
        values.put("goods_weight", myInquiry.getGoods_weight());
        values.put("goods_weight_unit", myInquiry.getGoods_weight_unit());
        values.put("outbox_number", myInquiry.getOutbox_number());
        values.put("outbox_size", myInquiry.getOutbox_size());
        values.put("outbox_weight", myInquiry.getOutbox_weight());
        values.put("outbox_weight_unit", myInquiry.getOutbox_weight_unit());
        values.put("centerbox_number", myInquiry.getCenterbox_number());
        values.put("centerbox_size", myInquiry.getCenterbox_size());
        values.put("centerbox_weight", myInquiry.getCenterbox_weight());
        values.put("centerbox_weight_unit", myInquiry.getCenterbox_weight_unit());
        values.put("supplier_companyname", myInquiry.getSupplier_companyname());
        values.put("supplier_name", myInquiry.getSupplier_name());
        values.put("uniqued_id", myInquiry.getUniqued_id());
        values.put("moq", myInquiry.getMoq());
        values.put("product_imgs1", myInquiry.getProduct_imgs1());
        values.put("product_imgs2", myInquiry.getProduct_imgs2());
        values.put("product_imgs3", myInquiry.getProduct_imgs3());
        values.put("product_imgs4", myInquiry.getProduct_imgs4());
        long row = db.insert("inquiry", "_id", values);
        db.close();
        return row;
    }

    //修改的方法
    public int updata(MyInquiry myInquiry) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id", myInquiry.get_id());
        values.put("commodity_id", myInquiry.getCommodity_id());
        values.put("serial_number", myInquiry.getSerial_number());
        values.put("goods_name", myInquiry.getGoods_name());
        values.put("goods_unit", myInquiry.getGoods_unit());
        values.put("material", myInquiry.getMaterial());
        values.put("color", myInquiry.getColor());
        values.put("price", myInquiry.getPrice());
        values.put("currency_varitety", myInquiry.getCurrency_varitety());
        values.put("price_clause", myInquiry.getPrice_clause());
        values.put("price_clause_diy", myInquiry.getPrice_clause_diy());
        values.put("remark", myInquiry.getRemark());
        values.put("introduction", myInquiry.getIntroduction());
        values.put("self_defined", myInquiry.getSelf_defined());
        values.put("product_imgs1", myInquiry.getProduct_imgs1());
        values.put("product_imgs2", myInquiry.getProduct_imgs2());
        values.put("product_imgs3", myInquiry.getProduct_imgs3());
        values.put("product_imgs4", myInquiry.getProduct_imgs4());
        values.put("supplier_name", myInquiry.getSupplier_name());
        values.put("supplier_companyname", myInquiry.getSupplier_companyname());
        values.put("supplier_phone", myInquiry.getSupplier_phone());
        values.put("inquiry_type", myInquiry.getInquiry_type());
        values.put("inquiry_time", myInquiry.getInquiry_time());
        values.put("uniqued_id", myInquiry.getUniqued_id());

        values.put("moq", myInquiry.getMoq());
        values.put("goods_weight", myInquiry.getGoods_weight());
        values.put("goods_weight_unit", myInquiry.getGoods_weight_unit());

        values.put("outbox_number", myInquiry.getOutbox_number());
        values.put("outbox_size", myInquiry.getOutbox_size());
        values.put("outbox_weight", myInquiry.getOutbox_weight());
        values.put("outbox_weight_unit", myInquiry.getOutbox_weight_unit());
        values.put("centerbox_number", myInquiry.getCenterbox_number());
        values.put("centerbox_size", myInquiry.getCenterbox_size());
        values.put("centerbox_weight", myInquiry.getCenterbox_weight());
        values.put("centerbox_weight_unit", myInquiry.getCenterbox_weight_unit());

        int row = db.update("inquiry", values, "_id=?", new String[]{myInquiry.get_id()});
        db.close();
        return row;
    }


    //根据id来查询的方法
    public Cursor selectById(String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from inquiry where _id=?";
        String[] selectionArgs = {id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //根据多个id来查询的方法
    public Cursor selectforIdsByInquiry(ArrayList<String> id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        StringBuilder stringBuilder = new StringBuilder();
        String[] selectionArgs = new String[id.size()];
        //创建sql语句
        for (int i = 0; i < id.size(); i++) {
            if (i == 0) {
                stringBuilder.append("_id=?");
            } else {
                stringBuilder.append(" or _id=?");
            }
            selectionArgs[i] = id.get(i);
        }
        String sql = "select * from inquiry where " + stringBuilder.toString();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //根据id来删除
    public int deleteById(MyInquiry myInquiry) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int row = db.delete("inquiry", "_id=?", new String[]{myInquiry.get_id()});
        return row;
    }


    //------------------------------2017-2-25---------3-3更新-------------------------------------
    //查询全部类型
    public Cursor queryAll(String uniqued_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from inquiry where uniqued_id=?";
        String[] selectionArgs = {uniqued_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //已询价的查询方法
    public Cursor queryYiXunJia(String uniqued_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from inquiry where uniqued_id=? and inquiry_type=1";
        String[] selectionArgs = {uniqued_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //预留询价的查询方法
    public Cursor queryYuLiuXunJia(String uniqued_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from inquiry where uniqued_id=? and inquiry_type=2";
        String[] selectionArgs = {uniqued_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //留样询价的查询方法
    public Cursor queryLiuYangXunJia(String uniqued_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from inquiry where uniqued_id=? and inquiry_type=3";
        String[] selectionArgs = {uniqued_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //模糊查询的方法
    public Cursor queryByLike(String searcherFilter) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("inquiry", null, "goods_name like ?", new String[]{"%" + searcherFilter + "%"}, null, null, null);
        return cursor;
    }

    //------------------------------------------2017-2-28------------------------------------------------------------


    //预留询价的查询方法
    public Cursor QueryByReqForYuLiuXunJia(String unique_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from inquiry where uniqued_id=? and inquiry_type=2";
        String[] selectionArgs = {unique_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }


    //留样询价的查询方法
    public Cursor QueryByReqForLiuYangXunJia(String unique_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from inquiry where uniqued_id=? and inquiry_type=3";
        String[] selectionArgs = {unique_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }


    //最新询价记录
    public Cursor QueryByTime(String unique_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from inquiry where uniqued_id=? and (inquiry_type=1 or inquiry_type=2 or inquiry_type=3) order by inquiry_time desc";
        String[] selectionArgs = {unique_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    /**
     * 分页查询
     *
     * @author RuanWei
     * @date 2017/3/13
     **/
    //查询全部类型
    public Cursor queryAll_Limit(String uniqued_id, int i) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from inquiry where uniqued_id=? limit " + i + ",5";
        String[] selectionArgs = {uniqued_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //已询价的查询方法
    public Cursor queryYiXunJia_Limit(String uniqued_id, int i) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from inquiry where uniqued_id=? and inquiry_type=1 limit " + i + ",5";
        String[] selectionArgs = {uniqued_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }


    //预留询价的查询方法
    public Cursor queryYuLiuXunJia_Limit(String uniqued_id, int i) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from inquiry where uniqued_id=? and inquiry_type=2 limit " + i + ",5";
        String[] selectionArgs = {uniqued_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor ;
    }

    //留样询价的查询方法
    public Cursor queryLiuYangXunJia_Limit(String uniqued_id, int i) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from inquiry where uniqued_id=? and inquiry_type=3 limit " + i + ",5";
        String[] selectionArgs = {uniqued_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }


    /**
     * 根据uniqued_id分页查询三种询价类型 并且一时间排序
     *
     * @author RuanWei
     * @date 2017/3/20
     **/

    //查询已询价的数据
    public Cursor SelectYiDesc(String uniqued_id, int i) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from inquiry where uniqued_id= ? and inquiry_type = 1 order by inquiry_time desc limit " + i + ",10";
        String[] selectionArgs = {uniqued_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //查询预留询价的数据
    public Cursor SelectYuLiuDesc(String uniqued_id, int i) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from inquiry where uniqued_id= ? and inquiry_type = 2 order by inquiry_time desc limit " + i + ",10";
        String[] selectionArgs = {uniqued_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //查询留样询价的数据
    public Cursor SelectLiuYangDesc(String uniqued_id, int i) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from inquiry where  uniqued_id= ? and inquiry_type = 3 order by inquiry_time desc limit " + i + ",10";
        String[] selectionArgs = {uniqued_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    public int deletdata2(String i) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int row = db.delete("inquiry", "uniqued_id=?", new String[]{i});
        return row;
    }

    //根据商品id来查询已询价的数据
    public Cursor SelectAllReadlyInquiry(int id){
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from inquiry where commodity_id=? and inquiry_type=1";
        String[] selectionArgs = {id + ""};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

}
