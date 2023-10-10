package com.fo.pp.service;

import com.fo.pp.dao.CommodityDAO;

import com.fo.pp.pojo.po.Commodity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommodityService {
    @Autowired
    private CommodityDAO commodityDAO;

    public Commodity getDetail(int product_id){
        return commodityDAO.selectCommodityById(product_id);
    }

    public Commodity[] getHot(){
        return commodityDAO.selectHotCommodity();
    }

    public Commodity[] getHotAndStatus(){
        return commodityDAO.selectHotAndStatusCommodity();
    }

    public  Commodity[] getByClassID(int class_id, int offset, int limit){
        return commodityDAO.selectCommodityByClassID(class_id, offset, limit);
    }
    public  Commodity[] getByClassIDAndStatus(int class_id, int offset, int limit){
        return commodityDAO.selectCommodityByClassIDAndStatus(class_id, offset, limit);
    }

    public  Commodity[] getAll(int class_id){
        return commodityDAO.selectCommodityByClassID(class_id, 0, Integer.MAX_VALUE);
    }
    public  Commodity[] getAllCommodities(Integer offset, Integer limit){
        return commodityDAO.selectAllCommodity(offset, limit);
    }
    public  Commodity[] getAllCommoditiesAndStatus(Integer offset, Integer limit){
        return commodityDAO.selectAllCommodityAndStatus(offset, limit);
    }
    public  Commodity[] getAllCommoditiesMgr(){
        return commodityDAO.selectAllCommodities();
    }
    public  Commodity[] getCommoditiesByName(String keyword, int offset, int limit){
        return commodityDAO.selectCommodityByName(keyword, offset, limit);
    }
    public  Commodity[] getCommoditiesByNameAndStatus(String keyword, int offset, int limit){
        return commodityDAO.selectCommodityByNameAndStatus(keyword, offset, limit);
    }

    public  Commodity[] getByClassIDAndName(int classId,String keyword, int offset, int limit){
        return commodityDAO.selectCommodityByClassIDAndName(classId,keyword, offset, limit);
    }
    public  Commodity[] getByClassIDAndNameAndStatus(int classId,String keyword, int offset, int limit){
        return commodityDAO.selectCommodityByClassIDAndNameAndStatus(classId,keyword, offset, limit);
    }
    public  Commodity getByIDAndClassIDAndName(int id,int classId,String keyword){
        return commodityDAO.selectCommodityByIDAndClassIDAndName(id,classId,keyword);
    }
    public  Commodity getByIDAndClassID(int id,int classId){
        return commodityDAO.selectCommodityByIDAndClassID(id,classId);
    }
    public  Commodity getByIDAndName(int id,String keyword){
        return commodityDAO.selectCommodityByIDAndName(id,keyword);
    }

    public int saveCommodity(String name,Integer class_id,String icon_url,String sub_images,String detail,String spec_param,Integer price,Integer stock){
        Commodity commodity=new Commodity();
        commodity.setName(name);
        commodity.setClassId(class_id);
        commodity.setIconUrl(icon_url);
        commodity.setSubImages(sub_images);
        commodity.setDetail(detail);
        commodity.setSpecParam(spec_param);
        commodity.setPrice(price);
        commodity.setStock(stock);
        if(commodityDAO.insertCommodity(commodity)==0){
            return 0;
        }else return commodity.getId();

    }

    public int updateCommodity(Integer id, String name, Integer class_id, String icon_url, String sub_images, String detail, String spec_param, Integer price, Integer stock) {
        return commodityDAO.updateCommodityById(id, name, class_id, icon_url, sub_images, detail, spec_param, price, stock);
    }

    public int updateCommodityStatus(Integer id, Integer status, Integer hot) {
        return commodityDAO.updateCommodityStatus(id, status, hot);
    }

    public int pop(int commodityId, int quantity) {
        return commodityDAO.pop(commodityId, quantity);
    }

    public int push(int commodityId, int quantity) {
        return commodityDAO.push(commodityId, quantity);
    }
}
