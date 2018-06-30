package com.baibeiyun.eazyfair.utils;

import android.content.Context;
import android.database.Cursor;

import com.baibeiyun.eazyfair.dao.EasyInquiryDao;
import com.baibeiyun.eazyfair.dao.EasyQuoteDao;
import com.baibeiyun.eazyfair.dao.LanguageDao;
import com.baibeiyun.eazyfair.dao.MyCommodityDao;
import com.baibeiyun.eazyfair.dao.MyCustomerDao;
import com.baibeiyun.eazyfair.dao.MyInquiryDao;
import com.baibeiyun.eazyfair.dao.MyQuoteDao;
import com.baibeiyun.eazyfair.dao.MyUserDao;
import com.baibeiyun.eazyfair.entities.EasyInquiry;
import com.baibeiyun.eazyfair.entities.EasyQuote;
import com.baibeiyun.eazyfair.entities.Language;
import com.baibeiyun.eazyfair.entities.MyCommodity;
import com.baibeiyun.eazyfair.entities.MyCustomer;
import com.baibeiyun.eazyfair.entities.MyInquiry;
import com.baibeiyun.eazyfair.entities.MyQuote;
import com.baibeiyun.eazyfair.entities.MyUser;

import java.util.ArrayList;

/**
 * cretae by RuanWei at 2017/3/15
 */

public class CursorUtils {
    public static ArrayList<MyCommodity> myCommodities = new ArrayList<>();
    public static MyCommodity myCommodity;

    public static ArrayList<MyQuote> myQuotes = new ArrayList<>();
    public static MyQuote myQuote;

    public static ArrayList<MyInquiry> myInquiries = new ArrayList<>();
    public static MyInquiry myInquiry;

    public static ArrayList<MyCustomer> myCustomers = new ArrayList<>();
    public static MyCustomer myCustomer;

    public static ArrayList<MyUser> myUsers = new ArrayList<>();
    public static MyUser myUser;


    public static ArrayList<Language> listforlanguage = new ArrayList<>();
    public static Language language;

    public static ArrayList<EasyQuote> easyQuotes = new ArrayList<>();
    public static EasyQuote easyQuote;

    public static ArrayList<EasyInquiry> easyInquiries = new ArrayList<>();
    public static EasyInquiry easyInquiry;


    //MyCommodity   商品表

