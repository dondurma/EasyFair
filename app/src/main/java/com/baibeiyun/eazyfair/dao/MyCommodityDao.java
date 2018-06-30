package com.baibeiyun.eazyfair.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baibeiyun.eazyfair.database.MyDataBaseHelper;
import com.baibeiyun.eazyfair.entities.MyCommodity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/30.
 * 我是供应商 模块 我的商品 表的增删查改方法
 */
public class MyCommodityDao {
    MyDataBaseHelper helper;//创建一个数据库对象
    Context context;//上下文对象

    //构造器
    public MyCommodityDao(Context context) {
        this.context = context;
        helper = new MyDataBaseHelper(context);
    }

    //添加数据的方法
    public long insertDataforAll(MyCommodity myCommodity) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //构建保存数据
        ContentValues values = new ContentValues();
        values.put("serial_number", myCommodity.getSerial_number());
        values.put("name", myCommodity.getName());
        values.put("unit", myCommodity.getUnit());
        values.put("material", myCommodity.getMaterial());
        values.put("color", myCommodity.getColor());
        values.put("price", myCommodity.getPrice());
        values.put("currency_variety", myCommodity.getCurrency_variety());
        values.put("price_clause", myCommodity.getPrice_clause());
        values.put("price_clause_diy", myCommodity.getPrice_clause_diy());
        values.put("remark", myCommodity.getRemark());
        values.put("introduction", myCommodity.getIntroduction());
        values.put("diy", myCommodity.getDiy());
        values.put("product_imgs1", myCommodity.getProduct_imgs1());
        values.put("product_imgs2", myCommodity.getProduct_imgs2());
        values.put("product_imgs3", myCommodity.getProduct_imgs3());
        values.put("product_imgs4", myCommodity.getProduct_imgs4());
        values.put("goods_type", myCommodity.getGoods_type());
        values.put("create_time", myCommodity.getCreate_time());
        values.put("moq", myCommodity.getMoq());
        values.put("goods_weight", myCommodity.getGoods_weight());
        values.put("goods_weight_unit", myCommodity.getGoods_weight_unit());
        values.put("outbox_number", myCommodity.getOutbox_number());
        values.put("outbox_size", myCommodity.getOutbox_size());
        values.put("outbox_weight", myCommodity.getOutbox_weight());
        values.put("outbox_weight_unit", myCommodity.getOutbox_weight_unit());
        values.put("centerbox_number", myCommodity.getCenterbox_number());
        values.put("centerbox_size", myCommodity.getCenterbox_size());
        values.put("centerbox_weight", myCommodity.getCenterbox_weight());
        values.put("centerbox_weight_unit", myCommodity.getCenterbox_weight_unit());

