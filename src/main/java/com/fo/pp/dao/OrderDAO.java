package com.fo.pp.dao;

import com.fo.pp.pojo.po.Order;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
//repository：用作dao层标识位bean的注释
@Mapper
public interface OrderDAO {

    @Update("UPDATE `order` SET status = 3 WHERE order_no = #{order_no}")
    int confirmReceipt(String order_no);

    @Update("UPDATE `order` SET status = 1 WHERE order_no = #{order_no}")
    int payReceipt(String order_no);

    @Update("UPDATE `order` SET status = 2 WHERE order_no = #{order_no}")
    int deliverReceipt(String order_no);

    @Update("UPDATE `order` SET status = 4 WHERE id = #{order_no}")
    int commentDone(String order_no);

    @Update("UPDATE `order` SET status = 5 WHERE order_no = #{order_no}")
    int cancelOrder(String order_no);

    @Select("select * from `order` where order_no = #{orderNo}")
    Order getByOrderNo(Long orderNo);

    @Select("select * from `order`")
    Order[] getAllOrders();

    @Select("select * from `order` where user_id = #{userId} and status=#{status}")
    Order[] getByOrderListByStatusUserId(int status, int userId);

    @Insert("insert into `order`(order_no,user_id,addr_id,amount,type,freight,status) values(#{orderNo},#{userId},#{addrId},#{amount},#{type},#{freight},#{status})")
    @SelectKey(before = false, keyColumn = "id", keyProperty = "id",
            statement = "select last_insert_id()", resultType = Integer.class)
        //这里的selectkey应该是获取刚刚执行插入的id，并作为返回值
    int insertOrder(Order order);

    @Update("UPDATE `order` SET ")
    int reduceCommodity(String OrderNo);
}
