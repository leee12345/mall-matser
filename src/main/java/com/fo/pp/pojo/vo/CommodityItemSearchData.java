package com.fo.pp.pojo.vo;

import java.util.List;

public class CommodityItemSearchData {
    private List<CommodityItem>  lists;
    private Integer count;

    public CommodityItemSearchData(List<CommodityItem> lists, Integer count) {
        this.lists = lists;
        this.count = count;
    }

    public List<CommodityItem> getLists() {
        return lists;
    }

    public void setLists(List<CommodityItem> lists) {
        this.lists = lists;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
