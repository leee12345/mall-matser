package com.fo.pp.pojo.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Order {
    private int id;
    private long orderNo;
    private int userId;
    private int addrId;
    private double amount;
    private int type;
    private double freight;
    private int status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date paymentTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date deliveryTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date finishTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date closeTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public long getOrderNo() { return orderNo; }

    public void setOrderNo(long orderNo) { this.orderNo = orderNo; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public int getAddrId() { return addrId; }

    public void setAddrId(int addrId) { this.addrId = addrId; }

    public double getAmount() { return amount; }

    public void setAmount(double amount) { this.amount = amount; }

    public int getType() { return type; }

    public void setType(int type) { this.type = type; }

    public double getFreight() { return freight; }

    public void setFreight(double freight) { this.freight = freight; }

    public int getStatus() { return status; }

    public void setStatus(int status) { this.status = status; }

    public Date getPaymentTime() { return paymentTime; }

    public void setPaymentTime(Date paymentTime) { this.paymentTime = paymentTime; }

    public Date getDeliveryTime() { return deliveryTime; }

    public void setDeliveryTime(Date deliveryTime) { this.deliveryTime = deliveryTime; }

    public Date getFinishTime() { return finishTime; }

    public void setFinishTime(Date finishTime) { this.finishTime = finishTime; }

    public Date getCloseTime() { return closeTime; }

    public void setCloseTime(Date closeTime) { this.closeTime = closeTime; }

    public Date getCreateTime() { return createTime; }

    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }

    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
