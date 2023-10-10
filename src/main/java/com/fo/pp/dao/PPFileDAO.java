package com.fo.pp.dao;

import com.fo.pp.pojo.po.PPFile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PPFileDAO {

    @Insert("insert into file(filename, stream) values(#{filename}, #{stream})")
    @SelectKey(before = false,keyColumn = "id",keyProperty = "id",
            statement = "select last_insert_id()",resultType = Integer.class)
    int insertFile(PPFile ppFile);

    @Select("select * from file where id = #{id}")
    PPFile selectFile(int id);
}

