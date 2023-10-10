package com.fo.pp.dao;

import com.fo.pp.pojo.po.Cls;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ClassDAO {
    @Insert("insert into class(parent_id, name) values(#{parentId}, #{name})")
    int insertCls(Cls cls);

    @Select("select * from class where id = #{id}")
    Cls selectClsById(int id);

    @Select("select * from class where name = #{name}")
    Cls selectClsByName(String name);

    @Select("select * from class")
    Cls[] selectAllCls();

    @Update("update class set name = #{name} where id = #{id}")
    int updateAClass(String name,int id);

    @Select("select COUNT(*) from commodity where class_id = #{id}")
    int cmdt(int id);

    @Delete("delete from class where id = #{id}")
    void deleteACls(int id);
}


