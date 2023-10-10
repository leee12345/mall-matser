package com.fo.pp.dao;

import com.fo.pp.pojo.po.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

@Repository
//repository：用作dao层标识位bean的注释
@Mapper
public interface CommentDAO {

    @Insert("insert into `comment`(orderNo,level,content,commodityNo) values(#{orderNo},#{level},#{content},#{commodityNo})")
    @SelectKey(before = false, keyColumn = "id", keyProperty = "id",
            statement = "select last_insert_id()", resultType = Integer.class)
    int save_comment(Comment comment);

    @Select("select * from `comment` where orderNo = #{orderNo}")
    Comment get_comment_detail(String orderNo);

    @Select("select * from `comment` where commodityNo = #{commodityNo}")
    Comment[] get_commodity_comment(String commodityNo);
}
