package com.baibeiyun.eazyfair.entities;

import java.io.Serializable;

/**
 * 报价表的实体类
 *
 * @author RuanWei
 * @date 2017/2/20
 **/
public class MyQuote implements Serializable {
    private String _id;//报价ID
    private int commodity_id;//商品表ID
    private String serial_number;//商品货号
    private String goods_name;//商品名
    private String goods_unit;//商品单位
    private String material;//商品材质
    private String color;//商品颜色
    private double price;//商品价格
    private String currency_varitety;//货币类型
    private String price_clause;//价格条款
    private String price_clause_diy;//价格条款自定义部分
    private String remark;//商品备注
    private String introduction;//商品简介
    private String self_defined;//自定义
    private byte[] product_imgs1;// 产品图片
    private byte[] product_imgs2;// 产品图片
    private byte[] product_imgs3;// 产品图片
    private byte[] product_imgs4;// 产品图片
    private String buyer_name;//采购商名称
    private String buyer_companyname;//采购商公司名
    private String buyer_phone;//采购商电话
    private String quote_type;//报价类型
    private String quote_time;//报价时间
    private String uniqued_id;//唯一识别ID
    private String moq;//最少起订量
    private String goods_weight;//商品重量
    private String goods_weight_unit;//商品重量单位

    private String outbox_number;
    private String outbox_size;
    private String outbox_weight;
    private String outbox_weight_unit;

    private String centerbox_number;
    private String centerbox_size;
    private String centerbox_weight;
    private String centerbox_weight_unit;

    public MyQuote(String _id, int commodity_id, String serial_number, String goods_name, String goods_unit, String material, String color, double price, String currency_varitety, String price_clause, String price_clause_diy, String remark, String introduction, String self_defined, byte[] product_imgs1, byte[] product_imgs2, byte[] product_imgs3, byte[] product_imgs4, String buyer_name, String buyer_companyname, String buyer_phone, String quote_type, String quote_time, String uniqued_id, String moq, String goods_weight, String goods_weight_unit, String outbox_number, String outbox_size, String outbox_weight, String outbox_weight_unit, String centerbox_number, String centerbox_size, String centerbox_weight, String centerbox_weight_unit) {
        this._id = _id;
        this.commodity_id = commodity_id;
        this.serial_number = serial_number;
        this.goods_name = goods_name;
        this.goods_unit = goods_unit;
        this.material = material;
        this.color = color;
        this.price = price;
        this.currency_varitety = currency_varitety;
        this.price_clause = price_clause;
        this.price_clause_diy = price_clause_diy;
        this.remark = remark;
        this.introduction = introduction;
        this.self_defined = self_defined;
        this.product_imgs1 = product_imgs1;
        this.product_imgs2 = product_imgs2;
        this.product_imgs3 = product_imgs3;
        this.product_imgs4 = product_imgs4;
        this.buyer_name = buyer_name;
        this.buyer_companyname = buyer_companyname;
        this.buyer_phone = buyer_phone;
        this.quote_type = quote_type;
        this.quote_time = quote_time;
        this.uniqued_id = uniqued_id;
        this.moq = moq;
        this.goods_weight = goods_weight;
        this.goods_weight_unit = goods_weight_unit;
        this.outbox_number = outbox_number;
        this.outbox_size = outbox_size;
        this.outbox_weight = outbox_weight;
        this.outbox_weight_unit = outbox_weight_unit;
        this.centerbox_number = centerbox_number;
        this.centerbox_size = centerbox_size;
        this.centerbox_weight = centerbox_weight;
        this.centerbox_weight_unit = centerbox_weight_unit;
    }

