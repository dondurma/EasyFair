package com.baibeiyun.eazyfair.entities;

import java.io.Serializable;

/**
 * cretae by RuanWei at 2017/3/8
 */

public class MyGoodsTemplet  implements Serializable{
    private int _id;
    private String serial_number;
    private String goods_name;
    private Double goods_price;
    private String goods_material;
    private String goods_color;
    private String moq;
    private String goods_weight;
    private String goods_weight_unit;
    private String outbox_number;
    private String outbox_size;
    private String outbox_weight;
    private String outbox_weight_unit;
    private String center_box_number;
    private String center_box_size;
    private String center_box_weight;
    private String center_box_weight_unit;
    private String goods_unit;
    private String currency_type;
    private String price_clause;
    private String price_clause_diy;
    private String input_time;

    public MyGoodsTemplet(int _id, String serial_number, String goods_name, Double goods_price, String goods_material, String goods_color, String moq, String goods_weight, String goods_weight_unit, String outbox_number, String outbox_size, String outbox_weight, String outbox_weight_unit, String center_box_number, String center_box_size, String center_box_weight, String center_box_weight_unit, String goods_unit, String currency_type, String price_clause, String price_clause_diy, String input_time) {
        this._id = _id;
        this.serial_number = serial_number;
        this.goods_name = goods_name;
        this.goods_price = goods_price;
        this.goods_material = goods_material;
        this.goods_color = goods_color;
        this.moq = moq;
        this.goods_weight = goods_weight;
        this.goods_weight_unit = goods_weight_unit;
        this.outbox_number = outbox_number;
        this.outbox_size = outbox_size;
        this.outbox_weight = outbox_weight;
        this.outbox_weight_unit = outbox_weight_unit;
        this.center_box_number = center_box_number;
        this.center_box_size = center_box_size;
        this.center_box_weight = center_box_weight;
        this.center_box_weight_unit = center_box_weight_unit;
        this.goods_unit = goods_unit;
        this.currency_type = currency_type;
        this.price_clause = price_clause;
        this.price_clause_diy = price_clause_diy;
        this.input_time = input_time;
    }

    public MyGoodsTemplet() {
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

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public Double getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(Double goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_material() {
        return goods_material;
    }

    public void setGoods_material(String goods_material) {
        this.goods_material = goods_material;
    }

    public String getGoods_color() {
        return goods_color;
    }

    public void setGoods_color(String goods_color) {
        this.goods_color = goods_color;
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

    public String getCenter_box_number() {
        return center_box_number;
    }

    public void setCenter_box_number(String center_box_number) {
        this.center_box_number = center_box_number;
    }

    public String getCenter_box_size() {
        return center_box_size;
    }

    public void setCenter_box_size(String center_box_size) {
        this.center_box_size = center_box_size;
    }

    public String getCenter_box_weight() {
        return center_box_weight;
    }

    public void setCenter_box_weight(String center_box_weight) {
        this.center_box_weight = center_box_weight;
    }

    public String getCenter_box_weight_unit() {
        return center_box_weight_unit;
    }

    public void setCenter_box_weight_unit(String center_box_weight_unit) {
        this.center_box_weight_unit = center_box_weight_unit;
    }

    public String getGoods_unit() {
        return goods_unit;
    }

    public void setGoods_unit(String goods_unit) {
        this.goods_unit = goods_unit;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
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

    public String getInput_time() {
        return input_time;
    }

    public void setInput_time(String input_time) {
        this.input_time = input_time;
    }
}
