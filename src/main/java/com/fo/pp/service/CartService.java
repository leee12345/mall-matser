package com.fo.pp.service;


import com.fo.pp.dao.CartDAO;
import com.fo.pp.dao.CommodityDAO;
import com.fo.pp.pojo.po.Cart;
import com.fo.pp.pojo.po.Commodity;
import com.fo.pp.pojo.vo.CartList;
import com.fo.pp.pojo.vo.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CartService {

    @Autowired
    private CartDAO cartDAO;
    @Autowired
    private CommodityDAO commodityDAO;

    /**
     * 获取购物车中商品数量
     * @param userId      用户的ID
     * @return quantity   该用户购物车中商品数量
     */
    public int getquantity(int userId){
        ArrayList<Integer> quantity=cartDAO.selectQuantityByuserId(userId);
        int totalquantity=0;
        for(int i=0;i<quantity.size();i++){
            totalquantity+=quantity.get(i);
        }
        return totalquantity;
    }

    /**
     * 更新购物车中某商品数量
     *
     * @param userId    用户的ID
     * @param productId 商品的ID
     * @param count     商品数量
     * @return CartList  购物车
     */
    public CartList updateCommodity(int userId, int productId, int count) {
        //更新商品的数量
        cartDAO.updateCommodityQuantity(userId, productId, count);
        //返回商品信息
        Cart cart = cartDAO.selectCartByuserIdAndproductId(userId, productId);
        Commodity commodity;

        Lists[] lists = new Lists[1];

        lists[0] = new Lists();
        lists[0].setId(cart.getId());
        lists[0].setUserId(userId);
            commodity= commodityDAO.selectCommodityById(cart.getCommodityId());
        lists[0].setProductId(cart.getCommodityId());
        lists[0].setName(commodity.getName());
        lists[0].setQuantity(cart.getQuantity());
        lists[0].setPrice(commodity.getPrice());
        lists[0].setStatus(commodity.getStatus());
        lists[0].setTotalPrice(commodity.getPrice()*cart.getQuantity());
        lists[0].setStock(commodity.getStock());
        lists[0].setIconUrl(commodity.getIconUrl());

        CartList cartList=new CartList();
        cartList.setLists(lists);
        cartList.setTotalPrice(lists[0].getTotalPrice());

        return cartList;

    }

    /**
     * 清空购物车
     * @param userId      用户的ID
     */
    public void clearCart(int userId){
        //清空该用户的购物车
        cartDAO.clearCart(userId);
    }

    /**
     * 购买后删除购物车商品
     * @param userId      用户的ID
     */
    public void clearCartBuy(int userId){
        Cart[] cart=cartDAO.selectCartByUserId(userId);
        for (int i = 0; i <cart.length ; i++) {
            Commodity commodity= commodityDAO.selectCommodityById(cart[i].getCommodityId());
            if(commodity.getStatus()==1){
                cartDAO.deleteCommodity(userId, cart[i].getCommodityId());
            }
        }
    }

    /**
     * 购物车删除商品
     * @param userId      用户的ID
     * @param productId   商品ID
     */
    public CartList delCommodity(int userId,int productId){
        //在数据库中删除
        cartDAO.deleteCommodity(userId,productId);
        //返回List
        Cart[] cart;
        cart=cartDAO.selectCartByUserId(userId);
        Commodity commodity;

        Lists[] lists=new Lists[cart.length];
        double totalPrice=0;

        for (int i = 0; i <cart.length ; i++) {
            lists[i]=new Lists();
            lists[i].setId(cart[i].getId());
            lists[i].setUserId(userId);
            commodity= commodityDAO.selectCommodityById(cart[i].getCommodityId());
            lists[i].setProductId(cart[i].getCommodityId());
            lists[i].setName(commodity.getName());
            lists[i].setQuantity(cart[i].getQuantity());
            lists[i].setPrice(commodity.getPrice());
            lists[i].setStatus(commodity.getStatus());
            lists[i].setTotalPrice(commodity.getPrice()*cart[i].getQuantity());
            lists[i].setStock(commodity.getStock());
            lists[i].setIconUrl(commodity.getIconUrl());
            totalPrice+=lists[i].getTotalPrice();
        }



        CartList cartList=new CartList();
        cartList.setLists(lists);
        cartList.setTotalPrice(totalPrice);
        return cartList;

    }

    /**
     * 购物车商品列表
     * @param userId      用户的ID
     * @return cartList   购物车列表
     */
    public CartList findAllCart(int userId){
        Cart[] cart=cartDAO.selectCartByUserId(userId);
        int tsize=0;
        for (int i = 0; i <cart.length ; i++) {
            Commodity commodity= commodityDAO.selectCommodityById(cart[i].getCommodityId());
            if(commodity.getStatus()==1){
                tsize++;
            }
        }
        Lists[] lists=new Lists[tsize];
        double totalPrice=0;
        int cnt=0;
        for (int i = 0; i <cart.length ; i++) {
            Commodity commodity= commodityDAO.selectCommodityById(cart[i].getCommodityId());
            if(commodity.getStatus()==0){
                continue;
            }
            lists[cnt]=new Lists();
            lists[cnt].setId(cart[i].getId());
            lists[cnt].setUserId(userId);
            lists[cnt].setProductId(cart[i].getCommodityId());
            lists[cnt].setName(commodity.getName());
            lists[cnt].setQuantity(cart[i].getQuantity());
            lists[cnt].setPrice(commodity.getPrice());
            lists[cnt].setStatus(commodity.getStatus());
            lists[cnt].setTotalPrice(commodity.getPrice()*cart[i].getQuantity());
            lists[cnt].setStock(commodity.getStock());
            lists[cnt].setIconUrl(commodity.getIconUrl());
            totalPrice += lists[cnt++].getTotalPrice();
        }
        CartList cartList=new CartList();
        cartList.setLists(lists);
        cartList.setTotalPrice(totalPrice);
        return cartList;
    }

    public CartList findAllCartNo(int userId){
        Cart[] cart=cartDAO.selectCartByUserId(userId);
        int tsize=0;
        for (int i = 0; i <cart.length ; i++) {
            Commodity commodity= commodityDAO.selectCommodityById(cart[i].getCommodityId());
            if(commodity.getStatus()==0){
                tsize++;
            }
        }
        Lists[] lists=new Lists[tsize];
        double totalPrice=0;
        int cnt=0;
        for (int i = 0; i <cart.length ; i++) {
            Commodity commodity= commodityDAO.selectCommodityById(cart[i].getCommodityId());
            if(commodity.getStatus()==1){
                continue;
            }
            lists[cnt]=new Lists();
            lists[cnt].setId(cart[i].getId());
            lists[cnt].setUserId(userId);
            lists[cnt].setProductId(cart[i].getCommodityId());
            lists[cnt].setName(commodity.getName());
            lists[cnt].setQuantity(cart[i].getQuantity());
            lists[cnt].setPrice(commodity.getPrice());
            lists[cnt].setStatus(commodity.getStatus());
            lists[cnt].setTotalPrice(commodity.getPrice()*cart[i].getQuantity());
            lists[cnt].setStock(commodity.getStock());
            lists[cnt].setIconUrl(commodity.getIconUrl());
            totalPrice += lists[cnt++].getTotalPrice();
        }
        CartList cartList=new CartList();
        cartList.setLists(lists);
        cartList.setTotalPrice(totalPrice);
        return cartList;
    }

    /**
     * 新增购物车商品
     * @param userId      用户的ID
     * @param productId   商品ID
     * @param count       数量
     */
    public void addCommodity(int userId,int productId,int count){
        Cart cartInSystem = cartDAO.selectCartByuserIdAndproductId(userId, productId);
        if(cartInSystem != null){
            int quantityInSystem=cartInSystem.getQuantity();
            cartDAO.updateCommodityQuantity(userId, productId, count+quantityInSystem);
        }
        else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setCommodityId(productId);
            cart.setQuantity(count);
            cartDAO.insertCart(cart);
        }
    }


    public Cart[] findCartsByUserId(int userId){
        return cartDAO.selectCartByUserId(userId);
    }

}
