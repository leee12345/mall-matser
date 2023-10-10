package com.fo.pp.controller;


import com.fo.pp.common.Response;
import com.fo.pp.dao.CommodityDAO;
import com.fo.pp.pojo.po.Cls;
import com.fo.pp.pojo.po.Commodity;
import com.fo.pp.pojo.po.User;
import com.fo.pp.pojo.vo.CommoditySearchData;
import com.fo.pp.pojo.vo.CommodityItemSearchData;
import com.fo.pp.pojo.vo.CommodityItem;
import com.fo.pp.service.CommodityService;
import com.fo.pp.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CommodityController {
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private ClassService classService;

    @Autowired
    private CommodityDAO commodityDAO;

    @RequestMapping("/mgr/product/classlist.do")
    @ResponseBody
    public Response findAllClass(HttpSession session)
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
    @RequestMapping("/product/getdetail.do")
    @ResponseBody
    public Response getDetail(Integer productId){
        try {

            Commodity commodity=commodityService.getDetail(productId);

            if(commodity!=null){
                return Response.success(commodity);
            }
            else return Response.error(1,"产品已经下架！");
        }catch (RuntimeException e){
            return Response.error(1, "商品不存在");
        }
    }

    @RequestMapping("/product/setstatus.do")
    @ResponseBody
    public Response setStatus(String productId,String status,String hot)
    {
        int productid,statusid,ishot;
        productid = Integer.valueOf(productId).intValue();
        statusid = Integer.valueOf(status).intValue();
        ishot = Integer.valueOf(hot).intValue();
        if((statusid==1||statusid==2||statusid==3)&&(ishot==1||ishot==2))
        {
            if(commodityService.updateCommodityStatus(productid,statusid,ishot)!=0)
            {
                return Response.success("修改产品状态完成");
            }
            else return Response.error(1,"修改产品参数失败");
        }
        else return Response.error(1,"非法参数");
    }

    @RequestMapping("/product/findhotproducts.do")
    @ResponseBody
    public Response getHot(String num){
        try {

            Commodity[] hot_commodities=commodityService.getHotAndStatus();



            int n=Integer.parseInt(num);

            if(n<= hot_commodities.length){
                ArrayList<Commodity> ht=new ArrayList<Commodity>();
                for(int i=0;i<n;i++){
                    hot_commodities[i].setSpecParam("");
                    hot_commodities[i].setDetail("");
                    ht.add(hot_commodities[i]);
                }
                return Response.success(ht);
            }
            else{
                return Response.success(hot_commodities);
            }
        }catch (RuntimeException e){
            return Response.error(1, "返回热销商品失败！");
        }
    }

    @RequestMapping("/mgr/product/saveproduct.do")
    @ResponseBody
    public Response saveProduct(HttpSession session,Integer id, String name, Integer class_id, String icon_url, String sub_images, String detail, String spec_param, Integer price, Integer stock){
        User mgr = (User) session.getAttribute("user");
        if (mgr == null || (mgr != null && mgr.getRole() != 1 && mgr.getRole() != 2)) {  //1:管理员账号
            return Response.error(1, "不是管理员");
        }
        if (name == "") {
            return Response.error(1, "产品名称不能为空");
        }
        if(id!=null){
            if(commodityService.updateCommodity(id, name, class_id, icon_url, sub_images, detail, spec_param, price, stock)!=0){
                return Response.success("产品新更新成功！");
            }
            else{
                return Response.error(1, "产品更新失败");
            }
        }
        else {
            int commodityid=commodityService.saveCommodity(name, class_id, icon_url, sub_images, detail, spec_param, price, stock);
            if ( commodityid!= 0) {
                return Response.success("产品新增成功！",commodityid);
            } else {
                return Response.error(1, "产品新增失败");
            }
        }
    }
    @RequestMapping("/product/searchproducts.do")
    @ResponseBody
    public Response searchProduct(String keyword,String classId, Integer offset, Integer limit){
            if(keyword == null || classId == null || offset == null || limit == null){
                return Response.error(1, "错误的参数传递");
            }
            if(keyword.equals("")){
                if (classId.equals("0")){
                    Commodity[] commodities=commodityService.getAllCommoditiesAndStatus(offset, limit);
                    int count = commodityDAO.selectAllCommodityStatusCount();
                    return Response.success(new CommoditySearchData(commodities, count));
                }else{
                    Commodity[] commodities=commodityService.getByClassIDAndStatus(Integer.parseInt(classId), offset, limit);
                    int count = commodityDAO.selectCommodityByClassIDStatusCount(Integer.parseInt(classId));
                    return Response.success(new CommoditySearchData(commodities, count));
                }
            }else{
                if (classId.equals("0")){
                    Commodity[] commodities=commodityService.getCommoditiesByNameAndStatus(keyword, offset, limit);
                    int count = commodityDAO.selectCommodityByNameStatusCount(keyword);
                    if(commodities.length!=0){
                        return Response.success(new CommoditySearchData(commodities, count));
                    }
                    else return Response.error(1,"未查找到商品");
                }else{
                    Commodity[] commodities=commodityService.getByClassIDAndNameAndStatus(Integer.parseInt(classId),keyword, offset, limit);
                    int count = commodityDAO.selectCommodityByClassIDAndNameStatusCount(Integer.parseInt(classId), keyword);
                    if(commodities.length!=0){
                        return Response.success(new CommoditySearchData(commodities, count));
                    }
                    else return Response.error(1,"未查找到商品");
                }
            }

    }

    @RequestMapping("/mgr/product/getdetail.do")
    @ResponseBody
    public Response getMgrDetail(HttpSession session,Integer productId){
        User mgr = (User) session.getAttribute("user");
        if(mgr==null||(mgr!=null&&mgr.getRole() != 1&&mgr.getRole() != 2)) {  //1-管理员账号
            return Response.error(1, "不是管理员");
        }
        try {

            Commodity commodity=commodityService.getDetail(productId);

            if(commodity!=null){
                return Response.success(commodity);
            }
            else return Response.error(1,"产品已经下架！");
        }catch (RuntimeException e){
            return Response.error(1, "商品不存在");
        }
    }
    @RequestMapping("/cart/buy.do")
    @ResponseBody
    public Response getCartBuyStatus(HttpSession session,Integer productId){
        try {

            Commodity commodity=commodityService.getDetail(productId);

            if(commodity!=null){

                return Response.success(commodity.getStatus());
            }
            else return Response.error(1,"产品已经下架！");
        }catch (RuntimeException e){
            return Response.error(1, "商品不存在");
        }
    }

    @RequestMapping("/mgr/product/productlist.do")
    @ResponseBody
    public Response getProductList(String id,Integer offset, Integer limit,HttpSession session){
        User mgr = (User) session.getAttribute("user");
        if(mgr==null||(mgr!=null&&mgr.getRole() != 1&&mgr.getRole() != 2)) {  //1-管理员账号
            return Response.error(1, "不是管理员");
        }
        if(id==null||offset == null || limit == null)
        {
            return Response.error(1, "传递错误参数");
        }
        if(id.equals("")){
            Commodity[] commodities=commodityService.getAllCommodities(offset, limit);
            int count = commodityDAO.selectAllCommodityCount();
            List<CommodityItem> commodityItems = new ArrayList<>();
            for (Commodity commodity:commodities ){
                CommodityItem commodityItem=new CommodityItem();
                commodityItem.setId(commodity.getId());
                commodityItem.setName(commodity.getName());
                commodityItem.setClassId(commodity.getClassId());

                String className=classService.selectClsById(commodity.getClassId()).getName();
                commodityItem.setClassname(className);

                commodityItem.setIconUrl(commodity.getIconUrl());
                commodityItem.setSubImages(commodity.getSubImages());
                commodityItem.setDetail(commodity.getDetail());
                commodityItem.setSpecParam(commodity.getSpecParam());
                commodityItem.setPrice(commodity.getPrice());
                commodityItem.setStock(commodity.getStock());
                commodityItem.setStatus(commodity.getStatus());
                commodityItem.setIsHot(commodity.getIsHot());
                commodityItem.setCreateTime(commodity.getCreateTime());
                commodityItem.setUpdateTime(commodity.getUpdateTime());
                commodityItems.add(commodityItem);
            }

            if(commodityItems.size()!=0){
                return Response.success(new CommodityItemSearchData(commodityItems, count));
            }else{
                return Response.error(1,"无商品！");
            }

        } else {
            Commodity commodity=commodityService.getDetail(Integer.parseInt(id));
            List<CommodityItem> commodityItems = new ArrayList<>();

            if(commodity!=null){
                CommodityItem commodityItem=new CommodityItem();
                commodityItem.setId(commodity.getId());
                commodityItem.setName(commodity.getName());
                commodityItem.setClassId(commodity.getClassId());

                String className=classService.selectClsById(commodity.getClassId()).getName();
                commodityItem.setClassname(className);

                commodityItem.setIconUrl(commodity.getIconUrl());
                commodityItem.setSubImages(commodity.getSubImages());
                commodityItem.setDetail(commodity.getDetail());
                commodityItem.setSpecParam(commodity.getSpecParam());
                commodityItem.setPrice(commodity.getPrice());
                commodityItem.setStock(commodity.getStock());
                commodityItem.setStatus(commodity.getStatus());
                commodityItem.setIsHot(commodity.getIsHot());
                commodityItem.setCreateTime(commodity.getCreateTime());
                commodityItem.setUpdateTime(commodity.getUpdateTime());
                commodityItems.add(commodityItem);
                return Response.success( new CommodityItemSearchData(commodityItems, 1));
            }
            else return Response.error(1,"产品已经下架！");
        }

    }
    @RequestMapping("/mgr/product/productlistbyclsname.do")
    @ResponseBody
    public Response getProductListByClsName(String id,String name,String cls,Integer offset, Integer limit,HttpSession session){
        User mgr = (User) session.getAttribute("user");
        if(mgr==null||(mgr!=null&&mgr.getRole() != 1&&mgr.getRole() != 2)) {  //1-管理员账号
            return Response.error(1, "不是管理员");
        }
        if(id==null||name==null||cls==null||offset == null || limit == null)
        {
            return Response.error(1, "传递错误参数");
        }
        if(id.equals("")&&name.equals("")&&cls.equals("")){
            System.out.println("都是空");
            Commodity[] commodities=commodityService.getAllCommodities(offset, limit);
            int count = commodityDAO.selectAllCommodityCount();
            List<CommodityItem> commodityItems = new ArrayList<>();
            for (Commodity commodity:commodities ){
                CommodityItem commodityItem=new CommodityItem();
                commodityItem.setId(commodity.getId());
                commodityItem.setName(commodity.getName());
                commodityItem.setClassId(commodity.getClassId());

                String className=classService.selectClsById(commodity.getClassId()).getName();
                commodityItem.setClassname(className);

                commodityItem.setIconUrl(commodity.getIconUrl());
                commodityItem.setSubImages(commodity.getSubImages());
                commodityItem.setDetail(commodity.getDetail());
                commodityItem.setSpecParam(commodity.getSpecParam());
                commodityItem.setPrice(commodity.getPrice());
                commodityItem.setStock(commodity.getStock());
                commodityItem.setStatus(commodity.getStatus());
                commodityItem.setIsHot(commodity.getIsHot());
                commodityItem.setCreateTime(commodity.getCreateTime());
                commodityItem.setUpdateTime(commodity.getUpdateTime());
                commodityItems.add(commodityItem);
            }

            if(commodityItems.size()!=0){
                return Response.success(new CommodityItemSearchData(commodityItems, count));
            }else{
                return Response.error(1,"无商品！");
            }

        } else if(cls.equals("")&&id.equals("")&&!name.equals("")){
            System.out.println("cls,id是空");
            Commodity[] commodities=commodityService.getCommoditiesByName(name,offset, limit);
            int count = commodityDAO.selectCommodityByNameCount(name);
            List<CommodityItem> commodityItems = new ArrayList<>();
            for (Commodity commodity:commodities ){
                CommodityItem commodityItem=new CommodityItem();
                commodityItem.setId(commodity.getId());
                commodityItem.setName(commodity.getName());
                commodityItem.setClassId(commodity.getClassId());

                String className=classService.selectClsById(commodity.getClassId()).getName();
                commodityItem.setClassname(className);

                commodityItem.setIconUrl(commodity.getIconUrl());
                commodityItem.setSubImages(commodity.getSubImages());
                commodityItem.setDetail(commodity.getDetail());
                commodityItem.setSpecParam(commodity.getSpecParam());
                commodityItem.setPrice(commodity.getPrice());
                commodityItem.setStock(commodity.getStock());
                commodityItem.setStatus(commodity.getStatus());
                commodityItem.setIsHot(commodity.getIsHot());
                commodityItem.setCreateTime(commodity.getCreateTime());
                commodityItem.setUpdateTime(commodity.getUpdateTime());
                commodityItems.add(commodityItem);
            }
            if(commodityItems.size()!=0){
                return Response.success(new CommodityItemSearchData(commodityItems, count));
            }
            else return Response.error(1,"没有该名称商品！");
        }else if(name.equals("")&&id.equals("")&&!cls.equals("")){
            System.out.println("name,id是空");
            Commodity [] commodities=commodityService.getByClassID(Integer.parseInt(cls),offset, limit);
            int count = commodityDAO.selectCommodityByClassIDCount(Integer.parseInt(cls));
            List<CommodityItem> commodityItems = new ArrayList<>();
            for (Commodity commodity:commodities ){
                CommodityItem commodityItem=new CommodityItem();
                commodityItem.setId(commodity.getId());
                commodityItem.setName(commodity.getName());
                commodityItem.setClassId(commodity.getClassId());

                String className=classService.selectClsById(commodity.getClassId()).getName();
                commodityItem.setClassname(className);

                commodityItem.setIconUrl(commodity.getIconUrl());
                commodityItem.setSubImages(commodity.getSubImages());
                commodityItem.setDetail(commodity.getDetail());
                commodityItem.setSpecParam(commodity.getSpecParam());
                commodityItem.setPrice(commodity.getPrice());
                commodityItem.setStock(commodity.getStock());
                commodityItem.setStatus(commodity.getStatus());
                commodityItem.setIsHot(commodity.getIsHot());
                commodityItem.setCreateTime(commodity.getCreateTime());
                commodityItem.setUpdateTime(commodity.getUpdateTime());
                commodityItems.add(commodityItem);
            }
            if(commodityItems.size()!=0){
                return Response.success(new CommodityItemSearchData(commodityItems, count));
            }
            else return Response.error(1,"没有该类别商品！");
        }else if(id.equals("")&&!name.equals("")&&!cls.equals("")){
            System.out.println("name,cls都不是空");
            Commodity [] commodities=commodityService.getByClassIDAndName(Integer.parseInt(cls),name,offset, limit);
            int count = commodityDAO.selectCommodityByClassIDAndNameCount(Integer.parseInt(cls), name);
            List<CommodityItem> commodityItems = new ArrayList<>();
            for (Commodity commodity:commodities ){
                CommodityItem commodityItem=new CommodityItem();
                commodityItem.setId(commodity.getId());
                commodityItem.setName(commodity.getName());
                commodityItem.setClassId(commodity.getClassId());

                String className=classService.selectClsById(commodity.getClassId()).getName();
                commodityItem.setClassname(className);

                commodityItem.setIconUrl(commodity.getIconUrl());
                commodityItem.setSubImages(commodity.getSubImages());
                commodityItem.setDetail(commodity.getDetail());
                commodityItem.setSpecParam(commodity.getSpecParam());
                commodityItem.setPrice(commodity.getPrice());
                commodityItem.setStock(commodity.getStock());
                commodityItem.setStatus(commodity.getStatus());
                commodityItem.setIsHot(commodity.getIsHot());
                commodityItem.setCreateTime(commodity.getCreateTime());
                commodityItem.setUpdateTime(commodity.getUpdateTime());
                commodityItems.add(commodityItem);
            }
            if(commodityItems.size()!=0){
                return Response.success(new CommodityItemSearchData(commodityItems, count));
            }
            else return Response.error(1,"没有该名称和类别的商品！");
        }else{
            //id 不为空
            if(name.equals("")&&cls.equals("")){
                Commodity commodity=commodityService.getDetail(Integer.parseInt(id));
                List<CommodityItem> commodityItems = new ArrayList<>();

                if(commodity!=null){
                    CommodityItem commodityItem=new CommodityItem();
                    commodityItem.setId(commodity.getId());
                    commodityItem.setName(commodity.getName());
                    commodityItem.setClassId(commodity.getClassId());

                    String className=classService.selectClsById(commodity.getClassId()).getName();
                    commodityItem.setClassname(className);

                    commodityItem.setIconUrl(commodity.getIconUrl());
                    commodityItem.setSubImages(commodity.getSubImages());
                    commodityItem.setDetail(commodity.getDetail());
                    commodityItem.setSpecParam(commodity.getSpecParam());
                    commodityItem.setPrice(commodity.getPrice());
                    commodityItem.setStock(commodity.getStock());
                    commodityItem.setStatus(commodity.getStatus());
                    commodityItem.setIsHot(commodity.getIsHot());
                    commodityItem.setCreateTime(commodity.getCreateTime());
                    commodityItem.setUpdateTime(commodity.getUpdateTime());
                    commodityItems.add(commodityItem);
                    return Response.success( new CommodityItemSearchData(commodityItems, 1));
                }
                else return Response.error(1,"产品已经下架！");
            }else if(name.equals("")&&!cls.equals("")){
                //id cls
                Commodity commodity=commodityService.getByIDAndClassID(Integer.parseInt(id),Integer.parseInt(cls));
                List<CommodityItem> commodityItems = new ArrayList<>();

                if(commodity!=null){
                    CommodityItem commodityItem=new CommodityItem();
                    commodityItem.setId(commodity.getId());
                    commodityItem.setName(commodity.getName());
                    commodityItem.setClassId(commodity.getClassId());

                    String className=classService.selectClsById(commodity.getClassId()).getName();
                    commodityItem.setClassname(className);

                    commodityItem.setIconUrl(commodity.getIconUrl());
                    commodityItem.setSubImages(commodity.getSubImages());
                    commodityItem.setDetail(commodity.getDetail());
                    commodityItem.setSpecParam(commodity.getSpecParam());
                    commodityItem.setPrice(commodity.getPrice());
                    commodityItem.setStock(commodity.getStock());
                    commodityItem.setStatus(commodity.getStatus());
                    commodityItem.setIsHot(commodity.getIsHot());
                    commodityItem.setCreateTime(commodity.getCreateTime());
                    commodityItem.setUpdateTime(commodity.getUpdateTime());
                    commodityItems.add(commodityItem);
                    return Response.success( new CommodityItemSearchData(commodityItems, 1));
                }
                else return Response.error(1,"产品不能存在！");

            }else if(!name.equals("")&&cls.equals("")){
                //id name
                Commodity commodity=commodityService.getByIDAndName(Integer.parseInt(id),name);
                List<CommodityItem> commodityItems = new ArrayList<>();

                if(commodity!=null){
                    CommodityItem commodityItem=new CommodityItem();
                    commodityItem.setId(commodity.getId());
                    commodityItem.setName(commodity.getName());
                    commodityItem.setClassId(commodity.getClassId());

                    String className=classService.selectClsById(commodity.getClassId()).getName();
                    commodityItem.setClassname(className);

                    commodityItem.setIconUrl(commodity.getIconUrl());
                    commodityItem.setSubImages(commodity.getSubImages());
                    commodityItem.setDetail(commodity.getDetail());
                    commodityItem.setSpecParam(commodity.getSpecParam());
                    commodityItem.setPrice(commodity.getPrice());
                    commodityItem.setStock(commodity.getStock());
                    commodityItem.setStatus(commodity.getStatus());
                    commodityItem.setIsHot(commodity.getIsHot());
                    commodityItem.setCreateTime(commodity.getCreateTime());
                    commodityItem.setUpdateTime(commodity.getUpdateTime());
                    commodityItems.add(commodityItem);
                    return Response.success( new CommodityItemSearchData(commodityItems, 1));
                }
                else return Response.error(1,"产品不能存在！");
            } else{
                //id name cls
                Commodity commodity=commodityService.getByIDAndClassIDAndName(Integer.parseInt(id),Integer.parseInt(cls),name);
                List<CommodityItem> commodityItems = new ArrayList<>();

                if(commodity!=null){
                    CommodityItem commodityItem=new CommodityItem();
                    commodityItem.setId(commodity.getId());
                    commodityItem.setName(commodity.getName());
                    commodityItem.setClassId(commodity.getClassId());

                    String className=classService.selectClsById(commodity.getClassId()).getName();
                    commodityItem.setClassname(className);

                    commodityItem.setIconUrl(commodity.getIconUrl());
                    commodityItem.setSubImages(commodity.getSubImages());
                    commodityItem.setDetail(commodity.getDetail());
                    commodityItem.setSpecParam(commodity.getSpecParam());
                    commodityItem.setPrice(commodity.getPrice());
                    commodityItem.setStock(commodity.getStock());
                    commodityItem.setStatus(commodity.getStatus());
                    commodityItem.setIsHot(commodity.getIsHot());
                    commodityItem.setCreateTime(commodity.getCreateTime());
                    commodityItem.setUpdateTime(commodity.getUpdateTime());
                    commodityItems.add(commodityItem);
                    return Response.success( new CommodityItemSearchData(commodityItems, 1));
                }
                else return Response.error(1,"产品不能存在！");
            }
        }

    }


    @RequestMapping("/mgr/product/setstatus.do")
    @ResponseBody
    public Response setStatusMgr(String productId,String status,String hot,HttpSession session)
    {
        User mgr = (User) session.getAttribute("user");
        if(mgr==null||(mgr!=null&&mgr.getRole() != 1&&mgr.getRole() != 2)) {  //1-管理员账号
            return Response.error(1, "不是管理员");
        }
        int productid,statusid,ishot;
        productid = Integer.valueOf(productId).intValue();
        statusid = Integer.valueOf(status).intValue();
        ishot = Integer.valueOf(hot).intValue();
        if((statusid==0||statusid==1||statusid==2||statusid==3)&&(ishot==0||ishot==1||ishot==2))
        {
            if(commodityService.updateCommodityStatus(productid,statusid,ishot)!=0)
            {
                return Response.success("修改产品状态完成");
            }
            else return Response.error(1,"修改产品参数失败");
        }
        else return Response.error(1,"非法参数");
    }


}
