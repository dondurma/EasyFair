package com.baibeiyun.eazyfair.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.baibeiyun.eazyfair.database.MyDataBaseHelper;
import com.baibeiyun.eazyfair.entities.MyGoodsTemplet;

/**
 * cretae by RuanWei at 2017/3/8
 */

public class MyGoodsTempletDao {
    MyDataBaseHelper helper;
    Context context;

    public MyGoodsTempletDao(Context context) {
        this.context = context;
        helper = new MyDataBaseHelper(context);
    }

    //添加的方法
    public long insertData(MyGoodsTemplet myGoodsTemplet) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //构建保存数据
        ContentValues values = new ContentValues();
        values.put("_id", myGoodsTemplet.get_id());
        values.put("serial_number", myGoodsTemplet.getSerial_number());
        values.put("goods_name", myGoodsTemplet.getGoods_name());
        values.put("goods_price", myGoodsTemplet.getGoods_price());
        values.put("goods_material", myGoodsTemplet.getGoods_material());
        values.put("goods_color", myGoodsTemplet.getGoods_color());
        values.put("moq", myGoodsTemplet.getMoq());
        values.put("goods_weight", myGoodsTemplet.getGoods_weight());
        values.put("goods_weight_unit", myGoodsTemplet.getGoods_weight_unit());
        values.put("outbox_number", myGoodsTemplet.getOutbox_number());
        values.put("outbox_size", myGoodsTemplet.getOutbox_size());
        values.put("outbox_weight", myGoodsTemplet.getOutbox_weight());
        values.put("outbox_weight_unit", myGoodsTemplet.getOutbox_weight_unit());
        values.put("center_box_number", myGoodsTemplet.getCenter_box_number());
        values.put("center_box_size", myGoodsTemplet.getCenter_box_size());
        values.put("center_box_weight", myGoodsTemplet.getCenter_box_weight());
        values.put("center_box_weight_unit", myGoodsTemplet.getCenter_box_weight_unit());
        values.put("goods_unit", myGoodsTemplet.getGoods_unit());
        values.put("currency_type", myGoodsTemplet.getCurrency_type());
        values.put("price_clause", myGoodsTemplet.getPrice_clause());
        values.put("price_clause_diy", myGoodsTemplet.getPrice_clause_diy());
        values.put("input_time", myGoodsTemplet.getInput_time());
        long row = db.insert("goods_templet", "_id", values);
        db.close();
        return row;
    }


    //修改的方法
    public int updata(MyGoodsTemplet myGoodsTemplet) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id", myGoodsTemplet.get_id());
        values.put("serial_number", myGoodsTemplet.getSerial_number());
        values.put("goods_name", myGoodsTemplet.getGoods_name());
        values.put("goods_price", myGoodsTemplet.getGoods_price());
        values.put("goods_material", myGoodsTemplet.getGoods_material());
        values.put("goods_color", myGoodsTemplet.getGoods_color());
        values.put("moq", myGoodsTemplet.getMoq());
        values.put("goods_weight", myGoodsTemplet.getGoods_weight());
        values.put("goods_weight_unit", myGoodsTemplet.getGoods_weight_unit());
        values.put("outbox_number", myGoodsTemplet.getOutbox_number());
        values.put("outbox_size", myGoodsTemplet.getOutbox_size());
        values.put("outbox_weight", myGoodsTemplet.getOutbox_weight());
        values.put("outbox_weight_unit", myGoodsTemplet.getOutbox_weight_unit());
        values.put("center_box_number", myGoodsTemplet.getCenter_box_number());
        values.put("center_box_size", myGoodsTemplet.getCenter_box_size());
        values.put("center_box_weight", myGoodsTemplet.getCenter_box_weight());
        values.put("center_box_weight_unit", myGoodsTemplet.getCenter_box_weight_unit());
        values.put("goods_unit", myGoodsTemplet.getGoods_unit());
        values.put("currency_type", myGoodsTemplet.getCurrency_type());
        values.put("price_clause", myGoodsTemplet.getPrice_clause());
        values.put("price_clause_diy", myGoodsTemplet.getPrice_clause_diy());
        values.put("input_time", myGoodsTemplet.getInput_time());
        int row = db.update("goods_templet", values, "_id=?", new String[]{myGoodsTemplet.get_id() + ""});
        db.close();
        return row;
    }


}
