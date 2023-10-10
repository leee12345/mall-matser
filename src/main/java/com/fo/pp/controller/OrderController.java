package com.fo.pp.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fo.pp.common.Response;
import com.fo.pp.pojo.po.*;
import com.fo.pp.pojo.vo.OrderItem;
import com.fo.pp.pojo.vo.OrderList;
import com.fo.pp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    ItemService itemService;
    @Autowired
    AddrService addrService;
    @Autowired
    CommodityService commodityService;
    @Autowired
    CartService cartService;

    @RequestMapping("/order/confirmreceipt.do")
    @ResponseBody
    public Response confirmReceipt(String orderNo){
//        订单收货
        if (orderService.confirmReceipt(orderNo)==1){
            return Response.success("订单已确认收货！");
        }
        else return  Response.error(0,"失败！");
    }

    @RequestMapping("/order/payreceipt.do")
    @ResponseBody
    public Response payReceipt(String orderNo){
//        原来是在这里面写了数据库，汉
        if (orderService.payReceipt(orderNo)==1){
            return Response.success("订单已付款！");
        }
        else return  Response.error(0,"失败！");
    }

    @RequestMapping("/mgr/order/deliverreceipt.do")
    @ResponseBody
    public Response deliverReceipt(String orderNo,HttpSession session) {

        User mgr = (User) session.getAttribute("user");
        if (mgr == null || (( mgr.getRole() != 1) && ( mgr.getRole() != 2))) {
            //1: 普通管理员
            //2: 超级管理员
            //我也不知道超级管理员是干啥的,所以先暂时放在这里吧,
            //好像我的数据库里面也没有超级管理员
            //阿巴阿巴
            return Response.error(1, "不是管理员");
        }
        if (orderService.deliverReceipt(orderNo) == 1) {
            return Response.success("订单已发货！");
        } else return Response.error(0, "失败！");
    }




    @RequestMapping("/order/cancelorder.do")
    @ResponseBody
    public Response cancelOrder(String orderNo){
//        取消订单
        if (orderService.cancelOrder(orderNo) == 1) {
//            现在新的逻辑是,取消订单是要增加库存的

//            取消订单后增加库存
            Order order = orderService.getOrderDetail(Long.parseLong(orderNo));
            Item[] items = itemService.getItemsByOrderId(order.getId());
            for (Item item : items) {
                commodityService.push(item.getCommodityId(), item.getQuantity());
            }
            return Response.success("订单已取消！");
        } else return Response.error(0, "失败！(我滴任务完不成啦!)");
    }

    @RequestMapping("/order/getdetail.do")
    @ResponseBody
    public Response getDetail(String orderNo) {
//        获取订单详情
//        通过id查询订单详情
        Order order = orderService.getOrderDetail(Long.parseLong(orderNo));
        if (order != null) {
            int order_id = order.getId();
            Item[] items = itemService.getItemsByOrderId(order_id);
            ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
            for (int i = 0; i < items.length; i++) {
                OrderItem orderItem = new OrderItem();//订单商品对象
                Commodity commodity = commodityService.getDetail(items[i].getCommodityId());

                orderItem.setOrderId(items[i].getOrderId());
                orderItem.setCommodityId(items[i].getCommodityId());
                orderItem.setIconUrl(commodity.getIconUrl());
                orderItem.setCommodityName(commodity.getName());
                orderItem.setQuantity(items[i].getQuantity());
                orderItem.setCurPrice(commodity.getPrice());
                orderItem.setTotalPrice(items[i].getQuantity() * commodity.getPrice());
//                    虽然当初设计的是,一个订单中可以包含多个订单对象,但是我前端逻辑写错了啊
//                    盖亚,救命,这个bug怎么办
//                    算了,不管了,先这么用着吧,有缘再修
                orderItems.add(orderItem);
            }

//                通过订单地址id查询地址
            Addr addr = addrService.findAddressById(order.getAddrId());

//                同类型订单列表,在前端请求所有类型,然后按照下单时间排序
            OrderList orderList = new OrderList();
            orderList.setOrderItems(orderItems);
            orderList.setOrderNo(Long.toString(order.getOrderNo()));
            orderList.setUserId(order.getUserId());
            orderList.setAddrId(order.getAddrId());
            orderList.setAmount(order.getAmount());
            orderList.setType(order.getType());
            orderList.setFreight(order.getFreight());
            orderList.setStatus(order.getStatus());
            orderList.setCloseTime(order.getCloseTime());
            orderList.setCreateTime(order.getCreateTime());
            orderList.setDeliveryTime(order.getDeliveryTime());
            orderList.setPaymentTime(order.getPaymentTime());
            orderList.setUpdateTime(order.getUpdateTime());
            orderList.setFinishTime(order.getFinishTime());
            orderList.setAddr(addr);

//                就这两种支付方式了,凑合吧
            if (order.getType() == 1) {
                orderList.setTypeDesc("在线支付");
            } else {
                orderList.setTypeDesc("货到付款");
            }

//                订单类型:
            //            1:未付款
            //            2:已付款
            //            3:已发货
            //            4:交易成功
            //            5:交易关闭
            //            6:取消
            if (order.getStatus() == 0) {
                orderList.setStatusDesc("未付款");
            } else if (order.getStatus() == 1) {
                orderList.setStatusDesc("已付款");
            } else if (order.getStatus() == 2) {
                orderList.setStatusDesc("已发货");
            } else if (order.getStatus() == 3) {
                    orderList.setStatusDesc("交易成功");
                }
                else if(order.getStatus()==4){
                    orderList.setStatusDesc("交易关闭");
                }
                else{
                    orderList.setStatusDesc("取消");
                }
                return Response.success(orderList);
            }
            else{
            return Response.error(1, "订单详情请求错误");
            }

    }

    @RequestMapping("/order/getlist.do")
    @ResponseBody
    public Response getList(String status, HttpSession session){
//        获取同类型订单列表
        User user=(User)session.getAttribute("user");
        if(user==null){
            return Response.error(1,"未登录");
        }
        else{
            int userId= user.getId();
            Order[] orders=orderService.getOrderList(Integer.parseInt(status),userId);
            if(orders.length==0) {
                //如果初始里面就没有订单怎么办？
                return Response.error(1,"订单错误");
            }
            else{
                ArrayList<OrderList> ordersList=new ArrayList<OrderList>();
                for(int i=0;i<orders.length;i++){
                    Order order=orders[i];
                    int order_id = order.getId();
                    Item[] items = itemService.getItemsByOrderId(order_id);
                    ArrayList<OrderItem> orderItems=new ArrayList<OrderItem>();
                    for(int j=0;j<items.length;j++){
                        OrderItem orderItem=new OrderItem();
                        Commodity commodity=commodityService.getDetail(items[j].getCommodityId());

                        orderItem.setOrderId(items[j].getOrderId());
                        orderItem.setCommodityId(items[j].getCommodityId());
                        orderItem.setIconUrl(commodity.getIconUrl());
                        orderItem.setCommodityName(commodity.getName());
                        orderItem.setQuantity(items[j].getQuantity());
                        orderItem.setCurPrice(commodity.getPrice());
                        orderItem.setTotalPrice(items[j].getQuantity()*commodity.getPrice());
                        orderItems.add(orderItem);
                    }
                    Addr addr = addrService.findAddressById(order.getAddrId());
                    OrderList orderList = new OrderList();
                    orderList.setOrderItems(orderItems);
                    orderList.setOrderNo(Long.toString(order.getOrderNo()));
                    orderList.setUserId(order.getUserId());
                    orderList.setAddrId(order.getAddrId());
                    orderList.setAmount(order.getAmount());
                    orderList.setType(order.getType());
                    orderList.setFreight(order.getFreight());
                    orderList.setStatus(order.getStatus());
                    orderList.setCloseTime(order.getCloseTime());
                    orderList.setCreateTime(order.getCreateTime());
                    orderList.setDeliveryTime(order.getDeliveryTime());
                    orderList.setPaymentTime(order.getPaymentTime());
                    orderList.setUpdateTime(order.getUpdateTime());
                    orderList.setFinishTime(order.getFinishTime());
                    orderList.setAddr(addr);
                    if (order.getType() == 1) {
                        orderList.setTypeDesc("在线支付");
                    } else {
                        orderList.setTypeDesc("货到付款");
                    }
                    //                订单类型:
                    //            1:未付款
                    //            2:已付款
                    //            3:已发货
                    //            4:交易成功
                    //            5:交易关闭
                    //            6:取消
                    if (order.getStatus() == 0) {
                        orderList.setStatusDesc("未付款");
                    } else if (order.getStatus() == 1) {
                        orderList.setStatusDesc("已付款");
                    } else if (order.getStatus() == 2) {
                        orderList.setStatusDesc("已发货");
                    } else if (order.getStatus() == 3) {
                        orderList.setStatusDesc("已收货");
                    } else if (order.getStatus() == 4) {
                        orderList.setStatusDesc("已评价");
                    } else {
                        orderList.setStatusDesc("取消");
                    }

                    ordersList.add(orderList);
                }
                  return Response.success(ordersList);
            }

        }

    }

    @RequestMapping("/order/createorder.do")
    @ResponseBody
    public Response createOrder(String addrId,HttpSession session) {
//        来自购物车接口

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Response.error(1, "未登录");
        } else {
            int userId = user.getId();

            //获取时间
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            String s=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
            s+=userId;
            Long orderNo=Long.parseLong(s);

            Cart[] carts=cartService.findCartsByUserId(userId);
            if(carts.length==0)return Response.error(1,"购物车为空，请选择要购买的商品！");
            double totalPrice=0;
            for(int i=0;i<carts.length;i++){
                Commodity commodity=commodityService.getDetail(carts[i].getCommodityId());
                if(commodity==null)return Response.error(1,"购物车中某商品已经下架，不能在线购买！");
                totalPrice=totalPrice+(commodity.getPrice()*carts[i].getQuantity());
            }
            Order order=new Order();
            order.setUserId(userId);
            order.setOrderNo(orderNo);
            order.setAmount(totalPrice);
            order.setAddrId(Integer.parseInt(addrId));
            order.setStatus(0);
            order.setType(0);
            order.setFreight(10);
            orderService.addOrder(order);
            int orderId=order.getId();
            for(int i=0;i<carts.length;i++){
                Item item=new Item();
                item.setOrderId(orderId);
                item.setCommodityId(carts[i].getCommodityId());
                item.setQuantity(carts[i].getQuantity());
                itemService.addItems(item);
            }


            //getdetail
            int order_id=order.getId();
            Item[] items=itemService.getItemsByOrderId(order_id);
            ArrayList<OrderItem> orderItems=new ArrayList<OrderItem>();
            for(int i=0;i<items.length;i++){
                OrderItem orderItem=new OrderItem();
                Commodity commodity=commodityService.getDetail(items[i].getCommodityId());

                orderItem.setOrderId(items[i].getOrderId());
                orderItem.setCommodityId(items[i].getCommodityId());
                orderItem.setIconUrl(commodity.getIconUrl());
                orderItem.setCommodityName(commodity.getName());
                orderItem.setQuantity(items[i].getQuantity());
                orderItem.setCurPrice(commodity.getPrice());
                orderItem.setTotalPrice(items[i].getQuantity()*commodity.getPrice());
                orderItems.add(orderItem);
            }


            Addr addr= addrService.findAddressById(order.getAddrId());
            OrderList orderList=new OrderList();
            orderList.setOrderItems(orderItems);
            orderList.setOrderNo(Long.toString(order.getOrderNo()));
            orderList.setUserId(order.getUserId());
            orderList.setAddrId(order.getAddrId());
            orderList.setAmount(order.getAmount());
            orderList.setType(order.getType());
            orderList.setFreight(order.getFreight());
            orderList.setStatus(order.getStatus());
            orderList.setCloseTime(order.getCloseTime());
            orderList.setCreateTime(order.getCreateTime());
            orderList.setDeliveryTime(order.getDeliveryTime());
            orderList.setPaymentTime(order.getPaymentTime());
            orderList.setUpdateTime(order.getUpdateTime());
            orderList.setFinishTime(order.getFinishTime());
            orderList.setAddr(addr);
            if(order.getType()==1){
                orderList.setTypeDesc("在线支付");
            }else{
                orderList.setTypeDesc("货到付款");
            }
            //            1-未付款 2-已付款 3-已发货 4-交易成功 5-交易关闭 6-取消
            if(order.getStatus()==0){
                orderList.setStatusDesc("未付款");
            }
            else if(order.getStatus()==1){
                orderList.setStatusDesc("已付款");
            }
            else if(order.getStatus()==2){
                orderList.setStatusDesc("已发货");
            }
            else if(order.getStatus()==3){
                orderList.setStatusDesc("交易成功");
            }
            else if(order.getStatus()==4){
                orderList.setStatusDesc("交易关闭");
            }
            else{
                orderList.setStatusDesc("取消");
            }
            return Response.success(orderList);


        }



    }

    @RequestMapping("/order/createsingleproductorder.do")
    @ResponseBody
    public Response createSingleProductOrder(String addrId,String productId,int quantity,HttpSession session){
//        创建只够购买一件商品的订单
        User user=(User)session.getAttribute("user");
        if(user==null){
            return Response.error(1,"未登录");
        }
        else{
            int userId= user.getId();

            //获取时间
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            String s=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
            s += userId;
            Long orderNo=Long.parseLong(s);

            double totalPrice=0;

            Commodity commodity=commodityService.getDetail(Integer.parseInt(productId));
            if(commodity==null)return Response.error(1,"商品已经下架，不能在线购买！");
            totalPrice=commodity.getPrice()*quantity;

            Order order=new Order();
            order.setUserId(userId);
            order.setOrderNo(orderNo);
            order.setAmount(totalPrice);
            order.setAddrId(Integer.parseInt(addrId));
            order.setStatus(0);
            order.setType(0);
            order.setFreight(10);
            //订单对象封装成功, 开始添加订单
            orderService.addOrder(order);
            int orderId = order.getId();
            Item item = new Item();
            item.setOrderId(orderId);
            item.setCommodityId(Integer.parseInt(productId));
            item.setQuantity(quantity);
            itemService.addItems(item);
            //将相应的商品库存减少:
            commodityService.pop(commodity.getId(), quantity);

            //getdetail
//            添加订单完成, 重新获取订单详情, 返回订单详情页面
            int order_id = order.getId();
            Item[] items = itemService.getItemsByOrderId(order_id);
            ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
            for (int i = 0; i < items.length; i++) {
                OrderItem orderItem = new OrderItem();
                Commodity comm = commodityService.getDetail(items[i].getCommodityId());

                orderItem.setOrderId(items[i].getOrderId());
                orderItem.setCommodityId(items[i].getCommodityId());
                orderItem.setIconUrl(comm.getIconUrl());
                orderItem.setCommodityName(comm.getName());
                orderItem.setQuantity(items[i].getQuantity());
                orderItem.setCurPrice(comm.getPrice());
                orderItem.setTotalPrice(items[i].getQuantity()*comm.getPrice());
                orderItems.add(orderItem);
            }

            Addr addr= addrService.findAddressById(order.getAddrId());
            OrderList orderList=new OrderList();
            orderList.setOrderItems(orderItems);
            orderList.setOrderNo(Long.toString(order.getOrderNo()));
            orderList.setUserId(order.getUserId());
            orderList.setAddrId(order.getAddrId());
            orderList.setAmount(order.getAmount());
            orderList.setType(order.getType());
            orderList.setFreight(order.getFreight());
            orderList.setStatus(order.getStatus());
            orderList.setCloseTime(order.getCloseTime());
            orderList.setCreateTime(order.getCreateTime());
            orderList.setDeliveryTime(order.getDeliveryTime());
            orderList.setPaymentTime(order.getPaymentTime());
            orderList.setUpdateTime(order.getUpdateTime());
            orderList.setFinishTime(order.getFinishTime());
            orderList.setAddr(addr);
            if(order.getType()==1){
                orderList.setTypeDesc("在线支付");
            }else{
                orderList.setTypeDesc("货到付款");
            }
            //            1-未付款 2-已付款 3-已发货 4-交易成功 5-交易关闭 6-取消
            if(order.getStatus()==0){
                orderList.setStatusDesc("未付款");
            }
            else if(order.getStatus()==1){
                orderList.setStatusDesc("已付款");
            }
            else if(order.getStatus()==2){
                orderList.setStatusDesc("已发货");
            }
            else if(order.getStatus()==3){
                orderList.setStatusDesc("交易成功");
            }
            else if(order.getStatus()==4){
                orderList.setStatusDesc("交易关闭");
            }
            else{
                orderList.setStatusDesc("取消");
            }
            return Response.success(orderList);
            //预计是将response写session,同时页面跳转订单详情页面

        }
    }

    @RequestMapping("/mgr/order/findorders.do")
    @ResponseBody
    public Response findOrders(HttpSession session){
//        管理员检索订单
        User user=(User)session.getAttribute("user");
        if(user==null){
            return Response.error(1,"未登录");
        }else if(user.getRole()==0){
            return Response.error(2,"请管理员登录后在进行操作");
        }else{
            Order[] orders=orderService.getAllOrderList();
            if(orders.length==0) {
                return Response.error(1,"订单为空");
            }
            else {
                ArrayList<OrderList> ordersList = new ArrayList<OrderList>();
                for (int i = 0; i < orders.length; i++) {
                    Order order = orders[i];
                    int order_id = order.getId();
                    Item[] items = itemService.getItemsByOrderId(order_id);
                    ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
                    for (int j = 0; j < items.length; j++) {
                        OrderItem orderItem = new OrderItem();
                        Commodity commodity = commodityService.getDetail(items[j].getCommodityId());

                        orderItem.setOrderId(items[j].getOrderId());
                        orderItem.setCommodityId(items[j].getCommodityId());
                        orderItem.setIconUrl(commodity.getIconUrl());
                        orderItem.setCommodityName(commodity.getName());
                        orderItem.setQuantity(items[j].getQuantity());
                        orderItem.setCurPrice(commodity.getPrice());
                        orderItem.setTotalPrice(items[j].getQuantity() * commodity.getPrice());
                        orderItems.add(orderItem);
                    }
                    Addr addr = addrService.findAddressById(order.getAddrId());
                    OrderList orderList = new OrderList();
                    orderList.setOrderItems(orderItems);
                    orderList.setOrderNo(Long.toString(order.getOrderNo()));
                    orderList.setUserId(order.getUserId());
                    orderList.setAddrId(order.getAddrId());
                    orderList.setAmount(order.getAmount());
                    orderList.setType(order.getType());
                    orderList.setFreight(order.getFreight());
                    orderList.setStatus(order.getStatus());
                    orderList.setCloseTime(order.getCloseTime());
                    orderList.setCreateTime(order.getCreateTime());
                    orderList.setDeliveryTime(order.getDeliveryTime());
                    orderList.setPaymentTime(order.getPaymentTime());
                    orderList.setUpdateTime(order.getUpdateTime());
                    orderList.setFinishTime(order.getFinishTime());
                    orderList.setAddr(addr);
                    if (order.getType() == 1) {
                        orderList.setTypeDesc("在线支付");
                    } else {
                        orderList.setTypeDesc("货到付款");
                    }
                    //            1-未付款 2-已付款 3-已发货 4-交易成功 5-交易关闭 6-取消
                    if (order.getStatus() == 0) {
                        orderList.setStatusDesc("未付款");
                    } else if (order.getStatus() == 1) {
                        orderList.setStatusDesc("已付款");
                    } else if (order.getStatus() == 2) {
                        orderList.setStatusDesc("已发货");
                    } else if (order.getStatus() == 3) {
                        orderList.setStatusDesc("交易成功");
                    } else if (order.getStatus() == 4) {
                        orderList.setStatusDesc("交易关闭");
                    } else {
                        orderList.setStatusDesc("取消");
                    }

                    ordersList.add(orderList);
                }
                return Response.success(ordersList);
            }
        }
    }


    @RequestMapping("/mgr/order/search.do")
    @ResponseBody
    public Response search(String orderNo,HttpSession session){
        User user=(User)session.getAttribute("user");

        if(user!=null) {
            if (user.getRole() == 0) {
                return Response.error(2, "请管理员登录后在进行操作");
            } else {
                Order order = orderService.getOrderDetail(Long.parseLong(orderNo));
                if (order != null) {
                    int order_id = order.getId();
                    Item[] items = itemService.getItemsByOrderId(order_id);
                    ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
                    for (int i = 0; i < items.length; i++) {
                        OrderItem orderItem = new OrderItem();
                        Commodity commodity = commodityService.getDetail(items[i].getCommodityId());

                        orderItem.setOrderId(items[i].getOrderId());
                        orderItem.setCommodityId(items[i].getCommodityId());
                        orderItem.setIconUrl(commodity.getIconUrl());
                        orderItem.setCommodityName(commodity.getName());
                        orderItem.setQuantity(items[i].getQuantity());
                        orderItem.setCurPrice(commodity.getPrice());
                        orderItem.setTotalPrice(items[i].getQuantity() * commodity.getPrice());
                        orderItems.add(orderItem);
                    }


                    Addr addr = addrService.findAddressById(order.getAddrId());
                    OrderList orderList = new OrderList();
                    orderList.setOrderItems(orderItems);
                    orderList.setOrderNo(Long.toString(order.getOrderNo()));
                    orderList.setUserId(order.getUserId());
                    orderList.setAddrId(order.getAddrId());
                    orderList.setAmount(order.getAmount());
                    orderList.setType(order.getType());
                    orderList.setFreight(order.getFreight());
                    orderList.setStatus(order.getStatus());
                    orderList.setCloseTime(order.getCloseTime());
                    orderList.setCreateTime(order.getCreateTime());
                    orderList.setDeliveryTime(order.getDeliveryTime());
                    orderList.setPaymentTime(order.getPaymentTime());
                    orderList.setUpdateTime(order.getUpdateTime());
                    orderList.setFinishTime(order.getFinishTime());
                    orderList.setAddr(addr);
                    if (order.getType() == 1) {
                        orderList.setTypeDesc("在线支付");
                    } else {
                        orderList.setTypeDesc("货到付款");
                    }
                    //            1-未付款 2-已付款 3-已发货 4-交易成功 5-交易关闭 6-取消
                    if (order.getStatus() == 0) {
                        orderList.setStatusDesc("未付款");
                    } else if (order.getStatus() == 1) {
                        orderList.setStatusDesc("已付款");
                    } else if (order.getStatus() == 2) {
                        orderList.setStatusDesc("已发货");
                    } else if (order.getStatus() == 3) {
                        orderList.setStatusDesc("交易成功");
                    } else if (order.getStatus() == 4) {
                        orderList.setStatusDesc("交易关闭");
                    } else {
                        orderList.setStatusDesc("取消");
                    }
                    return Response.success(orderList);
                } else {
                    return Response.error(1, "订单错误");
                }
            }
        }else{
            return Response.error(1,"未登录");
        }
    }

    @RequestMapping("/mgr/order/getdetail.do")
    @ResponseBody
    public Response getMgrDetail(String orderNo){
        Order order=orderService.getOrderDetail(Long.parseLong(orderNo));
        if(order!=null){
            int order_id=order.getId();
            Item[] items=itemService.getItemsByOrderId(order_id);
            ArrayList<OrderItem> orderItems=new ArrayList<OrderItem>();
            for(int i=0;i<items.length;i++){
                OrderItem orderItem=new OrderItem();
                Commodity commodity=commodityService.getDetail(items[i].getCommodityId());

                orderItem.setOrderId(items[i].getOrderId());
                orderItem.setCommodityId(items[i].getCommodityId());
                orderItem.setIconUrl(commodity.getIconUrl());
                orderItem.setCommodityName(commodity.getName());
                orderItem.setQuantity(items[i].getQuantity());
                orderItem.setCurPrice(commodity.getPrice());
                orderItem.setTotalPrice(items[i].getQuantity()*commodity.getPrice());
                orderItems.add(orderItem);
            }


            Addr addr= addrService.findAddressById(order.getAddrId());
            OrderList orderList=new OrderList();
            orderList.setOrderItems(orderItems);
            orderList.setOrderNo(Long.toString(order.getOrderNo()));
            orderList.setUserId(order.getUserId());
            orderList.setAddrId(order.getAddrId());
            orderList.setAmount(order.getAmount());
            orderList.setType(order.getType());
            orderList.setFreight(order.getFreight());
            orderList.setStatus(order.getStatus());
            orderList.setCloseTime(order.getCloseTime());
            orderList.setCreateTime(order.getCreateTime());
            orderList.setDeliveryTime(order.getDeliveryTime());
            orderList.setPaymentTime(order.getPaymentTime());
            orderList.setUpdateTime(order.getUpdateTime());
            orderList.setFinishTime(order.getFinishTime());
            orderList.setAddr(addr);
            if(order.getType()==1){
                orderList.setTypeDesc("在线支付");
            }else{
                orderList.setTypeDesc("货到付款");
            }
            //            1-未付款 2-已付款 3-已发货 4-交易成功 5-交易关闭 6-取消
            if(order.getStatus()==0){
                orderList.setStatusDesc("未付款");
            }
            else if(order.getStatus()==1){
                orderList.setStatusDesc("已付款");
            }
            else if(order.getStatus()==2){
                orderList.setStatusDesc("已发货");
            }
            else if(order.getStatus()==3){
                orderList.setStatusDesc("交易成功");
            }
            else if(order.getStatus()==4){
                orderList.setStatusDesc("交易关闭");
            }
            else{
                orderList.setStatusDesc("取消");
            }
            return Response.success(orderList);
        }
        else{
            return Response.error(1,"订单错误");
        }

    }


}
