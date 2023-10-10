package com.fo.pp.service;

import com.fo.pp.dao.ClassDAO;
import com.fo.pp.pojo.po.Cls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassService {

    @Autowired
    private ClassDAO classDAO;

    /**
     * 添加一个商品类别
     * @param parentId  父级别的id，为0时表示没有父类别
     * @param name      要添加的商品类别名称
     */
    public void addAClass(int parentId, String name) throws RuntimeException{
        if(name == null || name.equals("")) {
            throw new RuntimeException("不合法的类型名字");
        }
        Cls cls = new Cls();
        cls.setParentId(0);
        cls.setName(name);
        Cls parent = classDAO.selectClsById(parentId);
        if(parentId == 0 || parent != null){
            if(classDAO.insertCls(cls) == 0){
                throw new RuntimeException("添加失败");
            }
            return;
        }
        throw new RuntimeException("没有此父级id");
    }

    public Cls[] getAllCls(){
        return classDAO.selectAllCls();
    }
    public Cls selectClsById(int id){return classDAO.selectClsById(id);}
    public Cls selectClsByName(String name){return classDAO.selectClsByName(name);}
    public void updateAClass(String name, int id)
    {
        if(classDAO.updateAClass(name,id) ==0)
        {
            throw new RuntimeException("参数错误");
        }
        return;
    }

    public int findClsCmdt(int id)
    {
        if(classDAO.cmdt(id)==0)
            return 0;
        else return 1;
    }

    public void delACls(int id)
    {
        classDAO.deleteACls(id);
    }
}
