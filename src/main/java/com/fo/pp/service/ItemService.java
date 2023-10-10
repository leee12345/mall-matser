package com.fo.pp.service;

import com.fo.pp.dao.ItemDAO;
import com.fo.pp.pojo.po.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    @Autowired
    private ItemDAO itemDAO;

    public Item[] getItemsByOrderId(int orderId){
        return itemDAO.getItemsByOrderId(orderId);
    }

    public int addItems(Item item){return itemDAO.insertItem(item);}

}