    public MyQuote(String _id, int commodity_id, String serial_number, String goods_name, String goods_unit, String material, String color, double price, String currency_varitety, String price_clause, String price_clause_diy, String remark, String introduction, String self_defined, byte[] product_imgs1, byte[] product_imgs2, byte[] product_imgs3, byte[] product_imgs4, String buyer_name, String buyer_companyname, String buyer_phone, String quote_type, String quote_time, String uniqued_id, String moq, String goods_weight, String goods_weight_unit) {
        this._id = _id;
        this.commodity_id = commodity_id;
        this.serial_number = serial_number;
        this.goods_name = goods_name;
        this.goods_unit = goods_unit;
        this.material = material;
        this.color = color;
        this.price = price;
        this.currency_varitety = currency_varitety;
        this.price_clause = price_clause;
        this.price_clause_diy = price_clause_diy;
        this.remark = remark;
        this.introduction = introduction;
        this.self_defined = self_defined;
        this.product_imgs1 = product_imgs1;
        this.product_imgs2 = product_imgs2;
        this.product_imgs3 = product_imgs3;
        this.product_imgs4 = product_imgs4;
        this.buyer_name = buyer_name;
        this.buyer_companyname = buyer_companyname;
        this.buyer_phone = buyer_phone;
        this.quote_type = quote_type;
        this.quote_time = quote_time;
        this.uniqued_id = uniqued_id;
        this.moq = moq;
        this.goods_weight = goods_weight;
        this.goods_weight_unit = goods_weight_unit;
    }

    public MyQuote(String _id, int commodity_id, String serial_number, String goods_name, String goods_unit, String material, String color, double price, String currency_varitety, String price_clause, String price_clause_diy, String remark, String introduction, String self_defined, byte[] product_imgs1, byte[] product_imgs2, byte[] product_imgs3, byte[] product_imgs4, String buyer_name, String buyer_companyname, String buyer_phone, String quote_type, String quote_time, String uniqued_id) {
        this._id = _id;
        this.commodity_id = commodity_id;
        this.serial_number = serial_number;
        this.goods_name = goods_name;
        this.goods_unit = goods_unit;
        this.material = material;
        this.color = color;
        this.price = price;
        this.currency_varitety = currency_varitety;
        this.price_clause = price_clause;
        this.price_clause_diy = price_clause_diy;
        this.remark = remark;
        this.introduction = introduction;
        this.self_defined = self_defined;
        this.product_imgs1 = product_imgs1;
        this.product_imgs2 = product_imgs2;
        this.product_imgs3 = product_imgs3;
        this.product_imgs4 = product_imgs4;
        this.buyer_name = buyer_name;
        this.buyer_companyname = buyer_companyname;
        this.buyer_phone = buyer_phone;
        this.quote_type = quote_type;
        this.quote_time = quote_time;
        this.uniqued_id = uniqued_id;
    }

    public MyQuote(String _id, String goods_name, double price, String currency_varitety, byte[] product_imgs1, String quote_type) {
        this._id = _id;
        this.goods_name = goods_name;
        this.price = price;
        this.currency_varitety = currency_varitety;
        this.product_imgs1 = product_imgs1;
        this.quote_type = quote_type;
    }

    public MyQuote(byte[] product_imgs1, byte[] product_imgs2, byte[] product_imgs3, byte[] product_imgs4) {
        this.product_imgs1 = product_imgs1;
        this.product_imgs2 = product_imgs2;
        this.product_imgs3 = product_imgs3;
        this.product_imgs4 = product_imgs4;
    }

    public MyQuote() {
    }

