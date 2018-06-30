package com.baibeiyun.eazyfair.entities;

import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */
public class BussinessGY {

    /**
     * message : 成功！
     * businessOpportunityList : [{"productIntroduction":"好好好好好好好好好好","id":10,"firstIndustryType":"电子及家电类","sendMode":"email","pubDate":1483017124000,"publisherId":9,"priceRange":"100元-200元","secondIndustryType":"电子电气产品","fourthIndustryType":"复印机","thirdIndustryType":"商务自动化设备","type":"2","productName":"复印机"},{"productIntroduction":"好好好好好好好好好好","id":9,"firstIndustryType":"电子及家电类","sendMode":"email","pubDate":1482930724000,"publisherId":9,"priceRange":"100元-200元","secondIndustryType":"电子电气产品","fourthIndustryType":"复印机","thirdIndustryType":"商务自动化设备","type":"2","productName":"复印机"},{"productIntroduction":"好好好好好好好好好好","id":8,"firstIndustryType":"电子及家电类","sendMode":"email","pubDate":1482844324000,"publisherId":9,"priceRange":"100元-200元","secondIndustryType":"电子电气产品","fourthIndustryType":"复印机","thirdIndustryType":"商务自动化设备","type":"2","productName":"复印机"},{"productIntroduction":"好好好好好好好好好好","id":7,"firstIndustryType":"电子及家电类","sendMode":"email","pubDate":1482757924000,"publisherId":9,"priceRange":"100元-200元","secondIndustryType":"电子电气产品","fourthIndustryType":"复印机","thirdIndustryType":"商务自动化设备","type":"2","productName":"复印机"},{"productIntroduction":"好好好好好好好好好好","id":6,"firstIndustryType":"电子及家电类","sendMode":"email","pubDate":1482671524000,"publisherId":9,"priceRange":"100元-200元","secondIndustryType":"电子电气产品","fourthIndustryType":"复印机","thirdIndustryType":"商务自动化设备","type":"2","productName":"复印机"}]
     * code : 200
     */

    private String message;
    private String code;
    /**
     * productIntroduction :
     * id : 10
     * firstIndustryType : 电子及家电类
     * sendMode : email
     * pubDate : 1483017124000
     * publisherId : 9
     * priceRange : 100元-200元
     * secondIndustryType : 电子电气产品
     * fourthIndustryType : 复印机
     * thirdIndustryType : 商务自动化设备
     * type : 2
     * productName : 复印机
     */

    private List<BusinessOpportunityListBean> businessOpportunityList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<BusinessOpportunityListBean> getBusinessOpportunityList() {
        return businessOpportunityList;
    }

    public void setBusinessOpportunityList(List<BusinessOpportunityListBean> businessOpportunityList) {
        this.businessOpportunityList = businessOpportunityList;
    }

    public static class BusinessOpportunityListBean {
        private String productIntroduction;
        private int id;
        private String firstIndustryType;
        private String sendMode;
        private long pubDate;
        private int publisherId;
        private String priceRange;
        private String secondIndustryType;
        private String fourthIndustryType;
        private String thirdIndustryType;
        private String type;
        private String productName;

        public String getProductIntroduction() {
            return productIntroduction;
        }

        public void setProductIntroduction(String productIntroduction) {
            this.productIntroduction = productIntroduction;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFirstIndustryType() {
            return firstIndustryType;
        }

        public void setFirstIndustryType(String firstIndustryType) {
            this.firstIndustryType = firstIndustryType;
        }

        public String getSendMode() {
            return sendMode;
        }

        public void setSendMode(String sendMode) {
            this.sendMode = sendMode;
        }

        public long getPubDate() {
            return pubDate;
        }

        public void setPubDate(long pubDate) {
            this.pubDate = pubDate;
        }

        public int getPublisherId() {
            return publisherId;
        }

        public void setPublisherId(int publisherId) {
            this.publisherId = publisherId;
        }

        public String getPriceRange() {
            return priceRange;
        }

        public void setPriceRange(String priceRange) {
            this.priceRange = priceRange;
        }

        public String getSecondIndustryType() {
            return secondIndustryType;
        }

        public void setSecondIndustryType(String secondIndustryType) {
            this.secondIndustryType = secondIndustryType;
        }

        public String getFourthIndustryType() {
            return fourthIndustryType;
        }

        public void setFourthIndustryType(String fourthIndustryType) {
            this.fourthIndustryType = fourthIndustryType;
        }

        public String getThirdIndustryType() {
            return thirdIndustryType;
        }

        public void setThirdIndustryType(String thirdIndustryType) {
            this.thirdIndustryType = thirdIndustryType;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
    }
}
