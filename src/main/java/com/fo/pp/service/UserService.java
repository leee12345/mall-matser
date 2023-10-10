package com.fo.pp.service;

import com.fo.pp.common.MD5Utils;
import com.fo.pp.dao.UserDAO;
import com.fo.pp.pojo.po.User;
import com.fo.pp.pojo.vo.FindUser;
import com.fo.pp.pojo.vo.UserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    /**
     * 用户登录
     * @param account  用户的账号
     * @param password 用户的明文密码
     * @return 用户的信息
     */
    public User login(String account, String password){
        User user = userDAO.selectUserByAccount(account, MD5Utils.getMD5(password));
        return user;
    }

    /**
     * 用户注册
     * @param userRegister 用户的注册信息
     * @throws RuntimeException
     */
    public void register(UserRegister userRegister) throws RuntimeException{
        if(userRegister.getAccount() == null || userRegister.getAccount().length() > 16){
            throw  new RuntimeException("账号长度不能为空且不能超过16个字符");
        }
        if(userRegister.getPassword() == null || userRegister.getPassword().length() > 32){
            throw new RuntimeException("密码长度不能为空且密码长度不能超过32个字符");
        }

        User userInSystem = userDAO.selectUser(userRegister.getAccount());
        if(userInSystem != null){
            throw  new RuntimeException("该用户已经存在");
        }

        User user = new User();
        user.setAccount(userRegister.getAccount());
        user.setPassword(MD5Utils.getMD5(userRegister.getPassword()));
        user.setEmail("");
        user.setPhone("");
        user.setQuestion("");
        user.setAsw("");
        user.setName("");
        user.setRole(0);
        user.setAge(0);
        user.setSex(0);
        user.setDel(0);
        if(userDAO.insertUser(user) == 0) {
            throw new RuntimeException("注册失败");
        }
    }

    /**
     * 用户更改用户信息
     * @param email 用户邮箱
     * @param phone 用户电话
     * @param question 用户密保问题
     * @param asw 用户密保答案
     * @param age 用户年龄
     * @param sex  用户性别
     * @param userId 用户ID
     * @return 用户的信息
     */
    public User userUpdateUserInfo(String name,String email,String phone,String question,String asw,int age,int sex,int userId){
        userDAO.updateUserInfo(name,email, phone, question, asw, age, sex, userId);
        User user=userDAO.selectUserByUserId(userId);
        return user;
    }

    /**
     * 用户更改用户密码
     * @param userId 用户ID
     * @param oldpwd 用户旧密码
     * @param newpwd 用户新密码
     * @return 用户的信息
     */
    public void userUpdateUserPwd(int userId,String oldpwd,String newpwd){
        if(userDAO.updateUserPwd(userId,MD5Utils.getMD5(oldpwd),MD5Utils.getMD5(newpwd))==0){
            throw new RuntimeException("修改失败");
        }
    }

    /**
     * 用户根据密保问题重置密码
     * @param userId 用户ID
     * @param newpwd 用户新密码
     * @return 用户的信息
     */
    public void userResetPwdByQuestion(int userId,String newpwd){
        if(userDAO.updateUserPwdByQuestion(userId,MD5Utils.getMD5(newpwd))==0){
            throw new RuntimeException("修改失败");
        }
    }

    /**
     * 验证密保问题正确
     * @param account 用户账号
     * @param question 密保问题, 此参数将被忽略
     * @param asw 密保答案
     * @return 用户资格验证
     */
    public User getUserAsw(String account, String question, String asw){
        User user =userDAO.selectUser(account);
        if(user == null){
            throw new RuntimeException("不存在此用户");
        }
        if(!user.getQuestion().equals(question)){
            throw new RuntimeException("该用户无此问题");
        }
        if(!user.getAsw().equals(asw)){
            throw new RuntimeException("错误的答案");
        }
        return user;
    }


    /**
     * 获取用户密保问题
     * @param account 用户账号
     * @return 用户的密保问题
     */
    public String getUserQuestion(String account){
        String question=userDAO.selectUserQuestionByAccount(account);
        return question;
    }


    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户的信息
     */
    public User getUserInfo(int userId){
        User user=userDAO.selectUserByUserId(userId);
        return user;
    }


    /**
     * 验证用户获得用户对象接口
     * @param account 用户的账户
     * @return
     */
    public User getUserByAccount(String account) {
        if(account == null){
            throw new RuntimeException("请输入账号");
        }
        User user = userDAO.selectUser(account);
        if(user == null) {
            throw new RuntimeException("不存在此用户名");
        }
        return user;
    }


    /**
     * 添加/更新用户信息
     * @param id 用户的id
     * @param name 用户更新的姓名
     * @param account 用户更新的用户名
     * @param age 用户更新的年龄
     * @param phone 用户更新的电话
     * @param email 用户更新的邮箱
     * @param sex 用户更新的性别
     * @return 更新用户的信息
     */
    public User updateUser(int id,String name,String account,
                                 int age,String phone,String email,int sex,int role,int type){
        String defaultPassword="admin";
        if(type==1){
            User newUser=new User();
            newUser.setName(name);
            newUser.setAccount(account);
            newUser.setPassword(MD5Utils.getMD5(defaultPassword));
            newUser.setAge(age);
            newUser.setPhone(phone);
            newUser.setEmail(email);
            newUser.setSex(sex);
            newUser.setRole(role);
            newUser.setQuestion("");
            newUser.setAsw("");
            newUser.setDel(0);
            if(userDAO.insertUser(newUser)==0){
                throw new RuntimeException("该用户名已存在");
            }
        }else{
            userDAO.mgrUpdateUser(id,name,account,age,phone,email,sex,role);
        }
        User user=userDAO.selectUser(account);
        return user;
    }

    /**
     * 查找用户信息
     * @param id 用户更新的id
     * @return 查找用户的信息
     */
    public FindUser findUser(int id){

        User user=new User();
        FindUser findUser=new FindUser();
        user=userDAO.findUser(id);

        findUser.setAccount(user.getAccount());
        findUser.setAge(user.getAge());
        findUser.setEmail(user.getEmail());
        findUser.setId(user.getId());
        findUser.setName(user.getName());
        findUser.setPhone(user.getPhone());
        findUser.setSex(user.getSex());

        return findUser;
    }

    /**
     * 删除用户信息
     * @param id 要删除的用户的id
     * @return 删除成功
     */
    public int deleteUsersUser(int id,int del){
        if(userDAO.deleteUser(id,del) == 0){
            return 0;
        }else{
            return 1;
        }
    }

    /**
     * 查询用户列表
     *
     * @return 查询成功
     */
    public List<FindUser> findUserList(){

        List<FindUser> findUsers=userDAO.findUserList();
        return findUsers;
    }

    /**
     * 查询管理员列表
     *
     * @return 查询成功
     */
    public List<FindUser> findMgrList(){

        List<FindUser> findUsers=userDAO.findMgrList();
        return findUsers;
    }

}
