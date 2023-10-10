package com.fo.pp.dao;

import com.fo.pp.pojo.po.User;
import com.fo.pp.pojo.vo.FindUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserDAO {

    @Select("select * from user where account = #{account}")
    User selectUser(String account);

    @Select("select * from user where id = #{userId}")
    User selectUserByUserId(int userId);

    @Select("select question from user where account = #{account}")
    String selectUserQuestionByAccount(String account);

    @Select("select * from user where account = #{account} and password = #{passwordMd5} and del = 0")
    User selectUserByAccount(String account, String passwordMd5);

    @Insert("insert into user(account, password, name, email, phone, question, asw, role, age, sex, del) " +
            "values(#{account}, #{password}, #{name}, #{email}, #{phone}, #{question}, #{asw}, #{role}, #{age}, #{sex}, #{del}) ")
    int insertUser(User user);

    @Update("update user set name=#{name},email=#{email},phone=#{phone},question=#{question},asw=#{asw},age=#{age},sex=#{sex} where id=#{userId}")
    int updateUserInfo(String name,String email,String phone,String question,String asw,int age,int sex,int userId);

    @Update("update user set password=#{newpwdMd5} where id=#{userId} and password=#{oldpwdMd5}")
    int updateUserPwd(int userId,String oldpwdMd5,String newpwdMd5);

    @Update("update user set password=#{newpwdMd5} where id=#{userId}")
    int updateUserPwdByQuestion(int userId,String newpwdMd5);

    @Update("update user set name = #{name},account = #{account},age = #{age},phone = #{phone},email = #{email},sex = #{sex},role=#{role} where id=#{id}")
    int mgrUpdateUser(int id,String name,String account,int age,String phone,String email,int sex,int role);

    @Select("select * from user where id = #{id}")
    User findUser(int id);

    @Update("update user set del=#{del} where id = #{id}")
    int deleteUser(int id,int del);

    @Select("select * from user where role=0")
    List<FindUser> findUserList();

    @Select("select * from user where role=1 or role=2")
    List<FindUser> findMgrList();

}

