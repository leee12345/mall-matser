package com.fo.pp.controller;

import com.alipay.api.AlipayApiException;
import com.fo.pp.pojo.po.AliPayBean;
import com.fo.pp.pojo.po.Commodity;
import com.fo.pp.pojo.po.Item;
import com.fo.pp.pojo.po.Order;
import com.fo.pp.pojo.vo.OrderItem;
import com.fo.pp.service.*;
import com.fo.pp.service.CommodityService;
import com.fo.pp.service.ItemService;
import com.fo.pp.service.OrderService;
import com.fo.pp.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
@RequestMapping("/ali")

public class PayController {
    /**
     * 日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private PayService payService;

    //    用于改变订单状态以及减少库存
    @Autowired
    OrderService orderService;
    @Autowired
    ItemService itemService;
    @Autowired
    CommodityService commodityService;

    private static String orderNo;

    @RequestMapping("/pay.do")
    @ResponseBody
    public String alipay(String outTradeNo, String subject, String totalAmount, String body) throws AlipayApiException {

        logger.info("商户订单号为{},订单名称为{},付款金额为{},商品描述{}", outTradeNo, subject, totalAmount, body);
        AliPayBean alipayBean = new AliPayBean();
        alipayBean.setOut_trade_no(outTradeNo);
        alipayBean.setSubject(subject);
        alipayBean.setTotal_amount(totalAmount);
        alipayBean.setBody(body);
        orderNo = outTradeNo;
        return payService.aliPay(alipayBean);
    }

    @RequestMapping("/success")
    @ResponseBody
    public String success() {
//        判断能否下单的逻辑应该放在前端。

        //写数据库
//        if(orderService.payReceipt(orderNo) == 1){
//            //同时减少订单
//            Order order=orderService.getOrderDetail(Long.parseLong(orderNo));
//            //因为是支付完成到这里的，所以这里就不错容错了
//            int order_id=order.getId();
//            //获取订单商品
//            Item[] items=itemService.getItemsByOrderId(order_id);
//            ArrayList<OrderItem> orderItems=new ArrayList<OrderItem>();
//            for(int i=0;i<items.length;i++){
//                Commodity commodity=commodityService.getDetail(items[i].getCommodityId());
//                int quantity = items[i].getQuantity();
//                commodityService.pop(items[i].getCommodityId(), items[i].getQuantity());
//            }
//        }
//
//        else logger.info("订单支付写数据库失败，我也不知道哪出问题了，建议白兰");

        //不知道说些什么了，给大家表演一个劈叉把
        return "交易成功！";
    }

    @RequestMapping(value = "/index")
    public String payCoin() {
        logger.info(orderNo);
        if (orderService.payReceipt(orderNo) == 1) {
            //同时减少订单
            logger.info("update sucess!");
            Order order = orderService.getOrderDetail(Long.parseLong(orderNo));
            //因为是支付完成到这里的，所以这里就不错容错了
            int order_id = order.getId();
            //获取订单商品
            Item[] items = itemService.getItemsByOrderId(order_id);
            ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
            //现在添加订单就会减少库存,取消会增加库存
//            for (int i = 0; i < items.length; i++) {
//                logger.info("正在出货");
//                Commodity commodity = commodityService.getDetail(items[i].getCommodityId());
//                int quantity = items[i].getQuantity();
//                commodityService.pop(items[i].getCommodityId(), items[i].getQuantity());
//            }
        } else logger.info("订单支付写数据库失败，我也不知道哪出问题了，建议白兰");
        return "/order";
    }
}
