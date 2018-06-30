package com.baibeiyun.eazyfair.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/9/29.
 */
public class MyDataBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "eazyfair.txt";

    public MyDataBaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //商品表
        String commodity_sql = "create table commodity(_id Integer primary key,serial_number varchar(255),name varchar(255),unit varchar(255),material varchar(255),color varchar(255),price double(10,2),currency_variety varchar(255),price_clause varchar(255),price_clause_diy varchar(255),remark varchar(255),introduction varchar(2000),diy varchar(2000),product_imgs1 BLOB,product_imgs2 BLOB,product_imgs3 BLOB,product_imgs4 BLOB,goods_type Integer,create_time varchar(255),moq varchar(55),goods_weight varchar(55),goods_weight_unit varchar(55),outbox_number varchar(55),outbox_size varchar(55),outbox_weight varchar(55),outbox_weight_unit varchar(55),centerbox_number varhchar(55),centerbox_size varchar(55),centerbox_weight varchar(55),centerbox_weight_unit varchar(55))";

        String customer_sql = "create table customer(_id Integer primary key,name varchar(20),company_name varchar(50),phone varchar(20),email varchar(20),company_address varchar(50),industry_type varchar(50),job_position varchar(50),company_logo BLOB,customer_ohter varchar(100),create_time varchar(50),customer_type integer(11),department varchar(255),fax varchar(255),pager varchar(255),web varchar(255),postcode varchar(255),icq varchar(255),unique_id varchar(255),background_pic BLOB)";

        //个人信息表-------------------------主键id--------------------用户类型------------系统语言-------------------logo-------------------------公司名--------------------------行业类型-------------------------公司规模-------------------------主营业务---------------------主要产品----------------------------官网----------------------------------公司地址-----------------联系人---------------------职位------------------联系电话----------------------email-------------------客户唯一id----------------英文公司名----------------------英文行业类型---------------------英文公司规模--------------------英文主营业务-----------------------英文主要产品--------------------英文公司地址--------------英文联系人------------------英文职位--------------
        String user_sql = "create table user(_id Integer primary key,user_type Integer,system_language Integer,ch_portrait BLOB,ch_company_name varchar(255),ch_industry_type varchar(255),ch_company_size varchar(255),ch_main_business varchar(255),ch_main_product varchar(255),ch_official_website varchar(255),ch_address varchar(255),ch_contact varchar(255),ch_job varchar(255),ch_telephone varchar(255),ch_email varchar(255),unique_id varchar(255),en_company_name varchar(255),en_industry_type varchar(255),en_company_size varchar(255),en_main_bussiness varchar(255),en_main_product varchar(255),en_address varchar(255),en_contact varchar(255),en_job varchar(255))";

        //报价表--------------------------------报价id--------------商品表ID--------------商品货号--------------------商品名称-------------------商品单位------------------商品材质--------------商品颜色-----------商品价格-----------货币类型------------------------价格条款------------------价格条款自定义----------------备注------------------商品介绍------------------自定义---------------------产品图片-----------------------------------------------------------------------------------采购商名称----------------采购商公司名-------------------------采购商电话------------------报价类型------------报价时间-----------------唯一识别ID---------------最少起订量-------商品重量-------------------商品重量单位
        String quote_sql = "create table quote(_id varchar(255),commodity_id Integer,serial_number varchar(255),goods_name varchar(255),goods_unit varchar(255),material varchar(255),color varchar(255),price double(10,2),currency_varitety varchar(255),price_clause varchar(255),price_clause_diy varchar(255),remark varchar(255),introduction varchar(255),self_defined varchar(255),product_imgs1 BLOB,product_imgs2 BLOB,product_imgs3 BLOB,product_imgs4 BLOB,buyer_name varchar(255),buyer_companyname varchar(255),buyer_phone varchar(255),quote_type Integer,quote_time varchar(255),uniqued_id varchar(255),moq varchar(55),goods_weight varchar(55),goods_weight_unit varchar(55),outbox_number varchar(55),outbox_size varchar(55),outbox_weight varchar(55),outbox_weight_unit varchar(55),centerbox_number varhchar(55),centerbox_size varchar(55),centerbox_weight varchar(55),centerbox_weight_unit varchar(55))";

        //询价表
        String inquiry_sql = "create table inquiry(_id varchar(255),commodity_id Integer,serial_number varchar(255),goods_name varchar(255),goods_unit varchar(255),material varchar(255),color varchar(255),price double(10,2),currency_varitety varchar(255),price_clause varchar(255),price_clause_diy varchar(255),remark varchar(255),introduction varchar(255),self_defined varchar(255),product_imgs1 BLOB,product_imgs2 BLOB,product_imgs3 BLOB,product_imgs4 BLOB,supplier_name varchar(255),supplier_companyname varchar(255),supplier_phone varchar(255),inquiry_type Integer,inquiry_time varchar(255),uniqued_id varchar(255),moq varchar(55),goods_weight varchar(55),goods_weight_unit varchar(55),outbox_number varchar(55),outbox_size varchar(55),outbox_weight varchar(55),outbox_weight_unit varchar(55),centerbox_number varhchar(55),centerbox_size varchar(55),centerbox_weight varchar(55),centerbox_weight_unit varchar(55))";

        //中英文切换
        String switch_sql = "create table switch_language(id varchar(55) primary key,tag varchar(55))";

        //商品模板
        String goods_templet_sql = "create table goods_templet(_id Integer primary key,serial_number varchar(55),goods_name varchar(55),goods_price Double(10,2),goods_material varchar(55),goods_color varchar(55),moq varchar(55),goods_weight varchar(55),goods_weight_unit varchar(55),outbox_number varchar(55),outbox_size varchar(55),outbox_weight varchar(55),outbox_weight_unit varchar(55),center_box_number varchar(55),center_box_size varchar(55),center_box_weight varchar(55),center_box_weight_unit varchar(55),goods_unit varchar(55),currency_type varchar(55),price_clause varchar(55),price_clause_diy varchar(55),input_time varchar(255))";
        //简易报价
        String easy_quote_sql = "create table easy_quote(_id Integer primary key,goods_img1 BLOB,goods_img2 BLOB,goods_img3 BLOB,goods_img4 BLOB,goods_name varchar(255),content  varchar(2000),uniqued_id varchar(255),code varchar(255))";
        //简易询价
        String easy_inquiry_sql = "create table easy_inquiry(_id Integer primary key,goods_img1 BLOB,goods_img2 BLOB,goods_img3 BLOB,goods_img4 BLOB,goods_name varchar(255),content  varchar(2000),uniqued_id varchar(255),code varchar(255))";

        //执行sql语句
        db.execSQL(commodity_sql);
        db.execSQL(customer_sql);
        db.execSQL(user_sql);
        db.execSQL(quote_sql);
        db.execSQL(inquiry_sql);
        db.execSQL(goods_templet_sql);
        db.execSQL(switch_sql);
        db.execSQL(easy_quote_sql);
        db.execSQL(easy_inquiry_sql);
        //初始化数据
        db.execSQL("insert into switch_language values(?,?);", new Object[]{"easyfair", "ch"});

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