    public MyQuote(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_unit() {
        return goods_unit;
    }

    public void setGoods_unit(String goods_unit) {
        this.goods_unit = goods_unit;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency_varitety() {
        return currency_varitety;
    }

    public void setCurrency_varitety(String currency_varitety) {
        this.currency_varitety = currency_varitety;
    }

    public String getPrice_clause() {
        return price_clause;
    }

    public void setPrice_clause(String price_clause) {
        this.price_clause = price_clause;
    }

    public String getPrice_clause_diy() {
        return price_clause_diy;
    }

    public void setPrice_clause_diy(String price_clause_diy) {
        this.price_clause_diy = price_clause_diy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getSelf_defined() {
        return self_defined;
    }

    public void setSelf_defined(String self_defined) {
        this.self_defined = self_defined;
    }

    public byte[] getProduct_imgs1() {
        return product_imgs1;
    }

    public void setProduct_imgs1(byte[] product_imgs1) {
        this.product_imgs1 = product_imgs1;
    }

    public byte[] getProduct_imgs2() {
        return product_imgs2;
    }

    public void setProduct_imgs2(byte[] product_imgs2) {
        this.product_imgs2 = product_imgs2;
    }

    public byte[] getProduct_imgs3() {
        return product_imgs3;
    }

    public void setProduct_imgs3(byte[] product_imgs3) {
        this.product_imgs3 = product_imgs3;
    }

    public byte[] getProduct_imgs4() {
        return product_imgs4;
    }

    public void setProduct_imgs4(byte[] product_imgs4) {
        this.product_imgs4 = product_imgs4;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getBuyer_companyname() {
        return buyer_companyname;
    }

    public void setBuyer_companyname(String buyer_companyname) {
        this.buyer_companyname = buyer_companyname;
    }

    public String getBuyer_phone() {
        return buyer_phone;
    }

    public void setBuyer_phone(String buyer_phone) {
        this.buyer_phone = buyer_phone;
    }

    public String getQuote_type() {
        return quote_type;
    }

    public void setQuote_type(String quote_type) {
        this.quote_type = quote_type;
    }

    public String getQuote_time() {
        return quote_time;
    }

    public void setQuote_time(String quote_time) {
        this.quote_time = quote_time;
    }

    public String getUniqued_id() {
        return uniqued_id;
    }

    public void setUniqued_id(String uniqued_id) {
        this.uniqued_id = uniqued_id;
    }

    public int getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(int commodity_id) {
        this.commodity_id = commodity_id;
    }


    public String getMoq() {
        return moq;
    }

    public void setMoq(String moq) {
        this.moq = moq;
    }

    public String getGoods_weight() {
        return goods_weight;
    }

    public void setGoods_weight(String goods_weight) {
        this.goods_weight = goods_weight;
    }

    public String getGoods_weight_unit() {
        return goods_weight_unit;
    }

    public void setGoods_weight_unit(String goods_weight_unit) {
        this.goods_weight_unit = goods_weight_unit;
    }

    public String getOutbox_number() {
        return outbox_number;
    }

    public void setOutbox_number(String outbox_number) {
        this.outbox_number = outbox_number;
    }

    public String getOutbox_size() {
        return outbox_size;
    }

    public void setOutbox_size(String outbox_size) {
        this.outbox_size = outbox_size;
    }

    public String getOutbox_weight() {
        return outbox_weight;
    }

    public void setOutbox_weight(String outbox_weight) {
        this.outbox_weight = outbox_weight;
    }

    public String getOutbox_weight_unit() {
        return outbox_weight_unit;
    }

    public void setOutbox_weight_unit(String outbox_weight_unit) {
        this.outbox_weight_unit = outbox_weight_unit;
    }

    public String getCenterbox_number() {
        return centerbox_number;
    }

    public void setCenterbox_number(String centerbox_number) {
        this.centerbox_number = centerbox_number;
    }

    public String getCenterbox_size() {
        return centerbox_size;
    }

    public void setCenterbox_size(String centerbox_size) {
        this.centerbox_size = centerbox_size;
    }

    public String getCenterbox_weight() {
        return centerbox_weight;
    }

    public void setCenterbox_weight(String centerbox_weight) {
        this.centerbox_weight = centerbox_weight;
    }

    public String getCenterbox_weight_unit() {
        return centerbox_weight_unit;
    }

    public void setCenterbox_weight_unit(String centerbox_weight_unit) {
        this.centerbox_weight_unit = centerbox_weight_unit;
    }
}
