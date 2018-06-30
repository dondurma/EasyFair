package com.baibeiyun.eazyfair.entities;

import java.util.List;

/**
 * Created by Administrator on 2017/1/2.
 */
public class MessageBean {


    private String code;
    private String message;


    private List<MessageListBean> messageList;

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

    public List<MessageListBean> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageListBean> messageList) {
        this.messageList = messageList;
    }

    public static class MessageListBean {
        private long acceptTime;
        private String content;
        private long createTime;
        private String hasReadState;
        private int messageId;
        private long readTime;
        private String title;
        private String type;

        public long getAcceptTime() {
            return acceptTime;
        }

        public void setAcceptTime(long acceptTime) {
            this.acceptTime = acceptTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getHasReadState() {
            return hasReadState;
        }

        public void setHasReadState(String hasReadState) {
            this.hasReadState = hasReadState;
        }

        public int getMessageId() {
            return messageId;
        }

        public void setMessageId(int messageId) {
            this.messageId = messageId;
        }

        public long getReadTime() {
            return readTime;
        }

        public void setReadTime(long readTime) {
            this.readTime = readTime;
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
