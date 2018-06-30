package com.baibeiyun.eazyfair.entities;

import java.io.Serializable;

/**
 * Created by rw on 2017/4/30.
 */

public class EasyQuote implements Serializable {
    private int _id;
    private byte[] goods_img1;
    private byte[] goods_img2;
    private byte[] goods_img3;
    private byte[] goods_img4;
    private String goods_name;
    private String content;
    private String uniqued_id;
    private String code;

    public EasyQuote(int _id, byte[] goods_img1, byte[] goods_img2, byte[] goods_img3, byte[] goods_img4, String goods_name, String content, String uniqued_id, String code) {
        this._id = _id;
        this.goods_img1 = goods_img1;
        this.goods_img2 = goods_img2;
        this.goods_img3 = goods_img3;
        this.goods_img4 = goods_img4;
        this.goods_name = goods_name;
        this.content = content;
        this.uniqued_id = uniqued_id;
        this.code = code;
    }

    public EasyQuote(int _id, byte[] goods_img1, byte[] goods_img2, byte[] goods_img3, byte[] goods_img4, String goods_name, String content, String uniqued_id) {
        this._id = _id;
        this.goods_img1 = goods_img1;
        this.goods_img2 = goods_img2;
        this.goods_img3 = goods_img3;
        this.goods_img4 = goods_img4;
        this.goods_name = goods_name;
        this.content = content;
        this.uniqued_id = uniqued_id;
    }

    public EasyQuote(byte[] goods_img1, byte[] goods_img2, byte[] goods_img3, byte[] goods_img4) {
        this.goods_img1 = goods_img1;
        this.goods_img2 = goods_img2;
        this.goods_img3 = goods_img3;
        this.goods_img4 = goods_img4;
    }

    public EasyQuote(int _id, byte[] goods_img1, String goods_name, String uniqued_id, String code) {
        this._id = _id;
        this.goods_img1 = goods_img1;
        this.goods_name = goods_name;
        this.uniqued_id = uniqued_id;
        this.code = code;
    }



    public EasyQuote() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public byte[] getGoods_img1() {
        return goods_img1;
    }

    public void setGoods_img1(byte[] goods_img1) {
        this.goods_img1 = goods_img1;
    }

    public byte[] getGoods_img2() {
        return goods_img2;
    }

    public void setGoods_img2(byte[] goods_img2) {
        this.goods_img2 = goods_img2;
    }

    public byte[] getGoods_img3() {
        return goods_img3;
    }

    public void setGoods_img3(byte[] goods_img3) {
        this.goods_img3 = goods_img3;
    }

    public byte[] getGoods_img4() {
        return goods_img4;
    }

    public void setGoods_img4(byte[] goods_img4) {
        this.goods_img4 = goods_img4;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUniqued_id() {
        return uniqued_id;
    }

    public void setUniqued_id(String uniqued_id) {
        this.uniqued_id = uniqued_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
