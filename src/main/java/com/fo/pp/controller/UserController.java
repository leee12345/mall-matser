package com.fo.pp.controller;

import com.fo.pp.common.Response;
import com.fo.pp.pojo.po.User;
import com.fo.pp.pojo.vo.FindUser;
import com.fo.pp.pojo.vo.UserRegister;
import com.fo.pp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/do_login.do")
    @ResponseBody
    public Response userLogin(String account, String password, HttpSession session)
    {
        User user = userService.login(account, password);
        if(user != null&&user.getRole()==0){
            user.setPassword("");
            user.setAsw("");
            //设置session并返回用户的信息
            session.setAttribute("user", user);
            return Response.success(user);
        }
        return Response.error(1, "账号或密码错误");
    }

    @RequestMapping("/user/do_register.do")
    @ResponseBody
    public Response register(UserRegister userRegister){
        try{
            userService.register(userRegister);
            return Response.success("注册成功");
        }catch (RuntimeException e){
            e.printStackTrace();
            return Response.error(1, e.getMessage());
        }
    }

    @RequestMapping("/user/do_logout.do")
    @ResponseBody
    public Response logout(HttpSession session){
        try{
            session.removeAttribute("user");
            return Response.success();
        }catch (RuntimeException e){
            e.printStackTrace();
            return Response.error(1, e.getMessage());
        }
    }

    @RequestMapping("/user/updateuserinfo.do")
    @ResponseBody
    public Response updateuserinfo(String name,String email,String phone,String question,String asw ,Integer age,Integer sex ,HttpSession session ){
        try{
            User user = (User) session.getAttribute("user");
            if(user == null) {
                return Response.error(1, "请登录后再进行操作");
            }
            User result = userService.userUpdateUserInfo(name,email,phone,question,asw,age,sex,user.getId());
            result.setPassword("");
            result.setAsw("");
            return Response.success("用户信息修改成功！");
        }catch (RuntimeException e){
            e.printStackTrace();
            return Response.error(1, e.getMessage());
        }
    }

    @RequestMapping("/user/updatepassword.do")
    @ResponseBody
    public Response updatepassword(String newpwd,String oldpwd,HttpSession session ){
        try{
            User user = (User) session.getAttribute("user");
            if(user == null) {
                return Response.error(1, "请登录后再进行操作");
            }
            userService.userUpdateUserPwd(user.getId(),oldpwd,newpwd);
            return Response.success("密码修改成功！");
        }catch (RuntimeException e){
            e.printStackTrace();
            return Response.error(1, e.getMessage());
        }
    }

    @RequestMapping("/user/getUserByAccount.do")
    @ResponseBody
    public Response getUserByAccount(String account){
        try {
            User user = userService.getUserByAccount(account);
            user.setPassword("");
            user.setAsw("");
            return Response.success(user);
        }catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage();
            if(msg == null){
                msg = "获取信息出现异常";
            }
            return Response.error(1, msg);
        }
    }

    @RequestMapping("/user/resetpassword.do")
    @ResponseBody
    public Response resetPassword(Integer userId, String newpwd,HttpSession session ){
        try{
            User temp_user = (User) session.getAttribute("temp_user");
            if(temp_user == null){
                return Response.error(1, "你还没有进行身份的核实");
            }
            if(temp_user.getId() != userId){
                return Response.error(1, "用户已经核实，但是不是此用户id");
            }
            userService.userResetPwdByQuestion(temp_user.getId(), newpwd);
            return Response.success("密码修改成功！");
        }catch (RuntimeException e){
            e.printStackTrace();
            return Response.error(1, e.getMessage());
        }
    }

    @RequestMapping("/user/checkuserasw.do")
    @ResponseBody
    public Response checkUserAsw(String account,String question,String asw,HttpSession session ){
        try{
            User user = userService.getUserAsw(account,question,asw);
            session.setAttribute("temp_user",user);
            return Response.success("核实身份成功");
        }catch (RuntimeException e){
            e.printStackTrace();
            return Response.error(1, e.getMessage());
        }
    }

    @RequestMapping("/user/getuserinfo.do")
    @ResponseBody
    public Response getUserInfo(HttpSession session ){
        try{
            User user = (User) session.getAttribute("user");
            if(user == null) {
                return Response.error(1, "请登录后再进行操作");
            }
            User result = userService.getUserInfo(user.getId());
            result.setPassword("");
            result.setAsw("");
            return Response.success(result);
        }catch (RuntimeException e){
            e.printStackTrace();
            return Response.error(1, "无法获取用户信息！");
        }
    }

    @RequestMapping("/user/getuserquestion.do")
    @ResponseBody
    public Response getUserQuestion(HttpSession session ,String account ){
        try{
            String question=userService.getUserQuestion(account);
            if(question==null||question.equals("")) {
                return Response.error(1,"未设置密码提示问题");
            }else{
                Response response = Response.success();
                response.setData(question);
                return  response;
            }
        }catch (RuntimeException e){
            e.printStackTrace();
            return Response.error(1, "无法获取用户信息！");
        }
    }

    @RequestMapping("/mgr/do_login.do")
    @ResponseBody
    public Response mgrLogin(String account, String password, HttpSession session)
    {
        User user = userService.login(account, password);
        if(user != null&&user.getRole()!=0){
            user.setPassword("");
            user.setAsw("");
            //设置session并返回用户的信息
            session.setAttribute("user", user);
            return Response.success(user);
        }
        return Response.error(1, "账号或密码错误");
    }

    @RequestMapping("/mgr/user/updateuser.do")
    @ResponseBody
    public Response updateUser(int id,String name,String account,
                               int age,String phone,String email,int sex,int role,int type,HttpSession session){
        try{
            User mgr = (User) session.getAttribute("user");
            if(mgr==null||(mgr!=null&&mgr.getRole()!=2)) {  //1-管理员账号
                return Response.error(1, "没有权限");
            }
            User user=userService.updateUser(id,name,account,age,phone,email,sex,role,type);
            return Response.success("用户信息添加/修改成功！",user);
        }catch (RuntimeException e){
            return Response.error(1, "用户信息修改失败");
        }
    }

    @RequestMapping("/mgr/user/finduser.do")
    @ResponseBody
    public Response findUser(int id,HttpSession session){
        try{
            User mgr = (User) session.getAttribute("user");
            if(mgr==null||(mgr!=null&&mgr.getRole() != 1&&mgr.getRole() != 2)) {  //1-管理员账号
                return Response.error(1, "不是管理员,无法登录");
            }
            FindUser findUser=userService.findUser(id);
            return Response.success(findUser);
        }catch (RuntimeException e){
            return Response.error(1,"获取用户数据失败");
        }
    }

    @RequestMapping("/mgr/user/deleteusers.do")
    @ResponseBody
    public Response deleteUsers(int id,int del,HttpSession session){
        try{
            User mgr = (User) session.getAttribute("user");
            if(mgr==null||(mgr!=null&&mgr.getRole()!=2)) {  //1-管理员账号
                return Response.error(1, "没有权限");
            }
            if(userService.deleteUsersUser(id,del)==0){
                return  Response.error(1,"删除失败！");
            }else{
                return Response.success();
            }
        }catch (RuntimeException e){
            return Response.error(1, e.getMessage());
        }
    }

    @RequestMapping("/mgr/user/finduserlist.do")
    @ResponseBody
    public Response findUserList(HttpSession session){
        try{
            User mgr = (User) session.getAttribute("user");
            if(mgr==null||(mgr!=null&&mgr.getRole() != 1&&mgr.getRole()!=2)) {  //1-管理员账号
                return Response.error(1, "不是管理员");
            }
            List<FindUser> userList=userService.findUserList();
            return Response.success(userList);
        }catch (RuntimeException e){
            return Response.error(1, e.getMessage());
        }
    }

    @RequestMapping("/mgr/user/findmgrlist.do")
    @ResponseBody
    public Response findMgrList(HttpSession session){
        try{
            User mgr = (User) session.getAttribute("user");
            if(mgr==null||(mgr!=null&&mgr.getRole() != 1&&mgr.getRole()!=2)) {  //1-管理员账号
                return Response.error(1, "不是管理员");
            }
            List<FindUser> userList=userService.findMgrList();
            return Response.success(userList);
        }catch (RuntimeException e){
            return Response.error(1, e.getMessage());
        }
    }

    @RequestMapping("/mgr/user/resetpassword.do")
    @ResponseBody
    public Response resetPassword(int id,HttpSession session){
        try{
            User mgr=(User)session.getAttribute("user");
            if(mgr==null||(mgr!=null&&mgr.getRole()!=2)){
                return Response.error(1,"没有权限");
            }
            String defaultPassword="admin";
            userService.userResetPwdByQuestion(id,defaultPassword);
            return Response.success("重置成功");
        }catch (RuntimeException e){
            return Response.error(1, e.getMessage());
        }
    }

}
