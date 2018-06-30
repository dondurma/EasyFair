package com.baibeiyun.eazyfair.entities;

import java.io.Serializable;
import java.sql.Blob;

/**
*
* @author RuanWei
* @date 2017/2/25
**/
public class MyCommodity implements Serializable{
    private int _id;
    private String serial_number;
    private String name;
    private String unit;
    private String material;
    private String color;
    private Double price;
    private String currency_variety;
    private String price_clause;
    private String price_clause_diy;
    private String remark;
    private String introduction;
    private String diy;
    private byte[] product_imgs1;
    private byte[] product_imgs2;
    private byte[] product_imgs3;
    private byte[] product_imgs4;
    private int goods_type; //商品类型(0--供应商，1--采购商)
    private String create_time;//创建时间

    private String moq;//最少起订量
    private String goods_weight;
    private String goods_weight_unit;
    private String outbox_number;
    private String outbox_size;
    private String outbox_weight;
    private String outbox_weight_unit;
    private String centerbox_number;
    private String centerbox_size;
    private String centerbox_weight;
    private String centerbox_weight_unit;

    public MyCommodity(int _id, String serial_number, String name, String unit, String material, String color, Double price, String currency_variety, String price_clause, String price_clause_diy, String remark, String introduction, String diy, byte[] product_imgs1, byte[] product_imgs2,byte[] product_imgs3,byte[] product_imgs4,int goods_type, String create_time, String moq, String goods_weight, String goods_weight_unit, String outbox_number, String outbox_size, String outbox_weight, String outbox_weight_unit, String centerbox_number, String centerbox_size, String centerbox_weight, String centerbox_weight_unit) {
        this._id = _id;
        this.serial_number = serial_number;
        this.name = name;
        this.unit = unit;
        this.material = material;
        this.color = color;
        this.price = price;
        this.currency_variety = currency_variety;
        this.price_clause = price_clause;
        this.price_clause_diy = price_clause_diy;
        this.remark = remark;
        this.introduction = introduction;
        this.diy = diy;
        this.product_imgs1 = product_imgs1;
        this.product_imgs2 = product_imgs2;
        this.product_imgs3 = product_imgs3;
        this.product_imgs4 = product_imgs4;
        this.goods_type = goods_type;
        this.create_time = create_time;
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

    public MyCommodity(int _id, String serial_number, String name, String unit, String material, String color, Double price, String currency_variety, String price_clause, String price_clause_diy, String remark, String introduction, String diy, byte[] product_imgs1, byte[] product_imgs2, byte[] product_imgs3, byte[] product_imgs4, int goods_type, String create_time, String moq, String goods_weight, String goods_weight_unit) {
        this._id = _id;
        this.serial_number = serial_number;
        this.name = name;
        this.unit = unit;
        this.material = material;
        this.color = color;
        this.price = price;
        this.currency_variety = currency_variety;
        this.price_clause = price_clause;
        this.price_clause_diy = price_clause_diy;
        this.remark = remark;
        this.introduction = introduction;
        this.diy = diy;
        this.product_imgs1 = product_imgs1;
        this.product_imgs2 = product_imgs2;
        this.product_imgs3 = product_imgs3;
        this.product_imgs4 = product_imgs4;
        this.goods_type = goods_type;
        this.create_time = create_time;
        this.moq = moq;
        this.goods_weight = goods_weight;
        this.goods_weight_unit = goods_weight_unit;
    }

    public MyCommodity(int _id, String serial_number, String name, String unit, String material, String color, Double price, String currency_variety, String price_clause, String price_clause_diy, String remark, String introduction, String diy, byte[] product_imgs1, byte[] product_imgs2, byte[] product_imgs3, byte[] product_imgs4 , int goods_type, String create_time) {
        this._id = _id;
        this.serial_number = serial_number;
        this.name = name;
        this.unit = unit;
        this.material = material;
        this.color = color;
        this.price = price;
        this.currency_variety = currency_variety;
        this.price_clause = price_clause;
        this.price_clause_diy = price_clause_diy;
        this.remark = remark;
        this.introduction = introduction;
        this.diy = diy;
        this.product_imgs1 = product_imgs1;
        this.product_imgs2 = product_imgs2;
        this.product_imgs3 = product_imgs3;
        this.product_imgs4 = product_imgs4;
        this.goods_type = goods_type;
        this.create_time = create_time;
    }

    public MyCommodity(byte[] product_imgs1, byte[] product_imgs2, byte[] product_imgs3, byte[] product_imgs4) {
        this.product_imgs1 = product_imgs1;
        this.product_imgs2 = product_imgs2;
        this.product_imgs3 = product_imgs3;
        this.product_imgs4 = product_imgs4;
    }

    public MyCommodity(int _id, String name, Double price, String currency_variety, byte[] product_imgs1) {
        this._id = _id;
        this.name = name;
        this.price = price;
        this.currency_variety = currency_variety;
        this.product_imgs1 = product_imgs1;
    }

    public MyCommodity(int _id) {
        this._id = _id;
    }

    public MyCommodity() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency_variety() {
        return currency_variety;
    }

    public void setCurrency_variety(String currency_variety) {
        this.currency_variety = currency_variety;
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

    public String getDiy() {
        return diy;
    }

    public void setDiy(String diy) {
        this.diy = diy;
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

    public int getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(int goods_type) {
        this.goods_type = goods_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
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

    public String getGoods_weight_unit() {
        return goods_weight_unit;
    }

    public void setGoods_weight_unit(String goods_weight_unit) {
        this.goods_weight_unit = goods_weight_unit;
    }
}
