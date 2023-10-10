package com.fo.pp.controller;

import com.fo.pp.common.Response;
import com.fo.pp.pojo.po.Cls;
import com.fo.pp.pojo.po.Commodity;
import com.fo.pp.pojo.po.User;
import com.fo.pp.pojo.vo.ClsAndCommodity;
import com.fo.pp.service.ClassService;
import com.fo.pp.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class ClassController {

    @Autowired
    private ClassService classService;

    @Autowired
    private CommodityService commodityService;


    @RequestMapping("/mgr/param/saveparam.do")
    @ResponseBody
    public Response addAClass(Integer parent_id, String name, HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null){
            return Response.error(1, "请登录后在进行操作！");
        }
        if(user.getRole() == 0){
            return Response.error(1, "你没有权限进行此操作！");
        }
        try {
            if(classService.selectClsByName(name)!=null){
                return Response.error(1,"已有该商品类型！");
            }
            classService.addAClass(parent_id, name);
            return Response.success("产品参数新增成功！");
        }catch (RuntimeException e){
            return Response.error(1, "产品参数新增失败！");
        }
    }
    @RequestMapping("product/findfloors.do")
    @ResponseBody
    public Response getFloors(){
        try {
            ArrayList<ClsAndCommodity> cams = new ArrayList<ClsAndCommodity>();
            Cls[] cls=classService.getAllCls();

            if(cls.length<=4){
                for(Cls c : cls){
                    ClsAndCommodity cam = new ClsAndCommodity();
                    cam.setClass_id(c.getId());
                    cam.setName(c.getName());
                    Commodity[] commodities = commodityService.getByClassIDAndStatus(c.getId(), 0, Integer.MAX_VALUE);
                    if(commodities.length<=8){
                        for(Commodity co:commodities){
                            co.setDetail(null);
                            co.setSubImages(null);
                            co.setSpecParam(null);
                        }
                        cam.setChildren(commodities);
                        cams.add(cam);
                    }
                    else{
                        Commodity[] commodities1=new Commodity[8];
                        for(int j=0;j<8;j++){
                            commodities[j].setDetail(null);
                            commodities[j].setSpecParam(null);
                            commodities[j].setSubImages(null);
                            commodities1[j]=commodities[j];
                        }
                        cam.setChildren(commodities1);
                        cams.add(cam);

                    }

                }
                Response rs=new Response();
                rs.setData(cams);
                rs.setMsg("返回分类和商品成功");
                return rs;
            }
            else{
                for(int i=0;i<4;i++){
                    ClsAndCommodity cam = new ClsAndCommodity();
                    cam.setClass_id(cls[i].getId());
                    cam.setName(cls[i].getName());
                    Commodity[] commodities = commodityService.getByClassIDAndStatus(cls[i].getId(), 0, Integer.MAX_VALUE);
                    if(commodities.length<=8){
                        for(Commodity co:commodities){
                            co.setDetail(null);
                            co.setSubImages(null);
                            co.setSpecParam(null);
                        }
                        cam.setChildren(commodities);
                        cams.add(cam);
                    }
                    else{
                        Commodity[] commodities1=new Commodity[8];
                        for(int j=0;j<8;j++){
                            commodities[j].setDetail(null);
                            commodities[j].setSpecParam(null);
                            commodities[j].setSubImages(null);
                            commodities1[j]=commodities[j];
                        }
                        cam.setChildren(commodities1);
                        cams.add(cam);

                    }
                }

                Response rs=new Response();
                rs.setData(cams);
                rs.setMsg("返回分类和商品成功");
                return rs;
            }
        }catch (RuntimeException e){
            return Response.error(1, "返回分类和商品失败！");
        }
    }

    @RequestMapping("/param/findallparams.do")
    @ResponseBody
    public Response getAllClassAndCommodity(){
        try {
            ArrayList<ClsAndCommodity> cams = new ArrayList<ClsAndCommodity>();
            Cls[] cls=classService.getAllCls();
            for(Cls c : cls){
                ClsAndCommodity cam = new ClsAndCommodity();
                cam.setClass_id(c.getId());
                cam.setName(c.getName());
                Commodity[] commodities = commodityService.getByClassID(c.getId(), 0, Integer.MAX_VALUE);
                cam.setChildren(commodities);
                cams.add(cam);
            }
            Response rs=new Response();
            rs.setData(cams);
            rs.setMsg("返回分类和商品成功");
            return rs;
        }catch (RuntimeException e){
            return Response.error(1, "返回分类和商品失败！");
        }
    }

    @RequestMapping("/mgr/param/updateparam.do")
    @ResponseBody
    public Response updateClass(String name,String id ,HttpSession session)
    {
        User user = (User) session.getAttribute("user");
        if(user == null){
            return Response.error(1, "请登录后在进行操作！");
        }
        if(user.getRole() == 0){
            return Response.error(1, "你没有权限进行此操作！");
        }
        try
        {
            int updateid = Integer.valueOf(id).intValue();
            classService.updateAClass(name,updateid);
            return Response.success("产品参数修改成功");

        }catch (Exception e)
        {
            return Response.error(1,"产品参数修改失败");
        }
    }

    @RequestMapping("/mgr/param/findptype.do")
    @ResponseBody
    public Response findAllType(HttpSession session)
    {
        User user = (User) session.getAttribute("user");
        if(user == null){
            return Response.error(1, "请登录后在进行操作！");
        }
        if(user.getRole() == 0){
            return Response.error(1, "你没有权限进行此操作！");
        }
        Cls cls[] = classService.getAllCls();
        Response rs = new Response();
        rs.setData(cls);
        return rs;
    }



    @RequestMapping("/mgr/param/delparam.do")
    @ResponseBody
    public Response delAParam(String id,HttpSession session)
    {
        User user = (User) session.getAttribute("user");
        if(user == null){
            return Response.error(1, "请登录后在进行操作！");
        }
        if(user.getRole() == 0){
            return Response.error(1, "你没有权限进行此操作！");
        }
        int delid = Integer.valueOf(id).intValue();
        if(classService.findClsCmdt(delid)==1)
        {
            return Response.error(1,"不能删除有商品的类型");
        }
        else
        {
            classService.delACls(delid);
            return Response.success();
        }
    }
}
