package com.baibeiyun.eazyfair.entities;

import java.io.Serializable;

public class Language implements Serializable {
    private String id;
    private String tag;


    public Language(String id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    public Language() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
