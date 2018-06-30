package com.baibeiyun.eazyfair.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 */
public class CardBean implements Serializable{

    /**
     * status : OK
     * data : {"er":"http://121.40.44.191:9099/enginefile/scan/20161226/1482722064054_c37e9605-a790-449d-a652-d7b93927fdb9_79c3a238a4c343d884773ceada8b6bc8_er.jpg","dw":["name:643,1053;781,1117","company:211,1410;984,1503","addr:210,1554;1222,1640","addr:207,1632;1149,1716","mobile:202,1734;981,1865","email:199,1870;1355,1955","website:469,1988;983,2050"],"f":[{"n":"name","v":"林春"},{"n":"company","v":"杭州讯数科技有限公司"},{"n":"department"},{"n":"jobtitle"},{"n":"tel_main"},{"n":"tel_mobile","v":"15558127688"},{"n":"tel_home"},{"n":"tel_inter"},{"n":"fax"},{"n":"pager"},{"n":"web","v":"www.dtstack.com"},{"n":"email","v":"yanyan.miao@dtstacck.com"},{"n":"address_en","v":["浙江省杭州市西湖区紫展街176号","杭州互联网创新创业园之号楼sF"]},{"n":"postcode"},{"n":"ICQ"}]}
     */

    private String status;
    /**
     * er : http://121.40.44.191:9099/enginefile/scan/20161226/1482722064054_c37e9605-a790-449d-a652-d7b93927fdb9_79c3a238a4c343d884773ceada8b6bc8_er.jpg
     * dw : ["name:643,1053;781,1117","company:211,1410;984,1503","addr:210,1554;1222,1640","addr:207,1632;1149,1716","mobile:202,1734;981,1865","email:199,1870;1355,1955","website:469,1988;983,2050"]
     * f : [{"n":"name","v":"林春"},{"n":"company","v":"杭州讯数科技有限公司"},{"n":"department"},{"n":"jobtitle"},{"n":"tel_main"},{"n":"tel_mobile","v":"15558127688"},{"n":"tel_home"},{"n":"tel_inter"},{"n":"fax"},{"n":"pager"},{"n":"web","v":"www.dtstack.com"},{"n":"email","v":"yanyan.miao@dtstacck.com"},{"n":"address_en","v":["浙江省杭州市西湖区紫展街176号","杭州互联网创新创业园之号楼sF"]},{"n":"postcode"},{"n":"ICQ"}]
     */

    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String er;
        private List<String> dw;
        /**
         * n : name
         * v : 林春
         */

        private List<FBean> f;

        public String getEr() {
            return er;
        }

        public void setEr(String er) {
            this.er = er;
        }

        public List<String> getDw() {
            return dw;
        }

        public void setDw(List<String> dw) {
            this.dw = dw;
        }

        public List<FBean> getF() {
            return f;
        }

        public void setF(List<FBean> f) {
            this.f = f;
        }

        public static class FBean {
            private String n;
            private Object v;

            public String getN() {
                return n;
            }

            public void setN(String n) {
                this.n = n;
            }

            public Object getV() {
                return v;
            }

            public void setV(Object v) {
                this.v = v;
            }
        }
    }
}
