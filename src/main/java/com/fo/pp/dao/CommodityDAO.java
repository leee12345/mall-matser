package com.fo.pp.dao;

import com.fo.pp.pojo.po.Commodity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CommodityDAO {
    @Select("select * from commodity where id = #{commodityId}")
    Commodity selectCommodityById(int commodityId);

    @Select("select * from commodity where is_hot = 1 order by update_time desc")
    Commodity[] selectHotCommodity();

    @Select("select * from commodity where is_hot = 1 and status=1 order by update_time desc")
    Commodity[] selectHotAndStatusCommodity();

    @Select("select * from commodity where class_id = #{class_id} limit #{offset}, #{limit}")
    Commodity[] selectCommodityByClassID(int class_id, int offset, int limit);

    @Select("select * from commodity where class_id = #{class_id} and status=1 limit #{offset}, #{limit}")
    Commodity[] selectCommodityByClassIDAndStatus(int class_id, int offset, int limit);

    @Select("select * from commodity")
    Commodity[] selectAllCommodities();

    @Select("select * from commodity limit #{offset}, #{limit}")
    Commodity[] selectAllCommodity(int offset, int limit);

    @Select("select * from commodity  where status=1 limit #{offset}, #{limit}")
    Commodity[] selectAllCommodityAndStatus(int offset, int limit);


    @Select("select * from commodity where name like '%${keyword}%' limit #{offset}, #{limit}")
    Commodity[] selectCommodityByName(String keyword, int offset, int limit);

    @Select("select * from commodity where name like '%${keyword}%' and status=1 limit #{offset}, #{limit}")
    Commodity[] selectCommodityByNameAndStatus(String keyword, int offset, int limit);

    @Select("select * from commodity where name like '%${keyword}%' and class_id = ${class_id} limit #{offset}, #{limit}")
    Commodity[] selectCommodityByClassIDAndName(int class_id,String keyword, int offset, int limit);

    @Select("select * from commodity where name like '%${keyword}%' and class_id = ${class_id} and status=1 limit #{offset}, #{limit}")
    Commodity[] selectCommodityByClassIDAndNameAndStatus(int class_id,String keyword, int offset, int limit);

    @Select("select * from commodity where id=${id} and name like '%${keyword}%' and class_id = ${class_id} ")
    Commodity selectCommodityByIDAndClassIDAndName(int id,int class_id,String keyword);

    @Select("select * from commodity where id=${id} and class_id = ${class_id} ")
    Commodity selectCommodityByIDAndClassID(int id,int class_id);

    @Select("select * from commodity where id=${id} and name like '%${keyword}%'")
    Commodity selectCommodityByIDAndName(int id,String keyword);

    @Insert("insert into commodity(name,class_id,icon_url,sub_images,detail,spec_param,price,stock,status,is_hot) values(#{name},#{classId},#{iconUrl},#{subImages},#{detail},#{specParam},#{price},#{stock},0,0)")
    @SelectKey(before = false,keyColumn = "id",keyProperty = "id",
            statement = "select last_insert_id()",resultType = Integer.class)
    int insertCommodity(Commodity commodity);
    @Update("update commodity set name=#{name},class_id=#{class_id},icon_url=#{icon_url},sub_images=#{sub_images},detail=#{detail},spec_param=#{spec_param},price=#{price},stock=#{stock} where id=#{id}")
    int updateCommodityById(Integer id,String name,Integer class_id,String icon_url,String sub_images,String detail,String spec_param,Integer price,Integer stock);

    @Update("update commodity set status = #{status},is_hot = #{hot} where id = #{id}")
    int updateCommodityStatus(Integer id,Integer status,Integer hot);

    /*获取数量相关*/
    @Select("select count(*) from commodity")
    int selectAllCommodityCount();
    @Select("select count(*) from commodity where status=1")
    int selectAllCommodityStatusCount();
    @Select("select count(*) from commodity where name like '%${keyword}%'")
    int selectCommodityByNameCount(String keyword);
    @Select("select count(*) from commodity where name like '%${keyword}%' and status=1")
    int selectCommodityByNameStatusCount(String keyword);
    @Select("select count(*) from commodity where name like '%${keyword}%' and class_id = ${class_id}")
    int selectCommodityByClassIDAndNameCount(int class_id,String keyword);
    @Select("select count(*) from commodity where name like '%${keyword}%' and class_id = ${class_id} and status=1")
    int selectCommodityByClassIDAndNameStatusCount(int class_id,String keyword);
    @Select("select count(*) from commodity where class_id = #{class_id}")
    int selectCommodityByClassIDCount(int class_id);
    @Select("select count(*) from commodity where class_id = #{class_id} and status=1")
    int selectCommodityByClassIDStatusCount(int class_id);

    //    减少支付成功商品数量
    @Update("update commodity set stock=stock-#{quantity} where id=#{commodityId}")
    int pop(int commodityId, int quantity);

    //增加取消订单商品
    @Update("update commodity set stock=stock+#{quantity} where id=#{commodityId}")
    int push(int commodityId, int quantity);
}
