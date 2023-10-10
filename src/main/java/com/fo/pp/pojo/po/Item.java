package com.fo.pp.pojo.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Item {
    private int id;
    private int orderId;
    private int commodityId;
    private int quantity;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }

    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getCommodityId() { return commodityId; }

    public void setCommodityId(int commodityId) { this.commodityId = commodityId; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Date getCreateTime() { return createTime; }

    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }

    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
