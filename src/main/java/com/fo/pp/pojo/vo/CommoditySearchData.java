package com.fo.pp.pojo.vo;

import com.fo.pp.pojo.po.Commodity;

public class CommoditySearchData {
    private Commodity[] lists;
    private Integer count;

    public CommoditySearchData(Commodity[] lists, Integer count) {
        this.lists = lists;
        this.count = count;
    }

    public Commodity[] getLists() {
        return lists;
    }

    public void setLists(Commodity[] lists) {
        this.lists = lists;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
