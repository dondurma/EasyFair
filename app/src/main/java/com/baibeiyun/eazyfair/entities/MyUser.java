package com.baibeiyun.eazyfair.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/21.
 */
public class MyUser implements Serializable {
    private int _id;//主键_id
    private int user_type;//用户类型
    private int system_language;//系统语言

    private byte[] ch_portrait;//公司logo
    private String ch_company_name;//中文公司名
    private String ch_industry_type;//中文行业类型
    private String ch_company_size;//公司规模
    private String ch_main_business;//中文主营业务
    private String ch_main_product;//中文主要产品
    private String ch_official_website;//官网
    private String ch_address;//中文地址
    private String ch_contact;//中文联系人
    private String ch_job;//中文职位
    private String ch_telephone;//电话
    private String ch_email;//email

    private String unique_id;//唯一id

    //英文部分
    private String en_company_name;
    private String en_industry_type;
    private String en_company_size;
    private String en_main_bussiness;
    private String en_main_product;
    private String en_address;
    private String en_contact;
    private String en_job;


    public MyUser(int _id, int user_type, int system_language, byte[] ch_portrait, String ch_company_name, String ch_industry_type, String ch_company_size, String ch_main_business, String ch_main_product, String ch_official_website, String ch_address, String ch_contact, String ch_job, String ch_telephone, String ch_email, String unique_id, String en_company_name, String en_industry_type, String en_company_size, String en_main_bussiness, String en_main_product, String en_address, String en_contact, String en_job) {
        this._id = _id;
        this.user_type = user_type;
        this.system_language = system_language;
        this.ch_portrait = ch_portrait;
        this.ch_company_name = ch_company_name;
        this.ch_industry_type = ch_industry_type;
        this.ch_company_size = ch_company_size;
        this.ch_main_business = ch_main_business;
        this.ch_main_product = ch_main_product;
        this.ch_official_website = ch_official_website;
        this.ch_address = ch_address;
        this.ch_contact = ch_contact;
        this.ch_job = ch_job;
        this.ch_telephone = ch_telephone;
        this.ch_email = ch_email;
        this.unique_id = unique_id;
        this.en_company_name = en_company_name;
        this.en_industry_type = en_industry_type;
        this.en_company_size = en_company_size;
        this.en_main_bussiness = en_main_bussiness;
        this.en_main_product = en_main_product;
        this.en_address = en_address;
        this.en_contact = en_contact;
        this.en_job = en_job;
    }

    public MyUser(int _id, String ch_company_name, String ch_contact, String unique_id, String en_company_name, String en_contact) {
        this._id = _id;
        this.ch_company_name = ch_company_name;
        this.ch_contact = ch_contact;
        this.unique_id = unique_id;
        this.en_company_name = en_company_name;
        this.en_contact = en_contact;
    }

    public MyUser(String ch_company_name, String ch_contact) {
        this.ch_company_name = ch_company_name;
        this.ch_contact = ch_contact;

    }

    public MyUser() {
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getSystem_language() {
        return system_language;
    }

    public void setSystem_language(int system_language) {
        this.system_language = system_language;
    }

    public byte[] getCh_portrait() {
        return ch_portrait;
    }

    public void setCh_portrait(byte[] ch_portrait) {
        this.ch_portrait = ch_portrait;
    }

    public String getCh_company_name() {
        return ch_company_name;
    }

    public void setCh_company_name(String ch_company_name) {
        this.ch_company_name = ch_company_name;
    }

    public String getCh_industry_type() {
        return ch_industry_type;
    }

    public void setCh_industry_type(String ch_industry_type) {
        this.ch_industry_type = ch_industry_type;
    }

    public String getCh_company_size() {
        return ch_company_size;
    }

    public void setCh_company_size(String ch_company_size) {
        this.ch_company_size = ch_company_size;
    }

    public String getCh_main_business() {
        return ch_main_business;
    }

    public void setCh_main_business(String ch_main_business) {
        this.ch_main_business = ch_main_business;
    }

    public String getCh_main_product() {
        return ch_main_product;
    }

    public void setCh_main_product(String ch_main_product) {
        this.ch_main_product = ch_main_product;
    }

    public String getCh_official_website() {
        return ch_official_website;
    }

    public void setCh_official_website(String ch_official_website) {
        this.ch_official_website = ch_official_website;
    }

    public String getCh_address() {
        return ch_address;
    }

    public void setCh_address(String ch_address) {
        this.ch_address = ch_address;
    }

    public String getCh_contact() {
        return ch_contact;
    }

    public void setCh_contact(String ch_contact) {
        this.ch_contact = ch_contact;
    }

    public String getCh_job() {
        return ch_job;
    }

    public void setCh_job(String ch_job) {
        this.ch_job = ch_job;
    }

    public String getCh_telephone() {
        return ch_telephone;
    }

    public void setCh_telephone(String ch_telephone) {
        this.ch_telephone = ch_telephone;
    }

    public String getCh_email() {
        return ch_email;
    }

    public void setCh_email(String ch_email) {
        this.ch_email = ch_email;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }


    public String getEn_company_name() {
        return en_company_name;
    }

    public void setEn_company_name(String en_company_name) {
        this.en_company_name = en_company_name;
    }

    public String getEn_industry_type() {
        return en_industry_type;
    }

    public void setEn_industry_type(String en_industry_type) {
        this.en_industry_type = en_industry_type;
    }

    public String getEn_company_size() {
        return en_company_size;
    }

    public void setEn_company_size(String en_company_size) {
        this.en_company_size = en_company_size;
    }

    public String getEn_main_bussiness() {
        return en_main_bussiness;
    }

    public void setEn_main_bussiness(String en_main_bussiness) {
        this.en_main_bussiness = en_main_bussiness;
    }

    public String getEn_main_product() {
        return en_main_product;
    }

    public void setEn_main_product(String en_main_product) {
        this.en_main_product = en_main_product;
    }

    public String getEn_address() {
        return en_address;
    }

    public void setEn_address(String en_address) {
        this.en_address = en_address;
    }

    public String getEn_contact() {
        return en_contact;
    }

    public void setEn_contact(String en_contact) {
        this.en_contact = en_contact;
    }

    public String getEn_job() {
        return en_job;
    }

    public void setEn_job(String en_job) {
        this.en_job = en_job;
    }
}