        long rowid = db.insert("commodity", "_id", values);
        db.close();
        return rowid;
    }

    //添加数据的方法
    public long insertdata(MyCommodity myCommodity) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //构建保存数据
        ContentValues values = new ContentValues();
        values.put("serial_number", myCommodity.getSerial_number());
        values.put("name", myCommodity.getName());
        values.put("unit", myCommodity.getUnit());
        values.put("material", myCommodity.getMaterial());
        values.put("color", myCommodity.getColor());
        values.put("price", myCommodity.getPrice());
        values.put("currency_variety", myCommodity.getCurrency_variety());
        values.put("price_clause", myCommodity.getPrice_clause());
        values.put("price_clause_diy", myCommodity.getPrice_clause_diy());
        values.put("remark", myCommodity.getRemark());
        values.put("introduction", myCommodity.getIntroduction());
        values.put("diy", myCommodity.getDiy());
        values.put("product_imgs1", myCommodity.getProduct_imgs1());
        values.put("product_imgs2", myCommodity.getProduct_imgs2());
        values.put("product_imgs3", myCommodity.getProduct_imgs3());
        values.put("product_imgs4", myCommodity.getProduct_imgs4());
        values.put("goods_type", myCommodity.getGoods_type());
        values.put("create_time", myCommodity.getCreate_time());
        long rowid = db.insert("commodity", "_id", values);
        db.close();
        return rowid;
    }

    //删除数据的方法  根据id来删除
    public int deletedata(MyCommodity myCommodity) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int row = db.delete("commodity", "_id=?", new String[]{myCommodity.get_id() + ""});
        return row;
    }

    //查询全部
    public Cursor selectAll() {
        String sql = "select * from commodity";
        //获得SQLiteDatabase对象
        SQLiteDatabase db = helper.getReadableDatabase();
        //执行sql语句
        return db.rawQuery(sql, null);
    }

    public int updataAll(MyCommodity myCommodity) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //构造需要修改的数据
        ContentValues values = new ContentValues();
        values.put("_id", myCommodity.get_id());
        values.put("serial_number", myCommodity.getSerial_number());
        values.put("name", myCommodity.getName());
        values.put("unit", myCommodity.getUnit());
        values.put("material", myCommodity.getMaterial());
        values.put("color", myCommodity.getColor());
        values.put("price", myCommodity.getPrice());
        values.put("currency_variety", myCommodity.getCurrency_variety());
        values.put("price_clause", myCommodity.getPrice_clause());
        values.put("price_clause_diy", myCommodity.getPrice_clause_diy());
        values.put("remark", myCommodity.getRemark());
        values.put("introduction", myCommodity.getIntroduction());
        values.put("diy", myCommodity.getDiy());
        values.put("product_imgs1", myCommodity.getProduct_imgs1());
        values.put("product_imgs2", myCommodity.getProduct_imgs2());
        values.put("product_imgs3", myCommodity.getProduct_imgs3());
        values.put("product_imgs4", myCommodity.getProduct_imgs4());
        values.put("goods_type", myCommodity.getGoods_type());
        values.put("create_time", myCommodity.getCreate_time());

        values.put("moq", myCommodity.getMoq());
        values.put("goods_weight", myCommodity.getGoods_weight());
        values.put("goods_weight_unit", myCommodity.getGoods_weight_unit());

        values.put("outbox_number", myCommodity.getOutbox_number());
        values.put("outbox_size", myCommodity.getOutbox_size());
        values.put("outbox_weight", myCommodity.getOutbox_weight());
        values.put("outbox_weight_unit", myCommodity.getOutbox_weight_unit());

        values.put("centerbox_number", myCommodity.getCenterbox_number());
        values.put("centerbox_size", myCommodity.getCenterbox_size());
        values.put("centerbox_weight", myCommodity.getCenterbox_weight());
        values.put("centerbox_weight_unit", myCommodity.getCenterbox_weight_unit());

        int row = db.update("commodity", values, "_id=?", new String[]{myCommodity.get_id() + ""});
        //释放资源
        db.close();
        return row;

    }

    //修改数据的方法
    public int updatedata(MyCommodity myCommodity) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //构造需要修改的数据
        ContentValues values = new ContentValues();
        values.put("_id", myCommodity.get_id());
        values.put("serial_number", myCommodity.getSerial_number());
        values.put("name", myCommodity.getName());
        values.put("unit", myCommodity.getUnit());
        values.put("material", myCommodity.getMaterial());
        values.put("color", myCommodity.getColor());
        values.put("price", myCommodity.getPrice());
        values.put("currency_variety", myCommodity.getCurrency_variety());
        values.put("price_clause", myCommodity.getPrice_clause());
        values.put("price_clause_diy", myCommodity.getPrice_clause_diy());
        values.put("remark", myCommodity.getRemark());
        values.put("introduction", myCommodity.getIntroduction());
        values.put("diy", myCommodity.getDiy());
        values.put("product_imgs1", myCommodity.getProduct_imgs1());
        values.put("product_imgs2", myCommodity.getProduct_imgs2());
        values.put("product_imgs3", myCommodity.getProduct_imgs3());
        values.put("product_imgs4", myCommodity.getProduct_imgs4());
        values.put("goods_type", myCommodity.getGoods_type());
        values.put("create_time", myCommodity.getCreate_time());
        //修改数据 返回值为受影响的行数 如果为0 则表示修改失败
        int row = db.update("commodity", values, "_id=?", new String[]{myCommodity.get_id() + ""});
        //释放资源
        db.close();
        return row;
    }

    //模糊查询的方法
    public Cursor QueryByLikeforSupplier(String searcherFilter) {
        //获得SQLiteDatabase对象
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("commodity", null, "name like ? and goods_type=0", new String[]{"%" + searcherFilter + "%"}, null, null, null);
        return cursor;
    }

    //模糊查询的方法
    public Cursor QueryByLike(String searcherFilter) {
        //获得SQLiteDatabase对象
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("commodity", null, "name like ? and goods_type=1", new String[]{"%" + searcherFilter + "%"}, null, null, null);
        return cursor;
    }

    //根据goods_type字段查询的方法-采购商
    public Cursor SelectforType() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from commodity where goods_type=?";
        Cursor cursor = db.rawQuery(sql, new String[]{"1"});
        return cursor;
    }


    //根据goods_type字段查询的方法-供应商
    public Cursor SelectforTypeForSupplier() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from commodity where goods_type=?";
        Cursor cursor = db.rawQuery(sql, new String[]{"0"});
        return cursor;
    }

    //根据id来查询数据
    public Cursor selectforIdbyCommodity(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //创建sql语句
        String sql = "select * from commodity where _id=?";
        String[] selectionArgs = {id + ""};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //根据商品货号来查询的方法
    public Cursor SelectByHuohao(String serial_number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //创建sql语句
        String sql = "select * from commodity where serial_number=?";
        String[] selectionArgs = {serial_number};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }


    //根据多个id来查询数据
    public Cursor selectforIdsbyCommodity(ArrayList<Integer> id) {
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
            selectionArgs[i] = String.valueOf(id.get(i));
        }
        String sql = "select * from commodity where " + stringBuilder.toString();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }


    /**
     * 分页查询
     *
     * @author RuanWei
     * @date 2017/3/13
     **/
    //根据goods_type字段查询的方法-供应商
    public Cursor SelectforTypeForSupplier_limit(int i) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from commodity where goods_type=? limit " + i + ",10";
        Cursor cursor = db.rawQuery(sql, new String[]{"0"});
        return cursor;
    }


    //根据goods_type字段查询的方法-采购商
    public Cursor SelectforType_Limit(int i) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from commodity where goods_type=? limit " + i + ",10";
        Cursor cursor = db.rawQuery(sql, new String[]{"1"});
        return cursor;
    }


}


