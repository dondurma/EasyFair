package com.baibeiyun.eazyfair.entities;/**
 * Created by Administrator on 2017/3/24.
 */

import java.io.Serializable;
import java.util.List;

/**
 * cretae by RuanWei at 2017/3/24
 */

public class picBean implements Serializable {

    /**
     * code : 200
     * message : 展示成功！
     * urls : [{"urlbaidu":"http://www.hqsl.cn:8088/electric_motor/device/img1.jpg","urlyan":"http://www.hqsl.cn:8088/electric_motor/device/img2.jpg"}]
     */

    private String code;
    private String message;
    private List<UrlsBean> urls;

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

    public List<UrlsBean> getUrls() {
        return urls;
    }

    public void setUrls(List<UrlsBean> urls) {
        this.urls = urls;
    }

    public static class UrlsBean {
        /**
         * urlbaidu : http://www.hqsl.cn:8088/electric_motor/device/img1.jpg
         * urlyan : http://www.hqsl.cn:8088/electric_motor/device/img2.jpg
         */

        private String urlbaidu;
        private String urlyan;

        public String getUrlbaidu() {
            return urlbaidu;
        }

        public void setUrlbaidu(String urlbaidu) {
            this.urlbaidu = urlbaidu;
        }

        public String getUrlyan() {
            return urlyan;
        }

        public void setUrlyan(String urlyan) {
            this.urlyan = urlyan;
        }
    }
}
