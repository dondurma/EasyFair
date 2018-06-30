package com.baibeiyun.eazyfair.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baibeiyun.eazyfair.database.MyDataBaseHelper;
import com.baibeiyun.eazyfair.entities.MyQuote;

import java.util.ArrayList;
import java.util.List;

/**
 * 报价记录表
 *
 * @author RuanWei
 * @date 2016/12/20
 **/
public class MyQuoteDao {
    MyDataBaseHelper helper;//创建一个数据库对象
    Context context;//上下文对象

    public MyQuoteDao(Context context) {
        this.context = context;
        helper = new MyDataBaseHelper(context);
    }

    //添加的方法
    public long insertData(MyQuote myQuote) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //构建保存数据
        ContentValues values = new ContentValues();
        values.put("_id", myQuote.get_id());
        values.put("commodity_id", myQuote.getCommodity_id());
        values.put("serial_number", myQuote.getSerial_number());
        values.put("goods_name", myQuote.getGoods_name());
        values.put("goods_unit", myQuote.getGoods_unit());
        values.put("material", myQuote.getMaterial());
        values.put("color", myQuote.getColor());
        values.put("price", myQuote.getPrice());
        values.put("currency_varitety", myQuote.getCurrency_varitety());
        values.put("price_clause", myQuote.getPrice_clause());
        values.put("price_clause_diy", myQuote.getPrice_clause_diy());
        values.put("remark", myQuote.getRemark());
        values.put("introduction", myQuote.getIntroduction());
        values.put("self_defined", myQuote.getSelf_defined());
        values.put("product_imgs1", myQuote.getProduct_imgs1());
        values.put("product_imgs2", myQuote.getProduct_imgs2());
        values.put("product_imgs3", myQuote.getProduct_imgs3());
        values.put("product_imgs4", myQuote.getProduct_imgs4());
        values.put("buyer_name", myQuote.getBuyer_name());
        values.put("buyer_companyname", myQuote.getBuyer_companyname());
        values.put("buyer_phone", myQuote.getBuyer_phone());
        values.put("quote_type", myQuote.getQuote_type());
        values.put("quote_time", myQuote.getQuote_time());
        values.put("uniqued_id", myQuote.getUniqued_id());

        values.put("moq", myQuote.getMoq());
        values.put("goods_weight", myQuote.getGoods_weight());
        values.put("goods_weight_unit", myQuote.getGoods_weight_unit());

