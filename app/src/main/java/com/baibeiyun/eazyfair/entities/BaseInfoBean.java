package com.baibeiyun.eazyfair.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/29.
 */
public class BaseInfoBean implements Serializable {

    /**
     * code : 200
     * message : 查询成功！
     * userInfo : {"chAddress":"fsd","chCompanyName":"","chCompanySize":"fsdf","chContactPerson":"fsd","chIndustryType":"fds","chJobTitle":"fds","chMainBussiness":"sdd","chMainProduct":"fd","contactPhone":"8566","email":"vhj","enAddress":"23112","enCompanyName":"231","enCompanySize":"3213","enContactPerson":"213","enIndustryType":"3213","enJobTitle":"312","enMainBussiness":"23213","enMainProduct":"21321","logo":"/images/444ddc19-7779-4336-8ebe-393b53788a1e.jpg","systemLanguage":"0","vipLevel":"10","vipType":"皇冠会员","website":"2132bbnj"}
     */

    private String code;
    private String message;
    /**
     * chAddress : fsd
     * chCompanyName :
     * chCompanySize : fsdf
     * chContactPerson : fsd
     * chIndustryType : fds
     * chJobTitle : fds
     * chMainBussiness : sdd
     * chMainProduct : fd
     * contactPhone : 8566
     * email : vhj
     * enAddress : 23112
     * enCompanyName : 231
     * enCompanySize : 3213
     * enContactPerson : 213
     * enIndustryType : 3213
     * enJobTitle : 312
     * enMainBussiness : 23213
     * enMainProduct : 21321
     * logo : /images/444ddc19-7779-4336-8ebe-393b53788a1e.jpg
     * systemLanguage : 0
     * vipLevel : 10
     * vipType : 皇冠会员
     * website : 2132bbnj
     */

    private UserInfoBean userInfo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public static class UserInfoBean {
        private String chAddress;
        private String chCompanyName;
        private String chCompanySize;
        private String chContactPerson;
        private String chIndustryType;
        private String chJobTitle;
        private String chMainBussiness;
        private String chMainProduct;
        private String contactPhone;
        private String email;
        private String enAddress;
        private String enCompanyName;
        private String enCompanySize;
        private String enContactPerson;
        private String enIndustryType;
        private String enJobTitle;
        private String enMainBussiness;
        private String enMainProduct;
        private String logo;
        private String systemLanguage;
        private String vipLevel;
        private String vipType;
        private String website;
        private String uniqueId;

        public String getUniqueId() {
            return uniqueId;
        }

        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
        }

        public String getChAddress() {
            return chAddress;
        }

        public void setChAddress(String chAddress) {
            this.chAddress = chAddress;
        }

        public String getChCompanyName() {
            return chCompanyName;
        }

        public void setChCompanyName(String chCompanyName) {
            this.chCompanyName = chCompanyName;
        }

        public String getChCompanySize() {
            return chCompanySize;
        }

        public void setChCompanySize(String chCompanySize) {
            this.chCompanySize = chCompanySize;
        }

        public String getChContactPerson() {
            return chContactPerson;
        }

        public void setChContactPerson(String chContactPerson) {
            this.chContactPerson = chContactPerson;
        }

        public String getChIndustryType() {
            return chIndustryType;
        }

        public void setChIndustryType(String chIndustryType) {
            this.chIndustryType = chIndustryType;
        }

        public String getChJobTitle() {
            return chJobTitle;
        }

        public void setChJobTitle(String chJobTitle) {
            this.chJobTitle = chJobTitle;
        }

        public String getChMainBussiness() {
            return chMainBussiness;
        }

        public void setChMainBussiness(String chMainBussiness) {
            this.chMainBussiness = chMainBussiness;
        }

        public String getChMainProduct() {
            return chMainProduct;
        }

        public void setChMainProduct(String chMainProduct) {
            this.chMainProduct = chMainProduct;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEnAddress() {
            return enAddress;
        }

        public void setEnAddress(String enAddress) {
            this.enAddress = enAddress;
        }

        public String getEnCompanyName() {
            return enCompanyName;
        }

        public void setEnCompanyName(String enCompanyName) {
            this.enCompanyName = enCompanyName;
        }

        public String getEnCompanySize() {
            return enCompanySize;
        }

        public void setEnCompanySize(String enCompanySize) {
            this.enCompanySize = enCompanySize;
        }

        public String getEnContactPerson() {
            return enContactPerson;
        }

        public void setEnContactPerson(String enContactPerson) {
            this.enContactPerson = enContactPerson;
        }

        public String getEnIndustryType() {
            return enIndustryType;
        }

        public void setEnIndustryType(String enIndustryType) {
            this.enIndustryType = enIndustryType;
        }

        public String getEnJobTitle() {
            return enJobTitle;
        }

        public void setEnJobTitle(String enJobTitle) {
            this.enJobTitle = enJobTitle;
        }

        public String getEnMainBussiness() {
            return enMainBussiness;
        }

        public void setEnMainBussiness(String enMainBussiness) {
            this.enMainBussiness = enMainBussiness;
        }

        public String getEnMainProduct() {
            return enMainProduct;
        }

        public void setEnMainProduct(String enMainProduct) {
            this.enMainProduct = enMainProduct;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getSystemLanguage() {
            return systemLanguage;
        }

        public void setSystemLanguage(String systemLanguage) {
            this.systemLanguage = systemLanguage;
        }

        public String getVipLevel() {
            return vipLevel;
        }

        public void setVipLevel(String vipLevel) {
            this.vipLevel = vipLevel;
        }

        public String getVipType() {
            return vipType;
        }

        public void setVipType(String vipType) {
            this.vipType = vipType;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }
    }
}
