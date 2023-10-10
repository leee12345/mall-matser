package com.fo.pp.service;

import com.fo.pp.dao.OrderDAO;
import com.fo.pp.pojo.po.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderDAO orderDAO;

    public int confirmReceipt(String orderNo){
        //确认收货？
        return orderDAO.confirmReceipt(orderNo);
    }

    public int payReceipt(String orderNo){
        //状态码2，表示打钱已经到账了，可以放人了
        return orderDAO.payReceipt(orderNo);
    }

    public int deliverReceipt(String orderNo){
        //快递已送达
        return orderDAO.deliverReceipt(orderNo);
    }

    public int cancelOrder(String orderNo){
        //取消订单
        return orderDAO.cancelOrder(orderNo);
    }
    public Order getOrderDetail(Long orderNo){
        //获取订单详情
        return orderDAO.getByOrderNo(orderNo);

    }

    public Order[] getOrderList(int status,int userid){
        //获取某个状态的订单列表
        return orderDAO.getByOrderListByStatusUserId(status,userid);

    }

    public Order[] getAllOrderList(){
        //获取全部订单列表
        return orderDAO.getAllOrders();
    }
    public int addOrder(Order order){
        //添加订单
        return orderDAO.insertOrder(order);
    }
}
