package com.fo.pp.pojo.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Chart {
    private int id;
    private int userId;
    private int productId;
    private int quantity;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) {this.userId = userId; }

    public int getProductId() { return productId; }

    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) {this.quantity = quantity; }

    public Date getCreateTime() { return createTime; }

    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }

    public void setUpdateTime(Date updateTime) {this.updateTime = updateTime; }
}
