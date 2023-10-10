package com.fo.pp.service;

import com.fo.pp.dao.CommentDAO;
import com.fo.pp.dao.OrderDAO;
import com.fo.pp.pojo.po.Comment;
import com.fo.pp.pojo.po.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private OrderDAO orderDAO;

    public int save_comment(Comment comment, String orderNo){
        //保存评论信息：
        if (commentDAO.save_comment(comment) == 0) {
            return 1;
        }
        //更新订单状态：
        if(orderDAO.commentDone(orderNo) == 0){
            return 1;
        }

        return 0;
    }

    public Comment get_comment_detail(String orderNo){
        return commentDAO.get_comment_detail(orderNo);
    }

    public Comment[] get_commodity_comment(String commodityNo){
        return commentDAO.get_commodity_comment(commodityNo);
    }
}
