package com.baibeiyun.eazyfair.entities;

import java.util.List;

/**
 * Created by Administrator on 2016/12/29.
 */
public class ExhibitionInfoBean {

    /**
     * code : 200
     * exhibitionList : [{"address_en":"维多尼亚","chargeStandard":"维多尼亚","city":"维多尼亚","contact":"维多尼亚","endTime":1481166488000,"id":2,"introduction":"维多尼亚","organiser":"维多尼亚","pavilionName":"维多尼亚","range":"维多尼亚","startTime":1478574489000,"state":"","telephone":"维多尼亚","title":"维多尼亚","type":"维多尼亚"},{"address_en":"维多尼亚","chargeStandard":"维多尼亚","city":"维多尼亚","contact":"维多尼亚","endTime":1481166488000,"id":1,"introduction":"维多尼亚","organiser":"维多尼亚","pavilionName":"维多尼亚","range":"维多尼亚","startTime":1478574488000,"telephone":"维多尼亚","title":"维多尼亚","type":"维多尼亚"},{"id":3,"startTime":1478574481000}]
     * message : 成功！
     */

    private String code;
    private String message;
    /**
     * address_en : 维多尼亚
     * chargeStandard : 维多尼亚
     * city : 维多尼亚
     * contact : 维多尼亚
     * endTime : 1481166488000
     * id : 2
     * introduction : 维多尼亚
     * organiser : 维多尼亚
     * pavilionName : 维多尼亚
     * range : 维多尼亚
     * startTime : 1478574489000
     * state :
     * telephone : 维多尼亚
     * title : 维多尼亚
     * type : 维多尼亚
     */

    private List<ExhibitionListBean> exhibitionList;

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

    public List<ExhibitionListBean> getExhibitionList() {
        return exhibitionList;
    }

    public void setExhibitionList(List<ExhibitionListBean> exhibitionList) {
        this.exhibitionList = exhibitionList;
    }

    public static class ExhibitionListBean {
        private String address;
        private String chargeStandard;
        private String city;
        private String contact;
        private long endTime;
        private int id;
        private String introduction;
        private String organiser;
        private String pavilionName;
        private String range;
        private long startTime;
        private String state;
        private String telephone;
        private String title;
        private String type;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getChargeStandard() {
            return chargeStandard;
        }

        public void setChargeStandard(String chargeStandard) {
            this.chargeStandard = chargeStandard;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getOrganiser() {
            return organiser;
        }

        public void setOrganiser(String organiser) {
            this.organiser = organiser;
        }

        public String getPavilionName() {
            return pavilionName;
        }

        public void setPavilionName(String pavilionName) {
            this.pavilionName = pavilionName;
        }

        public String getRange() {
            return range;
        }

        public void setRange(String range) {
            this.range = range;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
