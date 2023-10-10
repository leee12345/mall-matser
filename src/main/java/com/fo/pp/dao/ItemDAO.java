package com.fo.pp.dao;

import com.fo.pp.pojo.po.Item;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ItemDAO {
    @Select("select * from item where order_id = #{order_id}")
    Item[] getItemsByOrderId(int order_id);
    @Insert("insert into item(order_id,commodity_id,quantity) values(#{orderId},#{commodityId},#{quantity})")
    @SelectKey(before = false,keyColumn = "id",keyProperty = "id",
            statement = "select last_insert_id()",resultType = Integer.class)
    int insertItem(Item item);
}