    //我是供应商-我的商品表-查询所有字段-返回对象
    public static MyCommodity selectById(Context context, int i) {
        myCommodity = null;
        myCommodities.clear();
        MyCommodityDao myCommodityDao = new MyCommodityDao(context);
        Cursor cursor = myCommodityDao.selectforIdbyCommodity(i);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String serial_number = cursor.getString(cursor.getColumnIndex("serial_number"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String unit = cursor.getString(cursor.getColumnIndex("unit"));
            String material = cursor.getString(cursor.getColumnIndex("material"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            Double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_variety = cursor.getString(cursor.getColumnIndex("currency_variety"));
            String price_clause = cursor.getString(cursor.getColumnIndex("price_clause"));
            String price_clause_diy = cursor.getString(cursor.getColumnIndex("price_clause_diy"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
            String diy = cursor.getString(cursor.getColumnIndex("diy"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            byte[] product_imgs2 = cursor.getBlob(cursor.getColumnIndex("product_imgs2"));
            byte[] product_imgs3 = cursor.getBlob(cursor.getColumnIndex("product_imgs3"));
            byte[] product_imgs4 = cursor.getBlob(cursor.getColumnIndex("product_imgs4"));
            int goods_type = cursor.getInt(cursor.getColumnIndex("goods_type"));
            String create_time = cursor.getString(cursor.getColumnIndex("create_time"));
            String moq = cursor.getString(cursor.getColumnIndex("moq"));
            String goods_weight = cursor.getString(cursor.getColumnIndex("goods_weight"));
            String goods_weight_unit = cursor.getString(cursor.getColumnIndex("goods_weight_unit"));
            String outbox_number = cursor.getString(cursor.getColumnIndex("outbox_number"));
            String outbox_size = cursor.getString(cursor.getColumnIndex("outbox_size"));
            String outbox_weight = cursor.getString(cursor.getColumnIndex("outbox_weight"));
            String outbox_weight_unit = cursor.getString(cursor.getColumnIndex("outbox_weight_unit"));
            String centerbox_number = cursor.getString(cursor.getColumnIndex("centerbox_number"));
            String centerbox_size = cursor.getString(cursor.getColumnIndex("centerbox_size"));
            String centerbox_weight = cursor.getString(cursor.getColumnIndex("centerbox_weight"));
            String centerbox_weight_unit = cursor.getString(cursor.getColumnIndex("centerbox_weight_unit"));
            myCommodity = new MyCommodity(id, serial_number, name, unit, material, color, price, currency_variety, price_clause, price_clause_diy, remark,
                    introduction, diy, product_imgs1, product_imgs2, product_imgs3, product_imgs4, goods_type, create_time, moq, goods_weight,
                    goods_weight_unit, outbox_number, outbox_size, outbox_weight, outbox_weight_unit, centerbox_number, centerbox_size,
                    centerbox_weight, centerbox_weight_unit);
            myCommodities.add(myCommodity);
        }
        cursor.close();
        return myCommodity;

    }

    //我是供应商-我的商品表-只查询图片-返回对象
    public static MyCommodity selectById2(Context context, int i) {
        myCommodity = null;
        myCommodities.clear();
        MyCommodityDao myCommodityDao = new MyCommodityDao(context);
        Cursor cursor = myCommodityDao.selectforIdbyCommodity(i);
        while (cursor.moveToNext()) {
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            byte[] product_imgs2 = cursor.getBlob(cursor.getColumnIndex("product_imgs2"));
            byte[] product_imgs3 = cursor.getBlob(cursor.getColumnIndex("product_imgs3"));
            byte[] product_imgs4 = cursor.getBlob(cursor.getColumnIndex("product_imgs4"));
            myCommodity = new MyCommodity(product_imgs1, product_imgs2, product_imgs3, product_imgs4);
            myCommodities.add(myCommodity);
        }
        cursor.close();
        return myCommodity;

    }


    //我是供应商-根据多个id来查询商品数据
    public static ArrayList<MyCommodity> selectCommodityIds(Context context, ArrayList<Integer> integers) {
        myCommodity = null;
        myCommodities.clear();
        MyCommodityDao myCommodityDao = new MyCommodityDao(context);
        Cursor cursor = myCommodityDao.selectforIdsbyCommodity(integers);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String serial_number = cursor.getString(cursor.getColumnIndex("serial_number"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String unit = cursor.getString(cursor.getColumnIndex("unit"));
            String material = cursor.getString(cursor.getColumnIndex("material"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            Double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_variety = cursor.getString(cursor.getColumnIndex("currency_variety"));
            String price_clause = cursor.getString(cursor.getColumnIndex("price_clause"));
            String price_clause_diy = cursor.getString(cursor.getColumnIndex("price_clause_diy"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
            String diy = cursor.getString(cursor.getColumnIndex("diy"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            byte[] product_imgs2 = cursor.getBlob(cursor.getColumnIndex("product_imgs2"));
            byte[] product_imgs3 = cursor.getBlob(cursor.getColumnIndex("product_imgs3"));
            byte[] product_imgs4 = cursor.getBlob(cursor.getColumnIndex("product_imgs4"));
            int goods_type = cursor.getInt(cursor.getColumnIndex("goods_type"));
            String create_time = cursor.getString(cursor.getColumnIndex("create_time"));
            String moq = cursor.getString(cursor.getColumnIndex("moq"));
            String goods_weight = cursor.getString(cursor.getColumnIndex("goods_weight"));
            String goods_weight_unit = cursor.getString(cursor.getColumnIndex("goods_weight_unit"));

            String outbox_number = cursor.getString(cursor.getColumnIndex("outbox_number"));
            String outbox_size = cursor.getString(cursor.getColumnIndex("outbox_size"));
            String outbox_weight = cursor.getString(cursor.getColumnIndex("outbox_weight"));
            String outbox_weight_unit = cursor.getString(cursor.getColumnIndex("outbox_weight_unit"));
            String centerbox_number = cursor.getString(cursor.getColumnIndex("centerbox_number"));
            String centerbox_size = cursor.getString(cursor.getColumnIndex("centerbox_size"));
            String centerbox_weight = cursor.getString(cursor.getColumnIndex("centerbox_weight"));
            String centerbox_weight_unit = cursor.getString(cursor.getColumnIndex("centerbox_weight_unit"));

            myCommodity = new MyCommodity(id, serial_number, name, unit, material, color, price, currency_variety,
                    price_clause, price_clause_diy, remark, introduction, diy, product_imgs1, product_imgs2, product_imgs3,
                    product_imgs4, goods_type, create_time, moq, goods_weight, goods_weight_unit, outbox_number,
                    outbox_size, outbox_weight, outbox_weight_unit, centerbox_number, centerbox_size, centerbox_weight,
                    centerbox_weight_unit);

            myCommodities.add(myCommodity);
        }
        cursor.close();
        return myCommodities;
    }

    //我是供应商-根据goods_type字段来查询-供应商商品
    public static ArrayList<MyCommodity> selectTypeforSup(Context context, int i) {
        myCommodity = null;
        myCommodities.clear();
        MyCommodityDao myCommodityDao = new MyCommodityDao(context);
        Cursor cursor = myCommodityDao.SelectforTypeForSupplier_limit(i);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_variety = cursor.getString(cursor.getColumnIndex("currency_variety"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            myCommodity = new MyCommodity(id, name, price, currency_variety, product_imgs1);
            myCommodities.add(myCommodity);
        }
        cursor.close();
        return myCommodities;

    }

    //我是供应商-供应商商品的模糊查询的方法
    public static ArrayList<MyCommodity> selectMoHuBySup(Context context, String data) {
        myCommodity = null;
        MyCommodityDao myCommodityDao = new MyCommodityDao(context);
        Cursor cursor = myCommodityDao.QueryByLikeforSupplier(data);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_variety = cursor.getString(cursor.getColumnIndex("currency_variety"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            myCommodity = new MyCommodity(id, name, price, currency_variety, product_imgs1);
            myCommodities.add(myCommodity);
        }
        cursor.close();
        return myCommodities;
    }

    //我是供应商-计算供应商商品的数量
    public static ArrayList<MyCommodity> selectNumbersBySup(Context context) {
        myCommodity = null;
        myCommodities.clear();
        MyCommodityDao myCommodityDao = new MyCommodityDao(context);
        Cursor cursor = myCommodityDao.SelectforTypeForSupplier();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            myCommodity = new MyCommodity(id);
            myCommodities.add(myCommodity);
        }
        cursor.close();
        return myCommodities;

    }


    //我是采购商-我的商品表-根据goods_type字段来查询-采购商商品
    public static ArrayList<MyCommodity> selectTypeforBuyer(Context context, int i) {
        myCommodity = null;
        myCommodities.clear();
        MyCommodityDao myCommodityDao = new MyCommodityDao(context);
        Cursor cursor = myCommodityDao.SelectforType_Limit(i);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_variety = cursor.getString(cursor.getColumnIndex("currency_variety"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));

            myCommodity = new MyCommodity(id, name, price, currency_variety, product_imgs1);
            myCommodities.add(myCommodity);
        }
        cursor.close();
        return myCommodities;

    }

    //我是采购商-模糊查询的方法
    public static ArrayList<MyCommodity> findDataByBuyer(Context context, String data) {
        myCommodity = null;
        myCommodities.clear();
        MyCommodityDao myCommodityDao = new MyCommodityDao(context);
        Cursor cursor = myCommodityDao.QueryByLike(data);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_variety = cursor.getString(cursor.getColumnIndex("currency_variety"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            myCommodity = new MyCommodity(id, name, price, currency_variety, product_imgs1);
            myCommodities.add(myCommodity);
        }
        cursor.close();
        return myCommodities;

    }

    //我是采购商-查询数量
    public static ArrayList<MyCommodity> selectGoodsforBuyerNumbers(Context context) {
        myCommodity = null;
        myCommodities.clear();
        MyCommodityDao myCommodityDao = new MyCommodityDao(context);
        Cursor cursor = myCommodityDao.SelectforType();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            myCommodity = new MyCommodity(id);
            myCommodities.add(myCommodity);
        }
        cursor.close();
        return myCommodities;
    }


    //MyQuote 报价表

    //根据商品id查询已报价数据-返回集合
    public static ArrayList<MyQuote> selectByAllReadlyQuote(Context context, int i) {
        myQuote = null;
        myQuotes.clear();
        MyQuoteDao myQuoteDao = new MyQuoteDao(context);
        Cursor cursor = myQuoteDao.SelectAllReadlyQuote(i);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));//id
            int commodity_id = cursor.getInt(cursor.getColumnIndex("commodity_id"));//商品id
            String serial_number = cursor.getString(cursor.getColumnIndex("serial_number"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String goods_unit = cursor.getString(cursor.getColumnIndex("goods_unit"));
            String material = cursor.getString(cursor.getColumnIndex("material"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            String price_clause = cursor.getString(cursor.getColumnIndex("price_clause"));
            String price_clause_diy = cursor.getString(cursor.getColumnIndex("price_clause_diy"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
            String self_defined = cursor.getString(cursor.getColumnIndex("self_defined"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            byte[] product_imgs2 = cursor.getBlob(cursor.getColumnIndex("product_imgs2"));
            byte[] product_imgs3 = cursor.getBlob(cursor.getColumnIndex("product_imgs3"));
            byte[] product_imgs4 = cursor.getBlob(cursor.getColumnIndex("product_imgs4"));
            String buyer_name = cursor.getString(cursor.getColumnIndex("buyer_name"));
            String buyer_companyname = cursor.getString(cursor.getColumnIndex("buyer_companyname"));
            String buyer_phone = cursor.getString(cursor.getColumnIndex("buyer_phone"));
            String quote_type = cursor.getString(cursor.getColumnIndex("quote_type"));
            String quote_time = cursor.getString(cursor.getColumnIndex("quote_time"));
            String uniqued_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            myQuote = new MyQuote(id, commodity_id, serial_number, goods_name, goods_unit, material, color, price,
                    currency_varitety, price_clause, price_clause_diy, remark, introduction, self_defined, product_imgs1, product_imgs2, product_imgs3, product_imgs4,
                    buyer_name, buyer_companyname, buyer_phone, quote_type, quote_time, uniqued_id);
            myQuotes.add(myQuote);
        }
        cursor.close();
        return myQuotes;

    }

    //查询全部类类型的报价（部分字段）
    public static ArrayList<MyQuote> selectQuoteforAll(Context context, String i, int j) {
        myQuote = null;
        myQuotes.clear();
        MyQuoteDao myQuoteDao = new MyQuoteDao(context);
        Cursor cursor = myQuoteDao.selectByUnique_id_limit(i, j);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            String quote_type = cursor.getString(cursor.getColumnIndex("quote_type"));
            myQuote = new MyQuote(id, goods_name, price, currency_varitety, product_imgs1, quote_type);
            myQuotes.add(myQuote);
        }
        cursor.close();
        return myQuotes;
    }

    //查询已报价的数据（部分字段）
    public static ArrayList<MyQuote> selectQuoteAllReadly(Context context, String i, int j) {
        myQuote = null;
        myQuotes.clear();
        MyQuoteDao myQuoteDao = new MyQuoteDao(context);
        Cursor cursor = myQuoteDao.QueryforReadly_limit(i, j);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            String quote_type = cursor.getString(cursor.getColumnIndex("quote_type"));
            myQuote = new MyQuote(id, goods_name, price, currency_varitety, product_imgs1, quote_type);
            myQuotes.add(myQuote);
        }
        cursor.close();
        return myQuotes;
    }


    //查询预留报价的数据（部分字段）
    public static ArrayList<MyQuote> selectQuoteYuLiu(Context context, String i, int j) {
        myQuote = null;
        myQuotes.clear();
        MyQuoteDao myQuoteDao = new MyQuoteDao(context);
        Cursor cursor = myQuoteDao.QueryforYuLiu_limit(i, j);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            String quote_type = cursor.getString(cursor.getColumnIndex("quote_type"));
            myQuote = new MyQuote(id, goods_name, price, currency_varitety, product_imgs1, quote_type);
            myQuotes.add(myQuote);
        }
        cursor.close();
        return myQuotes;
    }

    //查询留样报价的数据（部分字段）
    public static ArrayList<MyQuote> selectQuoteLiuYang(Context context, String i, int j) {
        myQuote = null;
        myQuotes.clear();
        MyQuoteDao myQuoteDao = new MyQuoteDao(context);
        Cursor cursor = myQuoteDao.QueryforLiuYang_limit(i, j);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            String quote_type = cursor.getString(cursor.getColumnIndex("quote_type"));
            myQuote = new MyQuote(id, goods_name, price, currency_varitety, product_imgs1, quote_type);
            myQuotes.add(myQuote);
        }
        cursor.close();
        return myQuotes;
    }


    //根据id来查询报价商品数据
    public static MyQuote selectQuoteById(Context context, String i) {
        myQuote = null;
        myQuotes.clear();
        MyQuoteDao myQuoteDao = new MyQuoteDao(context);
        Cursor cursor = myQuoteDao.selectById(i);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            int commodity_id = cursor.getInt(cursor.getColumnIndex("commodity_id"));
            String serial_number = cursor.getString(cursor.getColumnIndex("serial_number"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String goods_unit = cursor.getString(cursor.getColumnIndex("goods_unit"));
            String material = cursor.getString(cursor.getColumnIndex("material"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            String price_clause = cursor.getString(cursor.getColumnIndex("price_clause"));
            String price_clause_diy = cursor.getString(cursor.getColumnIndex("price_clause_diy"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
            String self_defined = cursor.getString(cursor.getColumnIndex("self_defined"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            byte[] product_imgs2 = cursor.getBlob(cursor.getColumnIndex("product_imgs2"));
            byte[] product_imgs3 = cursor.getBlob(cursor.getColumnIndex("product_imgs3"));
            byte[] product_imgs4 = cursor.getBlob(cursor.getColumnIndex("product_imgs4"));
            String buyer_name = cursor.getString(cursor.getColumnIndex("buyer_name"));
            String buyer_companyname = cursor.getString(cursor.getColumnIndex("buyer_companyname"));
            String buyer_phone = cursor.getString(cursor.getColumnIndex("buyer_phone"));
            String quote_type = cursor.getString(cursor.getColumnIndex("quote_type"));
            String quote_time = cursor.getString(cursor.getColumnIndex("quote_time"));
            String uniqued_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String moq = cursor.getString(cursor.getColumnIndex("moq"));
            String goods_weight = cursor.getString(cursor.getColumnIndex("goods_weight"));
            String goods_weight_unit = cursor.getString(cursor.getColumnIndex("goods_weight_unit"));

            String outbox_number = cursor.getString(cursor.getColumnIndex("outbox_number"));
            String outbox_size = cursor.getString(cursor.getColumnIndex("outbox_size"));
            String outbox_weight = cursor.getString(cursor.getColumnIndex("outbox_weight"));
            String outbox_weight_unit = cursor.getString(cursor.getColumnIndex("outbox_weight_unit"));
            String centerbox_number = cursor.getString(cursor.getColumnIndex("centerbox_number"));
            String centerbox_size = cursor.getString(cursor.getColumnIndex("centerbox_size"));
            String centerbox_weight = cursor.getString(cursor.getColumnIndex("centerbox_weight"));
            String centerbox_weight_unit = cursor.getString(cursor.getColumnIndex("centerbox_weight_unit"));
            myQuote = new MyQuote(id, commodity_id, serial_number, goods_name, goods_unit, material, color,
                    price, currency_varitety, price_clause, price_clause_diy, remark, introduction, self_defined,
                    product_imgs1, product_imgs2, product_imgs3, product_imgs4, buyer_name, buyer_companyname, buyer_phone,
                    quote_type, quote_time, uniqued_id, moq, goods_weight, goods_weight_unit, outbox_number, outbox_size,
                    outbox_weight, outbox_weight_unit, centerbox_number, centerbox_size, centerbox_weight, centerbox_weight_unit);
            myQuotes.add(myQuote);
        }
        cursor.close();
        return myQuote;
    }

    //根据id来查询报价商品图片
    public static MyQuote selectQuoteById2(Context context, String i) {
        myQuote = null;
        myQuotes.clear();
        MyQuoteDao myQuoteDao = new MyQuoteDao(context);
        Cursor cursor = myQuoteDao.selectById(i);
        while (cursor.moveToNext()) {
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            byte[] product_imgs2 = cursor.getBlob(cursor.getColumnIndex("product_imgs2"));
            byte[] product_imgs3 = cursor.getBlob(cursor.getColumnIndex("product_imgs3"));
            byte[] product_imgs4 = cursor.getBlob(cursor.getColumnIndex("product_imgs4"));
            myQuote = new MyQuote(product_imgs1, product_imgs2, product_imgs3, product_imgs4);
            myQuotes.add(myQuote);
        }
        cursor.close();
        return myQuote;

    }


    //查询全部类型的报价数量
    public static ArrayList<MyQuote> selectQuoteNumberforAll(Context context, String i) {
        myQuote = null;
        myQuotes.clear();
        MyQuoteDao myQuoteDao = new MyQuoteDao(context);
        Cursor cursor = myQuoteDao.selectByUnique_id(i);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            myQuote = new MyQuote(id);
            myQuotes.add(myQuote);
        }
        cursor.close();
        return myQuotes;
    }

    //查询已报价类型的数量
    public static ArrayList<MyQuote> selectQuoteNumberforYiBaoJia(Context context, String i) {
        myQuote = null;
        myQuotes.clear();
        MyQuoteDao myQuoteDao = new MyQuoteDao(context);
        Cursor cursor = myQuoteDao.QueryforReadly(i);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            myQuote = new MyQuote(id);
            myQuotes.add(myQuote);
        }
        cursor.close();
        return myQuotes;
    }

    //查询留样报价的数量
    public static ArrayList<MyQuote> selectQuoteNumberforLiuYang(Context context, String i) {
        myQuote = null;
        myQuotes.clear();
        MyQuoteDao myQuoteDao = new MyQuoteDao(context);
        Cursor cursor = myQuoteDao.QueryforLiuYang(i);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            myQuote = new MyQuote(id);
            myQuotes.add(myQuote);
        }
        cursor.close();
        return myQuotes;
    }

    //查询预留报价的数量
    public static ArrayList<MyQuote> selectQuoteNumberforYuLiu(Context context, String i) {
        myQuote = null;
        myQuotes.clear();
        MyQuoteDao myQuoteDao = new MyQuoteDao(context);
        Cursor cursor = myQuoteDao.QueryforYuLiu(i);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            myQuote = new MyQuote(id);
            myQuotes.add(myQuote);
        }
        cursor.close();
        return myQuotes;
    }


    //已报价
    //根据uniqued_id来 分页 查询 已报价 的数据并且以 时间排序 (全部字段) i-uniqued_id j-第几条
    public static ArrayList<MyQuote> selectYiQuoteDesc(Context context, String i, int j) {
        myQuote = null;
        myQuotes.clear();
        MyQuoteDao myQuoteDao = new MyQuoteDao(context);
        Cursor cursor = myQuoteDao.QueryYiDesc(i, j);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));//id
            int commodity_id = cursor.getInt(cursor.getColumnIndex("commodity_id"));//商品id
            String serial_number = cursor.getString(cursor.getColumnIndex("serial_number"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String goods_unit = cursor.getString(cursor.getColumnIndex("goods_unit"));
            String material = cursor.getString(cursor.getColumnIndex("material"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            String price_clause = cursor.getString(cursor.getColumnIndex("price_clause"));
            String price_clause_diy = cursor.getString(cursor.getColumnIndex("price_clause_diy"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
            String self_defined = cursor.getString(cursor.getColumnIndex("self_defined"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            byte[] product_imgs2 = cursor.getBlob(cursor.getColumnIndex("product_imgs2"));
            byte[] product_imgs3 = cursor.getBlob(cursor.getColumnIndex("product_imgs3"));
            byte[] product_imgs4 = cursor.getBlob(cursor.getColumnIndex("product_imgs4"));
            String buyer_name = cursor.getString(cursor.getColumnIndex("buyer_name"));
            String buyer_companyname = cursor.getString(cursor.getColumnIndex("buyer_companyname"));
            String buyer_phone = cursor.getString(cursor.getColumnIndex("buyer_phone"));
            String quote_type = cursor.getString(cursor.getColumnIndex("quote_type"));
            String quote_time = cursor.getString(cursor.getColumnIndex("quote_time"));
            String uniqued_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String moq = cursor.getString(cursor.getColumnIndex("moq"));
            String goods_weight = cursor.getString(cursor.getColumnIndex("goods_weight"));
            String goods_weight_unit = cursor.getString(cursor.getColumnIndex("goods_weight_unit"));
            myQuote = new MyQuote(id, commodity_id, serial_number, goods_name, goods_unit, material, color,
                    price, currency_varitety, price_clause, price_clause_diy, remark, introduction, self_defined,
                    product_imgs1, product_imgs2, product_imgs3, product_imgs4, buyer_name, buyer_companyname,
                    buyer_phone, quote_type, quote_time, uniqued_id, moq, goods_weight, goods_weight_unit);
            myQuotes.add(myQuote);
        }
        cursor.close();
        return myQuotes;
    }

    //预留报价
    //根据uniqued_id来 分页 查询 预留报价 的数据并且以 时间排序 (全部字段) i-uniqued_id j-第几条
    public static ArrayList<MyQuote> selectYuLiuQuoteDesc(Context context, String i, int j) {
        myQuote = null;
        myQuotes.clear();
        MyQuoteDao myQuoteDao = new MyQuoteDao(context);
        Cursor cursor = myQuoteDao.QueryYuLiuDesc(i, j);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));//id
            int commodity_id = cursor.getInt(cursor.getColumnIndex("commodity_id"));//商品id
            String serial_number = cursor.getString(cursor.getColumnIndex("serial_number"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String goods_unit = cursor.getString(cursor.getColumnIndex("goods_unit"));
            String material = cursor.getString(cursor.getColumnIndex("material"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            String price_clause = cursor.getString(cursor.getColumnIndex("price_clause"));
            String price_clause_diy = cursor.getString(cursor.getColumnIndex("price_clause_diy"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
            String self_defined = cursor.getString(cursor.getColumnIndex("self_defined"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            byte[] product_imgs2 = cursor.getBlob(cursor.getColumnIndex("product_imgs2"));
            byte[] product_imgs3 = cursor.getBlob(cursor.getColumnIndex("product_imgs3"));
            byte[] product_imgs4 = cursor.getBlob(cursor.getColumnIndex("product_imgs4"));
            String buyer_name = cursor.getString(cursor.getColumnIndex("buyer_name"));
            String buyer_companyname = cursor.getString(cursor.getColumnIndex("buyer_companyname"));
            String buyer_phone = cursor.getString(cursor.getColumnIndex("buyer_phone"));
            String quote_type = cursor.getString(cursor.getColumnIndex("quote_type"));
            String quote_time = cursor.getString(cursor.getColumnIndex("quote_time"));
            String uniqued_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String moq = cursor.getString(cursor.getColumnIndex("moq"));
            String goods_weight = cursor.getString(cursor.getColumnIndex("goods_weight"));
            String goods_weight_unit = cursor.getString(cursor.getColumnIndex("goods_weight_unit"));
            myQuote = new MyQuote(id, commodity_id, serial_number, goods_name, goods_unit, material, color,
                    price, currency_varitety, price_clause, price_clause_diy, remark, introduction, self_defined,
                    product_imgs1, product_imgs2, product_imgs3, product_imgs4, buyer_name, buyer_companyname,
                    buyer_phone, quote_type, quote_time, uniqued_id, moq, goods_weight, goods_weight_unit);
            myQuotes.add(myQuote);
        }
        cursor.close();
        return myQuotes;
    }

    //留样报价
    //根据uniqued_id来 分页 查询 留样报价 的数据并且以 时间排序 (全部字段) i-uniqued_id j-第几条
    public static ArrayList<MyQuote> selectLiuYangQuoteDesc(Context context, String i, int j) {
        myQuote = null;
        myQuotes.clear();
        MyQuoteDao myQuoteDao = new MyQuoteDao(context);
        Cursor cursor = myQuoteDao.QueryLiuYangDesc(i, j);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));//id
            int commodity_id = cursor.getInt(cursor.getColumnIndex("commodity_id"));//商品id
            String serial_number = cursor.getString(cursor.getColumnIndex("serial_number"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String goods_unit = cursor.getString(cursor.getColumnIndex("goods_unit"));
            String material = cursor.getString(cursor.getColumnIndex("material"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            String price_clause = cursor.getString(cursor.getColumnIndex("price_clause"));
            String price_clause_diy = cursor.getString(cursor.getColumnIndex("price_clause_diy"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
            String self_defined = cursor.getString(cursor.getColumnIndex("self_defined"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            byte[] product_imgs2 = cursor.getBlob(cursor.getColumnIndex("product_imgs2"));
            byte[] product_imgs3 = cursor.getBlob(cursor.getColumnIndex("product_imgs3"));
            byte[] product_imgs4 = cursor.getBlob(cursor.getColumnIndex("product_imgs4"));
            String buyer_name = cursor.getString(cursor.getColumnIndex("buyer_name"));
            String buyer_companyname = cursor.getString(cursor.getColumnIndex("buyer_companyname"));
            String buyer_phone = cursor.getString(cursor.getColumnIndex("buyer_phone"));
            String quote_type = cursor.getString(cursor.getColumnIndex("quote_type"));
            String quote_time = cursor.getString(cursor.getColumnIndex("quote_time"));
            String uniqued_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String moq = cursor.getString(cursor.getColumnIndex("moq"));
            String goods_weight = cursor.getString(cursor.getColumnIndex("goods_weight"));
            String goods_weight_unit = cursor.getString(cursor.getColumnIndex("goods_weight_unit"));
            myQuote = new MyQuote(id, commodity_id, serial_number, goods_name, goods_unit, material, color,
                    price, currency_varitety, price_clause, price_clause_diy, remark, introduction, self_defined,
                    product_imgs1, product_imgs2, product_imgs3, product_imgs4, buyer_name, buyer_companyname,
                    buyer_phone, quote_type, quote_time, uniqued_id, moq, goods_weight, goods_weight_unit);
            myQuotes.add(myQuote);
        }
        cursor.close();
        return myQuotes;

    }

    //根据多个id来查询报价表中的数据
    public static ArrayList<MyQuote> selectQuoteByIds(Context context, ArrayList<String> i) {
        myQuote = null;
        myQuotes.clear();
        MyQuoteDao myQuoteDao = new MyQuoteDao(context);
        Cursor cursor = myQuoteDao.selectforIdsByQuote(i);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            int commodity_id = cursor.getInt(cursor.getColumnIndex("commodity_id"));
            String serial_number = cursor.getString(cursor.getColumnIndex("serial_number"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String goods_unit = cursor.getString(cursor.getColumnIndex("goods_unit"));
            String material = cursor.getString(cursor.getColumnIndex("material"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            String price_clause = cursor.getString(cursor.getColumnIndex("price_clause"));
            String price_clause_diy = cursor.getString(cursor.getColumnIndex("price_clause_diy"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
            String self_defined = cursor.getString(cursor.getColumnIndex("self_defined"));
            byte[] product_imgs1s = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            byte[] product_imgs2s = cursor.getBlob(cursor.getColumnIndex("product_imgs2"));
            byte[] product_imgs3s = cursor.getBlob(cursor.getColumnIndex("product_imgs3"));
            byte[] product_imgs4s = cursor.getBlob(cursor.getColumnIndex("product_imgs4"));
            String buyer_name = cursor.getString(cursor.getColumnIndex("buyer_name"));
            String buyer_companyname = cursor.getString(cursor.getColumnIndex("buyer_companyname"));
            String buyer_phone = cursor.getString(cursor.getColumnIndex("buyer_phone"));
            String quote_type = cursor.getString(cursor.getColumnIndex("quote_type"));
            String quote_time = cursor.getString(cursor.getColumnIndex("quote_time"));
            String uniqued_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String moq = cursor.getString(cursor.getColumnIndex("moq"));
            String goods_weight = cursor.getString(cursor.getColumnIndex("goods_weight"));
            String goods_weight_unit = cursor.getString(cursor.getColumnIndex("goods_weight_unit"));
            String outbox_number = cursor.getString(cursor.getColumnIndex("outbox_number"));
            String outbox_size = cursor.getString(cursor.getColumnIndex("outbox_size"));
            String outbox_weight = cursor.getString(cursor.getColumnIndex("outbox_weight"));
            String outbox_weight_unit = cursor.getString(cursor.getColumnIndex("outbox_weight_unit"));
            String centerbox_number = cursor.getString(cursor.getColumnIndex("centerbox_number"));
            String centerbox_size = cursor.getString(cursor.getColumnIndex("centerbox_size"));
            String centerbox_weight = cursor.getString(cursor.getColumnIndex("centerbox_weight"));
            String centerbox_weight_unit = cursor.getString(cursor.getColumnIndex("centerbox_weight_unit"));
            myQuote = new MyQuote(id, commodity_id, serial_number, goods_name, goods_unit, material, color,
                    price, currency_varitety, price_clause, price_clause_diy, remark, introduction, self_defined, product_imgs1s,
                    product_imgs2s, product_imgs3s, product_imgs4s, buyer_name, buyer_companyname, buyer_phone,
                    quote_type, quote_time, uniqued_id, moq, goods_weight, goods_weight_unit, outbox_number, outbox_size,
                    outbox_weight, outbox_weight_unit, centerbox_number, centerbox_size, centerbox_weight, centerbox_weight_unit);
            myQuotes.add(myQuote);
        }
        cursor.close();
        return myQuotes;
    }


    //询价表


    //查询全部类型的询价数据（部分字段）
    public static ArrayList<MyInquiry> selectInquiryAll(Context context, String i, int j) {
        myInquiry = null;
        myInquiries.clear();
        MyInquiryDao myInquiryDao = new MyInquiryDao(context);
        Cursor cursor = myInquiryDao.queryAll_Limit(i, j);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));//
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));//
            double price = cursor.getDouble(cursor.getColumnIndex("price"));//
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));//
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));//
            String inquiry_type = cursor.getString(cursor.getColumnIndex("inquiry_type"));//
            myInquiry = new MyInquiry(id, goods_name, price, currency_varitety, product_imgs1, inquiry_type);
            myInquiries.add(myInquiry);
        }
        cursor.close();
        return myInquiries;
    }

    //查询已询价的数据
    public static ArrayList<MyInquiry> selectInquiryYiXunJia(Context context, String i, int j) {
        myInquiry = null;
        myInquiries.clear();
        MyInquiryDao myInquiryDao = new MyInquiryDao(context);
        Cursor cursor = myInquiryDao.queryYiXunJia_Limit(i, j);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            String inquiry_type = cursor.getString(cursor.getColumnIndex("inquiry_type"));
            myInquiry = new MyInquiry(id, goods_name, price, currency_varitety, product_imgs1, inquiry_type);
            myInquiries.add(myInquiry);
        }
        cursor.close();
        return myInquiries;
    }

    //查询预留询价的数据
    public static ArrayList<MyInquiry> selectInquiryYuLiu(Context context, String i, int j) {
        myInquiry = null;
        myInquiries.clear();
        MyInquiryDao myInquiryDao = new MyInquiryDao(context);
        Cursor cursor = myInquiryDao.queryYuLiuXunJia_Limit(i, j);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));//
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));//
            double price = cursor.getDouble(cursor.getColumnIndex("price"));//
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));//
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));//
            String inquiry_type = cursor.getString(cursor.getColumnIndex("inquiry_type"));//
            myInquiry = new MyInquiry(id, goods_name, price, currency_varitety, product_imgs1, inquiry_type);
            myInquiries.add(myInquiry);
        }
        cursor.close();
        return myInquiries;
    }

    //查询留样询价的数据（部分字段）
    public static ArrayList<MyInquiry> selectInquiryLiuYang(Context context, String i, int j) {
        myInquiry = null;
        myInquiries.clear();
        MyInquiryDao myInquiryDao = new MyInquiryDao(context);
        Cursor cursor = myInquiryDao.queryLiuYangXunJia_Limit(i, j);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));//
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));//
            double price = cursor.getDouble(cursor.getColumnIndex("price"));//
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));//
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));//
            String inquiry_type = cursor.getString(cursor.getColumnIndex("inquiry_type"));//
            myInquiry = new MyInquiry(id, goods_name, price, currency_varitety, product_imgs1, inquiry_type);
            myInquiries.add(myInquiry);
        }
        cursor.close();
        return myInquiries;
    }


    //根据id来查询询价表中的数据
    public static MyInquiry selectInquiryById(Context context, String i) {
        myInquiry = null;
        myInquiries.clear();
        MyInquiryDao myInquiryDao = new MyInquiryDao(context);
        Cursor cursor = myInquiryDao.selectById(i);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            int commodity_id = cursor.getInt(cursor.getColumnIndex("commodity_id"));
            String serial_number = cursor.getString(cursor.getColumnIndex("serial_number"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String goods_unit = cursor.getString(cursor.getColumnIndex("goods_unit"));
            String material = cursor.getString(cursor.getColumnIndex("material"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            String price_clause = cursor.getString(cursor.getColumnIndex("price_clause"));
            String price_clause_diy = cursor.getString(cursor.getColumnIndex("price_clause_diy"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
            String self_defined = cursor.getString(cursor.getColumnIndex("self_defined"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            byte[] product_imgs2 = cursor.getBlob(cursor.getColumnIndex("product_imgs2"));
            byte[] product_imgs3 = cursor.getBlob(cursor.getColumnIndex("product_imgs3"));
            byte[] product_imgs4 = cursor.getBlob(cursor.getColumnIndex("product_imgs4"));
            String supplier_name = cursor.getString(cursor.getColumnIndex("supplier_name"));
            String supplier_companyname = cursor.getString(cursor.getColumnIndex("supplier_companyname"));
            String supplier_phone = cursor.getString(cursor.getColumnIndex("supplier_phone"));
            String inquiry_type = cursor.getString(cursor.getColumnIndex("inquiry_type"));
            String inquiry_time = cursor.getString(cursor.getColumnIndex("inquiry_time"));
            String uniqued_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String moq = cursor.getString(cursor.getColumnIndex("moq"));
            String goods_weight = cursor.getString(cursor.getColumnIndex("goods_weight"));
            String goods_weight_unit = cursor.getString(cursor.getColumnIndex("goods_weight_unit"));

            String outbox_number = cursor.getString(cursor.getColumnIndex("outbox_number"));
            String outbox_size = cursor.getString(cursor.getColumnIndex("outbox_size"));
            String outbox_weight = cursor.getString(cursor.getColumnIndex("outbox_weight"));
            String outbox_weight_unit = cursor.getString(cursor.getColumnIndex("outbox_weight_unit"));
            String centerbox_number = cursor.getString(cursor.getColumnIndex("centerbox_number"));
            String centerbox_size = cursor.getString(cursor.getColumnIndex("centerbox_size"));
            String centerbox_weight = cursor.getString(cursor.getColumnIndex("centerbox_weight"));
            String centerbox_weight_unit = cursor.getString(cursor.getColumnIndex("centerbox_weight_unit"));
            myInquiry = new MyInquiry(id, commodity_id, serial_number, goods_name, goods_unit, material, color, price, currency_varitety,
                    price_clause, price_clause_diy, remark, introduction, self_defined, product_imgs1, product_imgs2, product_imgs3, product_imgs4,
                    supplier_name, supplier_companyname, supplier_phone, inquiry_type, inquiry_time, uniqued_id, moq, goods_weight, goods_weight_unit
                    , outbox_number, outbox_size, outbox_weight, outbox_weight_unit, centerbox_number, centerbox_size, centerbox_weight, centerbox_weight_unit);
            myInquiries.add(myInquiry);
        }
        cursor.close();
        return myInquiry;

    }

    //根据多个id来查询询价表中的数据
    public static ArrayList<MyInquiry> selectInquiryByIds(Context context, ArrayList<String> i) {
        myInquiry = null;
        myInquiries.clear();
        MyInquiryDao myInquiryDao = new MyInquiryDao(context);
        Cursor cursor = myInquiryDao.selectforIdsByInquiry(i);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            int commodity_id = cursor.getInt(cursor.getColumnIndex("commodity_id"));
            String serial_number = cursor.getString(cursor.getColumnIndex("serial_number"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String goods_unit = cursor.getString(cursor.getColumnIndex("goods_unit"));
            String material = cursor.getString(cursor.getColumnIndex("material"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            String price_clause = cursor.getString(cursor.getColumnIndex("price_clause"));
            String price_clause_diy = cursor.getString(cursor.getColumnIndex("price_clause_diy"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
            String self_defined = cursor.getString(cursor.getColumnIndex("self_defined"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            byte[] product_imgs2 = cursor.getBlob(cursor.getColumnIndex("product_imgs2"));
            byte[] product_imgs3 = cursor.getBlob(cursor.getColumnIndex("product_imgs3"));
            byte[] product_imgs4 = cursor.getBlob(cursor.getColumnIndex("product_imgs4"));
            String supplier_name = cursor.getString(cursor.getColumnIndex("supplier_name"));
            String supplier_companyname = cursor.getString(cursor.getColumnIndex("supplier_companyname"));
            String supplier_phone = cursor.getString(cursor.getColumnIndex("supplier_phone"));
            String inquiry_type = cursor.getString(cursor.getColumnIndex("inquiry_type"));
            String inquiry_time = cursor.getString(cursor.getColumnIndex("inquiry_time"));
            String uniqued_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String moq = cursor.getString(cursor.getColumnIndex("moq"));
            String goods_weight = cursor.getString(cursor.getColumnIndex("goods_weight"));
            String goods_weight_unit = cursor.getString(cursor.getColumnIndex("goods_weight_unit"));

            String outbox_number = cursor.getString(cursor.getColumnIndex("outbox_number"));
            String outbox_size = cursor.getString(cursor.getColumnIndex("outbox_size"));
            String outbox_weight = cursor.getString(cursor.getColumnIndex("outbox_weight"));
            String outbox_weight_unit = cursor.getString(cursor.getColumnIndex("outbox_weight_unit"));
            String centerbox_number = cursor.getString(cursor.getColumnIndex("centerbox_number"));
            String centerbox_size = cursor.getString(cursor.getColumnIndex("centerbox_size"));
            String centerbox_weight = cursor.getString(cursor.getColumnIndex("centerbox_weight"));
            String centerbox_weight_unit = cursor.getString(cursor.getColumnIndex("centerbox_weight_unit"));
            myInquiry = new MyInquiry(id, commodity_id, serial_number, goods_name, goods_unit, material, color, price, currency_varitety,
                    price_clause, price_clause_diy, remark, introduction, self_defined, product_imgs1, product_imgs2, product_imgs3, product_imgs4,
                    supplier_name, supplier_companyname, supplier_phone, inquiry_type, inquiry_time, uniqued_id, moq, goods_weight, goods_weight_unit
                    , outbox_number, outbox_size, outbox_weight, outbox_weight_unit, centerbox_number, centerbox_size, centerbox_weight, centerbox_weight_unit);
            myInquiries.add(myInquiry);
        }
        cursor.close();
        return myInquiries;


    }


    //根据id来查询询价表中的数据--只查询图片
    public static MyInquiry selectInquiryById2(Context context, String i) {
        myInquiry = null;
        myInquiries.clear();
        MyInquiryDao myInquiryDao = new MyInquiryDao(context);
        Cursor cursor = myInquiryDao.selectById(i);
        while (cursor.moveToNext()) {
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            byte[] product_imgs2 = cursor.getBlob(cursor.getColumnIndex("product_imgs2"));
            byte[] product_imgs3 = cursor.getBlob(cursor.getColumnIndex("product_imgs3"));
            byte[] product_imgs4 = cursor.getBlob(cursor.getColumnIndex("product_imgs4"));
            myInquiry = new MyInquiry(product_imgs1, product_imgs2, product_imgs3, product_imgs4);
            myInquiries.add(myInquiry);
        }
        cursor.close();
        return myInquiry;

    }

    //查询全部类型的询价数据
    public static ArrayList<MyInquiry> selectInquiryforAllNumbers(Context context, String i) {
        myInquiry = null;
        myInquiries.clear();
        MyInquiryDao myInquiryDao = new MyInquiryDao(context);
        Cursor cursor = myInquiryDao.queryAll(i);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            myInquiry = new MyInquiry(id);
            myInquiries.add(myInquiry);
        }
        cursor.close();
        return myInquiries;

    }

    //已询价的数量
    public static ArrayList<MyInquiry> selectInquiryforYiNumbers(Context context, String i) {
        myInquiry = null;
        myInquiries.clear();
        MyInquiryDao myInquiryDao = new MyInquiryDao(context);
        Cursor cursor = myInquiryDao.queryYiXunJia(i);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            myInquiry = new MyInquiry(id);
            myInquiries.add(myInquiry);
        }
        cursor.close();
        return myInquiries;
    }

    //预留询价的数量
    public static ArrayList<MyInquiry> selectInquiryforYuLiuNumbers(Context context, String i) {
        myInquiry = null;
        myInquiries.clear();
        MyInquiryDao myInquiryDao = new MyInquiryDao(context);
        Cursor cursor = myInquiryDao.queryYuLiuXunJia(i);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            myInquiry = new MyInquiry(id);
            myInquiries.add(myInquiry);
        }
        cursor.close();
        return myInquiries;

    }

    //留样询价的数量
    public static ArrayList<MyInquiry> selectInquiryforLiuYangNumbers(Context context, String i) {
        myInquiry = null;
        myInquiries.clear();
        MyInquiryDao myInquiryDao = new MyInquiryDao(context);
        Cursor cursor = myInquiryDao.queryLiuYangXunJia(i);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            myInquiry = new MyInquiry(id);
            myInquiries.add(myInquiry);
        }
        cursor.close();
        return myInquiries;
    }

    //已询价
    //根据uniqued_id来 分页 查询 已询价 的数据并且以 时间排序 (全部字段) i-uniqued_id j-第几条
    public static ArrayList<MyInquiry> selectYiXunJiaDesc(Context context, String i, int j) {
        myInquiry = null;
        myInquiries.clear();
        MyInquiryDao myInquiryDao = new MyInquiryDao(context);
        Cursor cursor = myInquiryDao.SelectYiDesc(i, j);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String goods_unit = cursor.getString(cursor.getColumnIndex("goods_unit"));
            String material = cursor.getString(cursor.getColumnIndex("material"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            String inquiry_time = cursor.getString(cursor.getColumnIndex("inquiry_time"));
            String moq = cursor.getString(cursor.getColumnIndex("moq"));
            myInquiry = new MyInquiry(id, goods_name, goods_unit, material, color, price, currency_varitety, inquiry_time, moq);
            myInquiries.add(myInquiry);
        }
        cursor.close();
        return myInquiries;
    }

    //预留询价
    //根据uniqued_id来 分页 查询 预留询价 的数据并且以 时间排序 (全部字段) i-uniqued_id j-第几条
    public static ArrayList<MyInquiry> selectYuLiuXunJiaDesc(Context context, String i, int j) {
        myInquiry = null;
        myInquiries.clear();
        MyInquiryDao myInquiryDao = new MyInquiryDao(context);
        Cursor cursor = myInquiryDao.SelectYuLiuDesc(i, j);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String goods_unit = cursor.getString(cursor.getColumnIndex("goods_unit"));
            String material = cursor.getString(cursor.getColumnIndex("material"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            String inquiry_time = cursor.getString(cursor.getColumnIndex("inquiry_time"));
            String moq = cursor.getString(cursor.getColumnIndex("moq"));
            myInquiry = new MyInquiry(id, goods_name, goods_unit, material, color, price, currency_varitety, inquiry_time, moq);
            myInquiries.add(myInquiry);
        }
        cursor.close();
        return myInquiries;
    }

    //留样询价
    //根据uniqued_id来 分页 查询 留样询价 的数据并且以 时间排序 (全部字段) i-uniqued_id j-第几条
    public static ArrayList<MyInquiry> selectLiuYangDesc(Context context, String i, int j) {
        myInquiry = null;
        myInquiries.clear();
        MyInquiryDao myInquiryDao = new MyInquiryDao(context);
        Cursor cursor = myInquiryDao.SelectLiuYangDesc(i, j);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String goods_unit = cursor.getString(cursor.getColumnIndex("goods_unit"));
            String material = cursor.getString(cursor.getColumnIndex("material"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            String inquiry_time = cursor.getString(cursor.getColumnIndex("inquiry_time"));
            String moq = cursor.getString(cursor.getColumnIndex("moq"));
            myInquiry = new MyInquiry(id, goods_name, goods_unit, material, color, price, currency_varitety, inquiry_time, moq);
            myInquiries.add(myInquiry);
        }
        cursor.close();
        return myInquiries;

    }

    //根据商品id来查询已询价的数据
    public static ArrayList<MyInquiry> selectYiByCommodityId(Context context, int i) {
        myInquiry = null;
        myInquiries.clear();
        MyInquiryDao myInquiryDao = new MyInquiryDao(context);
        Cursor cursor = myInquiryDao.SelectAllReadlyInquiry(i);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            int commodity_id = cursor.getInt(cursor.getColumnIndex("commodity_id"));
            String serial_number = cursor.getString(cursor.getColumnIndex("serial_number"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String goods_unit = cursor.getString(cursor.getColumnIndex("goods_unit"));
            String material = cursor.getString(cursor.getColumnIndex("material"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String currency_varitety = cursor.getString(cursor.getColumnIndex("currency_varitety"));
            String price_clause = cursor.getString(cursor.getColumnIndex("price_clause"));
            String price_clause_diy = cursor.getString(cursor.getColumnIndex("price_clause_diy"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
            String self_defined = cursor.getString(cursor.getColumnIndex("self_defined"));
            byte[] product_imgs1 = cursor.getBlob(cursor.getColumnIndex("product_imgs1"));
            byte[] product_imgs2 = cursor.getBlob(cursor.getColumnIndex("product_imgs2"));
            byte[] product_imgs3 = cursor.getBlob(cursor.getColumnIndex("product_imgs3"));
            byte[] product_imgs4 = cursor.getBlob(cursor.getColumnIndex("product_imgs4"));
            String supplier_name = cursor.getString(cursor.getColumnIndex("supplier_name"));
            String supplier_companyname = cursor.getString(cursor.getColumnIndex("supplier_companyname"));
            String supplier_phone = cursor.getString(cursor.getColumnIndex("supplier_phone"));
            String inquiry_type = cursor.getString(cursor.getColumnIndex("inquiry_type"));
            String inquiry_time = cursor.getString(cursor.getColumnIndex("inquiry_time"));
            String uniqued_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String moq = cursor.getString(cursor.getColumnIndex("moq"));
            String goods_weight = cursor.getString(cursor.getColumnIndex("goods_weight"));
            String goods_weight_unit = cursor.getString(cursor.getColumnIndex("goods_weight_unit"));

            String outbox_number = cursor.getString(cursor.getColumnIndex("outbox_number"));
            String outbox_size = cursor.getString(cursor.getColumnIndex("outbox_size"));
            String outbox_weight = cursor.getString(cursor.getColumnIndex("outbox_weight"));
            String outbox_weight_unit = cursor.getString(cursor.getColumnIndex("outbox_weight_unit"));
            String centerbox_number = cursor.getString(cursor.getColumnIndex("centerbox_number"));
            String centerbox_size = cursor.getString(cursor.getColumnIndex("centerbox_size"));
            String centerbox_weight = cursor.getString(cursor.getColumnIndex("centerbox_weight"));
            String centerbox_weight_unit = cursor.getString(cursor.getColumnIndex("centerbox_weight_unit"));
            myInquiry = new MyInquiry(id, commodity_id, serial_number, goods_name, goods_unit, material, color, price, currency_varitety,
                    price_clause, price_clause_diy, remark, introduction, self_defined, product_imgs1, product_imgs2, product_imgs3, product_imgs4,
                    supplier_name, supplier_companyname, supplier_phone, inquiry_type, inquiry_time, uniqued_id, moq, goods_weight, goods_weight_unit
                    , outbox_number, outbox_size, outbox_weight, outbox_weight_unit, centerbox_number, centerbox_size, centerbox_weight, centerbox_weight_unit);
            myInquiries.add(myInquiry);
        }
        cursor.close();
        return myInquiries;

    }

    //MyCustomer 客户表

    //查询部分的供应商
    public static MyCustomer selectPartSupplier(Context context) {
        myCustomer = null;
        myCustomers.clear();
        MyCustomerDao myCustomerDao = new MyCustomerDao(context);
        Cursor cursor = myCustomerDao.selectForSupplier();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String company_name = cursor.getString(cursor.getColumnIndex("company_name"));
            String unique_id = cursor.getString(cursor.getColumnIndex("unique_id"));
            myCustomer = new MyCustomer(id, name, company_name, unique_id);
            myCustomers.add(myCustomer);
        }
        cursor.close();
        return myCustomer;
    }

    //查询部分的采购商
    public static MyCustomer selectPartBuyer(Context context) {
        myCustomer = null;
        myCustomers.clear();
        MyCustomerDao myCustomerDao = new MyCustomerDao(context);
        Cursor cursor = myCustomerDao.selectForBuyer();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String company_name = cursor.getString(cursor.getColumnIndex("company_name"));
            String unique_id = cursor.getString(cursor.getColumnIndex("unique_id"));
            myCustomer = new MyCustomer(id, name, company_name, unique_id);
            myCustomers.add(myCustomer);
        }
        cursor.close();
        return myCustomer;
    }

    //查询所有的采购商
    public static ArrayList<MyCustomer> selectAllByType(Context context) {
        myCustomer = null;
        myCustomers.clear();
        MyCustomerDao myCustomerDao = new MyCustomerDao(context);
        Cursor cursor = myCustomerDao.SelectforTypeForSupplier();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String company_name = cursor.getString(cursor.getColumnIndex("company_name"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String company_address = cursor.getString(cursor.getColumnIndex("company_address"));
            String industry = cursor.getString(cursor.getColumnIndex("industry_type"));
            String job_position = cursor.getString(cursor.getColumnIndex("job_position"));
            byte[] company_logo = cursor.getBlob(cursor.getColumnIndex("company_logo"));
            String customer_ohter = cursor.getString(cursor.getColumnIndex("customer_ohter"));
            String create_time = cursor.getString(cursor.getColumnIndex("create_time"));
            int customer_type = cursor.getInt(cursor.getColumnIndex("customer_type"));
            String unique_id = cursor.getString(cursor.getColumnIndex("unique_id"));
            myCustomer = new MyCustomer(id, name, company_name, phone, email, company_address, industry, job_position, company_logo, customer_ohter, create_time, customer_type, unique_id);
            myCustomers.add(myCustomer);
        }
        cursor.close();
        return myCustomers;
    }

    //以客户姓名来模糊查询采购商
    public static ArrayList<MyCustomer> findDataByLike(Context context, String i) {
        myCustomer = null;
        myCustomers.clear();
        MyCustomerDao myCustomerDao = new MyCustomerDao(context);
        Cursor cursor = myCustomerDao.QueryByLikeforSupplier(i);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String company_name = cursor.getString(cursor.getColumnIndex("company_name"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String company_address = cursor.getString(cursor.getColumnIndex("company_address"));
            String industry = cursor.getString(cursor.getColumnIndex("industry_type"));
            String job_position = cursor.getString(cursor.getColumnIndex("job_position"));
            byte[] company_logo = cursor.getBlob(cursor.getColumnIndex("company_logo"));
            String customer_ohter = cursor.getString(cursor.getColumnIndex("customer_ohter"));
            String create_time = cursor.getString(cursor.getColumnIndex("create_time"));
            int customer_type = cursor.getInt(cursor.getColumnIndex("customer_type"));
            String unique_id = cursor.getString(cursor.getColumnIndex("unique_id"));
            myCustomer = new MyCustomer(id, name, company_name, phone, email, company_address, industry, job_position, company_logo, customer_ohter, create_time, customer_type, unique_id);
            myCustomers.add(myCustomer);
        }
        cursor.close();
        return myCustomers;
    }

    //以客户的公司名来模糊查询采购商
    public static ArrayList<MyCustomer> findDataByLike2(Context context, String i) {
        myCustomer = null;
        myCustomers.clear();
        MyCustomerDao myCustomerDao = new MyCustomerDao(context);
        Cursor cursor = myCustomerDao.QueryByLikeforSupplier2(i);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String company_name = cursor.getString(cursor.getColumnIndex("company_name"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String company_address = cursor.getString(cursor.getColumnIndex("company_address"));
            String industry = cursor.getString(cursor.getColumnIndex("industry_type"));
            String job_position = cursor.getString(cursor.getColumnIndex("job_position"));
            byte[] company_logo = cursor.getBlob(cursor.getColumnIndex("company_logo"));
            String customer_ohter = cursor.getString(cursor.getColumnIndex("customer_ohter"));
            String create_time = cursor.getString(cursor.getColumnIndex("create_time"));
            int customer_type = cursor.getInt(cursor.getColumnIndex("customer_type"));
            String unique_id = cursor.getString(cursor.getColumnIndex("unique_id"));
            myCustomer = new MyCustomer(id, name, company_name, phone, email, company_address, industry, job_position, company_logo, customer_ohter, create_time, customer_type, unique_id);
            myCustomers.add(myCustomer);
        }
        cursor.close();
        return myCustomers;

    }

    //根据id来查询客户信息的方法
    public static MyCustomer selectCustomerById(Context context, int i) {
        myCustomer = null;
        myCustomers.clear();
        MyCustomerDao myCustomerDao = new MyCustomerDao(context);
        Cursor cursor = myCustomerDao.selectforId(i);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String company_name = cursor.getString(cursor.getColumnIndex("company_name"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String company_address = cursor.getString(cursor.getColumnIndex("company_address"));
            String industry = cursor.getString(cursor.getColumnIndex("industry_type"));
            String job_position = cursor.getString(cursor.getColumnIndex("job_position"));
            byte[] company_logo = cursor.getBlob(cursor.getColumnIndex("company_logo"));
            String customer_ohter = cursor.getString(cursor.getColumnIndex("customer_ohter"));
            String create_time = cursor.getString(cursor.getColumnIndex("create_time"));
            int customer_type = cursor.getInt(cursor.getColumnIndex("customer_type"));
            String unique_id = cursor.getString(cursor.getColumnIndex("unique_id"));
            myCustomer = new MyCustomer(id, name, company_name, phone, email, company_address, industry, job_position, company_logo, customer_ohter, create_time, customer_type, unique_id);
            myCustomers.add(myCustomer);
        }
        cursor.close();
        return myCustomer;
    }

    //我是供应商-查询我的的所有客户（采购商）
    public static ArrayList<MyCustomer> selectAllBuyer(Context context) {
        myCustomer = null;
        myCustomers.clear();
        MyCustomerDao myCustomerDao = new MyCustomerDao(context);
        Cursor cursor = myCustomerDao.SelectforTypeForSupplier();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));//
            String company_name = cursor.getString(cursor.getColumnIndex("company_name"));//
            byte[] company_logos = cursor.getBlob(cursor.getColumnIndex("company_logo"));//
            myCustomer = new MyCustomer(id, company_name, company_logos);
            myCustomers.add(myCustomer);
        }
        cursor.close();
        return myCustomers;

    }


    //我是供应商-模糊查询我的客户（采购商）
    public static ArrayList<MyCustomer> selectMoHuBuyer(Context context, String i) {
        myCustomer = null;
        myCustomers.clear();
        MyCustomerDao myCustomerDao = new MyCustomerDao(context);
        Cursor cursor = myCustomerDao.QueryByLikeforSupplier2(i);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));//
            String company_name = cursor.getString(cursor.getColumnIndex("company_name"));//
            byte[] company_logos = cursor.getBlob(cursor.getColumnIndex("company_logo"));//
            myCustomer = new MyCustomer(id, company_name, company_logos);
            myCustomers.add(myCustomer);
        }
        cursor.close();
        return myCustomers;
    }

    //从数据库中查询所有的供应商
    public static ArrayList<MyCustomer> selectTypeBySupplier(Context context) {
        myCustomer = null;
        myCustomers.clear();
        MyCustomerDao myCustomerDao = new MyCustomerDao(context);
        Cursor cursor = myCustomerDao.SelectforTypeForBuyer();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String company_name = cursor.getString(cursor.getColumnIndex("company_name"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String company_address = cursor.getString(cursor.getColumnIndex("company_address"));
            String industry = cursor.getString(cursor.getColumnIndex("industry_type"));
            String job_position = cursor.getString(cursor.getColumnIndex("job_position"));
            byte[] company_logo = cursor.getBlob(cursor.getColumnIndex("company_logo"));
            String customer_ohter = cursor.getString(cursor.getColumnIndex("customer_ohter"));
            String create_time = cursor.getString(cursor.getColumnIndex("create_time"));
            int customer_type = cursor.getInt(cursor.getColumnIndex("customer_type"));
            String unique_id = cursor.getString(cursor.getColumnIndex("unique_id"));
            myCustomer = new MyCustomer(id, name, company_name, phone, email, company_address, industry, job_position, company_logo, customer_ohter, create_time, customer_type, unique_id);
            myCustomers.add(myCustomer);
        }
        cursor.close();
        return myCustomers;
    }

    //根据姓名来查询供应商的数据
    public static ArrayList<MyCustomer> findDataByLikeforSupplier(Context context, String i) {
        myCustomer = null;
        myCustomers.clear();
        MyCustomerDao myCustomerDao = new MyCustomerDao(context);
        Cursor cursor = myCustomerDao.QueryByLikeforBuyer(i);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String company_name = cursor.getString(cursor.getColumnIndex("company_name"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String company_address = cursor.getString(cursor.getColumnIndex("company_address"));
            String industry = cursor.getString(cursor.getColumnIndex("industry_type"));
            String job_position = cursor.getString(cursor.getColumnIndex("job_position"));
            byte[] company_logo = cursor.getBlob(cursor.getColumnIndex("company_logo"));
            String customer_ohter = cursor.getString(cursor.getColumnIndex("customer_ohter"));
            String create_time = cursor.getString(cursor.getColumnIndex("create_time"));
            int customer_type = cursor.getInt(cursor.getColumnIndex("customer_type"));
            String unique_id = cursor.getString(cursor.getColumnIndex("unique_id"));
            myCustomer = new MyCustomer(id, name, company_name, phone, email, company_address, industry, job_position, company_logo, customer_ohter, create_time, customer_type, unique_id);
            myCustomers.add(myCustomer);
        }
        cursor.close();
        return myCustomers;
    }

    //根据公司名来模糊查询供应商的数据
    public static ArrayList<MyCustomer> findDataByLike2forSupplier(Context context, String i) {
        myCustomer = null;
        myCustomers.clear();
        MyCustomerDao myCustomerDao = new MyCustomerDao(context);
        Cursor cursor = myCustomerDao.QueryByLikeforBuyer2(i);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String company_name = cursor.getString(cursor.getColumnIndex("company_name"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String company_address = cursor.getString(cursor.getColumnIndex("company_address"));
            String industry = cursor.getString(cursor.getColumnIndex("industry_type"));
            String job_position = cursor.getString(cursor.getColumnIndex("job_position"));
            byte[] company_logo = cursor.getBlob(cursor.getColumnIndex("company_logo"));
            String customer_ohter = cursor.getString(cursor.getColumnIndex("customer_ohter"));
            String create_time = cursor.getString(cursor.getColumnIndex("create_time"));
            int customer_type = cursor.getInt(cursor.getColumnIndex("customer_type"));
            String unique_id = cursor.getString(cursor.getColumnIndex("unique_id"));
            myCustomer = new MyCustomer(id, name, company_name, phone, email, company_address, industry, job_position, company_logo, customer_ohter, create_time, customer_type, unique_id);
            myCustomers.add(myCustomer);
        }
        cursor.close();
        return myCustomers;
    }


    //MyUser 个人信息表
    public static MyUser selectInfo(Context context) {
        myUsers.clear();
        MyUserDao myUserDao = new MyUserDao(context);
        Cursor cursor = myUserDao.selectAll();
        while (cursor.moveToNext()) {
            String ch_company_name = cursor.getString(cursor.getColumnIndex("ch_company_name"));
            String ch_contact = cursor.getString(cursor.getColumnIndex("ch_contact"));
            myUser = new MyUser(ch_company_name, ch_contact);
            myUsers.add(myUser);
        }
        cursor.close();
        return myUser;
    }


    //查询个人信息表中所有数据的方法
    public static MyUser selectOurInfo(Context context) {
        myUser = null;
        myUsers.clear();
        MyUserDao myUserDao = new MyUserDao(context);
        Cursor cursor = myUserDao.selectAll();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            int user_type = cursor.getInt(cursor.getColumnIndex("user_type"));
            int system_language = cursor.getInt(cursor.getColumnIndex("system_language"));
            byte[] ch_portrait = cursor.getBlob(cursor.getColumnIndex("ch_portrait"));
            String ch_company_name = cursor.getString(cursor.getColumnIndex("ch_company_name"));
            String ch_industry_type = cursor.getString(cursor.getColumnIndex("ch_industry_type"));
            String ch_company_size = cursor.getString(cursor.getColumnIndex("ch_company_size"));
            String ch_main_business = cursor.getString(cursor.getColumnIndex("ch_main_business"));
            String ch_main_product = cursor.getString(cursor.getColumnIndex("ch_main_product"));
            String ch_official_website = cursor.getString(cursor.getColumnIndex("ch_official_website"));
            String ch_address = cursor.getString(cursor.getColumnIndex("ch_address"));
            String ch_contact = cursor.getString(cursor.getColumnIndex("ch_contact"));
            String ch_job = cursor.getString(cursor.getColumnIndex("ch_job"));
            String ch_telephone = cursor.getString(cursor.getColumnIndex("ch_telephone"));
            String ch_email = cursor.getString(cursor.getColumnIndex("ch_email"));
            String unique_id = cursor.getString(cursor.getColumnIndex("unique_id"));

            String en_company_name = cursor.getString(cursor.getColumnIndex("en_company_name"));
            String en_industry_type = cursor.getString(cursor.getColumnIndex("en_industry_type"));
            String en_company_size = cursor.getString(cursor.getColumnIndex("en_company_size"));
            String en_main_bussiness = cursor.getString(cursor.getColumnIndex("en_main_bussiness"));
            String en_main_product = cursor.getString(cursor.getColumnIndex("en_main_product"));
            String en_address = cursor.getString(cursor.getColumnIndex("en_address"));
            String en_contact = cursor.getString(cursor.getColumnIndex("en_contact"));
            String en_job = cursor.getString(cursor.getColumnIndex("en_job"));
            myUser = new MyUser(id, user_type, system_language, ch_portrait, ch_company_name, ch_industry_type,
                    ch_company_size, ch_main_business, ch_main_product, ch_official_website, ch_address, ch_contact,
                    ch_job, ch_telephone, ch_email, unique_id, en_company_name, en_industry_type, en_company_size,
                    en_main_bussiness, en_main_product, en_address, en_contact, en_job);
            myUsers.add(myUser);
        }
        cursor.close();
        return myUser;

    }


    //查询当前的语言环境
    public static Language selectYuYan(Context context) {
        language = null;
        listforlanguage.clear();
        LanguageDao languageDao = new LanguageDao(context);
        Cursor cursor = languageDao.selectById("easyfair");
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String tag = cursor.getString(cursor.getColumnIndex("tag"));
            language = new Language(id, tag);
            listforlanguage.add(language);
        }
        cursor.close();
        return language;
    }


    //简易报价 根据唯一识别id来查询商品(全部)
    public static ArrayList<EasyQuote> selectByUniquedIdforEasyQuote(Context context, String i) {
        easyQuote = null;
        easyQuotes.clear();
        EasyQuoteDao easyQuoteDao = new EasyQuoteDao(context);
        Cursor cursor = easyQuoteDao.selectByUniqued_id(i);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            byte[] goods_img1 = cursor.getBlob(cursor.getColumnIndex("goods_img1"));
            byte[] goods_img2 = cursor.getBlob(cursor.getColumnIndex("goods_img2"));
            byte[] goods_img3 = cursor.getBlob(cursor.getColumnIndex("goods_img3"));
            byte[] goods_img4 = cursor.getBlob(cursor.getColumnIndex("goods_img4"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String unique_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String code = cursor.getString(cursor.getColumnIndex("code"));
            easyQuote = new EasyQuote(id, goods_img1, goods_img2, goods_img3, goods_img4, goods_name, content, unique_id, code);
            easyQuotes.add(easyQuote);
        }
        cursor.close();
        return easyQuotes;
    }

    //简易报价 根据唯一识别id来查询商品(部分)
    public static ArrayList<EasyQuote> selectByUniquedIdforEasyQuotePart(Context context, String i) {
        easyQuote = null;
        easyQuotes.clear();
        EasyQuoteDao easyQuoteDao = new EasyQuoteDao(context);
        Cursor cursor = easyQuoteDao.selectByUniqued_id(i);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            byte[] goods_img1 = cursor.getBlob(cursor.getColumnIndex("goods_img1"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String unique_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String code = cursor.getString(cursor.getColumnIndex("code"));
            easyQuote = new EasyQuote(id, goods_img1, goods_name, unique_id, code);
            easyQuotes.add(easyQuote);
        }
        cursor.close();
        return easyQuotes;
    }


    //简易报价 根据id来查询商品
    public static EasyQuote selectEasyQuoteById(Context context, int i) {
        easyQuote = null;
        easyQuotes.clear();
        EasyQuoteDao easyQuoteDao = new EasyQuoteDao(context);
        Cursor cursor = easyQuoteDao.selectById(i);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            byte[] goods_img1 = cursor.getBlob(cursor.getColumnIndex("goods_img1"));
            byte[] goods_img2 = cursor.getBlob(cursor.getColumnIndex("goods_img2"));
            byte[] goods_img3 = cursor.getBlob(cursor.getColumnIndex("goods_img3"));
            byte[] goods_img4 = cursor.getBlob(cursor.getColumnIndex("goods_img4"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String unique_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String code = cursor.getString(cursor.getColumnIndex("code"));
            easyQuote = new EasyQuote(id, goods_img1, goods_img2, goods_img3, goods_img4, goods_name, content, unique_id, code);
            easyQuotes.add(easyQuote);
        }
        cursor.close();
        return easyQuote;

    }

    //根据id来查询简易报价表中的数据--只查询图片
    public static EasyQuote selectEasyQuotePic(Context context, int i) {
        easyQuote = null;
        easyQuotes.clear();
        EasyQuoteDao easyQuoteDao = new EasyQuoteDao(context);
        Cursor cursor = easyQuoteDao.selectById(i);
        while (cursor.moveToNext()) {
            byte[] goods_img1 = cursor.getBlob(cursor.getColumnIndex("goods_img1"));
            byte[] goods_img2 = cursor.getBlob(cursor.getColumnIndex("goods_img2"));
            byte[] goods_img3 = cursor.getBlob(cursor.getColumnIndex("goods_img3"));
            byte[] goods_img4 = cursor.getBlob(cursor.getColumnIndex("goods_img4"));
            easyQuote = new EasyQuote(goods_img1, goods_img2, goods_img3, goods_img4);
            easyQuotes.add(easyQuote);
        }
        cursor.close();
        return easyQuote;
    }

    //分页查询简易报价的数据（全部）
    public static ArrayList<EasyQuote> selectEasyQuoteByLimit(Context context, String i, int j) {
        easyQuote = null;
        easyQuotes.clear();
        EasyQuoteDao easyQuoteDao = new EasyQuoteDao(context);
        Cursor cursor = easyQuoteDao.selectByUnique_id_limit(i, j);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            byte[] goods_img1 = cursor.getBlob(cursor.getColumnIndex("goods_img1"));
            byte[] goods_img2 = cursor.getBlob(cursor.getColumnIndex("goods_img2"));
            byte[] goods_img3 = cursor.getBlob(cursor.getColumnIndex("goods_img3"));
            byte[] goods_img4 = cursor.getBlob(cursor.getColumnIndex("goods_img4"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String unique_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String code = cursor.getString(cursor.getColumnIndex("code"));
            easyQuote = new EasyQuote(id, goods_img1, goods_img2, goods_img3, goods_img4, goods_name, content, unique_id, code);
            easyQuotes.add(easyQuote);
        }
        cursor.close();
        return easyQuotes;
    }

    //分页查询简易报价的数据（查询部分）
    public static ArrayList<EasyQuote> selectEasyQuoteByLimitPart(Context context, String i, int j) {
        easyQuote = null;
        easyQuotes.clear();
        EasyQuoteDao easyQuoteDao = new EasyQuoteDao(context);
        Cursor cursor = easyQuoteDao.selectByUnique_id_limit(i, j);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            byte[] goods_img1 = cursor.getBlob(cursor.getColumnIndex("goods_img1"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String unique_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String code = cursor.getString(cursor.getColumnIndex("code"));
            easyQuote = new EasyQuote(id, goods_img1, goods_name, unique_id, code);
            easyQuotes.add(easyQuote);
        }
        cursor.close();
        return easyQuotes;
    }

    //根据多个code来查询简易报价商品
    public static ArrayList<EasyQuote> selectEasyQuoteByIds(Context context, ArrayList<String> i) {
        easyQuote = null;
        easyQuotes.clear();
        EasyQuoteDao easyQuoteDao = new EasyQuoteDao(context);
        Cursor cursor = easyQuoteDao.selectIdsByEasyQuote(i);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            byte[] goods_img1 = cursor.getBlob(cursor.getColumnIndex("goods_img1"));
            byte[] goods_img2 = cursor.getBlob(cursor.getColumnIndex("goods_img2"));
            byte[] goods_img3 = cursor.getBlob(cursor.getColumnIndex("goods_img3"));
            byte[] goods_img4 = cursor.getBlob(cursor.getColumnIndex("goods_img4"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String unique_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String code = cursor.getString(cursor.getColumnIndex("code"));
            easyQuote = new EasyQuote(id, goods_img1, goods_img2, goods_img3, goods_img4, goods_name, content, unique_id, code);
            easyQuotes.add(easyQuote);
        }
        cursor.close();
        return easyQuotes;

    }


    //简易询价 根据唯一识别id来查询商品(部分)
    public static ArrayList<EasyInquiry> selectByUniquedIdforEasyInquiryPart(Context context, String i) {
        easyInquiry = null;
        easyInquiries.clear();
        EasyInquiryDao easyInquiryDao = new EasyInquiryDao(context);
        Cursor cursor = easyInquiryDao.selectByUniqued_id(i);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            byte[] goods_img1 = cursor.getBlob(cursor.getColumnIndex("goods_img1"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String unique_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String code = cursor.getString(cursor.getColumnIndex("code"));
            easyInquiry = new EasyInquiry(id, goods_img1, goods_name, unique_id, code);
            easyInquiries.add(easyInquiry);
        }
        cursor.close();
        return easyInquiries;
    }


    //分页查询简易询价的数据（查询部分）
    public static ArrayList<EasyInquiry> selectEasyInquiryByLimitPart(Context context, String i, int j) {
        easyInquiry = null;
        easyInquiries.clear();
        EasyInquiryDao easyInquiryDao = new EasyInquiryDao(context);
        Cursor cursor = easyInquiryDao.selectByUnique_id_limit(i, j);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            byte[] goods_img1 = cursor.getBlob(cursor.getColumnIndex("goods_img1"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String unique_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String code = cursor.getString(cursor.getColumnIndex("code"));
            easyInquiry = new EasyInquiry(id, goods_img1, goods_name, unique_id, code);
            easyInquiries.add(easyInquiry);
        }
        cursor.close();
        return easyInquiries;
    }


    //根据多个code来查询简易询价商品
    public static ArrayList<EasyInquiry> selectEasyInquiryByIds(Context context, ArrayList<String> i) {
        easyInquiry = null;
        easyInquiries.clear();
        EasyInquiryDao easyInquiryDao = new EasyInquiryDao(context);
        Cursor cursor = easyInquiryDao.selectIdsByEasyQuote(i);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            byte[] goods_img1 = cursor.getBlob(cursor.getColumnIndex("goods_img1"));
            byte[] goods_img2 = cursor.getBlob(cursor.getColumnIndex("goods_img2"));
            byte[] goods_img3 = cursor.getBlob(cursor.getColumnIndex("goods_img3"));
            byte[] goods_img4 = cursor.getBlob(cursor.getColumnIndex("goods_img4"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String unique_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String code = cursor.getString(cursor.getColumnIndex("code"));
            easyInquiry = new EasyInquiry(id, goods_img1, goods_img2, goods_img3, goods_img4, goods_name, content, unique_id, code);
            easyInquiries.add(easyInquiry);
        }
        cursor.close();
        return easyInquiries;

    }



    //简易询价 根据id来查询商品
    public static EasyInquiry selectEasyInquiryById(Context context, int i) {
        easyInquiry = null;
        easyInquiries.clear();
        EasyInquiryDao easyInquiryDao = new EasyInquiryDao(context);
        Cursor cursor = easyInquiryDao.selectById(i);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            byte[] goods_img1 = cursor.getBlob(cursor.getColumnIndex("goods_img1"));
            byte[] goods_img2 = cursor.getBlob(cursor.getColumnIndex("goods_img2"));
            byte[] goods_img3 = cursor.getBlob(cursor.getColumnIndex("goods_img3"));
            byte[] goods_img4 = cursor.getBlob(cursor.getColumnIndex("goods_img4"));
            String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String unique_id = cursor.getString(cursor.getColumnIndex("uniqued_id"));
            String code = cursor.getString(cursor.getColumnIndex("code"));
            easyInquiry = new EasyInquiry(id, goods_img1, goods_img2, goods_img3, goods_img4, goods_name, content, unique_id, code);
            easyInquiries.add(easyInquiry);
        }
        cursor.close();
        return easyInquiry;

    }


    //根据id来查询简易询价表中的数据--只查询图片
    public static EasyInquiry selectEasyInquiryPic(Context context, int i) {
        easyInquiry = null;
        easyInquiries.clear();
        EasyInquiryDao easyInquiryDao = new EasyInquiryDao(context);
        Cursor cursor = easyInquiryDao.selectById(i);
        while (cursor.moveToNext()) {
            byte[] goods_img1 = cursor.getBlob(cursor.getColumnIndex("goods_img1"));
            byte[] goods_img2 = cursor.getBlob(cursor.getColumnIndex("goods_img2"));
            byte[] goods_img3 = cursor.getBlob(cursor.getColumnIndex("goods_img3"));
            byte[] goods_img4 = cursor.getBlob(cursor.getColumnIndex("goods_img4"));
            easyInquiry = new EasyInquiry(goods_img1, goods_img2, goods_img3, goods_img4);
            easyInquiries.add(easyInquiry);
        }
        cursor.close();
        return easyInquiry;
    }


}
