package com.fo.pp.pojo.vo;

import com.fo.pp.pojo.po.Commodity;

public class ClsAndCommodity {
    private Integer class_id;
    private String name;

    private Commodity[] children;

    public Integer getClass_id() {
        return class_id;
    }

    public void setClass_id(Integer class_id) {
        this.class_id = class_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Commodity[] getChildren() {
        return children;
    }

    public void setChildren(Commodity[] children) {
        this.children = children;
    }


}
