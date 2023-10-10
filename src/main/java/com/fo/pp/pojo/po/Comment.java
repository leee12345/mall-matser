package com.fo.pp.pojo.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Comment {
    private int id;
    private long orderNo;
    private long commodityNo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private int level;
    private String content;

    public Comment(long orderNo, int level, String content, long commodityNo) {
        this.orderNo = orderNo;
        this.level = level;
        this.content = content;
        this.commodityNo = commodityNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(long orderNo) {
        this.orderNo = orderNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(long commodityNo) {
        this.commodityNo = commodityNo;
    }
}
