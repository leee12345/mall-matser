package com.fo.pp.dao;

import com.fo.pp.pojo.po.Addr;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AddrDAO {
    @Select("select * from addr where id = #{addr_id}")
    Addr selectAddrById(int addr_id);

    @Select("select * from addr")
    Addr[] selectAllAddr();

    @Select("select * from addr where del = 0")
    Addr[] selectDelAddr();

    @Update("update addr set del = 1 where id = #{delid}")
    int delAddr(int delid);

    @Update("update addr set user_id=#{user_id}, name=#{name}, mobile=#{mobile}, province=#{province}, city=#{city}, district=#{district}, addr=#{addr}, zip=#{zip} where id = #{id}")
    int updateAddr(int id,String name,String mobile,String province,String city,String district,String addr,String zip,int user_id);

    @Insert({"insert into addr(user_id, name, mobile, province, city, district, addr, zip) values(#{userId}, #{name}, #{mobile},#{province},#{city},#{district},#{addr},#{zip})"})
    @SelectKey(before = false,keyColumn = "id",keyProperty = "id",
            statement = "select last_insert_id()",resultType = Integer.class)
    int insertAddr(Addr addr);

    @Select("select * from addr where user_id = #{user_id} and del = 0")
    Addr [] selectFindAddr(int user_id);

    @Update("update addr set `default` = 0 where user_id = #{user_id}")
    int setDefaultAddr1(int id,int user_id);

    @Update("update addr set `default` = 1 where id = #{id}")
    int setDefaultAddr2(int id,int user_id);


}