        values.put("outbox_number", myQuote.getOutbox_number());
        values.put("outbox_size", myQuote.getOutbox_size());
        values.put("outbox_weight", myQuote.getOutbox_weight());
        values.put("outbox_weight_unit", myQuote.getOutbox_weight_unit());
        values.put("centerbox_number", myQuote.getCenterbox_number());
        values.put("centerbox_size", myQuote.getCenterbox_size());
        values.put("centerbox_weight", myQuote.getCenterbox_weight());
        values.put("centerbox_weight_unit", myQuote.getCenterbox_weight_unit());
        long row = db.insert("quote", "_id", values);
        db.close();
        return row;
    }

    //添加的方法
    public long addData(MyQuote myQuote) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //构建保存数据
        ContentValues values = new ContentValues();
        values.put("_id", myQuote.get_id());
        values.put("commodity_id", myQuote.getCommodity_id());
        values.put("serial_number", myQuote.getSerial_number());
        values.put("goods_name", myQuote.getGoods_name());
        values.put("goods_unit", myQuote.getGoods_unit());
        values.put("material", myQuote.getMaterial());
        values.put("color", myQuote.getColor());
        values.put("price", myQuote.getPrice());
        values.put("currency_varitety", myQuote.getCurrency_varitety());
        values.put("price_clause", myQuote.getPrice_clause());
        values.put("price_clause_diy", myQuote.getPrice_clause_diy());
        values.put("remark", myQuote.getRemark());
        values.put("introduction", myQuote.getIntroduction());
        values.put("self_defined", myQuote.getSelf_defined());
        values.put("goods_weight", myQuote.getGoods_weight());
        values.put("goods_weight_unit", myQuote.getGoods_weight_unit());
        values.put("outbox_number", myQuote.getOutbox_number());
        values.put("outbox_size", myQuote.getOutbox_size());
        values.put("outbox_weight", myQuote.getOutbox_weight());
        values.put("outbox_weight_unit", myQuote.getOutbox_weight_unit());
        values.put("centerbox_number", myQuote.getCenterbox_number());
        values.put("centerbox_size", myQuote.getCenterbox_size());
        values.put("centerbox_weight", myQuote.getCenterbox_weight());
        values.put("centerbox_weight_unit", myQuote.getCenterbox_weight_unit());
        values.put("buyer_companyname", myQuote.getBuyer_companyname());
        values.put("buyer_name", myQuote.getBuyer_name());
        values.put("uniqued_id", myQuote.getUniqued_id());
        values.put("moq", myQuote.getMoq());
        values.put("product_imgs1", myQuote.getProduct_imgs1());
        values.put("product_imgs2", myQuote.getProduct_imgs2());
        values.put("product_imgs3", myQuote.getProduct_imgs3());
        values.put("product_imgs4", myQuote.getProduct_imgs4());
        long row = db.insert("quote", "_id", values);
        db.close();
        return row;
    }

    // 修改数据的方法
    public int updata(MyQuote myQuote) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //构造需要修改的数据
        ContentValues values = new ContentValues();
        values.put("_id", myQuote.get_id());
        values.put("commodity_id", myQuote.getCommodity_id());
        values.put("serial_number", myQuote.getSerial_number());
        values.put("goods_name", myQuote.getGoods_name());
        values.put("goods_unit", myQuote.getGoods_unit());
        values.put("material", myQuote.getMaterial());
        values.put("color", myQuote.getColor());
        values.put("price", myQuote.getPrice());//double
        values.put("currency_varitety", myQuote.getCurrency_varitety());
        values.put("price_clause", myQuote.getPrice_clause());
        values.put("price_clause_diy", myQuote.getPrice_clause_diy());
        values.put("remark", myQuote.getRemark());
        values.put("introduction", myQuote.getIntroduction());
        values.put("self_defined", myQuote.getSelf_defined());
        values.put("product_imgs1", myQuote.getProduct_imgs1());
        values.put("product_imgs2", myQuote.getProduct_imgs2());
        values.put("product_imgs3", myQuote.getProduct_imgs3());
        values.put("product_imgs4", myQuote.getProduct_imgs4());
        values.put("buyer_name", myQuote.getBuyer_name());
        values.put("buyer_companyname", myQuote.getBuyer_companyname());
        values.put("buyer_phone", myQuote.getBuyer_phone());
        values.put("quote_type", myQuote.getQuote_type());
        values.put("quote_time", myQuote.getQuote_time());
        values.put("uniqued_id", myQuote.getUniqued_id());
        values.put("moq", myQuote.getMoq());
        values.put("goods_weight", myQuote.getGoods_weight());
        values.put("goods_weight_unit", myQuote.getGoods_weight_unit());
        values.put("outbox_number", myQuote.getOutbox_number());
        values.put("outbox_size", myQuote.getOutbox_size());
        values.put("outbox_weight", myQuote.getOutbox_weight());
        values.put("outbox_weight_unit", myQuote.getOutbox_weight_unit());
        values.put("centerbox_number", myQuote.getCenterbox_number());
        values.put("centerbox_size", myQuote.getCenterbox_size());
        values.put("centerbox_weight", myQuote.getCenterbox_weight());
        values.put("centerbox_weight_unit", myQuote.getCenterbox_weight_unit());

        int row = db.update("quote", values, "_id=?", new String[]{myQuote.get_id()});
        db.close();
        return row;
    }


    public Cursor selectByUnique_id(String uniqued_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //创建sql语句
        String sql = "select * from quote where uniqued_id=? order by quote_time desc";
        String[] selectionArgs = {uniqued_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }


    //条件查询的方法 预留报价的查询方法
    public Cursor QueryByRequirementforYuLiu(String unique_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from quote where uniqued_id=? and quote_type=2";
        String[] selectionArgs = {unique_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }


    //  条件查询的方法 三个条件 留样报价
    public Cursor QueryByRequirementforLiuYang(String unique_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from quote where uniqued_id=? and quote_type=1";
        String[] selectionArgs = {unique_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //---------------------------------------------------------------------------------------


    public Cursor QueryByRequirementforYiBaoJia(String unique_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from quote where unique_id=? and quote_type=3";
        String[] selectionArgs = {unique_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //--------------------------------------------------------------------------------------------------

    // 根据多个id来查询的方法
    public Cursor SelectForIdByQuote(List<String> id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        StringBuilder stringBuilder = new StringBuilder();
        String[] selectionArgs = new String[id.size()];
        for (int i = 0; i < id.size(); i++) {
            if (i == 0) {
                stringBuilder.append("_id=?");
            } else {
                stringBuilder.append(" or _id=?");
            }
            selectionArgs[i] = id.get(i);
        }
        String sql = "select * from quote where " + stringBuilder.toString();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //根据id来查询数据
    public Cursor selectById(String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //创建sql语句
        String sql = "select * from quote where _id=?";
        String[] selectionArgs = {id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }


    //根据id来删除的方法
    public int deletById(MyQuote myQuote) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int row = db.delete("quote", "_id=?", new String[]{myQuote.get_id()});
        return row;
    }

    //根据公司货号来查询
    public Cursor selectBySerialnumber(String serial_number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //创建sql语句
        String sql = "select * from quote where serial_number=?";
        String[] selectionArgs = {serial_number};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }


//------------------------------------------------------------------------------------------------------------------------------------


    public Cursor SelectAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from quote";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    //根据多个id来查询的方法
    public Cursor selectforIdsByQuote(ArrayList<String> id) {
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
        String sql = "select * from quote where " + stringBuilder.toString();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

//-----------------------------------------------------------------------------------------------------------------------------------


    //模糊查询的方法
    public Cursor QueryByLike(String searcherFilter) {
        //获得SQLiteDatabase对象
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("quote", null, "goods_name like ?", new String[]{"%" + searcherFilter + "%"}, null, null, null);
        return cursor;
    }


    //----------2017-2-22-----------------------------------------------------------

    //查询已报价
    public Cursor QueryforReadly(String unique_id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        //创建sql语句
        String sql = "select * from quote where uniqued_id = ? and quote_type=1";
        String[] selectionArgs = {unique_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }


    //查询留样报价
    public Cursor QueryforLiuYang(String unique_id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        //创建sql语句
        String sql = "select * from quote where uniqued_id = ? and quote_type=3";
        String[] selectionArgs = {unique_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //查询预留报价
    public Cursor QueryforYuLiu(String unique_id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        //创建sql语句
        String sql = "select * from quote where uniqued_id = ? and quote_type=2";
        String[] selectionArgs = {unique_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //-----------------------2017-2-28--------------------------------------------------


    // 条件查询的方法 按时间排序
    public Cursor QueryByTime(String unique_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //创建sql语句
        String sql = "select * from quote where uniqued_id=? and (quote_type=1 or quote_type=2 or quote_type=3) order by quote_time desc ";
        String[] selectionArgs = {unique_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //-----------------------------------2017-3-2-----------------------------------------------------


    //根据商品id来查询所有已报价数据
    public Cursor SelectAllReadlyQuote(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from quote where commodity_id=? and quote_type=1";
        String[] selectionArgs = {id + ""};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }


    //------------------------分页查询的语句---------------2017-3-11---------------------------------------------------------------------------------
    public Cursor selectByUnique_id_limit(String uniqued_id, int i) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //创建sql语句
        String sql = "select * from quote where uniqued_id=? order by quote_time desc limit " + i + ",5";
        String[] selectionArgs = {uniqued_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }


    //查询已报价 分页查询每次查询5条 从第i条开始查询
    public Cursor QueryforReadly_limit(String unique_id, int i) {
        SQLiteDatabase db = helper.getReadableDatabase();
        //创建sql语句
        String sql = "select * from quote where uniqued_id = ? and quote_type=1 limit " + i + ",5";
        String[] selectionArgs = {unique_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }


    //查询预留报价
    public Cursor QueryforYuLiu_limit(String unique_id, int i) {
        SQLiteDatabase db = helper.getReadableDatabase();
        //创建sql语句
        String sql = "select * from quote where uniqued_id = ? and quote_type=2 limit " + i + ",5";
        String[] selectionArgs = {unique_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //查询留样报价
    public Cursor QueryforLiuYang_limit(String unique_id, int i) {
        SQLiteDatabase db = helper.getReadableDatabase();
        //创建sql语句
        String sql = "select * from quote where uniqued_id = ? and quote_type=3 limit " + i + ",5";
        String[] selectionArgs = {unique_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    /**
     * 根据 uniqued_id分页查询三种报价类型 并且以时间排序
     * String sql = "select * from quote where uniqued_id=? order by quote_time desc limit " + i + ",5";
     *
     * @author RuanWei
     * @date 2017/3/18
     **/
    //查询已报价的数据
    public Cursor QueryYiDesc(String unique_id, int i) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from quote where uniqued_id=? and quote_type=1 order by quote_time desc limit " + i + ",10";
        String[] selectionArgs = {unique_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //查询预留报价的数据
    public Cursor QueryYuLiuDesc(String unique_id, int i) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from quote where uniqued_id=? and quote_type=2 order by quote_time desc limit " + i + ",10";
        String[] selectionArgs = {unique_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    //查询留样报价的数据
    public Cursor QueryLiuYangDesc(String unique_id, int i) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from quote where uniqued_id=? and quote_type=3 order by quote_time desc limit " + i + ",10";
        String[] selectionArgs = {unique_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    public int deletdata2(String i) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int row = db.delete("quote", "uniqued_id=?", new String[]{i});
        return row;
    }


}
