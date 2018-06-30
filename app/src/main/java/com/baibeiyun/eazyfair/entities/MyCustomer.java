package com.baibeiyun.eazyfair.entities;

import java.io.Serializable;

/**
 * 我是供应商--我的客户
 *
 * @author RuanWei
 * @date 2017/1/6 修改
 **/

public class MyCustomer implements Serializable {
    private int _id;
    private String name;
    private String company_name;
    private String phone;
    private String email;
    private String company_address;
    private String industry_type;
    private String job_position;
    private byte[] company_logo;
    private String customer_ohter;
    private String create_time;
    private int customer_type;
    private String department;
    private String fax;
    private String pager;
    private String web;
    private String postcode;
    private String icq;
    private String unique_id;
    private byte[] background_pic;

    public MyCustomer(int _id, String name, String company_name, String phone, String email, String commpany_address, String industry_type, String job_position, byte[] company_logo, String customer_ohter, String create_time, int customer_type, String department, String fax, String pager, String web, String postcode, String icq, String unique_id, byte[] background_pic) {
        this._id = _id;
        this.name = name;
        this.company_name = company_name;
        this.phone = phone;
        this.email = email;
        this.company_address = commpany_address;
        this.industry_type = industry_type;
        this.job_position = job_position;
        this.company_logo = company_logo;
        this.customer_ohter = customer_ohter;
        this.create_time = create_time;
        this.customer_type = customer_type;
        this.department = department;
        this.fax = fax;
        this.pager = pager;
        this.web = web;
        this.postcode = postcode;
        this.icq = icq;
        this.unique_id = unique_id;
        this.background_pic = background_pic;
    }

    public MyCustomer(int _id, String name, String company_name, String phone, String email, String commpany_address, String industry_type, String job_position, byte[] company_logo, String customer_ohter, String create_time, int customer_type, String department, String fax, String pager, String web, String postcode, String icq, String unique_id) {
        this._id = _id;
        this.name = name;
        this.company_name = company_name;
        this.phone = phone;
        this.email = email;
        this.company_address = commpany_address;
        this.industry_type = industry_type;
        this.job_position = job_position;
        this.company_logo = company_logo;
        this.customer_ohter = customer_ohter;
        this.create_time = create_time;
        this.customer_type = customer_type;
        this.department = department;
        this.fax = fax;
        this.pager = pager;
        this.web = web;
        this.postcode = postcode;
        this.icq = icq;
        this.unique_id = unique_id;
    }

    public MyCustomer(int _id, String name, String company_name, String phone, String email, String commpany_address, String industry_type, String job_position, byte[] company_logo, String customer_ohter, String create_time, int customer_type, String unique_id) {
        this._id = _id;
        this.name = name;
        this.company_name = company_name;
        this.phone = phone;
        this.email = email;
        this.company_address = commpany_address;
        this.industry_type = industry_type;
        this.job_position = job_position;
        this.company_logo = company_logo;
        this.customer_ohter = customer_ohter;
        this.create_time = create_time;
        this.customer_type = customer_type;
        this.unique_id = unique_id;
    }

    public MyCustomer(int _id, String name, String company_name, String phone, String email, String company_address, String industry_type, String job_position, byte[] company_logo, String customer_ohter, String fax, String web, String icq, String create_time, int customer_type, String postcode, String unique_id, byte[] background_pic) {
        this._id = _id;
        this.name = name;
        this.company_name = company_name;
        this.phone = phone;
        this.email = email;
        this.company_address = company_address;
        this.industry_type = industry_type;
        this.job_position = job_position;
        this.company_logo = company_logo;
        this.customer_ohter = customer_ohter;
        this.fax = fax;
        this.web = web;
        this.icq = icq;
        this.create_time = create_time;
        this.customer_type = customer_type;
        this.postcode = postcode;
        this.unique_id = unique_id;
        this.background_pic = background_pic;
    }

    public MyCustomer(int _id, String name, String company_name, byte[] company_logo, String unique_id) {
        this._id = _id;
        this.name = name;
        this.company_name = company_name;
        this.company_logo = company_logo;
        this.unique_id = unique_id;
    }

    public MyCustomer(int _id, String name, String company_name, String unique_id) {
        this._id = _id;
        this.name = name;
        this.company_name = company_name;
        this.unique_id = unique_id;
    }

    public MyCustomer(int _id, String name, String company_name, byte[] company_logo) {
        this._id = _id;
        this.name = name;
        this.company_name = company_name;
        this.company_logo = company_logo;
    }

    public MyCustomer(int _id, String company_name, byte[] company_logo) {
        this._id = _id;
        this.company_name = company_name;
        this.company_logo = company_logo;
    }

    public MyCustomer(String name, String company_name) {
        this.name = name;
        this.company_name = company_name;
    }

    public MyCustomer(int _id, byte[] background_pic) {
        this._id = _id;
        this.background_pic = background_pic;
    }
    public MyCustomer() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getIndustry_type() {
        return industry_type;
    }

    public void setIndustry_type(String industry_type) {
        this.industry_type = industry_type;
    }

    public String getJob_position() {
        return job_position;
    }

    public void setJob_position(String job_position) {
        this.job_position = job_position;
    }

    public byte[] getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(byte[] company_logo) {
        this.company_logo = company_logo;
    }

    public void setBackground_pic(byte[] background_pic) {
        this.background_pic = background_pic;
    }

    public String getCustomer_ohter() {
        return customer_ohter;
    }

    public void setCustomer_ohter(String customer_ohter) {
        this.customer_ohter = customer_ohter;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(int customer_type) {
        this.customer_type = customer_type;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPager() {
        return pager;
    }

    public void setPager(String pager) {
        this.pager = pager;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getIcq() {
        return icq;
    }

    public void setIcq(String icq) {
        this.icq = icq;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public byte[] getBackground_pic() {
        return background_pic;
    }
}
