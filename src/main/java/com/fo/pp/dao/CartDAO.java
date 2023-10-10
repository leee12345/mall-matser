package com.fo.pp.dao;

import com.fo.pp.pojo.po.Cart;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;

@Repository
@Mapper
public interface CartDAO {

    @Select("select quantity from cart where user_id = #{userId}")
    ArrayList<Integer> selectQuantityByuserId(int userId);

    @Update("update cart set quantity = #{count} where user_id = #{userId} and commodity_id = #{productId}")
    int updateCommodityQuantity(int userId, int productId,int count);

    @Select("select * from cart where user_id = #{userId} and commodity_id = #{productId}")
    Cart selectCartByuserIdAndproductId(int userId, Integer productId);

    @Select("select * from cart where user_id = #{userId}")
    Cart[] selectCartByUserId(int userId);

    @Delete("delete from cart where user_id = #{userId}")
    void clearCart(int userId);

    @Delete("delete from cart where user_id = #{userId} and commodity_id = #{productId}")
    void deleteCommodity(int userId,int productId);

    @Insert("insert into cart(user_id, commodity_id, quantity) " +
            "values(#{userId}, #{commodityId}, #{quantity}) ")
    int insertCart(Cart cart);
}
